/**
 * @author mardedi
 * @author daniega
 */

package amazingco;

import java.util.HashMap;
import es.uva.inf.poo.maps.GPSCoordinate;
import java.util.ArrayList;

public class PickingPointsSystem {
	
	private HashMap<String, PickingPoint> pickingPointsSystem = new HashMap<>();

    public PickingPointsSystem() {

    }
    
    public PickingPointsSystem(HashMap<String, PickingPoint> pickingPointsSystem) {
        setHashMap(pickingPointsSystem);
    }

	/*
	 * Permite conocer HashMap<String,pickingPoint> 
	 * @return HashMap<String,pickingPoint> cuya clave es el identificador del pickingPoint y tiene como valor el pickingPoint
	 */
	
    public HashMap<String, PickingPoint> getHashMap() {
        return pickingPointsSystem;
    }

	/*
	 * Permite cambiar  el HashMap<String,pickingPoint>  
	 * @param El nuevo HashMap que queremos utilizar  
	*/
	
    public void setHashMap(HashMap<String, PickingPoint> pickingPointsSystem) {
        this.pickingPointsSystem = pickingPointsSystem;
    }

	
	/*
	 * Es una funcion de tipo ArrayList que nos permite conocer que pickingPoint hay en un radio determinado
	 * @param El radio en metros en el que queremos ver que pickingPoint hay y las coordenadas desde las que queremos buscar
	 * @return Una lista de tipo pickingPoint en la que se encuentran todos los pickingPoints en ese radio
	 */
	
    public ArrayList<PickingPoint> getDistancetoPickingPoint(double m, GPSCoordinate GPSCoordinate) {
		if(GPSCoordinate==null) {
			throw new IllegalArgumentException("Las coordenadas no pueden ser nulas");
		}
		if(m<=0) {
			throw new IllegalArgumentException("El radio debe ser positivo");
		}
        ArrayList<PickingPoint> pickingPointListlessm = new ArrayList<>();
        for (PickingPoint i : pickingPointsSystem.values()) {
            if (GPSCoordinate.getDistanceTo(i.getGPSCoordinate()) <= m && i.hasAvailableSpace()) {
                pickingPointListlessm.add(i);
            }
        }
        return pickingPointListlessm;
    }
	
	/*
	 * Permite añadir un pickingPoint al HashMap
	 * @param El pickingPoint que queremos añadir
	 * @throw IllegalArgumentException si el identificador del pickingPoint ya esta asignado a un pickingPoint
	 */
	
    public void addPickingPoint(PickingPoint pickingPoint) {
        if (pickingPoint == pickingPointsSystem.get(pickingPoint.getIdentificador())) {
            throw new IllegalArgumentException("Ese PickingPoint ya est� contenido dentro del pickingPointsSystem");
        } else {
            pickingPointsSystem.put(pickingPoint.getIdentificador(), pickingPoint);
        }
    }
	
	/*
	 * Permite eliminar un pickingPoint del HashMap
	 * @param El identificador del pickingPoint que queremos eliminar
	 * @throw IllegalArgumentException si ese identificador no pertenece a ningun pickingPoint
	 */

    public void removePickingPoint(String identificador) {
        if (pickingPointsSystem.get(identificador) == null) {
            throw new IllegalArgumentException();
        } else {
            pickingPointsSystem.remove(identificador);
        }
    }
	
	/*
	 * Permite saber todos los pickingPoint que estan operativos
	 * @return Una Lista de tipo pickingPoint que contiene todos los pickingPoint operativos
	 */
	
    public ArrayList<PickingPoint> getPickingPointListOperativos() {
        ArrayList<PickingPoint> pointListOper = new ArrayList<>();
        for (PickingPoint i : pickingPointsSystem.values()) {
            if (i.getstate()) {
                pointListOper.add(i);
            }
        }
        return pointListOper;
    }
    
	/*
	 * Permite saber todos los pickingPoint que estan fuera de servicio
	 * @return Una Lista de tipo pickingPoint que contiene todos los pickingPoint fuera de servicio
	 */
	
	public ArrayList<PickingPoint> getPickingPointListOutOfService() {
        ArrayList<PickingPoint> pointListOFS = new ArrayList<>();
        for (PickingPoint i : pickingPointsSystem.values()) {
            if (!i.getstate()) {
                pointListOFS.add(i);
            }
        }
        return pointListOFS;
    }
	
	/*
	 *  Permite saber todos los pickingPoint que tienen espacio disponible
	 *  @return Una Lista de tipo pickingPoint que contiene todos los pickingPoints operativos con al menos una taquilla vacia
	 */
	
    public ArrayList<PickingPoint> getPickingPointListEmpty() {
        ArrayList<PickingPoint> pointListVacios = new ArrayList<>();
        for (PickingPoint i : pickingPointsSystem.values()) {
            if (i.hasAvailableSpace() && i.getstate()) {
                pointListVacios.add(i);
            }
        }
        return pointListVacios;
    }
}