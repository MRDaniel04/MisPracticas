package amazingco;

import es.uva.inf.poo.maps.GPSCoordinate;
import java.time.LocalDate;
import java.util.HashMap;

public class PostOffice extends PickingPoint implements IdentificationRegistry {

    private double cashToDeliver;
    private HashMap<String, Package> pickupRecords;

    public PostOffice(String identificador, GPSCoordinate GPSCoordinate) {
        super(identificador, GPSCoordinate);
        setCashToDeliver(0.0);
        iniciarHashMap();
    }

    public void setCashToDeliver(double cash) {
    	this.cashToDeliver=cash;
    }
    
    public double getCashToDeliver() {
    	return cashToDeliver;
    }
    
    public void iniciarHashMap() {
    	this.pickupRecords=new HashMap<String,Package>();
    }
    
    
    public boolean isPackageInPostOffice(String packageCode) {
        if(pickupRecords.containsKey(packageCode)) {
        	return true;
        }
        else {
        	return false;
        }
    }
    
    @Override 
    public boolean isPackageRegistered(String packageCode) {
    	if (isPackageInPostOffice(packageCode)) {
    		return pickupRecords.get(packageCode).getpackagetaken();
    	}
    	return false;
    }
    
    @Override
    public Package getPackageRegistered(String packageCode) {
    	if (isPackageInPostOffice(packageCode)) {
    		if (!isPackageRegistered(packageCode)) {
                throw new IllegalArgumentException("El paquete no está registrado.");
            }
            return pickupRecords.get(packageCode);
    	}
    	throw new IllegalArgumentException("El codigo de paquete no pertenece a ningún paquete en el PostOffice");
    }
    
    //TODO
    @Override
    public String getRegisteredIdFor(String packageCode) {
    	if (isPackageInPostOffice(packageCode)) {
    		if (!isPackageRegistered(packageCode)) {
                throw new IllegalArgumentException("El paquete no está registrado.");
        
            }
            return pickupRecords.get(packageCode).getDNITaken();
    	}
    	throw new IllegalArgumentException("El codigo de paquete no pertenece a ningún paquete en el PostOffice");
    }
    
    //TODO
    @Override
    public LocalDate getPickupDateFor(String packageCode) {
    	if (isPackageInPostOffice(packageCode)) {
    		if (!isPackageRegistered(packageCode)) {
                throw new IllegalArgumentException("El paquete no está registrado.");
            }
            return pickupRecords.get(packageCode).getpackagedatetaken();
    	}
    	throw new IllegalArgumentException("El codigo de paquete no pertenece a ningún paquete en el PostOffice");
    }

    @Override
    public void registerCertifiedPackagePickup(Package p, String dni, LocalDate pickupDate) {
    	if (isPackageInPostOffice(p.getpackagecode())) {
        	if(!p.getcertificado()) {
        		throw new IllegalArgumentException("El paquete debe de permitir entrega certificada");
        	}
        	else {
        		if(p.getDNI().contains(dni)) {
        			if(pickupDate.compareTo(p.getpackagedate())<0) {
        				p.setPackageDateTaken(pickupDate);
        				p.setDNITaken(dni);
        				p.setpackagetaken(true);
        			}
        			else {
        				throw new IllegalArgumentException("La fecha caduco");
        			}
        		}
        		else {
        			throw new IllegalArgumentException("El DNI no se encuentra entre los posibles");
        		}
        	}
    	}	
    }

    public void deliverCashToAmazingCo() {
        // Pagar a AmazingCo y reiniciar el acumulador
        cashToDeliver = 0.0;
    }
    
    public void addPackage(Package Package) {
    	assert(getstate()!=false);
    	Package.setpackagereturned(false);
    	Package.setpackagetaken(false);
    	for(Package p:pickupRecords.values()) {
    		if(p.equals(Package)) {
    			throw new IllegalArgumentException("El paquete ya se encuentra en el PostOffice");
    		}
    	}
    	pickupRecords.put(Package.getpackagecode(),Package);
    }
    
    public Package getPackageCertified(String packagecode,String DNI,LocalDate pickupDate) {
    	assert(getstate()!=false);
    	for(String i:pickupRecords.keySet()) {
			if(pickupRecords.get(i).getpackagecode().equals(packagecode)) {
				registerCertifiedPackagePickup(pickupRecords.get(i),DNI,pickupDate);
				return pickupRecords.get(i);
			}
    	}
        throw new IllegalArgumentException("El paquete no está registrado.");
    }
    
    @Override
    public boolean hasAvailableSpace() {
        return true;
    }
}
