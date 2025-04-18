"""ANTÓN SANZ,LUIS;GARCÍA SALINAS;DANIEL"""
#!/usr/bin/env python3
# -*- coding: UTF-8 -*-
#
# generated by wxGlade 1.0.5 on Wed May  3 17:14:36 2023
#
"""NOTA: A VECES EL PROGRAMA SE QUEDA PILLADO Y NO COLAPSA CORRECTAMENTE. PARA ARREGLAR ESTO, SIMPLEMENTE MINIMICE LA PANTALLA Y VUELVA A
ABRIRLA Y TODO DEBERÍA ESTAR CORRECTAMENTE"""
import wx
from wx.adv import Sound
import suburbious_1


# begin wxGlade: dependencies
# end wxGlade

# begin wxGlade: extracode
# end wxGlade


class SuburbiousFrame(wx.Frame):
    def __init__(self, *args, **kwds):
        # begin wxGlade: SuburbiousFrame.__init__
        kwds["style"] = kwds.get("style", 0) | wx.DEFAULT_FRAME_STYLE
        wx.Frame.__init__(self, *args, **kwds)
        self.SetSize((477, 300))
        self.SetTitle("frame")

        sizer_1 = wx.BoxSizer(wx.VERTICAL)

        self.sizer_2 = wx.BoxSizer(wx.HORIZONTAL)
        sizer_1.Add(self.sizer_2, 1, wx.EXPAND, 0)

        sizer_3 = wx.BoxSizer(wx.VERTICAL)
        self.sizer_2.Add(sizer_3, 1, wx.EXPAND, 0)

        grid_sizer_1 = wx.GridSizer(1, 2, 0, 0)
        sizer_3.Add(grid_sizer_1, 1, wx.EXPAND, 0)

        self.Boton_nueva_partida = wx.Button(self, wx.ID_ANY, "Nueva partida")  # Creación del botón de Nueva Partida
        self.Bind(wx.EVT_BUTTON, self.NuevaPartida, self.Boton_nueva_partida)
        grid_sizer_1.Add(self.Boton_nueva_partida, 0, wx.ALIGN_CENTER, 0)

        self.Checkbox_1 = wx.CheckBox(self, wx.ID_ANY, "Limite de tiempo")  # Creación Checkbox para el limite de tiempo
        grid_sizer_1.Add(self.Checkbox_1, 0, wx.ALIGN_CENTER, 0)
        self.Bind(wx.EVT_CHECKBOX, self.TimerReset, self.Checkbox_1)

        sizer_4 = wx.BoxSizer(wx.HORIZONTAL)
        sizer_3.Add(sizer_4, 1, wx.EXPAND, 0)

        self.sizer_Almacen = wx.StaticBoxSizer(wx.StaticBox(self, wx.ID_ANY, "Almacen"),wx.VERTICAL)  # Sizer con título para Almacen
        sizer_4.Add(self.sizer_Almacen, 1, wx.EXPAND, 0)

        self.Almacen = suburbious_1.Elem(suburbious_1.TipoElem.VACIO)
        self.label_Almacen = wx.StaticText(self, wx.ID_ANY, self.Almacen.car)
        self.label_Almacen.SetFont(wx.Font(20, wx.FONTFAMILY_DEFAULT, wx.FONTSTYLE_NORMAL, wx.FONTWEIGHT_BOLD, 0, ""))
        self.sizer_Almacen.Add(self.label_Almacen, 0, wx.ALIGN_CENTER_HORIZONTAL, 0)

        self.boton_intercambiar = wx.Button(self, wx.ID_ANY,"INTERCAMBIAR")  # Creación botón Intercambio (intercambiar elemento entre actual y almacén)
        self.Bind(wx.EVT_BUTTON, self.INTERCAMBIO,self.boton_intercambiar)  # Cuando se pulse el botón intercambiar, realizar la función INTERCAMBIO
        sizer_4.Add(self.boton_intercambiar, 0, wx.ALIGN_CENTER_VERTICAL, 0)

        self.sizer_Actual = wx.StaticBoxSizer(wx.StaticBox(self, wx.ID_ANY, "Actual"),wx.VERTICAL)  # Sizer con título para Actual
        sizer_4.Add(self.sizer_Actual, 1, wx.EXPAND, 0)

        self.label_Actual = wx.StaticText(self, wx.ID_ANY, ".")  # Texto que representa el elemento actual
        self.label_Actual.SetFont(wx.Font(20, wx.FONTFAMILY_DEFAULT, wx.FONTSTYLE_NORMAL, wx.FONTWEIGHT_BOLD, 0, ""))
        self.sizer_Actual.Add(self.label_Actual, 0, wx.ALIGN_CENTER_HORIZONTAL, 0)

        self.sizer_Puntuacion = wx.StaticBoxSizer(wx.StaticBox(self, wx.ID_ANY, "Puntuación"),wx.HORIZONTAL)  # Sizer con título para Puntuación
        sizer_3.Add(self.sizer_Puntuacion, 1, wx.EXPAND, 0)

        self.Puntuacion = '0'
        self.label_Puntuacion = wx.StaticText(self, wx.ID_ANY,self.Puntuacion)  # Creación de texto estático para Puntuación
        self.label_Puntuacion.SetFont(wx.Font(20, wx.FONTFAMILY_DEFAULT, wx.FONTSTYLE_NORMAL, wx.FONTWEIGHT_BOLD, 0, ""))
        self.sizer_Puntuacion.Add(self.label_Puntuacion, 0, wx.ALIGN_CENTER_VERTICAL, 0)

        self.sizer_Tiempo = wx.StaticBoxSizer(wx.StaticBox(self, wx.ID_ANY, "Tiempo restante:"),wx.HORIZONTAL)  # Sizer con título para Tiempo
        sizer_3.Add(self.sizer_Tiempo, 1, wx.EXPAND, 0)

        self.contador = 10
        self.label_Tiempo = wx.StaticText(self, wx.ID_ANY, str(self.contador))
        self.label_Tiempo.SetFont(wx.Font(20, wx.FONTFAMILY_DEFAULT, wx.FONTSTYLE_NORMAL, wx.FONTWEIGHT_BOLD, 0, ""))
        self.sizer_Tiempo.Add(self.label_Tiempo, 0, wx.ALIGN_CENTER_VERTICAL, 0)

        self.label_1 = wx.StaticText(self, wx.ID_ANY,"PULSE SOBRE EL TABLERO PARA COMENZAR")  # Creación de sizer con texto estático
        self.label_1.SetFont(wx.Font(10, wx.FONTFAMILY_DEFAULT, wx.FONTSTYLE_NORMAL, wx.FONTWEIGHT_BOLD, 0, ""))
        sizer_1.Add(self.label_1, 0, 0, 0)

        self.SetSizer(sizer_1)
        self.juego = None
        self.Actual = None
        self.dialogoinic = None
        self.num_fil = 0
        self.num_col = 0
        self.Layout()

    def TimerReset(self,event):  # Resetea el temporizador si se vuelve a marcar la checkbox
        self.contador = 11
        event.Skip()

    def Ontimer(self, event):  # Inicia el temporizador de 10 segundos si se activa la Checkbox de "Límite de tiempo"
        if (self.Checkbox_1.GetValue() == True):
            self.contador -= 1
            self.label_Tiempo.LabelText = str(self.contador)
            if (self.contador == 0):  # Resetea el contador cuando llega a 0
                centinela = False
                self.contador = 11
                if (self.Actual.car == 'W'):  # Si el elemento actual es un Wick comprueba que la posición donde se va a colocar no esté vacía
                    for i in range(self.num_fil):
                        for j in range(self.num_col):
                            if (self.coger_caracter(i, j) != '.' and centinela == False):
                                mov = [str(i), str(j)]
                                self.juego.setactual(self.Actual)
                                self.juego.jugar(mov)
                                self.Actual = self.juego.elige_actual()
                                self.label_Actual.LabelText = self.Actual.car
                                centinela = True
                                self.label_1.SetLabel(f"Turnos:{self.juego.turno}")
                elif (self.Actual.car != 'W'):  # Si el elemento actual no es un Wick comprueba que la posición donde se va a colocar esté vacía
                    for i in range(self.num_fil):
                        for j in range(self.num_col):
                            if (self.coger_caracter(i, j) == '.' and centinela == False):
                                mov = [str(i), str(j)]
                                self.juego.setactual(self.Actual)
                                self.juego.jugar(mov)
                                print(self.juego)
                                self.Actual = self.juego.elige_actual()
                                self.label_Actual.LabelText = self.Actual.car
                                centinela = True
                                self.label_1.SetLabel(f"Turnos:{self.juego.turno}")
            self.re_botones()
            self.Puntuacion = self.juego.calcula_puntos()
            self.label_Puntuacion.LabelText = (f"{self.Puntuacion}")
        event.Skip()

    def setNumFil(self, filas):  # Método Set para las filas
        self.num_fil = filas

    def getNumFil(self):  # Método Get para las filas
        return self.num_fil

    def setNumCol(self,columnas):  # Método Set para las columnas
        self.num_col = columnas

    def getNumCol(self):  # Método Get para las columnas
        return self.num_col

    def setJuego(self, juego):  # Método Set para Juego (código suburbious_1)
        self.juego = juego

    def getJuego(self, i, j):  # Método Get para Juego  (código suburbious_1)
        return self.juego[i, j]

    def iniciarTablero(self):  # Inicializa el tablero
        if (self.num_fil == 0):
            self.num_fil = 6
        if (self.num_col == 0):
            self.num_col = 6
        self.Tablero = wx.GridSizer(self.num_fil, self.num_col, 4, 4)
        self.sizer_2.Add(self.Tablero, 1, wx.EXPAND, 0)

    def iniciar(self):  # Inicializa los parámetros para la partida
        self.timer = wx.Timer(self)
        self.Bind(wx.EVT_TIMER, self.Ontimer, self.timer)  # Se activa la función Ontimer
        self.timer.Start(1000)
        i = 0
        j = 0
        self.buttons = []  # Crea una lista cuya posición determina el orden de los botones
        self.casillas = 6 * 6
        self.num_fil = self.getNumFil()
        self.num_col = self.getNumCol()
        self.Actual = self.juego.elige_actual()
        if (self.num_fil * self.num_col != 0):
            self.casillas = self.num_fil * self.num_col
        if (self.casillas == 6 * 6):
            self.num_col = 6
        for num in range(self.casillas):                    #Creación de la matriz de botones
            self.buttonmatrix = wx.Button(self)
            self.buttonmatrix.num = num
            self.buttonmatrix.SetLabelMarkup(self.coger_caracter(i, j))
            j += 1
            if j == self.num_col:
                j = 0
                i += 1
            self.Bind(wx.EVT_BUTTON, self.InsertarActual, self.buttonmatrix)
            self.Tablero.Add(self.buttonmatrix, 1, wx.EXPAND, 0)
            self.buttons.append(self.buttonmatrix)
        self.label_Actual.LabelText = self.Actual.car
        self.Puntuacion = self.juego.calcula_puntos()
        self.label_Puntuacion.LabelText = (f"{self.Puntuacion}")

    def coger_caracter(self, i, j):  # Coge el caracter de una posición de la matriz tablero
        elem = self.getJuego(i, j).car
        return elem

    def INTERCAMBIO(self,event):            # Función para intercambiar el contenido de Actual y el del Almacén
        sonido = Sound("./intercambiar")    # Efecto de sonido al pulsar el botón de intercambio
        sonido.Play()
        if (self.Almacen.car == '.'):
            self.Almacen = self.Actual
            self.label_Almacen.LabelText = self.Almacen.car
            self.Actual = self.juego.elige_actual()
            self.label_Actual.LabelText = self.Actual.car
        else:
            car = self.Almacen
            self.Almacen = self.Actual
            self.label_Almacen.LabelText = self.Almacen.car
            self.Actual = car
            self.label_Actual.LabelText = self.Actual.car
        event.Skip()

    def InsertarActual(self, event):  # Función para insertar el elemento Actual en el tablero
        self.contador = 11
        sonido = Sound("./cosa_tablero.wav")  # Sonido al pulsar un botón de los que está formado el tablero
        sonido.Play()
        buttonmatrix = event.EventObject
        if (self.Actual.car == 'W'):  # Comprueba si hay un hueco vacío en la posición donde se intenta colocar el Wick
            if ((self.juego[(buttonmatrix.num // self.num_col), (
                    buttonmatrix.num - ((buttonmatrix.num // self.num_col) * self.num_col))]).car != '.'):
                mov = [str(buttonmatrix.num // self.num_col),
                       str(buttonmatrix.num - ((buttonmatrix.num // self.num_col) * self.num_col))]
                self.juego.setactual(self.Actual)
                self.juego.jugar(mov)
                self.Actual = self.juego.elige_actual()
                self.label_Actual.LabelText = self.Actual.car
        elif (self.Actual != suburbious_1.TipoElem.WICK):
            if ((self.juego[(buttonmatrix.num // self.num_col), (buttonmatrix.num - ((buttonmatrix.num // self.num_col) * self.num_col))]).car == '.'):  # Compruaba si hay ya un elemento en la posición seleccionada
                mov = [str(buttonmatrix.num // self.num_col),str(buttonmatrix.num - ((buttonmatrix.num // self.num_col) * self.num_col))]
                self.juego.setactual(self.Actual)
                self.juego.jugar(mov)
                self.Actual = self.juego.elige_actual()
                self.label_Actual.LabelText = self.Actual.car
        self.re_botones()  # Actualiza el contenido de los botones
        self.Puntuacion = self.juego.calcula_puntos()  # Actualiza la puntuación tras cada turno
        self.label_Puntuacion.LabelText = (f"{self.Puntuacion}")
        self.label_1.SetLabel(f"Turnos:{self.juego.turno}")  # Actualiza el nº de turnos que han pasado

    def re_botones(self):  # Actualiza los caracteres de los botones que forman el tablero tras cada jugada
        i = 0
        j = 0
        for h in range(len(self.buttons)):
            self.buttons[h].SetLabel(self.coger_caracter(i, j))
            j += 1
            if j == self.num_col:
                j = 0
                i += 1

    def NuevaPartida(self,event):  # Resetea el juego
        self.Hide()
        app = MyApp(0)
        app.MainLoop()
        event.Skip()


# end of class SuburbiousFrame

class Dialogo(wx.Dialog):                   #Inicio de la clase Dialogo(clase que muestra el dialogo de elección de forma de jugar)
    def __init__(self, *args, **kwds):
        # begin wxGlade: Dialogo.__init__
        kwds["style"] = kwds.get("style", 0) | wx.DEFAULT_DIALOG_STYLE
        wx.Dialog.__init__(self, *args, **kwds)
        self.SetTitle("dialog")

        sizer_1 = wx.BoxSizer(wx.VERTICAL)

        sizer_3 = wx.BoxSizer(wx.HORIZONTAL)
        sizer_1.Add(sizer_3, 1, wx.EXPAND, 0)

        self.sizer_4 = wx.BoxSizer(wx.VERTICAL)
        sizer_3.Add(self.sizer_4, 1, wx.EXPAND, 0)

        label_1 = wx.StaticText(self, wx.ID_ANY, "Nueva Partida")
        self.sizer_4.Add(label_1, 0, 0, 0)

        self.radio_btn_1 = wx.RadioButton(self, wx.ID_ANY, "Leer de fichero")           #Opción 1
        self.sizer_4.Add(self.radio_btn_1, 0, 0, 0)

        self.radio_btn_2 = wx.RadioButton(self, wx.ID_ANY, "Crear al azar (6x6)")          #Opción 2
        self.sizer_4.Add(self.radio_btn_2, 0, 0, 0)

        self.radio_btn_3 = wx.RadioButton(self, wx.ID_ANY, u"Crear al azar (elegir tamaño)")    #Opción 3
        self.sizer_4.Add(self.radio_btn_3, 0, 0, 0)

        self.sizer_5 = wx.BoxSizer(wx.VERTICAL)
        sizer_3.Add(self.sizer_5, 1, wx.EXPAND, 0)

        self.label_4 = wx.StaticText(self, wx.ID_ANY, u"Tamaño del tablero")
        self.sizer_5.Add(self.label_4, 0, 0, 0)

        self.sizer_6 = wx.BoxSizer(wx.HORIZONTAL)
        self.sizer_5.Add(self.sizer_6, 1, wx.EXPAND, 0)

        label_2 = wx.StaticText(self, wx.ID_ANY, u"Nº filas")
        self.sizer_6.Add(label_2, 0, 0, 0)

        self.spin_ctrl_1 = wx.SpinCtrl(self, wx.ID_ANY, "0", min=0, max=10)                 #SpinCtrl de selección de nºfilas
        self.sizer_6.Add(self.spin_ctrl_1, 0, 0, 0)

        sizer_7 = wx.BoxSizer(wx.HORIZONTAL)
        self.sizer_5.Add(sizer_7, 1, wx.EXPAND, 0)

        self.label_3 = wx.StaticText(self, wx.ID_ANY, u"Nº columnas")
        sizer_7.Add(self.label_3, 0, 0, 0)

        self.spin_ctrl_2 = wx.SpinCtrl(self, wx.ID_ANY, "0", min=0, max=10)                 #SpinCtrl de selección de nºcolumnas
        sizer_7.Add(self.spin_ctrl_2, 0, 0, 0)

        sizer_2 = wx.StdDialogButtonSizer()
        sizer_1.Add(sizer_2, 0, wx.ALIGN_RIGHT | wx.ALL, 4)

        self.button_OK = wx.Button(self, wx.ID_OK, "")
        self.button_OK.SetDefault()
        sizer_2.AddButton(self.button_OK)

        self.button_CANCEL = wx.Button(self, wx.ID_CANCEL, "")
        sizer_2.AddButton(self.button_CANCEL)

        sizer_2.Realize()

        self.SetSizer(sizer_1)
        sizer_1.Fit(self)

        self.SetAffirmativeId(self.button_OK.GetId())
        self.SetEscapeId(self.button_CANCEL.GetId())
        self.juego=None                                                     #Inicialización de variables
        self.frame=None
        self.seleccion=None
        self.dialogo=None
        self.valor_boton=0
        self.Bind(wx.EVT_RADIOBUTTON,self.esconderse,self.radio_btn_2)      #Bind de eventos
        self.Bind(wx.EVT_RADIOBUTTON,self.esconderse,self.radio_btn_1)
        self.Bind(wx.EVT_RADIOBUTTON, self.salimos, self.radio_btn_3)
        self.Bind(wx.EVT_BUTTON,self.siguiente_boton,self.button_OK)
        self.Layout()

    def setDialogo(self,dialogo):                           #Método Set para obtener el cuadro de dialogo de lectura de ficheros
        self.dialogo=dialogo

    def getDialogo(self):                                   #Método Get para obtener el cuadro de dialogo de lectura de ficheros
        return self.dialogo

    def siguiente_boton(self,event):                        #Dependiendo del valor de los radiobuttons se tomara una decisión u otra
        sonido = Sound("./ok.wav")
        sonido.Play()
        if(self.valor_boton==1):                            #Si el valor es 1, muestra la ventana de lectura de ficheros
            self.Hide()
            self.dialogo.Show()
        if(self.valor_boton==2):                            #Inicia el juego con la matriz con 6 filas y 6 columnas
            self.frame = self.getsubframe()
            self.fich_tab=""
            self.fich_sec=""
            self.juego = suburbious_1.Juego(self.fich_tab, self.fich_sec,6,6)
            self.Actual = self.juego.elige_actual()
            self.frame.setJuego(self.juego)
            self.frame.setNumFil(6)
            self.frame.setNumCol(6)
            self.frame.iniciarTablero()
            self.frame.iniciar()
            self.frame.Show()
            self.Hide()
        if(self.valor_boton==3):                            #Inicia el juego con la matriz a el tamaño que te da el usuario con los spinctrl
            self.valor_fil=self.spin_ctrl_1.GetValue()
            self.valor_col=self.spin_ctrl_2.GetValue()
            self.frame = self.getsubframe()
            self.fich_tab = ""
            self.fich_sec = ""
            self.juego = suburbious_1.Juego(self.fich_tab, self.fich_sec,self.valor_fil,self.valor_col)
            self.Actual = self.juego.elige_actual()
            self.frame.setJuego(self.juego)
            self.frame.setNumFil(self.valor_fil)
            self.frame.setNumCol(self.valor_col)
            self.frame.iniciarTablero()
            self.frame.iniciar()
            self.Hide()
            self.frame.Show()

    def esconderse(self,event):                         #Esconde la elección de filas y columnas en caso de que tal opción no este seleccionada
        sonido = Sound("./elegir_opciones.wav")
        sonido.Play()
        if (self.radio_btn_2.GetValue() == True):
            self.valor_boton = 2                        #Damos valor a la variable self.valor_boton para saber exactamente en que radiobutton estamos
        if (self.radio_btn_1.GetValue() == True):
            self.valor_boton = 1
        self.sizer_5.Hide(True)
        self.label_4.Hide()
        self.spin_ctrl_2.Hide()
        self.label_3.Hide()
        event.Skip()

    def salimos(self,event):                            #Muestra la elección de filas y columnas con el spinctrl
        sonido = Sound("./elegir_opciones.wav")
        sonido.Play()
        if (self.radio_btn_3.GetValue()==True):
            self.valor_boton=3
        self.sizer_5.Show(True)
        self.label_4.Show()
        self.spin_ctrl_2.Show()
        self.label_3.Show()
        event.Skip()

    def setsubframe(self,frame):                        # Método set para obtener la frame de juego(matriz)
        self.frame=frame
    def getsubframe(self):                              # Método get para obtener la frame de juego(matriz)
        return self.frame


class MyDialog(wx.Dialog):
    def __init__(self, *args, **kwds):
        # begin wxGlade: MyDialog.__init__
        kwds["style"] = kwds.get("style", 0) | wx.DEFAULT_DIALOG_STYLE
        wx.Dialog.__init__(self, *args, **kwds)
        self.SetTitle("dialog")

        sizer_1 = wx.BoxSizer(wx.VERTICAL)

        sizer_3 = wx.BoxSizer(wx.VERTICAL)
        sizer_1.Add(sizer_3, 1, wx.EXPAND, 0)

        sizer_4 = wx.StaticBoxSizer(wx.StaticBox(self, wx.ID_ANY, "Fichero tablero:"), wx.HORIZONTAL)
        sizer_3.Add(sizer_4, 1, wx.EXPAND, 0)

        self.text_ctrl_1 = wx.TextCtrl(self, wx.ID_ANY, "")             #Text.ctrl para obtener el nombre de fichero de tablero
        sizer_4.Add(self.text_ctrl_1, 0, 0, 0)

        sizer_5 = wx.StaticBoxSizer(wx.StaticBox(self, wx.ID_ANY, "Fichero secuencia:"), wx.HORIZONTAL)
        sizer_3.Add(sizer_5, 1, wx.EXPAND, 0)

        self.text_ctrl_2 = wx.TextCtrl(self, wx.ID_ANY, "")             #Text.ctrl para obtener el nombre del fichero de secuencia
        sizer_5.Add(self.text_ctrl_2, 0, 0, 0)

        sizer_2 = wx.StdDialogButtonSizer()
        sizer_1.Add(sizer_2, 0, wx.ALIGN_RIGHT | wx.ALL, 4)

        self.button_OK = wx.Button(self, wx.ID_OK, "")              #Boton OK
        self.button_OK.SetDefault()
        sizer_2.AddButton(self.button_OK)

        self.button_CANCEL = wx.Button(self, wx.ID_CANCEL, "")      #Boton CANCEL
        sizer_2.AddButton(self.button_CANCEL)

        sizer_2.Realize()

        self.SetSizer(sizer_1)
        sizer_1.Fit(self)

        self.SetAffirmativeId(self.button_OK.GetId())
        self.SetEscapeId(self.button_CANCEL.GetId())

        self.Bind(wx.EVT_BUTTON,self.siguiente_boton,self.button_OK)            #Bind de eventos
        self.frame=None                                                         #Inicialización variables

        self.Layout()

    def setsubframe(self,frame):                # Método set para obtener la frame de juego(matriz)
        self.frame=frame

    def getsubframe(self):                      # Método get para obtener la frame de juego(matriz)
        return self.frame

    def siguiente_boton(self,event):
        #Creación de la matriz a partir de los fichero tablero y secuencia. No se ha implementado para que sea un único fichero.
        #En caso de que el fichero no se encuentre, la matriz se creará de forma aleatoria como si se hubiese elegido la opción 2 en los radiobuttons del dialogo anterior
        sonido = Sound("./ok.wav")
        sonido.Play()
        self.frame = self.getsubframe()
        self.fich_tab=self.text_ctrl_1.GetLineText(0)
        self.fich_sec=self.text_ctrl_2.GetLineText(0)
        try:                                                #Intento de abrir el fichero para obtener las dimensiones del tablero
            fich =open(self.fich_tab)
            self.valor_fil = len(fich.readlines())
            fich.close()
        except FileNotFoundError:                           #Si no se encuentra, valor por defecto = 6
            self.valor_fil = 6
        try:
            fich = open(self.fich_tab)
            col = fich.readlines()
            col = col[0]
            col = len(col) - 1
            self.valor_col = col
            fich.close()
        except FileNotFoundError:
            self.valor_col = 6
        self.frame.setNumCol(self.valor_col)
        self.frame.setNumFil(self.valor_fil)
        self.juego = suburbious_1.Juego(self.fich_tab, self.fich_sec,self.valor_fil,self.valor_col)
        self.Actual = self.juego.elige_actual()
        self.frame.setJuego(self.juego)
        self.frame.iniciarTablero()
        self.frame.iniciar()
        self.frame.Show()
        event.Skip()


        # end wxGlade

# end of class Dialogo

class MyApp(wx.App):                        #Clase MyApp (loop de la aplicación)
    def OnInit(self):
        self.fich_tab = None
        self.fich_sec = None
        self.dialogo=Dialogo(None,wx.ID_ANY,"")
        self.SetTopWindow(self.dialogo)
        self.dialogo.Show()
        self.dialogo2=MyDialog(None,wx.ID_ANY,"")
        self.SetTopWindow(self.dialogo2)
        self.dialogo.setDialogo(self.dialogo2)
        self.frame = SuburbiousFrame(None, wx.ID_ANY, "")
        self.SetTopWindow(self.frame)
        self.dialogo2.setsubframe(self.frame)
        self.dialogo.setsubframe(self.frame)
        return True



# end of class MyApp

if __name__ == "__main__":
    app = MyApp(0)
    app.MainLoop()