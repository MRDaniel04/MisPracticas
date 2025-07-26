.data
vector: .word 22,15,13,26,20,24,18,14,5,4,12,19,2,10,0,21,7,8,1,9,25,11,3,6,16,17,23

.text

la $t0, vector		#cargo dir. vector

addi $t1, $t0, 104		#limite i  (direccion V[25])
addi $t2, $t0, 108		#limite j  (direccion V[26])

move $t3, $t0		#direccion V[i]

loopI:
	lw $t5, 0($t3)		#cargo V[i]			
	addi $t4, $t3,4 	#direccion inicial V[J]= direccion V[i+1]
loopJ:
	lw $t6, 0($t4)		#cargo V[j]	
	slt $t7, $t6, $t5	#V[j] < V[i]??
	beq $t7, $zero, incJ	#si V[j] >  V[i], salto
	sw $t5,	0($t4)		#si es asi, guardo V[j] la poisicion de V[i]
	move $t5, $t6		#actualizo el registro que antes contenia V[i]
incJ:
	addi $t4, $t4, 4	#incremento V[j]	
	bne $t4, $t2, loopJ	#compruebo si se ha llegado al final en el loop J
	addi $t3, $t3, 4	#incremento V[i]
	sw $t5, -4($t3)		#guardo el valor de V[i]
	bne $t3, $t1, loopI
	
end:
	li $v0, 10
	syscall

