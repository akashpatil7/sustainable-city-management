from datetime import datetime
from src.common.response import Response
from enum import Enum

# -----------------------------------------------------------
# Creates a Bike class and functions to get different data 
# trends from Dublin Bike
# -----------------------------------------------------------

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
		"""Determine which trend to return."""
		try:
			return getattr(self, EndPointMethods[action].value)()
		except KeyError:
			print("[Bike Trends] EndPoint not found")
		except AttributeError:
			print("[Bike Trends] EndPoint cannot be resolved")
		return Response.not_found_404("trends bike: " + action + " not found")


	def get_current_hour_averages(self):
		"""Get bike availability averages for each hour of the data."""
		print("[Bike Trends] get current hour averages")

		hour_filter = {'_id.hour': datetime.now().hour}
		print("Filter: ", hour_filter)

		data = self.db.get_view("Bike_AvgHourlyAvailability", hour_filter)
		return Response.send_json_200(data)


	def get_hourly_average_for_all_station(self):
		"""Get average bike availability for all stations for all hours."""
		print("[Bike Trends] get hourly average for all station")
		return self.fetch_view_send_response("Bike_AvgHourlyAvailability")


	def get_popularity_average_historical(self):
		"""Get the most popular bike stations."""
		print("[Bike Trends] get popularity average for all station historical")
		return self.fetch_view_send_response("Bike_PopularityAverageHistorical")


	def get_popularity_average_today(self):
		"""Get most popular bike stop for today."""
		print("[Bike Trends] get popularity average for all station today")
		return self.fetch_view_send_response("Bike_PopularityAverageToday")


	def fetch_view_send_response(self, collection_name):
		"""Get the data trends from the database."""
		data = self.db.get_view(collection_name)
		return Response.send_json_200(data)