#Práctica realizada por:Daniel Garcia Salinas(Grupo T3)


import random
from enum import IntEnum                        #Importamos las librerias para poder usarlas más tarde

Blanco='.'
Tienda='a'
Casa='b'
Mansion='c'
Edificio='d'
Hospital='e'
Bigfoot='1'
Bebe='2'
Escuela='3'
Universidad='4'
Wick='W'
Escombro='X'                                    #Fin de la declaración de constantes goblales de tipo carácter
class Tamanho(IntEnum):                         #Declaración de constantes globales de tipo entero con la libreria Intenum
    Fil=7
    Col=7
class Tablero:                                                  #Inicialización objeto tablero
    fil=Tamanho.Fil
    col=Tamanho.Col
    matriz = []

    def __init__(self):                                              #Constructor

        return None
    def crearTablero(self,diccionario):                                    #Crea el tablero (solo la matriz, la rellena despues)
        dato =""
        cont = 0
        cont2 = 1
        for i in range(self.fil):
            if i==0:
                self.matriz.append([dato])
            else:
                self.matriz.append([diccionario[cont]])
            cont +=1
            for j in range(self.col):
                if i==0:
                    self.matriz[i].append([cont2])
                    cont2 += 1
                else:
                    self.matriz[i].append([dato])
    def rellenarTablerofich(self,nombrefich):                   #Rellena tablero con datos fichero
        cont = 0
        texto = fichero.read()
        for i in range(1,self.fil):
            for j in range(1,self.col):
                if (texto[cont]!='\n'):
                    simbol=texto[cont]
                    self.matriz[i][j] = simbol
                if (texto[cont]=='\n'):
                    simbol=texto[cont+1]
                    self.matriz[i][j] = simbol
                    cont+=1
                cont+=1
        fichero.close()
    def rellenarTableroaleat(self):                                         #Rellena tablero con datos aleatorios
        lista=[]
        for i in range(45):
            lista.append(Blanco)
        for i in range(18):
            lista.append(Tienda)
        for i in range(4):
            lista.append(Casa)
        for i in range(3):
            lista.append(Mansion)
        for i in range(2):
            lista.append(Bigfoot)
        for i in range (1,self.fil):
            for j in range (1,self.col):
                self.matriz[i][j]=random.choice(lista)
    def dibujarTablero(self):                               #Dibuja el tablero por pantalla
        for i in range(self.fil):
            for j in range(self.col):
                if i == 0:
                    print(self.matriz[i][j], end="   ")
                else:
                    print(self.matriz[i][j], end="     ")
            print(" ")
    def insertarelemento(self,jugada,diccionario2,Actual):                          #Funcion que dependiendo del elemento actual,lo inserta o si es un Wick, borra el elemento de la casilla seleccionada
        if(Actual==Wick):
            self.matriz[int(diccionario2[jugada[0]])][int(jugada[1])]=Blanco
        else:
            self.matriz[int(diccionario2[jugada[0]])][int(jugada[1])]=Actual

    def comprobacionletras(self, matriz2,i, j):
        # Función recursiva. Funcionamiento: vamos poniendo en una matriz de booleanos(inicializada a Falso, las letras que coincidan con la letra insertada.
        # Esta matriz se la pasamos despues a la proxima funcion(colapso) para que junta todas las letras en una de nivel superior(sabe que letras juntrar porque son las que
        # estan en la posicion de los falsos de la matriz del Tablero
        if (i - 1 >= 1):
            if (self.matriz[i - 1][j] == self.matriz[i][j] and matriz2[i - 1][j] != True):
                matriz2[i - 1][j] = True
                self.comprobacionletras(matriz2, i - 1, j)
        if (i + 1 <= 6):
            if (self.matriz[i + 1][j] == self.matriz[i][j] and matriz2[i + 1][j] != True):
                matriz2[i + 1][j] = True
                self.comprobacionletras(matriz2, i + 1, j)
        if (j - 1 >= 1):
            if (self.matriz[i][j - 1] == self.matriz[i][j] and matriz2[i][j - 1] != True):
                matriz2[i][j - 1] = True
                self.comprobacionletras(matriz2, i, j - 1)
        if (j + 1 <= 6):
            if (self.matriz[i][j + 1] == self.matriz[i][j] and matriz2[i][j + 1] != True):
                matriz2[i][j + 1] = True
                self.comprobacionletras(matriz2, i, j + 1)
        return matriz2
    def colapso(self,matriz2,h,k):
        # Matriz que adquiere la matriz de booleanos de la función comprobacion letras y junta las letras cuya posicion en la matriz de booleanos este en True
        cont=0
        for i in range(self.fil):
            for j in range(self.col):
                if (matriz2[i][j]==True):
                    cont+=1
        if(cont>=3):
            caracter = self.matriz[h][k]
            for i in range(self.fil):
                for j in range(self.col):
                    if(matriz2[i][j]==True):
                        self.matriz[i][j]=Blanco
            if(caracter==Tienda):
                self.matriz[h][k] = Casa
            if(caracter == Casa):
                self.matriz[h][k] = Mansion
            if(caracter == Mansion):
                self.matriz[h][k] = Edificio
            if(caracter == Edificio):
                self.matriz[h][k] = Hospital
            if (caracter == Bebe):
                self.matriz[h][k] = Escuela
            if (caracter == Escuela):
                self.matriz[h][k] = Universidad
            dato=False
            reiniciamatriz(matriz2,dato)
            Tablero.comprobacionletras(matriz2,h,k)                             #Esta funcion se tiene que volver a llamar a si misma y a la de comprobacion de letras, ya que, si existe un colapso,
            Tablero.colapso(matriz2,h,k)                                        #se debe comprobar a la vez si la letra formada colapsa o no

    def encierro_colapso_bigfoot(self, matriz2,matriz3):                        #Funcion que comprueba si los bigfoots están o no encerrados, para convertirlos a bebe
        cont=0                                                                  #En caso de que 3 o más big foot se conviertan a bebe en un lugar cercano, esta funcion también se ocupara de colapsarlos, llamando a la funcion colapso y a la comprobacion letras
        for i in range(Tamanho.Fil):
            for j in range(Tamanho.Col):
                if (self.matriz[i][j] == Bigfoot):
                    cont=0
                    if(i+1<=6):
                        if(self.matriz[i+1][j]!=Blanco):
                            cont+=1
                    else:
                        if(i+1==7):
                            cont+=1
                    if(j+1<=6):
                        if(self.matriz[i][j+1]!=Blanco):
                            cont+=1
                    else:
                        if(j+1==7):
                            cont+=1
                    if(self.matriz[i - 1][j] != Blanco):
                        cont+=1
                    if(self.matriz[i][j-1]!=Blanco):
                        cont+=1
                    if (cont==4):
                        self.matriz[i][j] = Bebe
                        dato = False
                        matriz2 = reiniciamatriz(matriz2, dato)
                        matriz2 = Tablero.comprobacionletras(matriz2, i, j)
                        Tablero.colapso(matriz2, i, j)

    def mov_bigfoot(self,matriz2,matriz3):              #Funcion que se ocupa del movimiento de los big foots(matriz3=matriz que contiene las edades de los big foots)
        cont0 = 0                                       #cont0 = número total de big foots en partida
        lista=[0]
        for i in range(self.fil):                       #Como los big foots deben moverse en orden de mayor edad a menos, lo que hacemos es crear una lista con las edades de los bigfoots y ordenarla
            for j in range(self.col):
                if (self.matriz[i][j] == Bigfoot):
                    centinela=True
                    if(len(lista)==1):
                        lista.append(matriz3[i][j])
                    else:
                        for h in range(len(lista)):
                            if(lista[h]>matriz3[i][j]and centinela==True):
                                lista.insert(h-1,matriz3[i][j])
                                centinela=False
                            else:
                                if(centinela==True):
                                    lista.append(matriz3[i][j])
                                    centinela=False
                    cont0 += 1
        # Bucle
        cont1 = 0                                       #cont1 = número total de big foots que se han movido en este turno
        cont2 = 0                                       #cont2 = número total de big foots que se movieron en el turno anterior
        centinela2 = False
        while (cont0 != cont1 and centinela2 == False):
            cont2 = cont1
            for h in range(len(lista)-1,0,-1):          #Recorremos la lista y se van moviendo los big foots en ese orden
                for i in range(self.fil):
                    for j in range(self.col):
                        if (matriz3[i][j] == lista[h]):
                            if(matriz2[i][j] == False):
                                if (self.matriz[i - 1][j]==Blanco and i-1 >= 1 and self.matriz[i][j]==Bigfoot):
                                    if(matriz3[i][j]>=10):
                                        self.matriz[i][j] = Escombro            #En caso de que la edad del big foot sea mayor a 10, debe dejar un escombro
                                    else:
                                        self.matriz[i][j]= Blanco
                                    matriz2[i - 1][j] = True
                                    self.matriz[i - 1][j] = Bigfoot
                                    matriz3[i - 1][j] = matriz3[i][j]
                                    matriz3[i][j]=0
                                    cont1 += 1
                                if(j+1<=6):
                                    if (self.matriz[i][j + 1]==Blanco  and self.matriz[i][j]==Bigfoot):
                                        if (matriz3[i][j] >= 10):
                                            self.matriz[i][j] = Escombro
                                        else:
                                            self.matriz[i][j] = Blanco
                                        matriz2[i][j + 1] = True
                                        self.matriz[i][j + 1] = Bigfoot
                                        matriz3[i][j+1] = matriz3[i][j]
                                        matriz3[i][j] = 0
                                        cont1 += 1
                                if(i+1<=6):
                                    if(self.matriz[i+1][j]=='.' and self.matriz[i][j]==Bigfoot):
                                        if (matriz3[i][j] >= 10):
                                            self.matriz[i][j] = Escombro
                                        else:
                                            self.matriz[i][j] = Blanco
                                        matriz2[i + 1][j] = True
                                        self.matriz[i + 1][j] = Bigfoot
                                        matriz3[i + 1][j] = matriz3[i][j]
                                        matriz3[i][j] = 0
                                        cont1 += 1
                                if (self.matriz[i][j - 1]==Blanco and i-1 >= 1 and self.matriz[i][j]==Bigfoot):
                                    if (matriz3[i][j] >= 10):
                                        self.matriz[i][j] = Escombro
                                    else:
                                        self.matriz[i][j] = Blanco
                                    matriz2[i][j - 1] = True
                                    self.matriz[i][j - 1] = Bigfoot
                                    matriz3[i][j-1] = matriz3[i][j]
                                    matriz3[i][j] = 0
                                    cont1 += 1
            if (cont2 == cont1):
                centinela2 = True
    def inicializaredadbf(self,matriz3):                    #Funcion que en caso de que ya haya algun bigfoot en el tablaro al comenzar, inicializara su edad a 1
        for i in range(self.fil):
            for j in range(self.col):
                    if(self.matriz[i][j]==Bigfoot):
                        matriz3[i][j]=1
        return matriz3
    def puntos(self,diccionariopuntos):                     #Funcion que recorre el tablero y suma los distintos puntos
        puntos=0
        for i in range(1,self.fil):
            for j in range(1,self.col):
                puntos=puntos+diccionariopuntos[self.matriz[i][j]]
        return puntos

    def comprobacion_jugada_correcta(self,jugada,Actual,diccionario2):                  #Comprueba, tanto si la jugada se encuentra en las coordenadas correctas, como si la casilla del tablero esta vacia o no dependiendo de lo necesario
        centinela = False
        if (jugada == '*'):
            centinela = True
        if(len(jugada) == 2):
            if (jugada[0] >= 'A' and jugada[0] <= 'F') and (jugada[1] >= '1' and jugada[1] <= '6'):
                if(Actual==Wick):
                    if(self.matriz[int(diccionario2[jugada[0]])][int(jugada[1])]!=Blanco):
                        centinela=True
                else:
                    if(self.matriz[int(diccionario2[jugada[0]])][int(jugada[1])]==Blanco):
                        centinela=True
        return centinela
    def comprobacion_finjuego(self):                            #Comprueba si el tablero esta lleno(significa que se acabo el juego)
        centinela=False
        for i in range(self.fil):
            for j in range(self.col):
                if(self.matriz[i][j]==Blanco):
                    centinela=True
        return centinela
#Fin clase Tablero
#Comienzo funciones del main


def actualAleat():                              #Funcion que rellena una lista con los distintos valores de Actual y elige uno al azar para cada turno
    lista=[]
    Actual=[]
    for i in range(30):
        lista.append(Tienda)
    for i in range(5):
        lista.append(Casa)
    lista.append(Mansion)
    for i in range(6):
        lista.append(Bigfoot)
    lista.append(Wick)
    Actual=random.choice(lista)
    return Actual

def actualfich(cont,nombresec):
    fichero=open(nombresec,"r")
    texto=fichero.readline()
    car=texto[cont]
    fichero.close()
    return car

def draw(turnos,puntos,Actual,Almacen,):                                #Funcion que dibuja los turnos, el almacen, los puntos y el Actual
    print(f"Turnos:{turnos}", end="  ")
    print(f"Puntos:{puntos}")
    print(f"Actual:[{Actual}]", end="    ")
    print(f"Almacen:[{Almacen}]")

def crearmatriz(matriz,dato):                                       #Funcion que crea las matrices externas a la del tablero(la de booleanos y la de enteros correspondiente a las edades de los big foots)
    for i in range(Tamanho.Fil):
        matriz.append([dato])
        for j in range(Tamanho.Col):
            matriz[i].append(dato)
    return matriz

def reiniciamatriz(matriz,dato):                                            #Funcion que reinicia las matrices dependiendo de el dato que le pases(usado sobretodo para la de booleanos, ya que sus datos se pueden borrar y ser reutilizada)
    for i in range(Tamanho.Fil):
        for j in range (Tamanho.Col):
            matriz[i][j]=dato
    return matriz

def incremento_edad_bigfoot(matriz):                                        #Si en la casilla de la matriz3 hay un bigfoot, esta funcion incrementara su edad en 1)
    for i in range(Tamanho.Fil):
        for j in range(Tamanho.Col):
            if(matriz[i][j]!=0):
                matriz[i][j]+=1
    return matriz3

if __name__ == "__main__":
    diccionario={1:'A',2:'B',3:'C',4:'D',5:'E',6:'F'}                               #Diccionarios usados para convertir la primera columna de la matriz(coordendas en letras en coordenadas en numero, y viceversa) de forma mas sencilla
    diccionario2={'A':1,'B':2,'C':3,'D':4,'E':5,'F':6}
    Tablero = Tablero()
    Tablero.crearTablero(diccionario)
    nombrefich=input("Digame el nombre del fichero de tablero: ")                                  #Input para pedir el nombre del fichero en caso de que se desee
    if (nombrefich==""):
        Tablero.rellenarTableroaleat()                                                   #En caso de que se desee jugar con un tablero al azar, no se escribira nada y se rellenara automáticamente con la función escrita
    else:
        centinela = False
        while (centinela == False):  # Comprobacion de que el fichero existe y apertura
            try:
                fichero = open(nombrefich, "r")
                centinela = True
            except(FileNotFoundError):  # Si no se encuentra se vuelve a pedir
                print("El fichero no se ha podido encontrar")
                nombrefich = input("Por favor reescriba el nombre del fichero: ")
        Tablero.rellenarTablerofich(nombrefich)
    nombresec=input("Digame el nombre del fichero de secuencia: ")
    #Dibujar Tablero
    Tablero.dibujarTablero()
    turnos=1                                                                        #Inicialización de los turnos en 1
    diccionariopts = {Blanco: 0, Tienda: 1, Casa: 5, Mansion: 25, Edificio: 125, Hospital: 625, Bigfoot: -25, Bebe: -5, Escuela: 50, Universidad: 500,
                      Escombro: -50}                                                #Diccionario de puntos para que sea mas sencillo contar
    puntos = Tablero.puntos(diccionariopts)
    contsec=0
    if(nombresec==""):
        Actual = actualAleat()
    else:
        centinela=False
        while(centinela==False):
            try:
                fichero=open(nombresec,"r")
                centinela=True
            except(FileNotFoundError):
                print("El fichero no se ha podido encontrar")
                nombresec = input("Por favor reescriba el nombre del fichero: ")
        Actual=actualfich(contsec,nombresec)
        contsec+=1
    Almacen = ''
    Almacen2='.'                                                                        #Almacen 2 requerido para hacer bien la conversión Almacen<->Actual
    draw(turnos,puntos,Actual,Almacen)                                                  #Dibujo de turnos, puntos, Actual, Almacen
    matriz2 = []
    dato = False
    matriz2 = crearmatriz(matriz2, dato)                                                    #Inicialización de la matriz de booleanos
    matriz3 = []
    dato2 = 0
    matriz3 = crearmatriz(matriz3, dato2)                                               #Inicialización de la matriz de enteros correspondiente a las edades de los big foots
    matriz3 = Tablero.inicializaredadbf(matriz3)
    finjuego=Tablero.comprobacion_finjuego()
    while(finjuego==True):                                                                  #Bucle con los turnos del juego hasta que la funcion finjuego lo diga
        jugada=input("Escriba su jugada: ")                                             #Escritura jugada
        centinela=False
        while(centinela!=True):
            centinela=Tablero.comprobacion_jugada_correcta(jugada,Actual,diccionario2)          #Comprueba si jugada correcta
            if (centinela==False):
                print("Jugada errónea")
                jugada=input("Escriba su nueva jugada")                                     #En caso de que la jugada sea errónea la vuelve a pedir
        if jugada=='*':
            if(Almacen2=='.'):                                                          #Cambio del almacén a actual en caso de que jugada sea *
                Almacen2=Almacen
                Almacen=Actual
                if(nombresec==""):
                    Actual=actualAleat()
                else:
                    actualfich(contsec,nombrefich)
                    contsec += 1
            else:
                Almacen2=Almacen
                Almacen=Actual
                Actual=Almacen2
        else:
            h = int(diccionario2[jugada[0]])                                                #División de jugada en coordenadas(en este caso h=fila,k=columna) (por esto es útil el diccionario anterior)
            k = int(jugada[1])
            if(Actual==Wick):                                                       #Si actual es el Wick, llama a insertar elemento, el cual borrara el eemento de la casilla seleccionada
                Tablero.insertarelemento(jugada,diccionario2,Actual)
            else:
                if(Actual==Bigfoot):                                                    #Si actual=bigfoot solo es necesario insertarlo
                    matriz3[h][k]=1
                Tablero.insertarelemento(jugada, diccionario2, Actual)
                if(Actual==Tienda or Actual==Casa or Actual==Mansion):                  #Si actual es una letra, ademas de insertarla, hay q mirar colapsos
                    matriz2=reiniciamatriz(matriz2,dato)
                    matriz2=Tablero.comprobacionletras(matriz2,h,k)
                    Tablero.colapso(matriz2,h,k)
            matriz3=incremento_edad_bigfoot(matriz3)
            Tablero.encierro_colapso_bigfoot(matriz2,matriz3)
            dato=False
            reiniciamatriz(matriz2,dato)
            Tablero.mov_bigfoot(matriz2,matriz3)
            if(nombresec==""):
                Actual = actualAleat()
            else:
                Actual=actualfich(contsec,nombresec)
                contsec += 1
        Tablero.dibujarTablero()
        puntos=Tablero.puntos(diccionariopts)
        turnos+=1
        draw(turnos, puntos, Actual, Almacen)
        finjuego=Tablero.comprobacion_finjuego()
    print("FIN DEL JUEGO")
    print(f"PUNTUACIÓN FINAL: {puntos}")