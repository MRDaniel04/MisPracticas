.data
	Almacen: .space 36
	.align 2
	Almacen2:.space 16
	.align 2
	Almacen3:.space 16
	almacen: .space 20
.text

#---------------------------------------------------------------------------------------------------------------Distancia

Distancia:
	addi $sp,$sp,-20			
	sw $ra,0($sp)
	sw $s1,4($sp)
	sw $s2,8($sp)
	sw $s3,12($sp)
	sw $s4,16($sp)
	
	add $s1,$a1,$zero				#Guardamos segundo punto
	add $s2,$a2,$zero				#Guardamos la salida del resultado
	
	la $a1,Almacen2
	jal String2Punto				#Devuelve punto en a1 (punto 1)
	beq $v0,1,exitfinal				#Comprobación errores
	beq $v0,2,exitfinal
	beq $v0,3,exitfinal
	add $s3,$a1,$zero				#Guardamos primer punto para que no se pierda la info.
	
	add $a0,$s1,$zero				#Recuperamos valor de segundo punto
	la $a1,Almacen3
	jal String2Punto				#Devuelve punto en a1(punto 2)
	beq $v0,1,exitfinal				#Comprobación errores
	beq $v0,2,exitfinal
	beq $v0,3,exitfinal
	add $a0,$s3,$zero				#Recuperamos valor primer punto
	
	
	jal CalculaDistancia
	add $s4,$v0,$zero				#Guardamos valor distancia
	add $t0,$v0,$zero				#comprobación errores
	li $v0,4
	beq $t0,-1,exitfinal
	li $v0,5
	beq $t0,-2,exitfinal
	add $a0,$s4,$zero				#Recuperamos valor distancia
	
	add $a2,$s2,$zero
	jal itoa
	li $v0,0
exitfinal:
	lw $ra,0($sp)
	lw $s1,4($sp)
	lw $s2,8($sp)
	lw $s3,12($sp)
	lw $s4,16($sp)					#Retorno
	addi $sp,$sp,20
	jr $ra
			
#---------------------------------------------------------------------------------------------------------------String2Punto
 
 #$s0 <- Almacen para pasarle el String a atoi
 #$s1 <- Punto
 #$s2 <- Contador de dimensiones
 #$s3 <- Apuntador del almacen 
 #$s4 <- Apuntador al punto
 #$s5 <- Tamaño del almacen
 #$t0 <- Cargamos el caracter de la cadena
 
 String2Punto:
 	#Reservamos memoria de la pila
 	addi $sp, $sp, -28
 	sw $ra, 0($sp)
 	sw $s0, 4($sp)
 	sw $s1, 8($sp)
 	sw $s2, 12($sp)
 	sw $s3, 16($sp)
 	sw $s4, 20($sp)
 	sw $s5, 24($sp)
 	
 	#Inicializamos variables
 	li $v0, 0
 	move $s0, $a0
 	move $s1, $a1
 	li $s2, 1	#Se inicializa a 1 porque contamos las dimensiones por el numero de ',' 2D = 1 ',' y 3D = 2 ','
 	la $s3, Almacen
 	move $s4, $a1
 	li $t0,0
 	li $s5, 10
 	li $t6, 0
 	li $t7, 0
 	
 	#El primer caracter debe de ser siempre '('
 	primerCaracter:	lb $t0, 0($s0)
 			bne $t0, '(', errorSintaxis	#Saltaria al error
 			addi $s0, $s0, 1
 	
 	bucleLectura:	lb $t0, 0($s0)
 			addi $t8,$t8,1
 			beq $t0, ',', contador				# ',' indica cambio de coordenada
 			beq $t0, ')', comprobarErrorDimensiones	# ')' indica fin del punto
 			
 			sb $t0, 0($s3)
 			addi $s3, $s3, 1
 			
 			j incrementa
 			
 	contador:	
 			addi $s2, $s2, 1	#incrementa la coordenada
 			beq $s2,4,errorCoordenadas
 			addi $s1, $s1, 4	#incrementa la posicion donde se almacena la coordenada
 			
 			jal atoi
 			beq $v1,1,errorSintaxis
 			beq $v1, 2, errorOverflow
 			sw $v0, 0($s1)		#almacena el valor entero de la coordenada en el punto
 			
 			jal limpiaBuffer	#limpia el buffer para poder introducir otra coordenada
 			
 	incrementa:	
 			beq $t8,14,errorSintaxis
 			addi $s0, $s0, 1	#incrementa direccion del string
 			j bucleLectura
 	
 	errorSintaxis: li $v0, 1		#error Sintaxis $v0 = 1
 			j fin
 			
 	errorOverflow: li $v0, 2		#error Overflow $v0 = 2
 			j fin
 	
 	comprobarErrorDimensiones:	beq $s2, 1, errorCoordenadas #Si tiene 1D error de distancia
 					addi $s1, $s1, 4
 					jal atoi
 					sw $v0, 0($s1)
 					li $t8,0
 					move $v0,$v1	
 					jal limpiaBuffer
 					
 					addi $s0, $s0, 1
 					lb $t0, 0($s0)
 					
 					slti $t7, $s2, 4
 					bne $t0, $zero, errorSintaxis #Si hay algo fuera de los parentesis errorSintaxis
 					
 					bne $t7, $zero, fin	#error dimenssiones incorrectas $v0 = 3
 	errorCoordenadas:		li $v0, 3
 					 
 	fin:	
 		sw $s2, 0($s4)
 		move $a1, $s4
 		
 		#Restauramos pila
 		lw $ra, 0($sp)
 		lw $s0, 4($sp)
 		lw $s1, 8($sp)
 		lw $s2, 12($sp)
 		lw $s3, 16($sp)
 		lw $s4, 20($sp)
 		lw $s5, 24($sp)
 		addi $sp, $sp, 28
 		jr $ra
 		
 
 #Funcion para limpiar el buffer que le pasas a atoi
 #$s3 <- Direccion del buffer; $s5 <- Longitud de vector	
 limpiaBuffer:	li $t0, 0
 		la $s3, Almacen
 		
 		bucleLimpieza:	beq $t0, $s5, finLimpieza
 				sb $zero, 0($s3)
 				addi $t0, $t0, 1
 				addi $s3, $s3, 1
 				j bucleLimpieza
 		
 		finLimpieza:	la $s3, Almacen
 				jr $ra
 
 
 #Funcion atoi
 atoi:	addi $sp,$sp,-4
 	sw $s0,0($sp)
 	
 	addi $t9,$zero,' '
 	addi $s0,$zero,0
	li $t1,0			#Inicializamos todos los registros
	la $t6, Almacen
	li $t3,0
	li $v1,0
	li $t4, 10
	
	bucle: 	lb $t5, 0($t6)			#Cogemos el primer caracter (almacenado en $t6)
		addi $t2,$zero,'+'
		addi $t8,$zero,'\0'
		slti $t0, $t5, '0'		
		beq $t5,$t2, positivo		#Si encontramos un mas simplemente pasamos al siguiente caracter
		addi $t2,$zero,'-'
		beq $t5,$t8, resultado		#Si caracter en $t5 = fin de cadena salimos del bucle
		beq $t5,$t2, negativo		#Si encontramos un menos ponemos el registro de signo a 1
		beq $t5,$t9, espacio		#Si caracter es un espacio vamos a función espacio
		bne $t0, $zero, noNumero	#Si no es un número acaba B
		slti $t0, $t5, ':'
		beq $t0, $zero, noNumero	#Si no es un número acaba
		mul $t1, $t1, $t4		#MULTIPLICAR SEGÚN ALGORITMO
		lui $t8,0x8000
		and $t2,$t1,$t8
		addi $t5, $t5, -48		#48 equivale a '\0'
		bne $t2,$zero,fueraRango		
		addu $t1, $t1, $t5	
		and $t2,$t1,$t8
		li $v1,0	
		bne $t2,$zero,fueraRango
		addi $s0,$s0,1	
				
				
		incrementar: 	addi $t6, $t6, 1	#Pasa a siguiente caracter de la cadena
				j bucle	
		
		espacio: 	bne $t1,$zero,resultado
				bne $v1, 0, noNumero
				j incrementar		
	
		negativo: 	li $t3,1		#Registro que guarda signo a 1 = se ha encontrado con un -
		positivo:	addi $v1,$v1, 1
				slti $t8,$v1,2
				bne $v1,1,noNumero
				j incrementar
				
		fueraRango:	li $v1, 2		#Valor fuera de rango
				j exit
		
		noNumero: 	li $v1, 1
				li $s0,1
		
		resultado: 	beq $s0,$zero,noNumero
				beq $t1, $zero, exit
				beq $t3, $zero, exit
				sub $t1, $zero, $t1
	exit:
		lw $s0,0($sp)
		addi $sp,$sp,4
		move $v0,$t1				#Devolución de valor debe ser en $v0
		jr $ra 
		
 #---------------------------------------------------------------------------------------------------------------CalculaDistancia
		
#$a0 - puntero a punto p
#$a1 - puntero a punto q
#$v0 - resultado del cálculo de la distancia: >=0 resultado correcto; -1 = nº de dimensiones de p y q es distinto; -2 = overflow en cálculo intermedio
#$t0 - Guarda valor dimensión p
#$t1 - Guarda valor dimension q
#$t2 - acumulador
#$t3 - guarda posiciones del punto p; guarda andi posicion x/y/z punto p
#$t4 - guarda posiciones del punto q; guarda andi resultado; guarda andi sumas finales
#$t5 - guarda vueltas a loop
#$t6 - guarda resta de posiciones y cuadrados

CalculaDistancia:	
	addi $sp,$sp,-4
	sw $ra,0($sp)

	lw $t0,0($a0)		
	lw $t1,0($a1)
	addi $t2,$zero,0
	addi $t5,$zero,0
	addi $a0,$a0,4
	addi $a1,$a1,4
	bne $t0,$t1,errordimensiones
	
Loop:	lw $t3,0($a0)
	lw $t4,0($a1)
	andi $t8,$t3,0x80000000		#Comprobación overflow1
	andi $t9,$t4,0x80000000
	subu $t6,$t3,$t4
	beq $t8,$t9,multiplicacion

CompoverflowNOigualsigno:	
	andi $t4,$t6,0x80000000
	bne $t8,$t4,Overflow

multiplicacion:
	andi $t7,$t6,0x80000000
	mulu $t6,$t6,$t6

	mflo $t3
	mfhi $t4
	andi $t3,$t3,0x80000000
	andi $t4,$t4,0x80000000
	bne $t3,$zero,Overflow
	bne $t4,$t7,Overflow
	
	addu $t2,$t2,$t6
	addi $t5,$t5,1
	andi $t4,$t2,0x80000000
	addi $a0,$a0,4
	bne $t4,$zero,Overflow		#Comprobación overflow3
	addi $a1,$a1,4
	bne $t5,$t1,Loop
	
	move $a0,$t2
	jal Sqrt	
	
exitCalculaDistancia:
	lw $ra,0($sp)
	addi $sp,$sp,4
	jr $ra
		
Overflow:
	addi $v0,$zero,-2
	j exitCalculaDistancia

errordimensiones:
	li $v0,-1
	j exitCalculaDistancia

#---------------------------------------------------------------------------------------------------------------itoa

itoa:	
	li $t8,2
	li $t9, 1
	move $t1,$a0
	addi $t2,$zero,10
	move $t4, $a2
	la $t7, almacen
	li $t6, 0
	li $t5,0
	
	negativo32:
		andi $t0,$t1,0x80000000		#Mira a ver si primer numero es un 1 (número negativo)
		beq $t0,$zero,positivoitoa	#En caso de que no sea un uno, es positivo asi que saltamos los pasos de que sea negativo
		xori $t1,$t1,0xffffffff		#Operacion xori cambia 1 por 0 y 0 por 1 (Algoritmo Ca2 de cambio de negativo a positivo)
		addi $t5,$zero,1		#$t1 = registro que guarda signo a 1 (negativo)
		addi $t1,$t1,1			#Añadimos 1 (Algoritmo Ca2 de cambio de negativo a positivo)
		
	natural32:
		andi $t1,$t1,0xffffffff		#Se convierte en un numo de 32 bits
		
			
	positivoitoa:				#Algoritmo para pasar número positivo a cadena	
			divu $t1,$t2		#Dividimos sin tener en cuenta el signo el número positivo entre 10
			mfhi $t3		#Guardamos resto de división en $t3
			mflo $t1		#Guardamos cociente de división en $t1 
			addi $t3,$t3,'0'	#Convertimos número a caracter
			
			addi $t6, $t6, 1
			
			sb $t3, 0($t7)		#Almacenamos el bit del resultado en el almacen
			addi $t7, $t7, 1	#Incrementamos la memoria y el contador
			
			bne $t1,$zero,positivoitoa	#Si aun quedan digitos del número,repetimos algoritmo
			
			beq $t5,$zero,cambioposicion	#Si el número es positivo salta
			addi $t0, $zero, '-'		#Si no se le añade el menos a la cadena de caracteres
			addi $t6, $t6, 1
			sb $t0, 0($t7)
			addi $t7, $t7, 1		#Se incrementa la memoria y el contador
	
	cambioposicion:
			addi $t7, $t7, -1		#Se resta uno a la memoria para empezar bien
			
			lb $t0, 0($t7)			#Se almacena el último bit del almacen en el primero del resultado
			addi $t6, $t6, -1
			sb $t0, 0($t4)
			addi $t4, $t4, 1		#Se incrementa la memoria y se disminuyes el contador
			
			
			bne $t6, $zero, cambioposicion	#Comprueba que se haya acabado de almacenar el resultado correcto
			
			addi $t0, $zero, '\0'		#Se añade el bit de fin de linea
			sb $t0, 0($t4)
			
			jr $ra