from flask_restful import Resource
from .bike import Bike
from .aqi import Aqi
from .bus import Bus

class Recommendations(Resource):
	def __init__(self, **kwargs):
		self.db = kwargs['db']
		self.bike = Bike(self.db)
		self.aqi = Aqi(self.db)
		self.bus = Bus(self.db)

	def get(self, data_indicator, action):
		if data_indicator == 'bike':
			return self.bike.perform_action(action)
		if data_indicator == 'aqi':
			return self.aqi.perform_action(action)
		if data_indicator == 'bus':
    			return self.bus.perform_action(action)
