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
addi $t3, $t1, 256 #limite de las iteraciones del bucle(fin del vector)
lw $t8,k

#Comienzo del bucle de operaciones
loop:
	lw $t4,0($t0) #Carga elemento de X
	lw $t5,4($t0) #Carga elemento de X+4
	sll $t4,$t4,1 #Xi*2
	sll $t5,$t5,1 #Xj*2
	lw $t6,0($t1) #Carga elemento de Y
	lw $t7,4($t1) #Carga elemento de Y+4
	mul $t6,$t6,$t8 #k*Yi
	mul $t7,$t7,$t8 #k*Yj
	add $t4,$t4,$t6 #Xi*2+k*Yi
	add $t5,$t5,$t7 #Xj*2+k*Yj
	sw $t4,0($t2) #Carga del resultado en Z
	sw $t5,4($t2) #Carga del resultado en Z+4
	# incremento de punteros (doble loop)
	addi $t0,$t0,8 
	addi $t1,$t1,8
	addi $t2,$t2,8
	# comprobacion fin de loop (fin del vector)
	bne $t3,$t1,loop
	

li $v0,10
syscall	