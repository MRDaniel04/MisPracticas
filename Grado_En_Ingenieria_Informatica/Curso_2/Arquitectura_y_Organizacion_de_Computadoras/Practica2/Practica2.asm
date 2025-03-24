.text
#a0 --> puntero a vector de indices
#a1 --> tamaño vector indices
#a2 --> puntero a vector
#a3 --> tamaño vector

#salida
#v0 --> suma elementos
#v1 --> error (0 --> todo ok, 1--> error ejecución)

SumaDispersa:
	slt $t0, $a3, $a1
	li $v0, 0   #sumatorio
	bne $t0, $zero, SumaDispersa_error  #si el vector de indices es mayor que el vector de datos, error
	li $t0, 0   #variable control bucle que recorre vector de indices
Loop_SumaDispersa:
	lw $t1, 0($a0)		#cargo indice
	addi $a0, $a0, 4	#incrementamos puntero
	slt $t5,$t1,$a3
	sll $t1, $t1, 2  	# Calculamos dirección: index *4
	addi $t0, $t0,1 	#incrementamos variable puntero bucle
	add $t1, $a2, $t1 	# Calculamos dirección: VEC + index*4
	lw  $t2, 0($t1)		#Cargamos dato
	beq $t5,$zero,SumaDispersa_error
	add $v0, $v0, $t2
	beq  $t0, $a1,SumaDispersa_ok
	j Loop_SumaDispersa
	
SumaDispersa_ok:
	li $v1, 0
	jr $ra
	
SumaDispersa_error: 
	li $v1, 1
	jr $ra

    
#calcula máximo y mínimo de un vector 
#a0 --> puntero a vector
#a1 --> N
#devuelve
#v0 --> máximo
#v1 --> mínimo

MaxMin: 
	li $v0, 0x80000000  	#minimo de los enteros
	li $v1, 0x7FFFFFFF   	#maximo de los enteros
	li $t0, 0		#contador bucle
	li $t4, 0
MaxMin_loop:
		lw $t1, 0($a0)			#CARGO PALABRA
		addi $t0, $t0, 1
		slt $t4, $t1, $v0
		beq $t4, $zero,etiqueta1 	#si v0 es mayor que el valor leído, no actualizo el máximo
		j check_min
etiqueta1:	move $v0, $t1

check_min:
		slt $t4, $v1, $t1
		addi $a0, $a0, 4
		beq $t4, $zero, etiqueta2 #si v1 es menor que el valor leído, no actualizo el mínimo
		j incr_punt
etiqueta2:	move $v1, $t1

incr_punt:
		beq $t0, $a1,finfuncion
		j MaxMin_loop
finfuncion:	jr $ra