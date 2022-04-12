from pymongo import MongoClient

# -----------------------------------------------------------
# Connect to views and collections in the database
# -----------------------------------------------------------

class Database:
	def __init__(self, connection_string, database):
		self._client = MongoClient(connection_string)
		self._db = self._client[database]

	def get_view(self, collection, filter=None):
		print(f"[DB] Getting view: {collection}")
		return list(self._db[collection].find(filter))

	def get_collection(self, collection):
		return self._db[collection]
