/**
 * @author mardedi
 * @author daniega
 */

package amazingco;

import java.util.HashMap;
import es.uva.inf.poo.maps.GPSCoordinate;

public class PackageLocker extends GroupablePoint {
	private HashMap<Integer,Package>TaquillaHash=new HashMap<Integer,Package>();
    private int numero_taquillas;
    private int numero_taquillas_vacias;
	private Package Packageremoved; 
	
	/**
	 * Constructor por defecto de la clase PackageLocker
	 * Suponemos que el PackageLocker esta operativo cuando se crea.
	 * Suponemos que las coordenadas del PackageLocker son (0.0,0.0) hasta que se introduzcan unas nuevas (ver método setCoordinates)
	 */
	
	public PackageLocker() {
		setIdentificador("00000");
		GPSCoordinate GPSCoordinate = new GPSCoordinate(0.0,0.0);
		setCoordinates(GPSCoordinate);
		setstate(true);
		setLocker(true);
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
		setLocker(true);
	}
	
	
	
	/**
	 * Inicialización de un HashMap que contiene el número de taquillas del PackageLocker,
	 * junto con el paquete que esta en esa taquilla. Como se acaba de crear el HashMap, los paquetes se inicializan a null,
	 * ya que aun no hay ninguno metido en ninguna taquilla.
	*/
	
	private void iniciarTaquilla() {
	    for (int numero_taquilla = 1; numero_taquilla <= getnumerotaquillas(); numero_taquilla++) {
	    	TaquillaHash.put(numero_taquilla,null);
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
			this.numero_taquillas=numero_total_taquillas;
			settaquillasvacias(numero_total_taquillas);
			iniciarTaquilla();
		}
	}
	
	/**
	 * Consulta el valor de las taquillas del PackageLocker.
	 * @return numero_total_taquillas - número de taquillas del PackageLocker
	 */
	
	public int getnumerotaquillas() {
		return numero_taquillas;
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
		return numero_taquillas-numero_taquillas_vacias;
	}
	
	/**
	 * Devuelve el objeto paquete e+que se encuentra en una taquilla
	 * @param numero_taquilla
	 * @return Taquillahash.get(numero_taquilla) - paquete en la taquilla numero_taquilla
	 */
	public Package getPackage(int numero_taquilla) {
		return TaquillaHash.get(numero_taquilla);
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
		for (Integer Taquilla : TaquillaHash.keySet()) {
			if(TaquillaHash.get(Taquilla)!=null&&Codigo.equals(TaquillaHash.get(Taquilla).getpackagecode())) {
				numero_taquilla=Taquilla;
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
		if (TaquillaHash.get(numero_taquilla)==null) {
			TaquillaHash.put(numero_taquilla,Package);
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
		for (Integer Taquilla : TaquillaHash.keySet()) {
			if(TaquillaHash.get(Taquilla)!=null&&Codigo.equals(TaquillaHash.get(Taquilla).getpackagecode())) {
				if (!returned) {
					TaquillaHash.get(Taquilla).setpackagereturned(false);
					TaquillaHash.get(Taquilla).setpackagetaken(true);
				}
				else {
					TaquillaHash.get(Taquilla).setpackagereturned(true);
					TaquillaHash.get(Taquilla).setpackagetaken(false);
				}
				setPackageremoved(TaquillaHash.get(Taquilla));
				TaquillaHash.put(Taquilla,null);
				centinela=true;
			}
		}
		if (!centinela) {
			throw new IllegalArgumentException("El código de paquete proporcionado no corresponde con el código de ningún paquete en el PackageLocker");
		}
		numero_taquillas_vacias++;
	}
	@Override
	public boolean hasAvailableSpace() {
		return numero_taquillas_vacias!=0;
	}
			
}	

