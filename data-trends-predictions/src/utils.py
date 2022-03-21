import math


def closest_bike_stand(location, bike_station_data, used_stands):
    closest_stand_dist = float('inf')
    closest_stand = None
    for stand in bike_station_data:

        distance = math.sqrt((float(stand['latitude']) -
                              float(location['streetLatitude']))**2 +
                             (float(stand['longitude']) -
                              float(location['streetLongitude']))**2)
        if distance < closest_stand_dist and stand not in used_stands:
            closest_stand_dist = distance
            closest_stand = stand
    return closest_stand


def top_aqi_locations(aqi_station_data, top_n, is_reverse):
    aqi_station_data = [x for x in aqi_station_data if x["aqi"] != "-"]
    aqi_station_data.sort(key=lambda x: int(x["aqi"]), reverse=is_reverse)
    aqi_station_data = aqi_station_data[0:top_n]
    return aqi_station_data


def get_avg_delay(bus):
    total_delay = 0
    index = 0
    for stop in bus["stopSequence"]:
        total_delay += stop["arrivalDelay"]
        index += 1
    return total_delay / index


def get_highest_aqi(db):
    aqi = db.get_collection("Aqi")
    highest_aqi_station = list(
        aqi.find({}, {
            'aqi': True,
            'stationName': True,
            'latitude': True,
            'longitude': True,
        }).sort([
            ("aqi", -1),
        ]))
    highest_aqi_station = list(
        filter(lambda x: x["aqi"] != "-", highest_aqi_station))
    highest_aqi_station.sort(key=lambda x: int(x["aqi"]), reverse=True)
    highest_aqi_station = highest_aqi_station[0:5]
    return highest_aqi_station


def get_most_polluted(buses, aqis):
    most_polluted = {}
    for aqi in aqis:
        closest_stop_dist = float('inf')
        closest_stop = None

        checked_buses = {}
        for bus in buses:
            if bus["routeLong"] in checked_buses:
                continue
            route = bus["routeLong"]
            checked_buses[route] = 1
            for stop in bus["stopSequence"]:
                lat_dist = (float(stop['stopLat']) - float(aqi['latitude']))**2
                lon_dist = (float(stop['stopLon']) -
                            float(aqi['longitude']))**2
                distance = math.sqrt(lat_dist + lon_dist)
                if distance < closest_stop_dist:
                    closest_stop_dist = distance
                    closest_stop = bus
        most_polluted[closest_stop['routeLong']] = aqi['aqi']
    return most_polluted
