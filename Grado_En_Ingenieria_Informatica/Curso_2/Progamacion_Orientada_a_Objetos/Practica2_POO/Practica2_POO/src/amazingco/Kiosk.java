package amazingco;

import es.uva.inf.poo.maps.GPSCoordinate;
import java.util.HashMap;

public class Kiosk extends GroupablePoint {
	private HashMap<Integer,Package>StorageHash=new HashMap<Integer,Package>();
    private int storageCapacity;
    private int llenos;
    private double cashToDeliver;

    public Kiosk() {
    	setLocker(false);
    }
    
    public Kiosk(String identificador, GPSCoordinate GPSCoordinate, int storageCapacity,int llenos,boolean state) {
        super(identificador, GPSCoordinate,state);
        if (storageCapacity <= 0) {
            throw new IllegalArgumentException("La capacidad de almacenamiento debe ser mayor que cero.");
        }
        setllenos(llenos);
        setstate(state);
        setStorageCapacity(storageCapacity);
        setCashToDeliver(0.0);
        iniciarStorage();
    }

    private void iniciarStorage() {
    	for (int numero_storage = 1; numero_storage <= getStorageCapacity(); numero_storage++) {
	        StorageHash.put(numero_storage,null);
	    }
    }
    
    public void setStorageCapacity(int storageCapacity) {
        if (storageCapacity <= 0) {
            throw new IllegalArgumentException("La capacidad de almacenamiento debe ser mayor que cero.");
        }
        else {
            this.storageCapacity = storageCapacity;
            setllenos(0);
            iniciarStorage();
        }
    }
    
    public int getStorageCapacity() { 
        return storageCapacity;
    }
    
    public void setllenos(int llenos) {
    	this.llenos=llenos;
    }
    
    public int getllenos() {
    	return llenos;
    }

    public void setCashToDeliver(double cashToDeliver) {
    	if (cashToDeliver < 0.0) {
            throw new IllegalArgumentException("No puedes tener dinero negativo.");
        }
    	this.cashToDeliver = cashToDeliver;
    }
    
    public double getCashToDeliver() {
        return cashToDeliver;
    }


    public void deliverCashToAmazingCo() {
        // Pagar a AmazingCo y reiniciar el acumulador
        cashToDeliver = 0.0;
    }

    public void addPackage(Package Package) {
    	assert(getstate()!=false);
    	Package.setpackagereturned(false);
    	Package.setpackagetaken(false);
    	if(llenos>=storageCapacity) {
    		throw new IllegalStateException("El Kiosk se encuentra lleno");
    	}
    	else {
    		for(Integer i:StorageHash.keySet()) {
    			if(StorageHash.get(i)==null) {
    				StorageHash.put(i,Package);
    				llenos++;
    				return;
    			}
    		}
    	}
    }

    public Package getPackageNoCertified(String packagecode) {
    	for(Integer i:StorageHash.keySet()) {
			if(StorageHash.get(i).getpackagecode().equals(packagecode)) {
				if(StorageHash.get(i).getpago()) {
	        		cashToDeliver+=StorageHash.get(i).getprecio();
	        	}
	    		return StorageHash.get(i);
			} 	
    	}
    	throw new IllegalArgumentException("El packagecode no coincide con ninguno del kiosk");
    }
    
    public Package getPackageCertified(String packagecode,String DNI) {
    	assert(getstate()!=false);
    	Package Package = getPackageNoCertified(packagecode);
    	if(Package.getDNI().contains(DNI)) {
    		return Package;
    	}
    	else {
    		throw new IllegalArgumentException ("El DNI no se encuentra entre los requeridos");
    	}
    }
    @Override
    public boolean hasAvailableSpace() {
        return llenos<storageCapacity;
    }
}
    