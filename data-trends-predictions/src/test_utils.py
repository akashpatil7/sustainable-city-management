import unittest

from src.utils import (closest_bike_stand, get_avg_delay, get_most_polluted,
                       most_delayed_buses, top_aqi_locations, get_distance)


class UtilsTests(unittest.TestCase):
    def test_closest_bike_stand(self):
        print("[TEST] closest_bike_stand")
        example_loc = {
            "streetLatitude": "53.34799",
            "streetLongitude": "-6.26021"
        }
        locations = [{}, {
            "streetLatitude": "",
            "streetLongitude": ""
        }, example_loc, example_loc]

        close_station = {"latitude": "53.3568", "longitude": "-6.26814"}
        far_station = {"latitude": "54.3568", "longitude": "-7.26814"}
        station_data = [
            [],
            [{
                "latitude": "",
                "longitude": ""
            }],
            [close_station, far_station],
            [close_station, far_station],
        ]

        used_stands_data = [[], [], [], [close_station]]
        results = [None, None, close_station, far_station]

        for loc, stations, used_stands, res in zip(locations, station_data,
                                                   used_stands_data, results):
            closest_stand = closest_bike_stand(loc, stations, used_stands)
            assert closest_stand == res

    def test_get_distance(self):
        print("[TEST] get_distance")
        example_lat = "10"
        example_lon = "2"
        lats = ["5.5", "5.5", "", "5.5"]
        lons = ["-2", "4", "", "0"]

        results = [
            6.020797289396148, 4.924428900898052,
            float('inf'), 4.924428900898052
        ]

        for lat, lon, res in zip(lats, lons, results):
            dist = get_distance(lat, lon, example_lat, example_lon)
            assert dist == res

    def test_top_aqi_locations(self):
        print("[TEST] top_aqi_locations")
        aqi_station_data = []
        top_n = 0
        is_reverse = False
        top_locations = top_aqi_locations(aqi_station_data, top_n, is_reverse)
        assert top_locations == []

    def test_get_avg_delay(self):
        print("[TEST] get_avg_delay")
        bus = {}
        bus["stopSequence"] = []
        self.assertRaises(ZeroDivisionError, get_avg_delay, bus)

    def test_get_most_polluted(self):
        print("[TEST] get_most_polluted")
        buses = []
        aqis = []
        most_polluted = get_most_polluted(buses, aqis)
        assert most_polluted == {}

    def test_most_delayed_buses(self):
        print("[TEST] most_delayed_buses")
        buses = []
        most_polluted = most_delayed_buses(buses)
        assert most_polluted == []
