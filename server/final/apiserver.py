from flask import Flask
import csv
from flask import jsonify
from flask import request
import os
import base64
import cv2
import datetime

app = Flask(__name__)

TOKEN = "pmr0:dY|`BP~~nu#)}@7a6:Zg8>QKTR1zq.>4%1:8~dWe#*AayTDxQm82jUU!vU"

@app.route("/Nombres", methods=['POST'])
def getNombres():
	tok = request.form["token"]
	if tok != TOKEN:
		return jsonify(),403
	if not (os.path.exists('pacientes.csv')):
		return jsonify(),500
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
	tok = request.form["token"]
	if tok != TOKEN:
		return jsonify(),403
	pp = 'Opciones\\'+pac+'.csv'
	if not (os.path.exists(pp)):
		return jsonify(),500
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
	tok = request.form["token"]
	if tok != TOKEN:
		return jsonify(),403
	pp = 'Opciones\\'+pac+'.csv'
	if not (os.path.exists(pp)):
		return jsonify(),500
	l = list()

	c1 = request.form["v1"]
	l.append(c1)
	c2 = request.form["v2"]
	l.append(c2)
	c3 = request.form["v3"]
	l.append(c3)
	c4 = request.form["v4"]
	l.append(c4)
	c5 = request.form["v5"]
	l.append(c5)
	c6 = request.form["v6"]
	l.append(c6)
	c7 = request.form["v7"]
	l.append(c7)

	s1 = request.form["v8"]
	l.append(s1)
	s2 = request.form["v9"]
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
	tip = request.form["tipo"]
	au = request.form["audio"]
	tok = request.form["token"]
	if tok != TOKEN:
		return jsonify(),403
	#POR IMPLEMENTAR LA COMPROBACIÓN DEL FICHERO DE MODELO
	tp = os.path.dirname(os.path.realpath(__file__))
	ahora = datetime.datetime.now()
	autemp = tp+ '\\Temp\\' + str(ahora.day) + '-' + str(ahora.month) + ';' + str(ahora.hour) + '-' + str(ahora.minute) + '-' + str(ahora.second) +'.mp4'
	while os.path.exists(autemp):
		autemp = 'v' + autemp
	decau = base64.b64decode(au, ' /')
	with open(autemp,'wb') as fau:
		fau.write(decau)

	#os.remove(autemp)
	return jsonify(["tristeza:75","enfado:25"])

if __name__ == '__main__':
	app.run(debug=True, host="0.0.0.0")