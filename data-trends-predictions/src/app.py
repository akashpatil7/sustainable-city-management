from flask import Flask
from flask_restful import Api
from flask_cors import CORS
from pymongo import MongoClient
import py_eureka_client.eureka_client as eureka_client

from src.resources.trends import Trends
from src.resources.recommendations import Recommendations

# INIT Flask App
app = Flask(__name__)

# INIT Configs
app.config.from_pyfile('settings.py')
DB_CONNECTION_STRING = app.config.get('DB_CONNECTION_STRING')
FLASK_RUN_PORT = int(app.config.get('FLASK_RUN_PORT'))
EUREKA_SERVER_HOST = app.config.get('EUREKA_SERVER_HOST')
EUREKA_REGISTERED_APP_NAME = app.config.get('EUREKA_REGISTERED_APP_NAME')

EUREKA_SERVER_NAME = f'http://{EUREKA_SERVER_HOST}:8761/eureka'

# INIT Eureka Client
eureka_client.init(eureka_server = EUREKA_SERVER_NAME,
                   app_name = EUREKA_REGISTERED_APP_NAME,
                   instance_port = FLASK_RUN_PORT)

# INIT Cors
cors = CORS(app, resources={r"/*": {"origins": "*"}})

# INIT RESTFul API 
api = Api(app)

# INIT MongoClient 
client = MongoClient(DB_CONNECTION_STRING)
client_as_args = {'client': client}

# ADD Routes
api.add_resource(Trends, '/trends/<string:data_indicator>/<string:action>', resource_class_kwargs=client_as_args)
api.add_resource(Recommendations, '/recommendations/<string:data_indicator>/<string:action>', resource_class_kwargs=client_as_args)