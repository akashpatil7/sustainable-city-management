from src.common.response import Response
from enum import Enum

class EndPointMethods(Enum):
	getRecommendations = "get_recommendations"

class Bike():
	def __init__(self, db):
		self.db = db
		print("Initiating Bike Recommendations")

	def perform_action(self, action):
		try:
			return getattr(self, EndPointMethods[action].value)()
		except KeyError:
			print("[Bike Recommendations] EndPoint not found")
		except AttributeError:
			print("[Bike Recommendations] EndPoint cannot be resolved")
		return Response.not_found_404("Recommendations bike: " + action + " not found")

	def get_recommendations(self):
		print("[Bike Recommendations] Get")
		dublin_bikes = self.db.get_collection("Dublin_Bikes")
		
		most_empty_bike_station_data = list(
			dublin_bikes
				.find({}, {
						'name': True,
						'harvestTime': True,
						'availableBikeStands': True,
						'bikeStands': True,
						'availableBikes': True,
						'_id': False
					}
				)
				.sort([
					("harvestTime", -1),
					("availableBikeStands", 1)
				])
				.limit(5)
		)

		most_available_bike_station_data = list(
			dublin_bikes.find({}, {
				'name': True,
				'harvestTime': True,
				'availableBikeStands': True,
				'bikeStands': True,
				'availableBikes': True,
				'_id': False
			}).sort([("harvestTime", -1),
						("availableBikeStands", -1)]).limit(5))

		data = {
			'mostEmptyBikeStationData':
			most_empty_bike_station_data,
			'mostAvailableBikeStationData':
			most_available_bike_station_data
		}
		return Response.send_json_200(data)
		
		# make_response(dumps({
		# 	'mostEmptyBikeStationData':
		# 	mostEmptyBikeStationData,
		# 	'mostAvailableBikeStationData':
		# 	mostAvailableBikeStationData
		# }))