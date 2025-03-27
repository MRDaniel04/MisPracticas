package amazingco;

import es.uva.inf.poo.maps.GPSCoordinate;

public abstract class PickingPoint {

	private String identificador;
    private GPSCoordinate GPSCoordinates;
    private boolean state;
    
    public PickingPoint() {
    	
    }
    public PickingPoint(String identificador,GPSCoordinate GPSCoordinates) {
    	setIdentificador(identificador);
    	setCoordinates(GPSCoordinates);
    	setstate(true);
    }
    
    
    /**
	 * Permite cambiar la posición GPS de  cualquier punto de entrega
	 * @param GPSCoordinate - Nuevo objeto coordenadas GPS
	 */
	
	public void setCoordinates(GPSCoordinate GPSCoordinate) {
		this.GPSCoordinates=GPSCoordinate;
	}
	
	/**
	 * Permite obtener la coordenada de cualquier punto de entrega
	 * @return GPSCoordinate - Objeto coordenadas GPS
	 */
	
	public  GPSCoordinate getGPSCoordinate() {
		return GPSCoordinates;
	}
    
    /**
	 * Cambia el valor del Identificador de cualquier punto de entrega. Válido cualquier valor string que sea un número
	 * @param identificador - nuevo identificador del PackageLocker
	 */
    
    public void setIdentificador(String identificador) {
		int numero=0;
		for (int i=0;i<identificador.length();i++) {
			numero = (int)identificador.charAt(i);
			if (numero<'0'||numero>'9') {
				throw new IllegalArgumentException("El identificador debe de ser un número");
			}
		}
		this.identificador=identificador;
	}
	
    /**
	 * Permite saber el identificador de cualquier punto de entrega.
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
	 * Permite saber el estado de cualquier punto de entrega (operativo o fuera de servicio)
	 * @return state - true = El punto de entrega está operativo; false = El punto de entrega está fuera de servicio
	 */
	
	public boolean getstate() {
		return state;
	}
	
	public abstract boolean hasAvailableSpace();
}