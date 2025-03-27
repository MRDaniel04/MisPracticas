
/**
 * @author mardedi
 * @author daniega
 */

package PackageLocker;

import java.util.HashMap;
import es.uva.inf.poo.maps.GPSCoordinate;

public class PackageLocker {
	private HashMap<Integer,Package> Taquillahash = new HashMap<Integer,Package>();
	private int  numero_total_taquillas; 
	private String identificador;
	private int numero_taquillas_vacias;
	private GPSCoordinate GPSCoordinate;
	private boolean state; //operativo o no
	private Package Packageremoved; 
	
	/**
	 * Constructor por defecto de la clase PackageLocker
	 * Suponemos que el PackageLocker esta operativo cuando se crea.
	 * Suponemos que las coordenadas del PackageLocker son (0.0,0.0) hasta que se introduzcan unas nuevas (ver método setCoordinates)
	 */
	
	public PackageLocker() {
		setIdentificador("00000");
		GPSCoordinate = new GPSCoordinate(0.0,0.0);
		state=true;
	}
	
	/**
	 * Constructor con parámetros de PackageLocker
	 * Suponemos el PackageLocker está operativo cuando se crea. state=true	
	 * @param numero_total_taquillas - número de taquillas que tiene el PackageLocker = número de taquillas vacias del PackageLocker ya que lo acabamos de crear.
	 * @param identificador - String con el identificador del PackageLocker.
	 * @param GPSCoordinate - Coordenadas GPS en las que se encuentra el PackageLOcker.
	 */
	
	public PackageLocker(int numero_total_taquillas, String identificador, GPSCoordinate GPSCoordinate) {
		setnumerotaquillas(numero_total_taquillas);
		setIdentificador(identificador);
		settaquillasvacias(numero_total_taquillas);
		setCoordinates(GPSCoordinate);
		setstate(true);
	}
	
	
	
	/**
	 * Inicialización de un HashMap que contiene el número de taquillas del PackageLocker,
	 * junto con el paquete que esta en esa taquilla. Como se acaba de crear el HashMap, los paquetes se inicializan a null,
	 * ya que aun no hay ninguno metido en ninguna taquilla.
	*/
	
	private void iniciarTaquilla() {
	    for (int numero_taquilla = 1; numero_taquilla <= getnumerotaquillas(); numero_taquilla++) {
	        Taquillahash.put(numero_taquilla,null);
	    }
	}
	
	/**
	 * Cambia el valor del numero total de taquillas (vacias o llenas) del PackageLocker
	 * @param numero_total_taquillas - válido cualquier número natural (hasta el límite de int).
	 * @throws IllegalArgumentException si el número de taquillas no es un número positivo
	*/
	
	public void setnumerotaquillas(int numero_total_taquillas) {
		if (numero_total_taquillas<0) {
			throw new IllegalArgumentException("El número de taquillas debe ser positivo");
		}
		else {
			this.numero_total_taquillas=numero_total_taquillas;
			settaquillasvacias(numero_total_taquillas);
			iniciarTaquilla();
		}
	}
	
	/**
	 * Consulta el valor de las taquillas del PackageLocker.
	 * @return numero_total_taquillas - número de taquillas del PackageLocker
	 */
	
	public int getnumerotaquillas() {
		return numero_total_taquillas;
	}
	
	/**
	 * Cambia el valor del Identificador del PackageLocker. Válido cualquier valor int
	 * @param identificador - nuevo identificador del PackageLocker
	 */
	
	public void setIdentificador(String identificador) {
		int numero=0;
		for (int i=0;i<identificador.length();i++) {
			numero = (int)identificador.charAt(i);
			if (numero<'0'||numero>'9') {
				throw new IllegalArgumentException();
			}
		}
		this.identificador=identificador;
	}
	
	/**
	 * Permite saber el identificador del PackageLocker.
	 * @return identificador - identificador del PackageLocker
	 */
	
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * Cambia el estado del PackageLocker (operativo o fuera de servicio)
	 * @param state - nuevo estado del PackageLocker
	 */
	public void setstate(boolean state) {
		this.state=state;
	}
	
	/**
	 * Permite saber el estado del PackageLocker (operativo o fuera de servicio)
	 * @return state - true = El packageLocker está operativo; false = El PackageLocker está fuera de servicio
	 */
	
	public boolean getstate() {
		return state;
	}
	
	/**
	 * Permite cambiar el número de taquillas vacias
	 * @param numero_taquillas_vacias
	 */
	
	public void settaquillasvacias(int numero_taquillas_vacias) {
		this.numero_taquillas_vacias=numero_taquillas_vacias;
	}
	
	/**
	 * Permite saber numero de taquillas vacias del PackageLocker
	 * @return numero_taquillas_vacias 
	 */
	
	public int getNumeroTaquillasVacias() {
		return numero_taquillas_vacias;
	}
	
	/**
	 * Permite saber numero de taquillas llenas del PackageLocker
	 * @return numero_taquillas_vacias
	 */
	
	public int getNumeroTaquillasLlenas() {
		return numero_total_taquillas-numero_taquillas_vacias;
	}
	
	/**
	 * Permite cambiar la posición GPS de un PackageLocker
	 * @param GPSCoordinate - Nuevo objeto coordenadas GPS
	 */
	
	public void setCoordinates(GPSCoordinate GPSCoordinate) {
		this.GPSCoordinate=GPSCoordinate;
	}
	
	/**
	 * Permite obtener la coordenada del PackageLocker
	 * @return GPSCoordinate - Objeto coordenadas GPS
	 */
	
	public  GPSCoordinate getGPSCoordinate() {
		return GPSCoordinate;
	}
	
	/**
	 * Devuelve el objeto paquete e+que se encuentra en una taquilla
	 * @param numero_taquilla
	 * @return Taquillahash.get(numero_taquilla) - paquete en la taquilla numero_taquilla
	 */
	public Package getPackage(int numero_taquilla) {
		return Taquillahash.get(numero_taquilla);
	}
	
	/**
	 * Permite cambiar el valor de el estado del paquete (removed/not removed)
	 * @param Packageremoved
	 */
	
	public void setPackageremoved(Package Packageremoved) {
		this.Packageremoved=Packageremoved;
	}
	
	/**
	 * Permite obtener el valor de el estado del paquete (removed/not removed)
	 * @return Packageremoved
	 */
	
	public Package getPackageremoved() {
		return Packageremoved;
	}
	
	/**
	 * Consulta la taquilla en la que se encuentra un paquete en especifico
	 * @param Codigo - Codigo del paquete a buscar
	 * @return numero_taquilla - taquilla en la que se encuentra el paquete
	 * @throws IllegalArgumentException si el código de paquete no coincide con ningún paquete en el PackageLocker o es un código inválido
	 */
	
	public long WherePackage(String Codigo) {
		assert (getstate()!=false);
		int numero_taquilla=0;
		for (Integer Taquilla : Taquillahash.keySet()) {
			if(Taquillahash.get(Taquilla)!=null) {
				if (Codigo==Taquillahash.get(Taquilla).getpackagecode()) {
					numero_taquilla=Taquilla;
				}
			}
		}
		if (numero_taquilla==0) {
			throw new IllegalArgumentException("El código de paquete no se encuentra en este PackageLocker o es inválido");
		}
		return numero_taquilla;
	}
	
	/**
	 * Asigna un paquete a una taquilla específica y vacía del PackageLocker
	 * @param numero_taquilla - numero de la taquilla domnde queremos meter el paquete
	 * @param Package - Paquete que queremos meter en la taquilla
	 * @throws IllegalArgumentException si la taquilla seleccionada ya contiene un paquete
	 */
	
	public void addPackage(int numero_taquilla,Package Package) {
		assert (getstate()!=false);
		Package.setpackagereturned(false);
		Package.setpackagetaken(false);
		if (Taquillahash.get(numero_taquilla)==null) {
			Taquillahash.put(numero_taquilla,Package);
		}
		else {
			throw new IllegalArgumentException ("No se puede meter un paquete en una taquilla que ya tenga uno");
		}
		numero_taquillas_vacias--;
	}
	
	/**
	 * Saca un paquete de una taquilla en especifico y cambia el estado del paquete dependiendo de el paramtero returned
	 * @param Codigo
	 * @param returned - true = paquete devuelto a central, false = paquete recogido por cliente
	 * @throws IllegalArgumentException si el código del paquete proporcionado no corresponde a el código de ningún paquete en el PackageLocker
	 */
	
	public void removePackage(String Codigo,boolean returned) {
		assert (getstate()!=false);
		boolean centinela = false;
		for (Integer Taquilla : Taquillahash.keySet()) {
			if(Taquillahash.get(Taquilla)!=null) {
				if (Codigo.equals(Taquillahash.get(Taquilla).getpackagecode())) {
					if (returned==false) {
						Taquillahash.get(Taquilla).setpackagereturned(false);
						Taquillahash.get(Taquilla).setpackagetaken(true);
					}
					else {
						Taquillahash.get(Taquilla).setpackagereturned(true);
						Taquillahash.get(Taquilla).setpackagetaken(false);
					}
					setPackageremoved(Taquillahash.get(Taquilla));
					Taquillahash.put(Taquilla,null);
					centinela=true;
				}
			}
		}
		if (centinela==false) {
			throw new IllegalArgumentException("El código de paquete proporcionado no corresponde con el código de ningún paquete en el PackageLocker");
		}
		numero_taquillas_vacias++;
	}
			
}