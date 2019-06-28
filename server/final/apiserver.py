"""
Clase con el servidor Flask para el funcionamiento de la aplicación.
Autor: José Miguel Ramírez Sanz.
Colaboración: Sergio Chico Carrancio.
Versión: 1.0
"""

#imports
from flask import Flask
import csv
from flask import jsonify
from flask import request
import os
import base64
import datetime
import numpy as np
from sklearn.externals import joblib
import clip
import pandas as pd

app = Flask(__name__)

#Token de seguridad
TOKEN = "pmr0:dY|`BP~~nu#)}@7a6:Zg8>QKTR1zq.>4%1:8~dWe#*AayTDxQm82jUU!vU"

"""
Método que devuelve los nombres de los pacientes que se pueden interpretar.
"""
@app.route("/Nombres", methods=['POST'])
def getNombres():
	#Recogemos el token pasado por el dispositivo.
	tok = request.form["token"]

	#Si no es el mismo al nuestro mandamos un error 403.
	if tok != TOKEN:
		return jsonify(),403

	#Si no existe el fichero con los nombres en el servidor mandamos un error 500.
	if not (os.path.exists('pacientes.csv')):
		return jsonify(),500
	n= list()

	#Leemos el fichero con los nombres.
	with open('pacientes.csv', 'r') as nombres:
		csvr = csv.reader(nombres)
		for p in csvr:
			n += p
		nombres.close()
	#Devolvemos la lista de los nombres.
	return jsonify(n)

"""
Método que devuelve las opciones almacenadas en el servidor para un paciente.
"""
@app.route("/ObtOpciones", methods=['POST'])
def getOpcPac():
	#Paciente.
	pac = request.form["paciente"]
	#Token de seguridad.
	tok = request.form["token"]

	#Si el token no coincide error 403.
	if tok != TOKEN:
		return jsonify(),403
	pp = 'Opciones\\'+pac+'.csv'

	#Si el fichero con las opciones del paciente no existe error 500.
	if not (os.path.exists(pp)):
		return jsonify(),500
	n= list()

	#Leemos el fichero con las opciones del paciente seleccionado.
	with open('Opciones/' + pac + '.csv', 'r') as opc:
		csvr = csv.reader(opc)
		for p in csvr:
			n += p
		opc.close()
	#Devolvemos la lista con las opciones.
	return jsonify(n)

"""
Método que nos permite guardar las opciones pasadas para el paciente pasado.
"""
@app.route("/GuaOpciones", methods=['POST'])
def setOpcPac():
	#Paciente.
	pac = request.form["paciente"]
	#Token.
	tok = request.form["token"]

	#Si los tokens no coinciden error 403.
	if tok != TOKEN:
		return jsonify(),403
	pp = 'Opciones\\'+pac+'.csv'

	#Si el fichero con las opciones no existe error 500.
	if not (os.path.exists(pp)):
		return jsonify(),500
	l = list()

	#Obtenemos los valores
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

	#Eliminamos el fichero actual.
	os.remove('Opciones/' + pac + '.csv')

	#Escribimos los nuevos valores.
	with open('Opciones/' + pac + '.csv', 'w') as opc:
		csvw = csv.writer(opc)
		csvw.writerows([l])
		opc.close()
	#Devolvemos true.
	return jsonify("true")

"""
Método que nos permite interpretar una respuesta o una emoción.
"""
@app.route("/Clasifica", methods=['POST'])
def clasifica():
	#Paciente.
	pac = request.form["paciente"]
	#Tipo de interpretación, True emoción, False respuesta.
	tip = request.form["tipo"]
	if tip == "true":
		tipo = True
	else:
		tipo = False
	#Audio en base64.
	au = request.form["audio"]
	#Token de seguridad.
	tok = request.form["token"]

	#Si los tokens no coinciden error 403.
	if tok != TOKEN:
		return jsonify(),403

	#Path
	tp = os.path.dirname(os.path.realpath(__file__))
	ahora = datetime.datetime.now()
	#Nombre del audio en el servidor.
	autemp = tp+ '\\Temp\\' + str(ahora.day) + '-' + str(ahora.month) + ';' + str(ahora.hour) + '-' + str(ahora.minute) + '-' + str(ahora.second) +'.mp4'
	#Mientras exista un audio con ese nombre añadimos una v.
	while os.path.exists(autemp):
		autemp = autemp.split(".")[0]+'v.'+autemp.split(".")[1]
		print(autemp)
	#Decodificamos el audio.
	decau = base64.b64decode(au, ' /')
	#Escribimos el audio en el nombre que hemos obtenido antes.
	with open(autemp,'wb') as fau:
		fau.write(decau)
	#Si el paciente es JMiguel, el único con un clasificador real.
	if pac=="JMiguel":
		if tipo:
			pp = "Modelos\\rf_1_em.pkl"
		else:
			pp = "Modelos\\rf_1_sino.pkl"
		#Si no existe el modelo para predecir error 500.
		if not (os.path.exists(pp)):
			print("Hola")
			return jsonify(),500
		#Predecimos
		res=predecir(autemp,tipo,pp)
	else:
		#Sino creamos un resultado aleatorio.
		res = resultadoAleatorio(tipo)
	#Eliminamos el audio.
	os.remove(autemp)
	#Devolvemos la interpretación.
	return jsonify(res)

"""
Función que devuelve un resultado aleatorio.
Params: tipo: tipo de interpretación, True emoción, False respuesta si/no.
"""
def resultadoAleatorio(tipo):
    l=list()
    p=100
    res=[]
    r=0
    num = []
    v=0
    #Según el tipo ponemos la lista de posibles soluciones.
    if tipo:
        l=["hambre","tristeza","dolor","enfado"]
    else:
        l=["si","no"]
    #Creamos las probabilidades.
    while p > 0:
        if v == len(l)-1:
            num.append(p)
            break
        r = np.random.randint(0,p)
        if r==0:
            r+=1
        num.append(r)
        p -= r
        v+=1
    #Los ordenmos y asignamos a cada uno un valor de la lista con las emociones o si/no.
    num.sort(reverse=True)
    for i in num:
        ind = np.random.randint(0,len(l))
        res.append(l[ind]+":"+str("{0:.2f}".format(i)))
        l.pop(ind)
    return res

"""
Función que devuelve la interpretación de un audio para JMiguel.
Params: audio: ruta al audio.
		tipo: True emoción, False si/no.
		pp: ruta al modelo.
"""
def predecir(audio,tipo,pp):
	#Obtenemos el modelo.
	with open(pp,'rb') as p:
		model = joblib.load(p)
	l1=[]
	l2=[]
	#Obtenemos las características del audio.
	l1.append(clip.Clip(audio))
	l2.append(l1)
	test = create_set(l2)
	#Obtenemos la interpretación.
	r = model.predict_proba(test.loc[:, 'MFCC_1 mean':'ZCR std dev'])
	r=r[0]
	sol = []
	est = model.classes_
	#Asignamos a cada probabilidad su valor emoción o si/no.
	pos = sorted(range(len(r)), key=lambda k : r[k], reverse=True)
	for i in pos:
		sol.append(est[i].lower()+":"+str("{0:.2f}".format(r[i]*100)))
	#Devolvemos la lista con la solución.
	return sol

#Hecho por Sergio.
def create_set(clips):
    cases = pd.DataFrame()

    for c in range(0, len(clips)):
        
        for i in range(0, len(clips[c])):
            case = pd.DataFrame([clips[c][i].filename], columns=['filename'])
            case['category'] = c
            case['category_name'] = clips[c][i].category
            case['fold'] = i%6
            mfcc_mean = pd.DataFrame(np.mean(clips[c][i].mfcc[:, :], axis=0)[1:]).T
            mfcc_mean.columns = list('MFCC_{} mean'.format(i) for i in range(np.shape(clips[c][i].mfcc)[1]))[1:]
            mfcc_std = pd.DataFrame(np.std(clips[c][i].mfcc[:, :], axis=0)[1:]).T
            mfcc_std.columns = list('MFCC_{} std dev'.format(i) for i in range(np.shape(clips[c][i].mfcc)[1]))[1:]
            case = case.join(mfcc_mean)
            case = case.join(mfcc_std)
            
            case['ZCR mean'] = np.mean(clips[c][i].zcr)
            case['ZCR std dev'] = np.std(clips[c][i].zcr)

            cases = cases.append(case)
    
    cases[['category', 'fold']] = cases[['category', 'fold']].astype(int)
    return cases

if __name__ == '__main__':
	app.run(debug=True, host="0.0.0.0")