from flask_restful import Resource
from .bike import Bike
from .aqi import Aqi
from .pedestrian import Pedestrian
from .bus import Bus

# -----------------------------------------------------------
# Determines which data indicator and which recommendation 
# to retrieve
# -----------------------------------------------------------

class Recommendations(Resource):
	def __init__(self, **kwargs):
		self.db = kwargs['db']
		self.bike = Bike(self.db)
		self.aqi = Aqi(self.db)
		self.pedestrian = Pedestrian(self.db)
		self.bus = Bus(self.db)

	def get(self, data_indicator, action):
		"""Call function from class depending on data indicator."""
		if data_indicator == 'bike':
			return self.bike.perform_action(action)
		if data_indicator == 'aqi':
			return self.aqi.perform_action(action)
		if data_indicator == 'pedestrian':
			return self.pedestrian.perform_action(action)
		if data_indicator == 'bus':
			return self.bus.perform_action(action)
