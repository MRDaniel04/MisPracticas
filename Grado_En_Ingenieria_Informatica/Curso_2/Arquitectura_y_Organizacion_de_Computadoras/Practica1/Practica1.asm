.data
	almacen: .space 20
	resultado: .space 20
.text


pila_test:
	addi $sp, $sp, -16	#Reservamos en pila 12 espacios en pila 
	
	sw $ra, 0($sp)		#Apilamos
	sw $s0, 4($sp)
	sw $s1, 8($sp)
	sw $s2, 12($sp)
	sw $a1, 16($sp)

	jal atoi		#Salta a atoi con $a0 la cadena de caracteres que se da
	
	move $s0, $v0		#Almacenamos el entero en $s0
	
	move $a0, $s0
	jal operacion1		#Llamamos a operacion1
	
	move $s1, $v0		#Almacenamos el resultado en $s1
	
	move $a0, $s0
	jal operacion2		#Llamamos a operacion2 con $a0 el mismo entero que en operacion1
	
	move $s2, $v0		#Almacenamos el resultado en $s2
	
	mul $a0,$s1,$s2		#Hacemos la multiplicacion
	li $a1, 0		#Almacenamos en a1 el valor 0 ya que el número se debe interpretar como un numero en Ca2 de 32 bits
	lw $a1,16($sp)		#Recuperamos el valor pasado a pila_test de a1 que es el puntero a la cadena donde debemos almacenar los caracteres
	move $a2,$a1		#El parámetro que corresponde al puntero se debe almacenar en a2 luego lo movemos ahi
	jal itoa		#Llamamos a itoa con $a0 el resultado de la multiplicación y $a1 el código de un entero de 32 bits en complemento a 2
	
	move $a1,$a2		#Almacenamos el resultado en $a1 como nos dicen
	
	lw $ra, 0($sp)		#Cargamos los registros de la pila y llevamos el apuntador a donde estaba antes
	lw $s0, 4($sp)
	lw $s1, 8($sp)
	lw $s2, 12($sp)
	
	addi $sp, $sp, 16	
	jr $ra
	
	
	
atoi:	add $t1,$zero,$zero			#Inicializamos todos los registros
	add $t3,$zero,$zero
	add $t7,$zero,$zero
	addi $v1,$zero,0
	li $t4, 10
	move $t6,$a0
	
	bucle: 	lb $t5, 0($t6)			#Cogemos el primer caracter (almacenado en $t6)
		beq $t5, '\0', Resultado	#Si caracter en $t5 = fin de cadena salimos del bucle
		beq $t5, ' ', espacio		#Si caracter es un espacio vamos a función espacio
		beq $t5, '-', negativo		#Si encontramos un menos ponemos el registro de signo a 1
		beq $t5, '+', Positivo		#Si encontramos un mas simplemente pasamos al siguiente caracter
		
		
		slti $t0, $t5, '0'
		bne $t0, $zero, noNumero	#Si no es un número acaba		
		slti $t0, $t5, ':'		
		beq $t0, $zero, noNumero	#Si no es un número acaba
	
	
		addi $t5, $t5, -48		#48 equivale a '\0'
		mul $t1, $t1, $t4		#MULTIPLICAR SEGÚN ALGORITMO
		andi $t2,$t1,0x80000000		#COMPROBACIÓN NO DESBORDAMIENTO
		bne $t2,$zero,fueraRango		
		addu $t1, $t1, $t5		
		andi $t2,$t1,0x80000000		
		bne $t2,$zero,fueraRango	
		
		li $v1, 0			#Si el numero es coorecto, el signo de error es 0
				
		incrementar: 	addi $t6, $t6, 1	#Pasa a siguiente caracter de la cadena
				j bucle	
		
		espacio: 	bne $t1,$zero,resultado	
				j incrementar		
	
		negativo: 	li $t3, 1		#Registro que guarda signo a 1 = se ha encontrado con un -
				addi $t7,$t7,1
				li $v1, 1
				j incrementar
				
		Positivo:	li $v1, 1		#Registro que guarda signo a 1 = se ha encontrado con un +
				addi $t7,$t7,1
				j incrementar
				
		fueraRango:	li $v1, 2		#Valor fuera de rango
				j exit
		
		noNumero: 	bne $t1,$zero,Resultado
				li $v1, 1
		
		Resultado: 	beq $t1, $zero, exit
				beq $t3, $zero, exit
				sub $t1, $zero, $t1
	exit:
		move $v0,$t1				#Devolución de valor debe ser en $v0
		jr $ra


itoa:	move $t1,$a0
	addi $t2,$zero,10
	move $t4, $a2
	la $t7, almacen
	li $t6, 0
	li $t5,0
	
	#Secuencia de saltos según el enunciado del valor de $a1
	beq $a1,0,negativo32
	beq $a1,1,natural32
	beq $a1,2,negativo16
	beq $a1,3,natural16
	beq $a1,4,negativo8
	beq $a1,5,natural8
	
	negativo32:
		andi $t0,$t1,0x80000000		#Mira a ver si primer numero es un 1 (número negativo)
		beq $t0,$zero,positivo		#En caso de que no sea un uno, es positivo asi que saltamos los pasos de que sea negativo
		addi $t5,$zero,1		#$t1 = registro que guarda signo a 1 (negativo)
		xori $t1,$t1,0xffffffff		#Operacion xori cambia 1 por 0 y 0 por 1 (Algoritmo Ca2 de cambio de negativo a positivo)
		addi $t1,$t1,1			#Añadimos 1 (Algoritmo Ca2 de cambio de negativo a positivo)
		j natural32
		
		
	negativo16:				#Ver comentarios de negativo32
		andi $t0,$t1,0x00008000
		beq $t0,$zero,natural16
		addi $t5,$zero,1
		xori $t1,$t1,0xffffffff
		addi $t1,$t1,1
		j natural16
		
		
	negativo8:				#Ver comentarios de negativo32
		andi $t0,$t1,0x00000080
		beq $t0,$zero,natural8
		addi $t5,$zero,1
		xori $t1,$t1,0xffffffff
		addi $t1,$t1,1
		j natural8
		
	natural32:
		andi $t1,$t1,0xffffffff		#Se convierte en un numo de 32 bits
		j positivo
	
	natural16:
		andi $t1,$t1,0x0000ffff		#Se convierte en un numero de 16 bits
		j positivo
	
	natural8:
		andi $t1,$t1,0x000000ff		#Se convierte en un numero de 8 bits
		j positivo
			
	positivo:				#Algoritmo para pasar número positivo a cadena	
			divu $t1,$t2		#Dividimos sin tener en cuenta el signo el número positivo entre 10
			mfhi $t3		#Guardamos resto de división en $t3
			mflo $t1		#Guardamos cociente de división en $t1 
			addi $t3,$t3,'0'	#Convertimos número a caracter
			
			sb $t3, 0($t7)		#Almacenamos el bit del resultado en el almacen
			addi $t7, $t7, 1	#Incrementamos la memoria y el contador
			addi $t6, $t6, 1
	
			
			bne $t1,$zero,positivo	#Si aun quedan digitos del número,repetimos algoritmo
			
			beq $t5,0,cambioposicion	#Si el número es positivo salta
			addi $t0, $zero, '-'		#Si no se le añade el menos a la cadena de caracteres
			sb $t0, 0($t7)
			addi $t7, $t7, 1		#Se incrementa la memoria y el contador
			addi $t6, $t6, 1
	
	cambioposicion:
			addi $t7, $t7, -1		#Se resta uno a la memoria para empezar bien
			add $t0, $zero, $zero		#Se inicializa a 0 el registo auxiliar
			
			lb $t0, 0($t7)			#Se almacena el último bit del almacen en el primero del resultado
			sb $t0, 0($t4)
			
			addi $t4, $t4, 1		#Se incrementa la memoria y se disminuyes el contador
			addi $t6, $t6, -1
			
			bne $t6, $zero, cambioposicion	#Comprueba que se haya acabado de almacenar el resultado correcto
			
			addi $t0, $zero, '\0'		#Se añade el bit de fin de linea
			sb $t0, 0($t4)
			
			la $t4, resultado
			move $v0, $t4
			
			jr $ra
