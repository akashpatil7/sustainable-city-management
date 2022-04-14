from flask_restful import Resource
from .bike import Bike
from .aqi import Aqi
from .pedestrian import Pedestrian
from .bus import Bus
from src.resources.endpointsResolver import EndPointResolver

class Recommendations(Resource):
	def __init__(self, **kwargs):
		self.db = kwargs['db']
		self.bike = Bike(self.db)
		self.aqi = Aqi(self.db)
		self.pedestrian = Pedestrian(self.db)
		self.bus = Bus(self.db)

	def get(self, data_indicator, action):
		if data_indicator == 'bike':
			return EndPointResolver.perform_action(self.bike, action)
		if data_indicator == 'aqi':
			return EndPointResolver.perform_action(self.aqi, action)
		if data_indicator == 'pedestrian':
			return EndPointResolver.perform_action(self.pedestrian, action)
		if data_indicator == 'bus':
			return EndPointResolver.perform_action(self.bus, action)
