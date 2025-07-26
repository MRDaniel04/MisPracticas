.data
X: .word 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63
Y: .word 63 62 61 60 59 58 57 56 55 54 53 52 51 50 49 48 47 46 45 44 43 42 41 40 39 38 37 36 35 34 33 32 31 30 29 28 27 26 25 24 23 22 21 20 19 18 17 16 15 14 13 12 11 10 9 8 7 6 5 4 3 2 1 0
Z: .space 256
k: .word 10
.text

#Cargamos todos los arrays y las constantes en el programa
la $t0,X
la $t1,Y
la $t2,Z
addi $t3, $t1, 244 #limite de las iteraciones del bucle(fin del vector)
lw $t8,k

#Fase de inicialización
	#Primera
	lw $t4,0($t0) #Carga elemento de X
	lw $t6,0($t1) #Carga elemento de Y
	sll $t5,$t4,1 #Xi*2
	mul $t7,$t6,$t8 #k*Yi
	add $t9,$t5,$t7 #Xi*2+k*Yi
	#Segunda
	lw $t4,4($t0) #Carga elemento de X
	lw $t6,4($t1) #Carga elemento de Y
	sll $t5,$t4,1 #Xi*2
	mul $t7,$t6,$t8 #k*Yi
	#Tercera
	lw $t4,8($t0) #Carga elemento de X
	lw $t6,8($t1) #Carga elemento de Y

#Comienzo del bucle de operaciones
loop:
	sw $t9,0($t2) #Carga del resultado en Z
	add $t9,$t5,$t7 #Xi*2+k*Yi
	mul $t7,$t6,$t8 #k*Yi
	sll $t5,$t4,1 #Xi*2
	lw $t6,12($t1) #Carga elemento de Y
	lw $t4,12($t0) #Carga elemento de X
	#Incrementamos los punteros
	addi $t0,$t0,4
	addi $t1,$t1,4
	addi $t2,$t2,4 
	# comprobacion fin de loop (fin del vector)
	bne $t3,$t1,loop
	
#Fase de finalización
	#Primera
	sw $t9,0($t2) #Carga del resultado en Z
	add $t9,$t5,$t7 #Xi*2+k*Yi
	mul $t7,$t6,$t8 #k*Yi
	sll $t5,$t4,1 #Xi*2
	#Segunda
	sw $t9,4($t2) #Carga del resultado en Z
	add $t9,$t5,$t7 #Xi*2+k*Yi
	#Tercera
	sw $t9,8($t2) #Carga del resultado en Z
li $v0,10
syscall	