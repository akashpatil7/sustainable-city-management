from flask_restful import Resource
from .bike import Bike

class Recommendations(Resource):
	def __init__(self, **kwargs):
		self.client = kwargs['client']
		self.bike = Bike(self.client)
	
	def get(self, data_indicator, action):
		return self.bike.perform_action(action)