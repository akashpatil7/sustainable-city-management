from flask import Flask
from flask_restful import Api
from flask_cors import CORS
from pymongo import MongoClient
import argparse
import py_eureka_client.eureka_client as eureka_client

from src.resources.trends import Trends
from src.resources.recommendations import Recommendations

#parser = argparse.ArgumentParser()
#parser.add_argument('--env', help='Evironment in which to run', type=str)
#args = parser.parse_args()
host = "localhost"
rest_port = 8050
#if args.env:
    #host = args.env

eureka_client.init(eureka_server=f'http://{host}:8761/eureka',
                   app_name="trends",
                   instance_port=rest_port)

app = Flask(__name__)
cors = CORS(app, resources={r"/*": {"origins": "*"}})
api = Api(app)

client = MongoClient("mongodb+srv://admin:admin@cluster1.varva.mongodb.net/city_dashboard")
client_as_args = {'client': client}

api.add_resource(Trends, '/trends/<string:data_indicator>/<string:action>', resource_class_kwargs=client_as_args)
api.add_resource(Recommendations, '/recommendations/<string:data_indicator>/<string:action>', resource_class_kwargs=client_as_args)