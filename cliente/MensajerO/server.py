from flask import Flask,request,jsonify
from flask_restful import Resource, Api
import random

app = Flask(__name__)
api = Api(app)

nombre = ''
password = ''
token = 'magic'


class login(Resource):
	def post(self):
		root = request.get_json(force=True)
		if root.get('id', '') == nombre and root.get('password', '') == password:
			return { "token": token }, 201
		else:
			return '', 401

class usuarios(Resource):
	def get(self):
		return  [ {"id": "foo", "nombre": "fake", "estado" : "conectado"}, {"id": "juan", "nombre": "Juan Perez", "estado" : "conectado"}, {"id": "carlos", "nombre": "Carlos Johnson", "estado" : "desconectado"} ] , 200


class usuario(Resource):
	def get(self, user_id):
		if request.args.get('token', '') == token and user_id == "adriano":
			return { "username": "Adriano Du Pastel", "ubicacion": "Brazil" }
		else:
			return '', 401
	def post(self, user_id):
		root = request.get_json(force=True)
		global nombre
		nombre = root['nombre']
		global password
		password = root['password']
		return '', 201

# Agregamos los URIs para cada recurso
api.add_resource(login, '/login')
api.add_resource(usuarios, '/usuarios')
api.add_resource(usuario, '/usuario/<user_id>')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
