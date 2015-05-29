from flask import Flask,request,jsonify
from flask_restful import Resource, Api
import random

app = Flask(__name__)
api = Api(app)


password = ''
ide = ''
nombre = ''
token = 'magic'
usertest = 'test'


class login(Resource):
	def post(self):
		root = request.get_json(force=True)
		if root.get('id', '') == ide and root.get('password', '') == password:
			return { "token": token }, 201
		else:
			return { "error": "clave o usuario incorrecto" } , 401

class usuarios(Resource):
	def get(self):
		if request.args.get('r_user', '') ==  ide and request.args.get('token', '') == token:
			return  [ {"id": "pedro", "nombre": "Pedro Gomez", "estado" : "conectado", "nuevomsg": "true" }, {"id": "juan", "nombre": "Juan Perez", "estado" : "conectado", "nuevomsg": "false"}, {"id": "carlos", "nombre": "Carlos Johnson", "estado" : "desconectado", "nuevomsg": "false"} ] , 200
		else:
			return { "error": "Token invalido" }, 401


class usuario(Resource):
	def get(self, user):
		if request.args.get('token', '') != token:
			return { "error": "Token invalido" }, 401
		else:
			if request.args.get('r_user', '') ==  usertest:
				return { "nombre": "Usuario de prueba", "ubicacion": "Brazil", "foto": "una foto" }, 200
			else:
				return { "error": "No existe el usuario" }, 401
	def post(self, user):
		root = request.get_json(force=True)
		global ide
		ide = user
		global nombre
		nombre = root['nombre']
		global password
		password = root['password']
		return '', 201
	def put(self, user):
		root = request.get_json(force=True)
		if request.args.get('token', '') == token and request.args.get('r_user', '') ==  usertest:
			global nombre
			nombre = root['nombre']
			print "nuevo nombre: " + nombre
			global password
			password = root['password']
			print "nuevo password: " + password
			return '', 201
		else:
			return {"error": "Token invalido no existe el usuario"}, 401
			

# Agregamos los URIs para cada recurso
api.add_resource(login, '/login')
api.add_resource(usuarios, '/usuarios')
api.add_resource(usuario, '/usuario/<user>')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
