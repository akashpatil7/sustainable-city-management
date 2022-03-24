from flask_restful import Resource
from .model_pedestrians import PedestrianModel
from .model_aqi import AqiModel

class Models(Resource):
    def __init__(self, **kwargs):
        self.db = kwargs['db']
        self.pedestrian_model = PedestrianModel(self.db)
        self.aqi_model = AqiModel(self.db)
        #self.bikes_model = Bikes_model(self.db)
        

    def get(self, data_indicator, action):
        if data_indicator == 'pedestrian':
            return self.pedestrian_model.perform_action(action)
        if data_indicator == 'aqi':
            return self.aqi_model.perform_action(action)