from datetime import datetime
from bson.json_util import dumps
from flask import make_response


class Bike():
	def __init__(self, client):
		self.client = client
		print("Initiating Bike Trends")


	def perform_action(self, action):
		print("Performing: " + action)
		if (action == "getCurrentHourAverages"):
			return self.get_current_hour_averages()
		elif (action == "getHourlyAverageForAllStation"):
			return self.get_hourly_average_for_all_station()
		elif (action == "getHistoricalPopularityAverage"):
			return self.get_popularity_average_historical()

		return "trends bike: " + action + " not found"


	def get_current_hour_averages(self):
		print("[Bike Trends] get current hour averages")
		avg_hourly_availability = self.client.city_dashboard.Bike_AvgHourlyAvailability

		hour_filter = {'_id.hour': datetime.now().hour}
		print("Filter: ", hour_filter)

		data = avg_hourly_availability.find(filter=hour_filter)
		self.send_response(data)


	def get_hourly_average_for_all_station(self):
		print("[Bike Trends] get hourly average for all station")
		self.fetch_data_send_response("Bike_AvgHourlyAvailability")
		# avg_hourly_availability = self.client.city_dashboard.Bike_AvgHourlyAvailability

		# data = avg_hourly_availability.find()
		# self.send_response(data)


	def get_popularity_average_historical(self):
		print("[Bike Trends] get popularity average for all station historical")
		self.fetch_data_send_response("Bike_PopularityAverageHistorical")
		# popularity_average_historical = self.client.city_dashboard.Bike_PopularityAverageHistorical

		# data = popularity_average_historical.find()
		# self.send_response(data)


	def fetch_data_send_response(self, collection_name):
		collection = self.client.city_dashboard[collection_name]
		print(collection)
		data = collection.find()
		print(dumps(list(data)))
		self.send_response(data)


	def send_response(self, data):
		print("Returning data ... ")
		#return make_response(dumps(list(data)))
		return (dumps(list(data)))