.data
X: .word 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37
Y: .word 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37
Z: .space 152

.text
la $t0, X
la $t1, Y
la $t2, Z
li $t6, 5100   #escalar a
addi $t3, $t2, 152   #limite del bucle (38 elementos enteros)
loop:
	lw $t4, 0($t0)		#cargar X[i] y Y[i]
	lw $t5, 0($t1)
	add $t5, $t5, $t4
	add $t5, $t5, $t6 	
	sw $t5, 0($t2)	
	addi $t0, $t0,4		#incrementar punteros
	addi $t1, $t1,4
	addi $t2, $t2,4
	bne $t2, $t3, loop
li $v0, 10
syscall
