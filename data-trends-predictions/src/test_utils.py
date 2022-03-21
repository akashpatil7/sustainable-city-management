import unittest
from src.utils import closest_bike_stand, top_aqi_locations, get_avg_delay, get_most_polluted, most_delayed_buses


class UtilsTests(unittest.TestCase):
    def test_closest_bike_stand(self):
        print("[TEST] closest_bike_stand")
        location = []
        bike_station_data = []
        used_stands = []
        closest_stand = closest_bike_stand(location, bike_station_data,
                                           used_stands)
        assert closest_stand is None

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
        