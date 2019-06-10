from flask import Flask
import csv
from flask import jsonify
from flask import request
import os
import base64
import cv2
import datetime
import numpy as np
from sklearn.externals import joblib
import clip
import pandas as pd

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
	if tip == "true":
		tipo = True
	else:
		tipo = False
	au = request.form["audio"]
	tok = request.form["token"]
	if tok != TOKEN:
		return jsonify(),403
	tp = os.path.dirname(os.path.realpath(__file__))
	ahora = datetime.datetime.now()
	autemp = tp+ '\\Temp\\' + str(ahora.day) + '-' + str(ahora.month) + ';' + str(ahora.hour) + '-' + str(ahora.minute) + '-' + str(ahora.second) +'.mp4'
	while os.path.exists(autemp):
		autemp = autemp.split(".")[0]+'v.'+autemp.split(".")[1]
		print(autemp)
	decau = base64.b64decode(au, ' /')
	with open(autemp,'wb') as fau:
		fau.write(decau)
	if pac=="JMiguel":
		if tipo:
			pp = "Modelos\\rf_1_em.pkl"
		else:
			pp = "Modelos\\rf_1_sino.pkl"
		if not (os.path.exists(pp)):
			print("Hola")
			return jsonify(),500
		res=predecir(autemp,tipo,pp)
	else:
		res = resultadoAleatorio(tipo)
	os.remove(autemp)
	return jsonify(res)

def resultadoAleatorio(tipo):
    l=list()
    p=100
    res=[]
    r=0
    num = []
    v=0
    if tipo:
        l=["hambre","tristeza","dolor","enfado"]
    else:
        l=["si","no"]
        
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
    num.sort(reverse=True)
    for i in num:
        ind = np.random.randint(0,len(l))
        res.append(l[ind]+":"+str("{0:.2f}".format(i)))
        l.pop(ind)
    return res

def predecir(audio,tipo,pp):
	with open(pp,'rb') as p:
		model = joblib.load(p)
	l1=[]
	l2=[]
	l1.append(clip.Clip(audio))
	l2.append(l1)
	test = create_set(l2)
	r = model.predict_proba(test.loc[:, 'MFCC_1 mean':'ZCR std dev'])
	r=r[0]
	sol = []
	est = model.classes_
	pos = sorted(range(len(r)), key=lambda k : r[k], reverse=True)
	for i in pos:
		sol.append(est[i].lower()+":"+str("{0:.2f}".format(r[i]*100)))
	return sol


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