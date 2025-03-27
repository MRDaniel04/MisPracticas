
/**
 * @author mardedi
 * @author daniega
 */

package PackageLocker;

import org.junit.Test;


import static org.junit.Assert.*;

import java.time.LocalDate;


public class PackageTest {

	@Test(expected=Error.class) //creo el package null falla la fecha?
	public void testPackageNullpackagedate() {
		Package Package = new Package();
		assertNotNull(Package.getpackagedate());	
	}
	
	@Test(expected=Error.class)  //creo el package null falla packagereturned?
	public void testPackageNullpackagereturned() {
		Package Package = new Package();
		assertTrue(Package.getpackagereturned());
	}
	
	@Test(expected=Error.class) //creo el package null falla packagetaken?
	public void testPackageNullpackagetaken() {
		Package Package = new Package();
		assertTrue(Package.getpackagetaken());
	}
	

	@Test //Lo creo null se crea nulo?
	public void testSetpackagecodeNull() {
		Package Package = new Package();
		assertEquals(Package.getpackagecode(), "0000000000");
		assertNull(Package.getpackagedate());
		assertFalse(Package.getpackagereturned());
		assertFalse(Package.getpackagetaken());
	}
	
	@Test(expected=Error.class) //creo el package null falla el codigo?
	public void testSetpackagecodeNullPackageCode() {
		Package Package = new Package();
		assertNotEquals(Package.getpackagecode(), "0000000000");
	}
	
	
	
//----------------------------------------------------------------------------------------------------
	
	
	
	
	@Test
	public void testSetPackage() { //creo el package correctamente, se crea bien? B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		assertEquals(Package.getpackagecode(),"1111111119" );
		assertEquals(Package.getpackagedate(),LocalDate.of(2023,11,30) );
		assertFalse(Package.getpackagereturned());
		assertFalse(Package.getpackagetaken());
	}
	
	@Test (expected=IllegalArgumentException.class)  //creo el package fallando el ultimo digito, falla?? B
	public void testSetPackagePackageCodeUltimoDigitoMal() {
		Package Package = new Package("1111111114",LocalDate.of(2024,11,9));
	}
	
	@Test (expected=IllegalArgumentException.class) //creo el package fallando con 9 digitos, falla?? B
	public void testSetPackagePackageCode9Digitos() {
		Package Package = new Package("999999992",LocalDate.of(2024,11,9));
	}
	
	@Test (expected=IllegalArgumentException.class) //creo el package fallando con 11 digitos, falla?? B
	public void testSetPackagePackageCode11Digitos() {
		Package Package = new Package("00000000000",LocalDate.of(2024,11,9));
	}
	
	@Test (expected=IllegalArgumentException.class) //Fallan la fecha(antigua) B
	public void testSetPackageFechaAntigua() { 
		Package Package = new Package("1111111119",LocalDate.of(2023,8,9));
	}
	
	@Test  //Cambio la fecha por una de despues cuando haya caducado B
	public void testSetPackageFechaCuandoPackageACaducado() { 
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		assertTrue(Package.datepassed(LocalDate.of(2023,12,30)));
	}
	
	@Test  //Cambio la fecha por una de despues cuando haya caducado B
	public void testSetPackageCambioFechaCuandoPackageCaduca() { 
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		assertFalse(Package.datepassed(LocalDate.of(2023,11,30)));
	}
	
	@Test //Cambio la fecha por una correcta B
	public void testSetPackageCambioFecha() { 
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		assertFalse(Package.datepassed(LocalDate.of(2023,11,14)));
	}
	
	@Test (expected=java.time.DateTimeException.class) 
	public void testSetPackageFechaInexistente() { //Fecha no existe B
		Package Package = new Package("1111111119",LocalDate.of(2024,2,30));
	}
	
	@Test
	public void testSetPackageFechaBisiesta() { //Año bisiesto B
		Package Package = new Package("1111111119",LocalDate.of(2024,2,29));
		assertEquals(Package.getpackagedate(),LocalDate.of(2024,2,29) );
	}
	
	@Test
	public void testSetPackagePackagetaken() { //creamos paquete y lo recoge el cliente B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		Package.setpackagetaken(true);
		assertFalse(Package.getpackagereturned());
		assertTrue(Package.getpackagetaken());
	}
	
	@Test  (expected=IllegalArgumentException.class) //creamos paquete y lo recoge el cliente y a la vez vuelve a la central
	public void testSetPackageTakenPackageReturnedTrue() { 
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		Package.setpackagetaken(true);
		Package.setpackagereturned(true);
	}
	
	@Test  (expected=IllegalArgumentException.class) //creamos paquete y vuelve a la central y a la vez lo recoge el cliente
	public void testSetPackage20() { 
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		Package.setpackagereturned(true);
		Package.setpackagetaken(true);
	}
	
	@Test
	public void testSetPackageReturnedPackageTakenTrue() { //creamos paquete y lo vuelve  B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		Package.setpackagereturned(true);
		assertTrue(Package.getpackagereturned());
		assertFalse(Package.getpackagetaken());
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSetPackagePackageCodeCaracterMenorque0() { //1caracter menor que '0' B
		Package Package = new Package("/111111119",LocalDate.of(2023,11,30));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSetPackageackageCodeCaracteres() { //Todo caracteres B
		Package Package = new Package("abncdefght",LocalDate.of(2023,11,30));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSetPackagePackageCodeCaracterMayorque0() { //1caracter mayor que '9' B
		Package Package = new Package("/11111111:",LocalDate.of(2023,11,30));
	}
	

	@Test (expected=IllegalArgumentException.class)
	public void testSetPackagePackageCodeTodoCaracterMayorque0() { // todo caracteres menor que '0' B
		Package Package = new Package("//////////",LocalDate.of(2023,11,30));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testSetPackagePackageCodeTodoCaracterMenorque9() { //todo caracteres mayor que '9' B
		Package Package = new Package("::::::::::",LocalDate.of(2023,11,30));
	}
	
	@Test
	public void testGetPackageCode() { //creamos paquete y lo vuelve  B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		String CodigoActual=Package.getpackagecode();
		assertEquals(CodigoActual, Package.getpackagecode() );
	}
	
	@Test
	public void testGetPackageDate() { //creamos paquete y lo vuelve  B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		LocalDate fecha=Package.getpackagedate();
		assertEquals(fecha, Package.getpackagedate() );
	}
	
	@Test
	public void testSetPackageCode() { //creamos paquete y lo vuelve  B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		String CodigoActual="0000000000";
		Package.setpackagecode(CodigoActual);
		assertEquals(CodigoActual, Package.getpackagecode() );
	}
	
	@Test
	public void testSetPackageDate() { //creamos paquete y lo vuelve  B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		LocalDate fecha= LocalDate.of(2023,12,30);
		Package.setPackageDate(fecha);
		assertEquals(fecha, Package.getpackagedate() );
	}
	
	@Test
	public void testSetPackagetaken() { //creamos paquete y lo recoge el cliente B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		boolean estado=false;
		Package.setpackagetaken(estado);
		assertEquals(estado,Package.getpackagetaken());
	}
	
	@Test
	public void testGetPackagetaken() { //creamos paquete y lo recoge el cliente B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		boolean estado=Package.getpackagetaken();
		assertEquals(estado,Package.getpackagetaken());
	}
	
	@Test
	public void testSetPackagereturned() { //creamos paquete y lo recoge el cliente B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		boolean estado=false;
		Package.setpackagereturned(estado);
		assertEquals(estado,Package.getpackagereturned());
	}
	
	@Test
	public void testGetPackagereturned() { //creamos paquete y lo recoge el cliente B
		Package Package = new Package("1111111119",LocalDate.of(2023,11,30));
		boolean estado=Package.getpackagereturned();
		assertEquals(estado,Package.getpackagereturned());
	}
	
	
	
}