from flask import Flask
from flask_cors import CORS
from flask_restful import Api
import py_eureka_client.eureka_client as eureka_client
from src.common.database import Database
from src.resources.recommendations import Recommendations
from src.resources.trends import Trends
from src.resources.models import Models

def create_app():
	# INIT Flask App
	app = Flask(__name__)	

	# INIT Configs
	app.config.from_pyfile('settings.py')
	DB_CONNECTION_STRING = app.config.get('DB_CONNECTION_STRING')
	DB_NAME = app.config.get('DB_NAME')
	FLASK_RUN_PORT = app.config.get('FLASK_RUN_PORT')
	EUREKA_SERVER_HOST = app.config.get('EUREKA_SERVER_HOST')
	EUREKA_REGISTERED_APP_NAME = app.config.get('EUREKA_REGISTERED_APP_NAME')
	EUREKA_SERVER_NAME = app.config.get('EUREKA_SERVER_NAME')
	print(
		f'FLASK_RUN_PORT: {FLASK_RUN_PORT}\nEUREKA_REGISTERED_APP_NAME: {EUREKA_REGISTERED_APP_NAME}\nEUREKA_SERVER_NAME: {EUREKA_SERVER_NAME}'
	)
	cors = CORS(app, resources={r"/*": {"origins": "*"}})
	# INIT Eureka Client
	eureka_client.init(eureka_server=EUREKA_SERVER_NAME,
					   app_name=EUREKA_REGISTERED_APP_NAME,
					   instance_port=FLASK_RUN_PORT)

	# INIT RESTFul API
	api = Api(app, catch_all_404s=True)

	# INIT DB Object
	db = Database(connection_string=DB_CONNECTION_STRING, database=DB_NAME)

	# ADD Routes
	route_args = {'db': db}

	api.add_resource(
		Recommendations,
		'/recommendations/<string:data_indicator>/<string:action>',
		resource_class_kwargs=route_args)

	api.add_resource(Trends,
					 '/trends/<string:data_indicator>/<string:action>',
					 resource_class_kwargs=route_args)
	
	api.add_resource(Models, 
					'/models/<string:data_indicator>/<string:action>',
					resource_class_kwargs=route_args)
					 
	print("url map",app.url_map)

	return app
