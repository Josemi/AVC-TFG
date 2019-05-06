from flask import Flask

app= Flask(__name__)

@app.route("/prueba1", methods=['POST'])
def prueba():
    return "Esto es una prueba"

@app.route("/prueba2", methods=['POST'])
def prueba2():
    return "Esto es una prueba"

if __name__ == '__main__':
	app.run(debug=False)