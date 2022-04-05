import math
import queue
import numpy as np
from collections import defaultdict
import statistics
from datetime import datetime

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
        if "latitude" not in aqi or "longitude" not in aqi:
            continue
        closest_stop_dist = float('inf')
        closest_stop = None

        checked_buses = {}
        for bus in buses:
            if "routeLong" not in bus or bus["routeLong"] in checked_buses:
                continue
            route = bus["routeLong"]
            checked_buses[route] = 1
            if "stopSequence" not in bus:
                continue
            for stop in bus["stopSequence"]:
                if "stopLat" not in stop or "stopLon" not in stop:
                    continue
                lat_dist = (float(stop['stopLat']) - float(aqi['latitude']))**2
                lon_dist = (float(stop['stopLon']) -
                            float(aqi['longitude']))**2
                distance = math.sqrt(lat_dist + lon_dist)
                if distance < closest_stop_dist:
                    closest_stop_dist = distance
                    closest_stop = bus
        if closest_stop is not None:
            most_polluted[closest_stop['routeLong']] = aqi['aqi']
    return most_polluted


def most_delayed_buses(buses):
    most_delayed = queue.PriorityQueue(maxsize=5)
    checked_buses = {}
    for bus in buses:
        if "routeLong" not in bus or bus["routeLong"] in checked_buses:
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
    most_delayed = most_delayed.queue
    most_delayed.reverse()
    return most_delayed


def get_testing_data_using_epoch(x_values, unixTime):
    epoch_times = []
    for i in range(len(x_values.values())):
        epoch_times.append(unixTime)

    return np.column_stack((list(x_values.values()), epoch_times))

def update_average_departure_delays_for_predictions(db):
    average_delays = defaultdict(list)
    collection = db.get_collection('DBus_Historical')
    data = collection.find({
        'routeShort': '7'
    })
    for doc in data:
        for stop in doc['stopSequence']:
            stop_ = db.get_collection('DBus_Stops').find_one({
                    'stop_id': stop['stopId']
                })

            stop_number_index = stop_['stop_name'].index('stop ') + 5
            average_delays[stop_['stop_name'][stop_number_index:]].append(stop['departureDelay'])


    store_to_db = []
    for stop in average_delays.keys():
        entry = {'stop_number': stop, 'average_departure_delay': statistics.mean(average_delays[stop])}
        store_to_db.append(entry)
    
    new_entry = {"$set": {"average_departure_delays": store_to_db, "last_updated": datetime.now()}}
    info = db.get_collection('predictive_models').update_one({"task": 'predictions'}, new_entry)

    return info
