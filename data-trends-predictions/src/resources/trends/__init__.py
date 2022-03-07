from flask_restful import Resource
from .bike import Bike

class Trends(Resource):
	def __init__(self, **kwargs):
		self.db = kwargs['db']
		self.bike = Bike(self.db)
	
	def get(self, data_indicator, action):
		print(f"Data Indicator: {data_indicator}, Action: {action}")
		if data_indicator == "bike":
			return self.bike.perform_action(action)
		
		return "trends: " + data_indicator + " not found"