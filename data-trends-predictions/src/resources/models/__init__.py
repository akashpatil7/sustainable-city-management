from flask_restful import Resource
from .model_pedestrians import PedestrianModel
from .model_aqi import AqiModel
from .model_bikes import BikesModel
from .model_bus import BusModel
from .DublinBike import DublinBike
from src.resources.endpointsResolver import EndPointResolver

class Models(Resource):
    def __init__(self, **kwargs):
        self.db = kwargs['db']
        self.pedestrian_model = PedestrianModel(self.db)
        self.aqi_model = AqiModel(self.db)
        self.bikes_model = BikesModel(self.db)
        self.bus_model = BusModel(self.db)
        

    def get(self, data_indicator, action):
        if data_indicator == 'pedestrian':
            return EndPointResolver.perform_action(self.pedestrian_model, action)
        if data_indicator == 'aqi':
            return EndPointResolver.perform_action(self.aqi_model, action)
        if data_indicator == 'bikes':
            return EndPointResolver.perform_action(self.bikes_model, action)
        if data_indicator == 'bus':
            return EndPointResolver.perform_action(self.bus_model, action)
