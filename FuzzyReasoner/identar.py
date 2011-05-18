import json
print json.dumps(json.load(open("dump.json")), indent=4)
