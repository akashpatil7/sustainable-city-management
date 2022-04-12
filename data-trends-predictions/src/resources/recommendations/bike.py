from src.common.response import Response
from enum import Enum
from src.utils import closest_bike_stand

# -----------------------------------------------------------
# Creates a Bike class and functions to get bike availabiliy 
# and compare with pedestrian data
# -----------------------------------------------------------

class EndPointMethods(Enum):
    getRecommendations = "get_recommendations"
    getBikePedestrianRecommendations = "get_bike_pedestrian_recommendations"


class Bike():
    def __init__(self, db):
        self.db = db
        print("Initiating Bike Recommendations")

    def perform_action(self, action):
        try:
            return getattr(self, EndPointMethods[action].value)()
        except AttributeError:
            print("[Bike Recommendations] EndPoint cannot be resolved")
        return Response.not_found_404("Recommendations bike: " + action +
                                      " not found")

    def get_recommendations(self):
        """Get the most empty and most available bike stations."""
        print("[Bike Recommendations] Get")
        dublin_bikes = self.db.get_collection("Dublin_Bikes")

        most_empty_bike_station_data = list(
            dublin_bikes.find({}, {
                'name': True,
                'harvestTime': True,
                'availableBikeStands': True,
                'bikeStands': True,
                'availableBikes': True,
                '_id': False
            }).sort([("harvestTime", -1),
                     ("availableBikeStands", 1)]).limit(5))

        most_available_bike_station_data = list(
            dublin_bikes.find({}, {
                'name': True,
                'harvestTime': True,
                'availableBikeStands': True,
                'bikeStands': True,
                'availableBikes': True,
                '_id': False
            }).sort([("harvestTime", -1),
                     ("availableBikeStands", -1)]).limit(5))

        data = {
            'mostEmptyBikeStationData': most_empty_bike_station_data,
            'mostAvailableBikeStationData': most_available_bike_station_data
        }

        return Response.send_json_200(data)

    def get_bike_pedestrian_recommendations(self):
        """Get the bike station with the most bikes to move to areas of high population."""
        print("[Bike-Pedestrian Recommendations] Get")
        dublin_bikes = self.db.get_collection("Dublin_Bikes")
        pedestrian = self.db.get_collection("Pedestrian")

        # get 5 most filled bike stations
        most_available_bike_station_data = list(
            dublin_bikes.find({}, {
                'name': True,
                'harvestTime': True,
                'availableBikeStands': True,
                'bikeStands': True,
                'availableBikes': True,
                '_id': False,
                'latitude': True,
                'longitude': True
            }).sort([("harvestTime", -1), ("availableBikes", -1)]).limit(5))

        # get 5 most busy pedestrian areas
        highest_count_pedestrian_data = list(
            pedestrian.find({}, {
                'count': True,
                'street': True,
                'streetLatitude': True,
                'streetLongitude': True,
                'time': True
            }).sort([("time", -1), ("count", -1)]).limit(5))

        # for each busy area, calculuate the closest bike station that is full
        # recommend sending bikes from closest fullest station to this area
        move_bikes_from = []
        move_bikes_to = []

        for ped in highest_count_pedestrian_data:
            closest_stand = closest_bike_stand(
                ped, most_available_bike_station_data, move_bikes_from)

            if not closest_stand is None:
                move_bikes_from.append(closest_stand)
                move_bikes_to.append(ped)

        data = {'moveBikesFrom': move_bikes_from, 'moveBikesTo': move_bikes_to}

        return Response.send_json_200(data)
