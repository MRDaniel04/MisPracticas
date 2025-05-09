package amazingco;
import java.time.LocalDate;

public interface IdentificationRegistry {
	
	/**
	 * Consulta, dado su c�digo, si la recogida de un paquete ha sido registrada
	 * @param packageCode identificador de un paquete
	 * @return {@code true} cuando existe un registro de recogida de {@code packageCode}, 
	 * <br> {@code false} en caso contrario
	 */
	public boolean isPackageRegistered(String packageCode);
	
	/**
	 * Consulta, dado su c�digo, un paquete cuya recogida ha sido registrada
	 * @param packageCode identificador del paquete que se quiere, requiere {@code isPackageRegistered(packageCode)}
	 * @return garantiza que el resultado es {@code != null}
	 * @throws IllegalArgumentException o AssertionError cuando {@code !isPackageRegistered(packageCode)}
	 */
	public Package getPackageRegistered(String packageCode);
	
	/**
	 * Consulta el dni de la persona que recogi� el paquete cuyo identificador se pasa como argumento
	 * @param packageCode identificador del paquete que se consulta, requiere {@code isPackageRegistered(packageCode)}
	 * @return garantiza que el resultado es un dni v�lido (en esta pr�ctica solamente importar� que no sea null).
	 * @throws IllegalArgumentException o AssertionError cuando {@code !isPackageRegistered(packageCode)}
	 */
	public String getRegisteredIdFor(String packageCode);
	
	/**
	 * Consulta la fecha en la que se recogi� el paquete dado
	 * @param packageCode identificador del paquete que se consulta, requiere {@code isPackageRegistered(packageCode)} 
	 * @return garantiza que el resultado es {@code != null}
	 * @throws IllegalArgumentException o AssertionError cuando {@code !isPackageRegistered(packageCode)}
	 */
	public LocalDate getPickupDateFor(String packgeCode);
	
	/**
	 * Realiza el registro de la recogida certificada de un paquete 
	 * @param p paquete cuya recogida se registra, requiere que {@code p!= null}, 
	 * que no est� registrado {@code !isPackageRegistered(p.getCode())} y que p sea un paquete certificado 
	 * @param dni de la persona que recoge, requiere que el dni est� entre los autorizados en el paquete p para la recogida
	 * @param pickupDate fecha en la que se produce la recogida, requiere {@code pickupDate != null} 
	 * y pickupDate no es posterior a la fecha de vencimiento de la recogida
	 * @throws IllegalArgumentException o AssertionError cuando {@code pickupDate != null} o cuando pickupDate 
	 * no es posterior a la fecha de vencimiento de la recogida
	 */
	public void registerCertifiedPackagePickup(Package p, String dni, LocalDate pickupDate);
	
}
