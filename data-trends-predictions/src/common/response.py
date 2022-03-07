from bson.json_util import dumps
from flask import make_response

class Response:

	@staticmethod
	def send_json_200(data):
		print("Returning data ... ")
		response = make_response(dumps(data))
		response.status_code = 200
		response.headers["Content-Type"] = "application/json"
		return response

	@staticmethod
	def not_found_404(message):
		data = {
			"error": "EndPoint not found",
			"message": message
		}
		response = make_response(dumps(data))
		response.status_code = 404
		response.headers["Content-Type"] = "application/json"
		return response