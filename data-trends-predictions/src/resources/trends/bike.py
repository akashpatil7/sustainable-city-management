from datetime import datetime
from src.common.response import Response


class Bike():
	def __init__(self, db):
		self.db = db
		print("Initiating Bike Trends")

	#Endpoint
	def getCurrentHourAverages(self):
		print("[Bike Trends] get current hour averages")

		hour_filter = {'_id.hour': datetime.now().hour}
		print("Filter: ", hour_filter)

		data = self.db.get_view("Bike_AvgHourlyAvailability", hour_filter)
		return Response.send_json_200(data)

    #Endpoint
	def getHourlyAverageForAllStation(self):
		print("[Bike Trends] get hourly average for all station")
		return self.fetch_view_send_response("Bike_AvgHourlyAvailability")

    #Endpoint
	def getPopularityAverageHistorical(self):
		print("[Bike Trends] get popularity average for all station historical")
		return self.fetch_view_send_response("Bike_PopularityAverageHistorical")

    #Endpoint
	def getPopularityAverageToday(self):
		print("[Bike Trends] get popularity average for all station today")
		return self.fetch_view_send_response("Bike_PopularityAverageToday")


	def fetch_view_send_response(self, collection_name):
		data = self.db.get_view(collection_name)
		return Response.send_json_200(data)