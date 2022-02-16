from app import client, recommendations
from bson.json_util import dumps

class Recommendations():

	@recommendations.route('/')
	def index():
		return "This is trends"

	@recommendations.route("/getRecommendations", methods=['GET'])
	def getRecommendations(self):
		print("Getting DublinBikes data from MongoDB")
		Dublin_Bikes = client.city_dashboard.Dublin_Bikes
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

		print("Calculating recommendations")

		return dumps({
			'mostEmptyBikeStationData':
			mostEmptyBikeStationData,
			'mostAvailableBikeStationData':
			mostAvailableBikeStationData
		})