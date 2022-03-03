from datetime import datetime
from bson.json_util import dumps
from flask import make_response

class Bike():
	def __init__(self, client):
		self.client = client
		print("Initiating Bike Trends")

	def perform_action(self, action):
		if (action == "getCurrentHourAverages"):
			return self.get_current_hour_averages()
		elif (action == "getHourlyAverageForAllStation"):
			return self.get_hourly_average_for_all_station()

		return "trends bike: " + action + " not found"

	def get_current_hour_averages(self):
		print("[Bike Trends] get current hour averages")
		AvgHourlyAvailability = self.client.city_dashboard.AvgHourlyAvailability

		hourFilter = {'_id.hour': datetime.now().hour}
		print("Filter: ", hourFilter)

		data = AvgHourlyAvailability.find(filter=hourFilter)
		print("Returning data ... ")

		return make_response(dumps(list(data)))

	def get_hourly_average_for_all_station(self):
		print("[Bike Trends] get hourly average for all station")
		AvgHourlyAvailability = self.client.city_dashboard.AvgHourlyAvailability

		data = AvgHourlyAvailability.find()
		print("Returning data ... ")

		return make_response(dumps(list(data)))