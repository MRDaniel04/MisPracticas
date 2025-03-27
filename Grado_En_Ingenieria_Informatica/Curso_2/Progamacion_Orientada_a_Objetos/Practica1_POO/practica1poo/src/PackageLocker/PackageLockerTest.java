
/**
 * @author mardedi
 * @author daniega
 */

package PackageLocker;

import java.util.HashMap;
import static org.junit.Assert.*;
import es.uva.inf.poo.maps.GPSCoordinate;
import org.junit.Test;
import java.time.LocalDate;

public class PackageLockerTest {

	@Test
	public void testPackageLockernoparam() {
		PackageLocker PackageLockertry = new PackageLocker();
		GPSCoordinate GPSCoordinate1 = new GPSCoordinate(0.0,0.0);
		assertEquals(PackageLockertry.getnumerotaquillas(),0);
		assertEquals(PackageLockertry.getNumeroTaquillasLlenas(),0);
		assertEquals(PackageLockertry.getNumeroTaquillasVacias(),0);
		assertEquals(PackageLockertry.getGPSCoordinate(),GPSCoordinate1);
		assertEquals(PackageLockertry.getIdentificador(),"00000");
		assertTrue(PackageLockertry.getstate());
	}

	@Test
	public void testpackagelockerparam() {
		int numero_total_taquillas = 10;
		String identificador = "12345";
		double coordenada1=0;
		double coordenada2=0;
		GPSCoordinate GPSCoordinate1 = new GPSCoordinate(coordenada1,coordenada2);
		int numero_taquillas_vacias=numero_total_taquillas;
		int numero_taquillas_llenas=0;
		boolean state = true;
		PackageLocker PackageLockertry = new PackageLocker(numero_total_taquillas,identificador,GPSCoordinate1);
		assertEquals(PackageLockertry.getnumerotaquillas(),numero_total_taquillas);
		assertEquals(PackageLockertry.getNumeroTaquillasLlenas(),numero_taquillas_llenas);
		assertEquals(PackageLockertry.getNumeroTaquillasVacias(),numero_taquillas_vacias);
		assertEquals(PackageLockertry.getGPSCoordinate(),GPSCoordinate1);
		assertEquals(PackageLockertry.getIdentificador(),identificador);
		assertEquals(PackageLockertry.getstate(),state);
	}
	
	@Test
	public void testSetandGetnumerotaquillas() {
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas=3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		assertEquals(PackageLockertry.getnumerotaquillas(),numero_taquillas);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetandGetnumerotaquillasneg() {
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas=-3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
	}

	@Test
	public void testSetandGetIdentificador() {
		PackageLocker PackageLockertry = new PackageLocker();
		String identificador = "12345";
		PackageLockertry.setIdentificador(identificador);
		assertEquals(PackageLockertry.getIdentificador(),identificador);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetandGetIdentificadorIAE() {
		PackageLocker PackageLockertry = new PackageLocker();
		String identificador = "a2345";
		PackageLockertry.setIdentificador(identificador);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testSetandGetIdentificadorIAE2() {
		PackageLocker PackageLockertry = new PackageLocker();
		String identificador = "()2345";
		PackageLockertry.setIdentificador(identificador);
	}
	

	@Test
	public void testSetandGetState() {
		PackageLocker PackageLockertry = new PackageLocker();
		boolean state = false;
		PackageLockertry.setstate(state);
		assertEquals(PackageLockertry.getstate(),state);

	}

	@Test
	public void testSetandGetCoordinates() {
		PackageLocker PackageLockertry = new PackageLocker();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0) ;
		PackageLockertry.setCoordinates(GPSCoordinates);
		assertEquals(PackageLockertry.getGPSCoordinate(),GPSCoordinates);
	}
	
	@Test (expected=IllegalArgumentException.class) 
	public void testSetandGetCoordinatesIAE() { 
		GPSCoordinate coordinates = new GPSCoordinate(10000, 1000);
	}

	@Test 
	public void testsetandGetPackageRemoved() {
		PackageLocker PackageLockertry = new PackageLocker();
		Package Package=new Package();
		PackageLockertry.setPackageremoved(Package);
		assertEquals(PackageLockertry.getPackageremoved(),Package);
		
	}

	@Test
	public void testWherePackageOK() {
		PackageLocker PackageLockertry=new PackageLocker();
		PackageLockertry.setnumerotaquillas(3);
		Package Package = new Package("1000000001",LocalDate.of(2023,11,30));
		PackageLockertry.addPackage(1, Package);
		Package Package2 = new Package("2000000002",LocalDate.of(2023,11,30));
		PackageLockertry.addPackage(2, Package2);
		assertEquals(2,PackageLockertry.WherePackage("2000000002"));
	}
	
	@Test(expected=AssertionError.class)
	public void testWherePackagestatefalse() {
		PackageLocker PackageLockertry=new PackageLocker();
		PackageLockertry.setnumerotaquillas(3);
		Package Package2 = new Package("2000000002",LocalDate.of(2023,11,30));
		PackageLockertry.addPackage(2, Package2);
		PackageLockertry.setstate(false);
		PackageLockertry.WherePackage("2000000002");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWherePackageIAE() {
		PackageLocker PackageLockertry=new PackageLocker();
		PackageLockertry.setnumerotaquillas(3);
		Package Package = new Package("1000000001",LocalDate.of(2023,11,30));
		PackageLockertry.addPackage(1, Package);
		PackageLockertry.WherePackage("2000000002");
	}
	

	@Test
	public void testAddPackageOK() {
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas = 3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		Package Package = new Package();
		int numero_taquilla = 1;
		PackageLockertry.addPackage(numero_taquilla,Package);
		assertEquals(PackageLockertry.getNumeroTaquillasLlenas(),1);
		assertEquals(PackageLockertry.getNumeroTaquillasVacias(),2);
		assertEquals(PackageLockertry.getPackage(numero_taquilla),Package);
	}
	
	@Test (expected=AssertionError.class)
	public void testAddPackagestatefalse() {
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas = 3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		Package Package = new Package();
		int numero_taquilla = 1;
		PackageLockertry.setstate(false);
		PackageLockertry.addPackage(numero_taquilla,Package);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddPackageIAE() {
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas = 3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		Package Package = new Package();
		int numero_taquilla = 1;
		PackageLockertry.addPackage(numero_taquilla,Package);
		assertEquals(PackageLockertry.getNumeroTaquillasLlenas(),1);
		assertEquals(PackageLockertry.getNumeroTaquillasVacias(),2);
		assertEquals(PackageLockertry.getPackage(numero_taquilla),Package);
		Package Package2 = new Package("2000000002",LocalDate.of(2023,11,30));
		PackageLockertry.addPackage(1, Package2);
	}
	
	@Test
	public void testRemovePackagereturned() {
		boolean returned = true;
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas = 3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		Package Package = new Package();
		int numero_taquilla = 1;
		PackageLockertry.addPackage(numero_taquilla,Package);
		Package Package2 = new Package("2000000002",LocalDate.of(2023,11,30));
		numero_taquilla++;
		PackageLockertry.addPackage(numero_taquilla,Package2);
		PackageLockertry.removePackage("0000000000", returned);
		numero_taquilla--;
		assertEquals(PackageLockertry.getNumeroTaquillasLlenas(),1);
		assertEquals(PackageLockertry.getNumeroTaquillasVacias(),2);
		assertEquals(PackageLockertry.getPackage(numero_taquilla),null);
		assertEquals(PackageLockertry.getPackageremoved().getpackagereturned(),true);
		assertEquals(PackageLockertry.getPackageremoved().getpackagetaken(),false);
	}
	
	@Test
	public void testRemovePackagetaken() {
		boolean returned = false;
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas = 3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		Package Package = new Package();
		int numero_taquilla = 1;
		PackageLockertry.addPackage(numero_taquilla,Package);
		PackageLockertry.removePackage("0000000000", returned);
		assertEquals(PackageLockertry.getNumeroTaquillasLlenas(),0);
		assertEquals(PackageLockertry.getNumeroTaquillasVacias(),3);
		assertEquals(PackageLockertry.getPackage(numero_taquilla),null);
		assertFalse(PackageLockertry.getPackageremoved().getpackagereturned());
		assertTrue(PackageLockertry.getPackageremoved().getpackagetaken());
	}
	
	
	@Test(expected=AssertionError.class)
	public void testRemovePackagetakenstatefalse() {
		boolean returned = false;
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas = 3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		Package Package = new Package();
		int numero_taquilla = 1;
		PackageLockertry.addPackage(numero_taquilla,Package);
		PackageLockertry.setstate(false);
		PackageLockertry.removePackage("0000000000", returned);
	}
		
	
	@Test(expected=IllegalArgumentException.class)
	public void testRemovePackageTakenIAE() {
		boolean returned = false;
		PackageLocker PackageLockertry = new PackageLocker();
		int numero_taquillas = 3;
		PackageLockertry.setnumerotaquillas(numero_taquillas);
		PackageLockertry.removePackage("1000000001", returned);
	}
}
