package amazingco;

import java.time.LocalDate;
import java.util.ArrayList;

public class Package {
	private String packagecode;
	private LocalDate packagedate;
	private LocalDate packagedatetaken;
	private boolean packagetaken;
	private boolean packagereturned;
	private double precio;
	private boolean pago; 
    private boolean certificado; 
    private ArrayList<String> DNI;
    private String DNItaken;
	
	public Package() {
		setpackagecode("0000000000");
	}
	
	public Package(String packagecode,  LocalDate packagedate, boolean pago,double precio, boolean certificado, ArrayList<String> DNI ) {
		setpackagecode(packagecode);
		setPackageDate(packagedate);
		setpago(pago);
		setprecio(precio);
		setcertificado(certificado);
		setDNI(DNI);
		setPackageDateTaken(null);
	}
	public Package(String packagecode,LocalDate packagedate,boolean pago,boolean certificado) {
		setpackagecode(packagecode);
		setPackageDate(packagedate);
		setpago(pago);
		setprecio(precio);
		setcertificado(certificado);
		setPackageDateTaken(null);
	}
	
	/*
	 * Permite cambiar el codigo del Paquete
	 * El codigo sera valido si cummple. El código del paquete debe tener diez caracteres,
	 * de los cuales los primeros nueve son dígitos y el décimo es un dígito resultante del resto de la división entre 10 de la suma de los 9 primeros
	 * 
	 * @param El nuevo codigo que queremos que tenga el paquete
	 */
	
	public void setpackagecode(String packagecode) {
		if(packagecode.length()!=10) {
			throw new IllegalArgumentException("El codigo no tiene 10 cifras");
		}
		int codigo=0;
		for(int i =0; i<packagecode.length();i++) {
			char digit = (char)(packagecode.charAt(i) - '0');
			if (0<=digit && digit<=9) {
				codigo += (digit * Math.pow(10, (packagecode.length() - i - 1)));
			}else {
				throw new IllegalArgumentException("El codigo no son todos numeros");
			}
	      }
		long packagecodeminus = codigo/10;
		long ultimo = codigo%10;
		int i =1;
		long numero=0;
		while (i<=10) {
			long numerodelcodigo=packagecodeminus % 10;
			numero += numerodelcodigo;
			packagecodeminus=packagecodeminus/10;
			i++;
		}
		if(numero%10!=ultimo) {
			throw new IllegalArgumentException("El ultimo digito no es correcto");
		}
		this.packagecode=packagecode;
	}
	
	// Método setter para isCOD
    public void setpago(boolean pago) {
        this.pago = pago;
    }

    // Método getter para isCOD
    public boolean getpago() {
        return pago;
    }

    // Método setter para isCertified
    public void setcertificado(boolean certificado) {
        this.certificado = certificado;
    }

    // Método getter para isCertified
    public boolean getcertificado() {
        return certificado;
    }

    // Método setter para authorizedDNIs
    public void setDNI(ArrayList<String> DNI) {
    	if (DNI.contains(null)) {
    		throw new IllegalArgumentException("No puede haber ningún DNI nulo");
    	}
        this.DNI = DNI;
    }

    // Método getter para authorizedDNIs
    public ArrayList<String> getDNI() {
        return DNI;
    }
    
    public void setprecio(double precio) {
		this.precio=precio;
	}
	
	public double getprecio() {
		return precio;
	}
	
	/**
	 * Permite conocer el código del paquete
	 * @return el codigo del paquete
	 */
	
	public String getpackagecode() {
		return packagecode;
	}
	
	/**
	 * Permite cambiar la fecha de fin de almacenaje
	 * @param La fecha en la que queremos que el paquete caduque
	 * @throws  IllegalArgumentException si esa fecha ha pasado ya no permite cambiarla
	 */
	
	public void setPackageDate(LocalDate packagedate) {
		if (LocalDate.now().compareTo(packagedate)>0) {
			throw new IllegalArgumentException("Esa fecha ya ha pasado");
		}
		this.packagedate=packagedate;
	}
	
	/**
	 * Permite conocer el dia en el que el paquete expira
	 * @return fecha en la que expira el paquete
	 */
	
	public LocalDate getpackagedate() {
		return packagedate;
	}
	
	/**
     * Permite cambiar el estado de recogida del paquete
     *
     * @param El estado del paquete: true= paquete ha sido recogido por el cliente, false= paquete no ha sido recogido por el cliente
     * @throws IllegalArgumentException si el paquete ya ha sido devuelto a la central
     */
	
	public void setPackageDateTaken(LocalDate packagedate) {
		if (LocalDate.now().compareTo(packagedate)>0) {
			throw new IllegalArgumentException("Esa fecha ya ha pasado");
		}
		this.packagedatetaken=packagedate;
	}
	
	public LocalDate getpackagedatetaken() {
		return packagedatetaken;
	}
	
	public void setDNITaken(String DNITaken) {
		this.DNItaken=DNITaken;
	}
	
	public String getDNITaken() {
		return this.DNItaken;
	}
	
    public void setpackagetaken(boolean packagetaken) {
        if (getpackagereturned() && packagetaken) {
            throw new IllegalArgumentException("El paquete no puede ser recogido por el cliente y devuelto a la central a la vez");
        }

        // Check if the package is certified and requires payment on delivery
        if (certificado && !pago && !packagetaken) {
            throw new IllegalArgumentException("Paquete certificado requiere pago contra reembolso");
        }

        this.packagetaken = packagetaken;
    }
	
	/**
	 * Permite saber si el paquete ha sido recogido o no por el cliente
	 * @return true = paquete recogido; false = paquete en packagelocker
	 */
	
	public boolean getpackagetaken() {
		return packagetaken;
	}
	
	/**
	 * Permite cambiar el estado de devolución del paquete
	 * @param el estado del paquete: true= paquete ha sido devuelto a la central , false= paquete no ha sido devuelto a la central
	 * @throws IllegalArgumentException si el paquete ya ha sido recogido por el cliente
	 */
	
	public void setpackagereturned(boolean packagereturned) {
		if(getpackagetaken()==true && packagereturned==true) {
			throw new IllegalArgumentException("El paquete no puede ser recogido por el cliente y devuelto a la central a la vez");
		}
		this.packagereturned=packagereturned;
	}
	
	/**
	 * Permite saber si el paquete ha sido devuelto o no a la central
	 * @return true = paquete devuelto; false = paquete en packagelocker
	 */
	
	public boolean getpackagereturned() {
		return packagereturned;
	}
	
	/**
	 * Permite saber si una fecha dada ha excedido la fecha de fin de almacenaje
	 * @param datex = fecha dada
	 * @return datepassed: true = paquete excedio fecha dada; false = paquete no excedio fecha dada
	 */
	
	public boolean datepassed(LocalDate datex) {
		boolean datepassed = false;
		if(getpackagedate().compareTo(datex)<0) {
			datepassed = true;
		}
		return datepassed;
	}

	public void addDNI(String dni) {
		if(getpackagetaken()==false&&getpackagereturned()==false) {
			DNI.add(dni);
		}
	}
}
