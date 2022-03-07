from datetime import datetime
from src.common.response import Response
from enum import Enum

class EndPointMethods(Enum):
	getCurrentHourAverages = "get_current_hour_averages"
	getHourlyAverageForAllStation = "get_hourly_average_for_all_station"
	getPopularityAverageHistorical = "get_popularity_average_historical"
	getPopularityAverageToday = "get_popularity_average_today"

class Bike():
	def __init__(self, db):
		self.db = db
		print("Initiating Bike Trends")


	def perform_action(self, action):
		try:
			return getattr(self, EndPointMethods[action].value)()
		except KeyError:
			print("[Bike Trends] EndPoint not found")
		except AttributeError:
			print("[Bike Trends] EndPoint cannot be resolved")
		return Response.not_found_404("trends bike: " + action + " not found")


	def get_current_hour_averages(self):
		print("[Bike Trends] get current hour averages")

		hour_filter = {'_id.hour': datetime.now().hour}
		print("Filter: ", hour_filter)

		data = self.db.get_view("Bike_AvgHourlyAvailability", hour_filter)
		return Response.send_json_200(data)


	def get_hourly_average_for_all_station(self):
		print("[Bike Trends] get hourly average for all station")
		return self.fetch_view_send_response("Bike_AvgHourlyAvailability")


	def get_popularity_average_historical(self):
		print("[Bike Trends] get popularity average for all station historical")
		return self.fetch_view_send_response("Bike_PopularityAverageHistorical")


	def get_popularity_average_today(self):
		print("[Bike Trends] get popularity average for all station today")
		return self.fetch_view_send_response("Bike_PopularityAverageToday")


	def fetch_view_send_response(self, collection_name):
		data = self.db.get_view(collection_name)
		return Response.send_json_200(data)