from flask import Flask
import csv
from flask import jsonify

app = Flask(__name__)

@app.route("/Nombres", methods=['POST'])
def getNombres():
	n= list()
	with open('pacientes.csv', 'rb') as nombres:
		csvr = csv.reader(nombres)
		for p in csvr:
			n.append(p)
	return jsonify({0:n})

if __name__ == '__main__':
	app.run(debug=True)