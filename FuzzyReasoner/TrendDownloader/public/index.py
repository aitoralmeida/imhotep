import MySQLdb as dbi
import random

FIRST_YEAR = 2004

def _get_connection():
    return dbi.connect(user="piramide", passwd="piramide_password", db="PiramideTrends")

def view_all(req, **kwargs):
    connection = _get_connection()
    cursor = connection.cursor()
    try:
        RESULT = """<html><head><title>Results</title></head>
        <body>
	%s
        <table cellspacing="20">
        <tr><td><b>Sum</b></td><td><b>Name</b></td><td><b>Percent</b></td></tr>
        %s
        </table>
        </body></html>"""

        total   = 0
        without = 0
        max_50  = []
        cursor.execute("SELECT device_name, who, when_started, id, real_height, real_width, reso_height, reso_width FROM Downloaded WHERE who IS NOT NULL")
        for device_name, who, when_started, device_id, real_height, real_width, reso_height, reso_width in cursor.fetchall():
            syear = kwargs.get("from_year")
            fromyear = 0
            if syear is not None:
               try:
                  fromyear = int(syear)
               except:
                  pass
            syear = kwargs.get("to_year")
            toyear = 3000
            if syear is not None:
               try:
                  toyear = int(syear)
               except:
                  pass
           
            cursor.execute("SELECT SUM(value) FROM Value WHERE device_id = %s AND year >= %s AND year <= %s", (device_id, fromyear, toyear))
            sum = cursor.fetchall()[0][0]
            if sum is None:
               sum = 0
            total += sum
            if sum == 0:
               without += 1
            else:
               max_50.append((sum, device_name))
               max_50.sort(lambda x,y : cmp(y[0], x[0]))
               max_50 = max_50[:50]

        content = ""
        for sum, device_name in max_50:
           content += "<tr><td>%s</td><td>%s</td><td>%s</td></tr>\n" % (sum, device_name, 100.0 * sum / total)

        return RESULT % ("<br><b>Sum of all:</b> %s<br><b>Mobile devices with value = 0:</b> %s<br>" % (total, without), content)
    finally:
        connection.close()


def view(req):
    connection = _get_connection()
    cursor = connection.cursor()
    try:
        RESULT = """<html><head><title>Results</title></head>
        <body>
	<b>Finished:</b> %s out of %s (%2.2f %%). <br>
        <b>Remaining:</b> %s out of %s. <br>
        %s
        </body></html>"""

        cursor.execute("SELECT COUNT(id) FROM Downloaded")
        total = cursor.fetchall()[0][0]

        cursor.execute("SELECT COUNT(id) FROM Downloaded WHERE who IS NULL")
        remaining = cursor.fetchall()[0][0]

        table = """<table cellspacing="20">
            <tr>
                <td><b>Who</b></td>
                <td><b>When</b></td>
                <td><b>What</b></td>
                <td><b>Total value</b></td>
                <td><b>Height</b></td>
                <td><b>Width</b></td>
                <td><b>Reso height</b></td>
                <td><b>Reso width</b></td>
            </tr>
        """
        cursor.execute("SELECT device_name, who, when_started, id, real_height, real_width, reso_height, reso_width FROM Downloaded WHERE who IS NOT NULL ORDER BY when_started DESC LIMIT 250")
        for device_name, who, when_started, device_id, real_height, real_width, reso_height, reso_width in cursor.fetchall():
            cursor.execute("SELECT SUM(value) FROM Value WHERE device_id = %s", (device_id,))
            sum = cursor.fetchall()[0][0]
            table += """<tr>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                    <td>%s</td>
                </tr>
                """ % (who, when_started, device_name, sum, real_height, real_width, reso_height, reso_width )
        table += """</table>\n"""
        
        return RESULT % (total - remaining, total, 100.0 * (total - remaining) / total, remaining, total, table)
    finally:
        connection.close()


def _clean_old():
    connection = _get_connection()
    cursor = connection.cursor()
    cursor.execute("SELECT id FROM Downloaded WHERE who IS NOT NULL AND NOW() - when_started > 7200 AND 0 < (SELECT COUNT(id) FROM Value WHERE device_id = Downloaded.id AND value IS NULL)")
    for row in cursor.fetchall():
        cursor.execute("UPDATE Downloaded SET who = NULL, when_started = NULL WHERE id = %s", row)
    connection.commit()
    connection.close()

def post(req, **kwargs):
    username = kwargs.get('username')
    data     = kwargs.get('data')
    device   = kwargs.get('device')
    
    if username is None or data is None or device is None:
        return "You must provide a username, a device and data as '5.5,5.6,0.0'"

    per_year = [ float(x) for x in data.split(',') ]
    years    = [ 2004 + x for x in range(len(per_year)) ]
    years_data = dict(zip(years, per_year))

    connection = _get_connection()
    try:
        cursor = connection.cursor()
        for year in years_data:
            number = cursor.execute("SELECT Value.id FROM Value, Downloaded WHERE device_id = Downloaded.id AND device_name = %s AND year = %s AND who = %s", (device, year, username))
            if number == 0:
                return "Not found"
            id = cursor.fetchall()[0][0]
            sentence = "UPDATE Value SET value = %s WHERE id = %s"
            cursor.execute(sentence, (years_data[year], id))
        connection.commit()
    finally:
        connection.close()

    return "Info updated for years %s" % str(years)

def next(req, **kwargs):

    username = kwargs.get('username')

    if username is None:
        return "You must provide a username"

    _clean_old()

    connection = _get_connection()
    try:
        cursor = connection.cursor()
        number = cursor.execute("SELECT id, device_name FROM Downloaded WHERE who is NULL LIMIT 100")
        if number == 0:
            return "Not found"

	all_results = cursor.fetchall()
	
        
        id, device_name = all_results[ random.randint(0, len(all_results) - 1) ]
        cursor.execute("UPDATE Downloaded SET who = %s, when_started = NOW() WHERE id = %s", (username, id))
        connection.commit()

        return "Device: %s" % device_name
    finally:
        connection.close()
        
def failed(req, **kwargs):
    username = kwargs.get('username')
    device   = kwargs.get('device')

    if username is None or device is None:
        return "You must provide a username and a device"

    connection = _get_connection()
    try:
        cursor = connection.cursor()

        cursor.execute("UPDATE Downloaded SET who = NULL, when_started = NULL WHERE device_name = %s AND who = %s", (device, username))
        connection.commit()
        return "ok"
    finally:
        connection.close()

def index(req):
    return """<html><body>
        <ul>
            <li><a href="index.py/view">view</a></li>
            <li><a href="index.py/post">post</a></li>
            <li><a href="index.py/next">next</a></li>
            <li><a href="index.py/failed">failed</a></li>
        </ul>
        
    </body></html>"""

