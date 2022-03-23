import math
import queue


def closest_bike_stand(location, bike_station_data, used_stands):
    closest_stand_dist = float('inf')
    closest_stand = None
    for stand in bike_station_data:

        distance = get_distance(stand['latitude'], stand['longitude'],
                                location['streetLatitude'],
                                location['streetLongitude'])
        if distance < closest_stand_dist and stand not in used_stands:
            closest_stand_dist = distance
            closest_stand = stand
    return closest_stand


def get_distance(stand_lat, stand_lon, loc_lat, loc_lon):
    if '' in (stand_lat, stand_lon, loc_lat, loc_lon):
        return float('inf')
    distance = math.sqrt((float(stand_lat) - float(loc_lat))**2 +
                         (float(stand_lon) - float(loc_lon))**2)
    return distance


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


def most_delayed_buses(buses):
    most_delayed = queue.PriorityQueue(maxsize=5)
    checked_buses = {}
    for bus in buses:
        if bus["routeLong"] in checked_buses:
            continue
        route = bus["routeLong"]
        checked_buses[route] = 1
        avg_delay = get_avg_delay(bus)
        if most_delayed.full():
            delay, _ = most_delayed.queue[0]
            if delay < avg_delay:
                most_delayed.get()
                most_delayed.put((avg_delay, bus["routeLong"]))
        else:
            most_delayed.put((avg_delay, bus["routeLong"]))
    return most_delayed.queue
