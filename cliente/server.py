# Instalar lo que hace falta
# 1. sudo apt-get install python-pip
# 2. sudo pip install flask-restful
# Ponemos el script en un archivo server.py
# Para ejecutarlo 'python server.py'
# En la consola vemos la direccion y puerto para testear

from flask import Flask
from flask_restful import Resource, Api

app = Flask(__name__)
api = Api(app)

# Handler para cada URI
class Default(Resource):
	# Se puede definir para GET, POST, etc
	# Ver mas ejemplos aca http://f...content-available-to-author-only...s.org/en/latest/quickstart.html#resourceful-routing
    def get(self, path):
        return {'path': str(path), 'hello': 'world', 'array': ['one', 'two', 'three']}

# Agregamos los URIs para cada recurso
api.add_resource(Default, '/<string:path>')

if __name__ == '__main__':
    app.run(debug=True)
