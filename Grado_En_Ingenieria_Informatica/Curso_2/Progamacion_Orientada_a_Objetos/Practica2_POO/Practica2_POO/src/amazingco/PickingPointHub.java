package amazingco;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class PickingPointHub extends PickingPoint{

	private ArrayList<GroupablePoint> GroupablePoints;
	private boolean operativo;
	
	//CONSTRUCTOR
	public PickingPointHub(GroupablePoint[] GroupablePoints) {
		setOperational();
		if (!ArrayValido(GroupablePoints)) {
			throw new IllegalArgumentException("La inicialización de la lista es incorrecta");
			
        } else {
        	this.GroupablePoints = new ArrayList<>();
			this.GroupablePoints.addAll(Arrays.asList(GroupablePoints));
        }
    	
    }
	
	//VALIDA INICIALIZACIÓN
	public boolean ArrayValido(GroupablePoint[] GroupablePoints) {
	        if(GroupablePoints==null) {
	        	return false;
	        }
			if (GroupablePoints.length < 2) {
	            return false;
	        }
	        for (GroupablePoint elementsinGroupablePoint1 : GroupablePoints) {
	            for (GroupablePoint elementsinGroupablePoint2 : GroupablePoints) {
	                if (elementsinGroupablePoint1.equals(elementsinGroupablePoint2) ||
	                	(elementsinGroupablePoint1.getIdentificador().equals(elementsinGroupablePoint2.getIdentificador()))||
	                	!(elementsinGroupablePoint1.getGPSCoordinate().equals(elementsinGroupablePoint2.getGPSCoordinate()))) {
	                    return false;
	                }
	            }
	        }
	        return true;
	}
	
	//CANTIDAD DE PUNTOS QUE FORMAN EL PICKING POINT HUB
	public int getNumberOfGroupablePoints() {
		return GroupablePoints.size();
	}
	
	//PUNTO AGRUPABLE FORMA PARTE DEL CONCENTRADOR
	public boolean getGroupablePointinHub(GroupablePoint GroupablePoint) {
		return GroupablePoints.contains(GroupablePoint);
	}
	
	//PUNTOS QUE FORMAN EL CONCENTRADOR
	public List<GroupablePoint> getListOfGroupablePoints(){
	    GroupablePoint[] array = GroupablePoints.toArray(new GroupablePoint[GroupablePoints.size()]);
	    return Arrays.asList(array);
	}
    
	//ESPACIO DISPONIBLE
	public boolean hasAvailableSpac() {
		for (GroupablePoint elementsinGroupablePoint : GroupablePoints) {
    		if(elementsinGroupablePoint.hasAvailableSpace()) {
    			return true;
    		}
		}
		return false;
	}
	
    //PASA EL CONCENTRADOR A NO OPERATIVO/OPERATIVO
    public void setNotOperational() {
    	this.operativo=false;
        for (GroupablePoint elementsinGroupablePoint : GroupablePoints) {
        		elementsinGroupablePoint.setstate(false);
        }
    }
    
    //ELEGIBLE PARA PAGAR CONTRA REEMBOLSO??
    public boolean getCCR() {
    	for (GroupablePoint elementsinGroupablePoint : GroupablePoints) {
    		if (!(elementsinGroupablePoint.getLocker())) {
    			return true;
    		}
    	}
    	return false;
    }
    
    //PASA A ESTADO OPERATIVO
    public void setOperational() {
    	for (GroupablePoint elementsinGroupablePoint : GroupablePoints) {
    		if (elementsinGroupablePoint.getstate()) {
    			this.operativo=true;
    			return;
    		}
    	}
    	throw new IllegalStateException("Si no hay ningún GroupablePoint con estado operativo, no se puede poner el estado del Hub en operativo");
    }
    
    //AÑADE A HUB
    public void addGroupablePoint(GroupablePoint newGroupable) {
    	if (GroupablePoints == null) {
    		throw new NullPointerException("No se ha incializado la lista.");
    	}
    	if(GroupablePoints.get(0).getGPSCoordinate().equals(newGroupable.getGPSCoordinate())) {
    		for (GroupablePoint elementsinGroupablePoint : GroupablePoints) {
    			if(elementsinGroupablePoint.equals(newGroupable)||
    					elementsinGroupablePoint.getIdentificador().equals(newGroupable.getIdentificador())){
    				throw new IllegalArgumentException("El GroupablePoint dado no es válido");
    			}
    		}
    		GroupablePoints.add(newGroupable);
    	}
    	else {
			throw new IllegalArgumentException("El GroupablePoint dado no es válido");
    	}
    	
    }
    
    //QUITA DE HUB
    public void removeGroupablePoint(GroupablePoint newGroupable) {
    	if (GroupablePoints.size()<=2) {
            throw new IllegalStateException("No se pueden dejar menos de dos elementos en el PickingPointHub.");
    	}
    	else if (GroupablePoints.contains(newGroupable)) {
        	GroupablePoints.remove(newGroupable);
        }
    	else {
    		throw new IllegalArgumentException("El GroupablePoint no se encuentra en el PickingPointHub");
    	}
    }
    
    //ESPACIO??
    public boolean hasAvailableSpace() {
        for (GroupablePoint elementsinGroupablePoint : GroupablePoints) {
            if (!elementsinGroupablePoint.hasAvailableSpace()) {
                return false;
            }
        }
        return true;
    }
}