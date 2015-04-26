from flask import Flask,request,jsonify
from flask_restful import Resource, Api
import random

app = Flask(__name__)
api = Api(app)

magic = "FHJWIJWFIWEFKDFMDK"

class Login(Resource):
        def post(self):
                root = request.get_json(force=True)
                if root.get('user', '') == 'pepe':
                        return { "token": magic }, 201
                else:
                        return '', 401
                end

class Usuarios(Resource):
        def get(self):
		return  [ {"nombre": "pedro", "estado" : "online"}, {"nombre": "juan", "estado" : "online"}, {"nombre": "carlos", "estado" : "offline"} ] , 200
               

class Usuario(Resource):
        def get(self, user_id):
                if request.args.get('token', '') == magic and user_id == "adriano":
                        return { "username": "Adriano Du Pastel", "ubicacion": "Brazillllllll" }
                else:
                        return '', 401

# Agregamos los URIs para cada recurso
api.add_resource(Login, '/Login')
api.add_resource(Usuarios, '/Usuario')
api.add_resource(Usuario, '/Usuario/<user_id>')

if __name__ == '__main__':
    app.run(host='0.0.0.0', debug=True)
