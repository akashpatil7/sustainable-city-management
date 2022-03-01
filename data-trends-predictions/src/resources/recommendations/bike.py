from flask import make_response
from bson.json_util import dumps

class Bike():
	def __init__(self, client):
		self.client = client
		print("Initiating Bike Recommendations")

	def perform_action(self, action):
		if (action == "getRecommendations"):
			return self.get_recommendations()

		return "trends bike: " + action + " not found"

	def get_recommendations(self):
		print("[Bike Recommendations] Get")
		Dublin_Bikes = self.client.city_dashboard.Dublin_Bikes
		mostEmptyBikeStationData = list(
			Dublin_Bikes.find({}, {
				'name': True,
				'harvestTime': True,
				'availableBikeStands': True,
				'bikeStands': True,
				'availableBikes': True,
				'_id': False
			}).sort([("harvestTime", -1),
						("availableBikeStands", 1)]).limit(5))

		mostAvailableBikeStationData = list(
			Dublin_Bikes.find({}, {
				'name': True,
				'harvestTime': True,
				'availableBikeStands': True,
				'bikeStands': True,
				'availableBikes': True,
				'_id': False
			}).sort([("harvestTime", -1),
						("availableBikeStands", -1)]).limit(5))

		return make_response(dumps({
			'mostEmptyBikeStationData':
			mostEmptyBikeStationData,
			'mostAvailableBikeStationData':
			mostAvailableBikeStationData
		}))