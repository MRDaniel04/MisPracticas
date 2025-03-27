
/**
 * @author mardedi
 * @author daniega
 */

package PackageLocker;

import java.util.HashMap;
import es.uva.inf.poo.maps.GPSCoordinate;
import java.util.ArrayList;

public class PickingPointsSystem {
	
	private HashMap<String,PackageLocker> PickingPointsSystem = new HashMap<String,PackageLocker>();
	
	public PickingPointsSystem() {
		
	}
	
	public PickingPointsSystem(HashMap<String,PackageLocker> PickingPointsSystem) {
		setHashMap(PickingPointsSystem);
	}
	
	/*
	 * Permite conocer HashMap<String,PackageLocker> 
	 * @return HashMap<String,PackageLocker> cuya clave es el identificador del PackageLocker y tiene como valor el PackageLocker
	 */
	
	public HashMap<String,PackageLocker> getHashMap() {
		return PickingPointsSystem;
	}
	
	/*
	 * Permite cambiar  el HashMap<String,PackageLocker>  
	 * @param El nuevo HashMap que queremos utilizar  
	*/
	
	public void setHashMap(HashMap<String,PackageLocker> PickingPointsSystem) {
		this.PickingPointsSystem=PickingPointsSystem;
	}
	
	/*
	 * Es una funcion de tipo ArrayList que nos permite conocer que PackagesLocker hay en un radio determinado
	 * @param El radio en metros en el que queremos ver que PackageLocker hay y las coordenadas desde las que queremos buscar
	 * @return Una lista de tipo PackageLocker en la que se encuentran todos los PackageLockers en ese radio
	 */
	
	public ArrayList<PackageLocker> getDistancetoPackageLocker(double m,GPSCoordinate GPSCoordinate) {
		ArrayList<PackageLocker>PackageLockerListlessm = new ArrayList<>();
		for (PackageLocker i : PickingPointsSystem.values()) {
			if(GPSCoordinate.getDistanceTo(i.getGPSCoordinate())<=m) {
				PackageLockerListlessm.add(i);
			}
		}
		return PackageLockerListlessm; 
	}
	
	/*
	 * Permite añadir un PackageLocker al HashMap
	 * @param El PackageLocker que queremos añadir
	 * @throw IllegalArgumentException si el identificador del PackageLocker ya esta asignado a un PackageLocker
	 */
	
	public void addPackageLocker(PackageLocker PackageLocker) {
		if (PackageLocker == PickingPointsSystem.get(PackageLocker.getIdentificador())) {
			throw new IllegalArgumentException("Ese PackageLocker ya esta contenido dentro de el pickingpointssystem");
		}
		else {
			PickingPointsSystem.put(PackageLocker.getIdentificador(),PackageLocker);
		}
	}
	
	/*
	 * Permite añadir un PackageLocker nuevo al HashMap
	 * @param El numero total de taquillas del PackageLocker, su identificador y las coordenadas donde se encuentra
	 */
	
	public void addPackageLockernew(int numero_total_taquillas,String identificador,GPSCoordinate GPSCoordinates) {		
		PackageLocker PackageLocker = new PackageLocker(numero_total_taquillas,identificador,GPSCoordinates);
		PickingPointsSystem.put(PackageLocker.getIdentificador(),PackageLocker);
	}
	
	/*
	 * Permite eliminar un PackageLocker del HashMap
	 * @param El identificador del PackageLocker que queremos eliminar
	 * @throw IllegalArgumentException si ese identificador no pertenece a ningun PackageLocker
	 */
	
	public void removePackageLocker(String identificador) {
		if(PickingPointsSystem.get(identificador)==null) {
			throw new IllegalArgumentException();
		}
		else{
			PickingPointsSystem.remove(identificador);
		}
	}
	
	/*
	 * Permite saber todos los PackageLocker que estan operativos
	 * @return Una Lista de tipo PackageLocker que contiene todos los PackageLocker operativos
	 */
	
	public ArrayList<PackageLocker> getPackageLockerListoperativos(){
		ArrayList<PackageLocker> LockerListoper = new ArrayList<>();
		for (PackageLocker i : PickingPointsSystem.values()) {
			if (i.getstate()==true) {
				LockerListoper.add(i);
			}
		}
		return LockerListoper;
	}
	
	/*
	 * Permite saber todos los PackageLocker que estan fuera de servicio
	 * @return Una Lista de tipo PackageLocker que contiene todos los PackageLocker fuera de servicio
	 */
	
	public ArrayList<PackageLocker> getPackageLockerListoutofservice(){
		ArrayList<PackageLocker> LockerListofs = new ArrayList<>();
		for (PackageLocker i : PickingPointsSystem.values()) {
			if(i.getstate()==false) {
				LockerListofs.add(i);
			}
		}
		return LockerListofs;
	}
	
	/*
	 *  Permite saber todos los PackageLocker que tienen alguna taquilla vacia
	 *  @return Una Lista de tipo PackageLocker que contiene todos los PackageLockers operativos con al menos una taquilla vacia
	 */
	
	public ArrayList<PackageLocker> getPackageLockerListempty() {
		ArrayList<PackageLocker> LockerListVacios = new ArrayList<>();
		for (PackageLocker i: PickingPointsSystem.values()) {
			if(i.getNumeroTaquillasVacias()>0 && i.getstate()==true) {
				LockerListVacios.add(i);
			}
		}
		return LockerListVacios;
	}
}