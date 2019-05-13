from flask import Flask
import csv
from flask import jsonify
from flask import request
import os

app = Flask(__name__)

@app.route("/Nombres", methods=['POST'])
def getNombres():
	n= list()
	with open('pacientes.csv', 'r') as nombres:
		csvr = csv.reader(nombres)
		for p in csvr:
			n += p
		nombres.close()
	return jsonify(n)

@app.route("/ObtOpciones", methods=['POST'])
def getOpcPac():
	pac = request.form["paciente"]
	n= list()
	with open('Opciones/' + pac + '.csv', 'r') as opc:
		csvr = csv.reader(opc)
		for p in csvr:
			n += p
		opc.close()
	return jsonify(n)

@app.route("/GuaOpciones", methods=['POST'])
def setOpcPac():
	pac = request.form["paciente"]
	
	l = list()

	c1 = request.form["c1"]
	l.append(c1)
	c2 = request.form["c2"]
	l.append(c2)
	c3 = request.form["c3"]
	l.append(c3)
	c4 = request.form["c4"]
	l.append(c4)
	c5 = request.form["c5"]
	l.append(c5)
	c6 = request.form["c6"]
	l.append(c6)
	c7 = request.form["c7"]
	l.append(c7)

	s1 = request.form["s1"]
	l.append(s1)
	s2 = request.form["s2"]
	l.append(s2)

	os.remove('Opciones/' + pac + '.csv')

	with open('Opciones/' + pac + '.csv', 'w') as opc:
		csvw = csv.writer(opc)
		csvw.writerows([l])
		opc.close()
	return jsonify("true")

@app.route("/Clasifica", methods=['POST'])
def clasifica():
	pac = request.form["paciente"]
	au = request.form["audio"]
	return jsonify({"dolor":"75","enfado":"25"})

	return
if __name__ == '__main__':
	app.run(debug=True, host="0.0.0.0")