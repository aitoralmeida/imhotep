#!/usr/bin/env python

import sys
import time
import getpass
import urllib
import urllib2
import base64

from pyGTrends import pyGTrends

HOST        = "dev.morelab.deusto.es"
URL         = "http://%s/piramide/index.py/" % HOST
ERROR_TIME  = 3600
NORMAL_TIME = 100

google_username = raw_input("Google username: ")
google_password = getpass.getpass("Google password: ")



def download_data(region, device):
    while True:
        try:
            connector = pyGTrends(google_username, google_password)
        except Exception, e:
            if str(e).startswith('Cannot parse GALX'):
                continue
            else:
                raise
        else:
            break

    connector.download_report(device, geo = region, scale = 1)
    return connector.raw_data

def identifiers():
    while True:
        try:
            full_url = URL + 'next?username=%s' % urllib2.quote(google_username)
            content = urllib2.urlopen( full_url ).read().lower().strip()
        except:
            time.sleep(180)
            continue
        if content == 'not found':
            print "Nothing to search for. Sleeping...", time.asctime()
            time.sleep(100)
            continue

        region_field = content.split('; device:')[0]
        region = region_field[len('region: '):]
        device = content[len(region_field) + len('; device: '):]

        yield region, device

def post_results(region, device, results):
    username = urllib2.quote(google_username)
    device   = urllib2.quote(device)
    region   = urllib2.quote(region)
    data     = urllib.urlencode({ "data" : base64.encodestring(results) })
    full_url = URL + 'post?username=%s&device=%s&region=%s' % (username, device, region)

    response = urllib2.urlopen( full_url, data = data ).read().lower().strip()

    if not response.startswith('info updated'):
        print "There was an error posting results!!! %s %s... : %s" % (device, results[:50].replace("\n","\\n").replace("\r","\\r"), response)

def failed(region, device):
    username = urllib2.quote(google_username)
    device   = urllib2.quote(device)
    region   = urllib2.quote(region)
    full_url = URL + 'failed?username=%s&device=%s&region=%s' % (username, device, region)

    response = urllib2.urlopen( full_url ).read().lower().strip()

    if not response.startswith('ok'):
        print "There was an error informing a fail!!! ", device
   
for region, device in identifiers():
    old_device = device
    for bad_char in ("[","]","{","}"):
        device = device.replace(bad_char," ")

    try:
        results = download_data(region, device)
        if results.find("could not be interpreted")  >= 0 and results.find("Try removing colons") >= 0:
            raise Exception("Bad name bad name")
        if results.find("reached your quota limit. Please try again") >= 0:
            raise Exception("quota limit")
    except Exception, e:
        print "Unexpected error retrieving information of '%s': " % device, e
        print "Sleeping for %s seconds..." % ERROR_TIME
        time_to_sleep = ERROR_TIME
        failed(region, old_device)
    else:
        time_to_sleep = NORMAL_TIME
        post_results(region, old_device, results)

    sys.stdout.flush()
    time.sleep(time_to_sleep)

