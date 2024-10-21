%ejercicio 1 - ver si lo dado es una lista

lista([]).
lista([_|Y]):-lista(Y).

%ejercicio 2 - ver longitud lista

longitud([],0).
longitud([_|Y],S):-longitud(Y,S1),S is S1+1.

%ejercicio 3 - suma elementos de listas

suma([],0).
suma([X|Y],S):-suma(Y,S1),S is S1+X.

%ejercicio 4 - borrar un elemento de la lista

borrar(X,[X|Y],Y).
borrar(X,[X1|Y],[X1|L1]):-borrar(X,Y,L1).

%ejercicio 5 - insertar elemento en una lista

insertar(X,L,[X|L]).
insertar(X,[X1|L],[X1|L1]):-insertar(X,L,L1).

%ejercicio 6 - determinar si lista numérica está en orden creciente

creciente([_]).
creciente([X|[Y|Z]]):-X<Y,creciente([Y|Z]).

%ejercicio 7 - determinar si lista numérica ordenada en orden decreciente

decreciente([_]).
decreciente([X|[Y|Z]]):-X>Y,creciente([Y|Z]).

%ejercicio 8 - eliminar elemento que se encuentre en la enésima posición de una lista

sacapos([_|L],1,L).
sacapos([X|L1],N,[X|L]):-N1 is N-1,sacapos(L1,N1,L).

%ejercicio 9 - calcular maximo de una lista numérica

maximo([X],X).
maximo([X|[Y|Z]],X1):-X<Y,maximo([Y|Z],X1).
maximo([X|[Y|Z]],X1):-X>Y,maximo([X|Z],X1).

%ejercicio 10 - determinar si un par de elementos son consecutivos
consecut([X|[Y|_]],X,Y).
consecut([_|[Y1|Z]],X,Y):-consecut([Y1|Z],X,Y).

%ejercicio 11 - invertir una lista

invertir([],A,A).
invertir([X|Y],A,L):-invertir(Y,[X|A],L).
invertir(X,L):-invertir(X,[],L).

%ejercicio 12 - obtener último elemento de una lista

ultimo(X,[X|[]]).
ultimo(X,[_|Z]):-ultimo(X,Z).

%ejercicio 13 - obtener todos los elementos de una lista excepto el último

primeros([_],[]).
primeros([X|Y],[X|L]):-primeros(Y,L).

%ejercicio 14 - Obtener todos los prefijos de una lista o saber si una lista es prefijo de otra

prefijo([],[]).
prefijo([],[_|_]).
prefijo([X|Y],[X|Z]):-prefijo(Y,Z).

%ejercicio 15 - Obtener todos los sufijos de una lista o saber si una lista es sufijo de otra

sufijo([],[]).
sufijo([X|Y],[X|Y]).
sufijo([X|Y],[_|Y1]):-sufijo([X|Y],Y1).

%ejercicio 16 - Saber si una lista es sublista de otra

sublista([],_).
sublista([X|[]],[X|_]).
sublista([X|[Y|Z1]],[X|[Y|Z2]]):-sublista([Y|Z1],[Y|Z2]).
sublista([X|Y],[_|Y1]):-sublista([X|Y],Y1).

%ejercicio 17 -  borrar apariciones de un elemento en una lista

borrartodos(_,[],[]).
borrartodos(X,[X|L],L1):-borrartodos(X,L,L1).
borrartodos(X,[Y|L],[Y|L1]):-X\=Y,borrartodos(X,L,L1).

%ejercicio 18 - decir si una palabra/secuencia de caracteres es un palíndromo

palindromo(N):-name(N,L),invertir(L,L).

%hoja8-------------------------------------------------------------------------------------------------------------------------------

%ejercicio 1 - devuelve lista suma de elementos de 2 en 2

sumar2en2([],[]).
sumar2en2([X|[]],[X|[]]).
sumar2en2([X|[Y|Z]],[S|L]):- S is X+Y,sumar2en2(Z,L).

%ejercicio 2 - número decimal a binario

dec_bin(0,A,[0|A]).
dec_bin(1,A,[1|A]).
dec_bin(X,A,L):-X=\=1,X=\=0,S is X mod 2,X1 is X//2,dec_bin(X1,[S|A],L).

dec_bin(X,L):-dec_bin(X,[],L).

%ejercicio 3 - familia

%ENUNCIADO
familia(persona(juan,perez,50),persona(maria,alonso,45),[persona(carlos,perez,20),persona(andres,perez,18),persona(elena,perez,12)]).
familia(persona(pedro,lopez,40),persona(carmen,ruiz,39),[persona(carlos,lopez,19),persona(teresa,lopez,8)]).
familia(persona(carlos,martinez,25),persona(lola,garcia,22),[]).
edad(persona(_,_,E),E).

%PROBLEMA A
ultimo(X,Y,Z):-familia(X,Y,Z1),ultimo(Z,Z1).
%ultimo(X,[X|[]]).
%ultimo(X,[_|Z]):-ultimo(X,Z).

%PROBLEMA B
numhijosPar(X,Y,H,Num):-familia(X,Y,H),longitud(H,Num),Num mod 2 =:=0.

%PROBLEMA C Y D
media(X,Y,MediaDec,T,Totalbin):-familia(X,Y,Z),mediahijos(Z,Num,T1),edad(X,Num2),edad(Y,Num3),T is Num3+Num2+T1,P is Num+2,MediaDec is T//P,dec_bin(T,Totalbin).

mediahijos([],0,0).
mediahijos([C|L],N,T):-edad(C,Num),mediahijos(L,N1,T1),N is N1+1,T is T1 + Num.

%ejercicio 4 -  decir si una lista tiene todos los elementos iguales

todos_iguales([X|L]):-todos_iguales(X,L).
todos_iguales(_,[]).
todos_iguales(X,[X|L]):-todos_iguales(X,L).

%ejercicio 5 - transformar número natural en una lista de sus cifras

cifras_lista(0,L,L).
cifras_lista(N,R,L):-N=\=0,N1 is N mod 10,N2 is N//10,cifras_lista(N2,[N1|R],L).
cifras_lista(N,L):-cifras_lista(N,[],L).

%ejercicio 6 - rotar una lista una posición hacia la izquierda

rotar_izquierda([X|L],R):-rotar_izquierda(X,L,R).
rotar_izquierda(X,[Y|L],[Y|R]):-rotar_izquierda(X,L,R).
rotar_izquierda(X,[],[X]).

%ejercicio 7 - rotar una lista n posiciones a la izquierda

rotar_izquierda_posiciones(R2,0,R2).
rotar_izquierda_posiciones(L,N,R):-N=\=0,rotar_izquierda(L,R2),N2 is N-1,rotar_izquierda_posiciones(R2,N2,R).

%ejercicio 8 - construir una lista con un elemento repetido N veces

repetir(_,0,[]).
repetir(X,N,[X|L]):-N=\=0,N2 is N-1,repetir(X,N2,L).

%ejercicio 9 - contar las veces que aparece un elemento en una lista

contar(_,[],0).
contar(X,[X|L],R):-contar(X,L,R2),R is R2+1.
contar(X,[Y|L],R):-X\=Y,contar(X,L,R).

%ejercicio 10

distintos([]).
distintos([X|L]):-contar(X,L,R),R=:=0,distintos(L).

%ejercicio 11 - rotar una lista a la derecha

rotar_derecha([X|L],[X1|L2]):-ultimo(X1,[X|L]),borrarultimo([X|L],L2).

borrarultimo([X|L],[X|R]):-borrarultimo(L,R).
borrarultimo([_|[]],[]).

%ejercicio 12 - rotar una lista n posiciones a la derecha

rotar_derecha_posiciones(R2,0,R2).
rotar_derecha_posiciones(L,N,R):-N=\=0,rotar_derecha(L,R2),N2 is N-1,rotar_derecha_posiciones(R2,N2,R).

%ensayo examenes

%ejercicio 1 y 2 - programa al q se le pasa una lista que puede contener a su vez listas
unalista([X|L],R):-es_lista(X),unalista(X,R2),unalista(L,R1),juntar_listas(R2,R1,R),!.
unalista([X|L],[X|R]):-not(es_lista(X)),unalista(L,R).
unalista([],[]).

juntar_listas([X|L1],L2,[X|R]):-juntar_listas(L1,L2,R).
juntar_listas([],L2,L2).

es_lista([]).
es_lista([_|Y]):-es_lista(Y).

%ejercicio 3 y 4 - programa ordenar en burbuja

burbuja(L,R):-bucle(L,R,1).
bucle(L,R,N):-N=\=0,burbuja(L,R1,N1),bucle(R1,R,N1).
bucle(L,L,0).
burbuja([X|[Y|L]],[Y|R],N):-X>Y,burbuja([X|L],R,N1),N is N1+1.
burbuja([X|[Y|L]],[X|R],N):-X=<Y,burbuja([Y|L],R,N).
burbuja([X|[]],[X],0).

%ejercicio 5 y 6 - eliminar elementos repetidos de una lista

borrar_repes([X|[]],[X]).
borrar_repes([X|L],R):-borrar_repes(L,R1),igual(X,R1,R).
igual(_,[],[]).
igual(X,[Y|[]],[X|[Y]]):-X\=Y.
igual(X,[X|[]],[X]).
igual(X,[X|L],R):-igual(X,L,R).
igual(X,[Y|L],[Y|R]):-X\=Y,L\=[],igual(X,L,R).

%ejercicio 7 y 8 - triángulo

fila(1,[]).
fila(N,L):-N=\=1,N1 is N-1,fila(N1,L1),calculaanterior([1|L1],L).

calculaanterior([X|[]],[X]).
calculaanterior([X|[Y|L]],[N|R]):-calculaanterior([Y|L],R),N is X+Y.
