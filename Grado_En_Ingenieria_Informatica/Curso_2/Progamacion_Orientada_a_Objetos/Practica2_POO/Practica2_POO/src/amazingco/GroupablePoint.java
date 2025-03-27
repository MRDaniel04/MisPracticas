package amazingco;

import es.uva.inf.poo.maps.GPSCoordinate;

public abstract class GroupablePoint extends PickingPoint {
	
	protected boolean Locker;
	

	public GroupablePoint() {
		
	}
	
    public GroupablePoint(String identificador, GPSCoordinate GPSCoordinates,boolean state) {
        super(identificador,GPSCoordinates);
        setstate(state);
    }
    
    public void setLocker(boolean Locker) {
    	this.Locker=Locker;
    }
    
    public boolean getLocker() {
    	return Locker;
    }
    
    public abstract boolean hasAvailableSpace();
    
    
    
}
