from algoliasearch import algoliasearch

client = algoliasearch.Client("HD0XCBXFH1", 'a579548162960a8ecf866b64340c86e4')
index = client.init_index('signs')

# res = index.add_objects([
#     {"name": "Stop", "meaning": "You have to stop."},
#     {"name": "Start", "meaning": "You have to start."}
# ])

# print res
dictionary = {
    "startId":"17375292",
    "stopId":"17375282"
}
res = index.get_objects([dictionary["startId"]])

print(res['results'][0]['meaning'])

res = index.get_objects([dictionary["stopId"]])

print(res['results'][0]['meaning'])