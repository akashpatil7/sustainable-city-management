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
        small = {"aqi": "1"}
        med = {"aqi": "2"}
        big = {"aqi": "51"}
        aqi_station_data = [[], [{
            "aqi": "-"
        }], [small], [small, med], [small, med], [small, med, big],
                            [small, med, big]]

        top_n = [1, 1, 1, 1, 1, 2, 2]
        is_reverse = [False, False, False, False, True, False, True]
        results = [[], [], [small], [small], [med], [small, med], [big, med]]

        for data, n, reverse, res in zip(aqi_station_data, top_n, is_reverse,
                                         results):
            top_locations = top_aqi_locations(data, n, reverse)
            assert top_locations == res

    def test_get_avg_delay(self):
        print("[TEST] get_avg_delay")
        error_bus = {"stopSequence": []}
        bus_one = {"stopSequence": [{"arrivalDelay": 1}]}
        bus_two = {"stopSequence": [{"arrivalDelay": 1}, {"arrivalDelay": 3}]}
        bus_three = {"stopSequence": [{"arrivalDelay": 0}]}

        self.assertRaises(ZeroDivisionError, get_avg_delay, error_bus)
        avg_delay = get_avg_delay(bus_one)
        assert avg_delay == 1
        avg_delay = get_avg_delay(bus_two)
        assert avg_delay == 2
        avg_delay = get_avg_delay(bus_three)
        assert avg_delay == 0

    def test_get_most_polluted(self):
        print("[TEST] get_most_polluted")

        example_aqi = {"aqi": 10, "longitude": 0, "latitude": 0}
        missing_lat = {"aqi": 10, "longitude": 0}

        example_bus_1 = {
            "routeLong": "A",
            "stopSequence": [{
                "stopLat": 1,
                "stopLon": 2
            }]
        }
        example_bus_2 = {
            "routeLong": "B",
            "stopSequence": [{
                "stopLat": 2,
                "stopLon": -4
            }]
        }
        missing_lon = {"routeLong": "B", "stopSequence": [{"stopLat": 1}]}

        bus_data = [[], [], [missing_lon], [example_bus_1, example_bus_2]]
        aqi_data = [[], [example_aqi], [missing_lat], [example_aqi]]
        results = [{}, {}, {}, {"A": 10}]

        for aqis, buses, res in zip(aqi_data, bus_data, results):
            most_polluted = get_most_polluted(buses, aqis)
            assert most_polluted == res

    def test_most_delayed_buses(self):
        print("[TEST] most_delayed_buses")
        buses = []
        most_polluted = most_delayed_buses(buses)
        assert most_polluted == []
