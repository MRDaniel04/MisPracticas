
/**
 * @author mardedi
 * @author daniega
 */

package PackageLocker;

import static org.junit.Assert.*;
import java.util.HashMap;
import es.uva.inf.poo.maps.GPSCoordinate;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class PickingPointsSystemTest {
	
	@Test
	public void testPickingPointsSystemConstructor() {
		HashMap<String,PackageLocker>PickingPointsSystemtry = new HashMap<String,PackageLocker>();
		PackageLocker PackageLocker1= new PackageLocker();
		String Id = "12345";
		PickingPointsSystemtry.put(Id,PackageLocker1);
		PickingPointsSystem Pickingpointsystem = new PickingPointsSystem(PickingPointsSystemtry);
		assertEquals(Pickingpointsystem.getHashMap(),PickingPointsSystemtry);
		}
	
	@Test
	public void testPickingPointsSystemConstruirVacio() {
		PickingPointsSystem Pickingpointsystem = new PickingPointsSystem();
		assertTrue(Pickingpointsystem.getHashMap().isEmpty());
		}

	@Test
	public void testPickingPointsSystemSetHasMap() {
		PickingPointsSystem Pickingpointsystem = new PickingPointsSystem();
		HashMap<String,PackageLocker>PickingPointsSystemtry = new HashMap<String,PackageLocker>();
		PackageLocker PackageLocker1= new PackageLocker();
		PickingPointsSystemtry.put("1",PackageLocker1);
		Pickingpointsystem.setHashMap(PickingPointsSystemtry);
		assertEquals(Pickingpointsystem.getHashMap(),PickingPointsSystemtry);
	}
	
	
	@Test
	public void testPickingPointsSystemGetHasMap() {
		PickingPointsSystem Pickingpointsystem = new PickingPointsSystem();
		HashMap<String,PackageLocker>PickingPointsSystemtry = new HashMap<String,PackageLocker>();
		HashMap<String,PackageLocker>PickingPointsSystemtry2 = new HashMap<String,PackageLocker>();
		PackageLocker PackageLocker1= new PackageLocker();
		PickingPointsSystemtry.put("1",PackageLocker1);
		PickingPointsSystemtry2=Pickingpointsystem.getHashMap();
		assertEquals(Pickingpointsystem.getHashMap(),PickingPointsSystemtry2);
	}
	
	
	@Test
	public void testPickingPointsSystemGetDistancetoPackageLocker() { 
		HashMap<String,PackageLocker>PickingPointsSystemtry = new HashMap<String,PackageLocker>();
		GPSCoordinate coordinates = new GPSCoordinate(0, 0);
		PackageLocker PackageLocker1= new PackageLocker(80,"1234", coordinates );
		ArrayList<PackageLocker> PackageLockerLista = new ArrayList<PackageLocker>();
		PackageLockerLista.add(PackageLocker1);
		PickingPointsSystemtry.put("1",PackageLocker1);
		PickingPointsSystem Pickingpointsystem = new PickingPointsSystem(PickingPointsSystemtry);
		assertEquals(Pickingpointsystem.getDistancetoPackageLocker(10, coordinates),PackageLockerLista);
	}
	
	@Test
	public void testPickingPointsSystemGetDistancetoPackageLockerFarAway() { 
		HashMap<String,PackageLocker>PickingPointsSystemtry = new HashMap<String,PackageLocker>();
		GPSCoordinate coordinates1 = new GPSCoordinate(0, 0);
		GPSCoordinate coordinates2 = new GPSCoordinate(80,150);
		PackageLocker PackageLocker1= new PackageLocker(80,"1234", coordinates1 );
		ArrayList<PackageLocker> PackageLockerLista = new ArrayList<PackageLocker>();
		PackageLockerLista.add(PackageLocker1);
		PickingPointsSystemtry.put("1",PackageLocker1);
		PickingPointsSystem Pickingpointsystem = new PickingPointsSystem(PickingPointsSystemtry);
		assertNotEquals(Pickingpointsystem.getDistancetoPackageLocker(10, coordinates2),PackageLockerLista);
	}
	
	@Test (expected=IllegalArgumentException.class) 
	public void testPickingPointsSystemGetDistancetoPackageLockerNoAcceptableGPSCoordinates() { 
		HashMap<String,PackageLocker>PickingPointsSystemtry = new HashMap<String,PackageLocker>();
		GPSCoordinate coordinates = new GPSCoordinate(10000, 1000);
	}
	
	@Test
	public void testAddPackageLocker() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		PackageLocker PackageLockertry = new PackageLocker();
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		assertTrue(PickingPointsSystemtry.getHashMap().containsKey(PackageLockertry.getIdentificador()));
		assertTrue(PickingPointsSystemtry.getHashMap().containsValue(PackageLockertry));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddPackageLockerIAE() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		PackageLocker PackageLockertry = new PackageLocker();
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
	}
	
	@Test
	public void testAddPackageLockernew() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		int numero_total_taquillas=3;
		String id ="12345";
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PickingPointsSystemtry.addPackageLockernew(numero_total_taquillas,id,GPSCoordinates);
		assertEquals(PickingPointsSystemtry.getHashMap().get(id).getIdentificador(),id);
		assertEquals(PickingPointsSystemtry.getHashMap().get(id).getnumerotaquillas(),numero_total_taquillas);
		assertEquals(PickingPointsSystemtry.getHashMap().get(id).getGPSCoordinate(),GPSCoordinates);
	}

	@Test
	public void testRemovePackageLocker() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		int numero_total_taquillas=3;
		String id ="12345";
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PickingPointsSystemtry.addPackageLockernew(numero_total_taquillas,id,GPSCoordinates);
		PickingPointsSystemtry.removePackageLocker(id);
		assertNull(PickingPointsSystemtry.getHashMap().get(id));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testRemovePackageLockerIAE() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		String id ="12345";
		PickingPointsSystemtry.removePackageLocker(id);
	}

	@Test
	public void testGetPackageLockerListoperativos() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PackageLocker PackageLockertry = new PackageLocker(3,"1",GPSCoordinates);
		PackageLocker PackageLockertry2 = new PackageLocker(3,"2",GPSCoordinates);
		PackageLocker PackageLockertry3 = new PackageLocker(3,"3",GPSCoordinates);
		PackageLockertry2.setstate(false);
		ArrayList<PackageLocker> LockerListoperativos = new ArrayList<PackageLocker>();
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry2);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry3);
		LockerListoperativos.add(PackageLockertry);
		LockerListoperativos.add(PackageLockertry3);
		assertEquals(PickingPointsSystemtry.getPackageLockerListoperativos(),LockerListoperativos );
	}
	
	@Test
	public void testGetPackageLockerListoperativosnull() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PackageLocker PackageLockertry = new PackageLocker(1,"1",GPSCoordinates);
		PackageLockertry.setstate(false);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		ArrayList<PackageLocker> LockerListoperativos = new ArrayList<PackageLocker>();
		assertEquals(PickingPointsSystemtry.getPackageLockerListoperativos(),LockerListoperativos);
	}

	@Test
	public void testGetPackageLockerListoutofservice() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PackageLocker PackageLockertry = new PackageLocker(3,"1",GPSCoordinates);
		PackageLocker PackageLockertry2 = new PackageLocker(3,"2",GPSCoordinates);
		PackageLocker PackageLockertry3 = new PackageLocker(3,"3",GPSCoordinates);
		PackageLockertry2.setstate(false);
		ArrayList<PackageLocker> LockerListoperativos = new ArrayList<PackageLocker>();
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry2);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry3);
		LockerListoperativos.add(PackageLockertry2);
		assertEquals(PickingPointsSystemtry.getPackageLockerListoutofservice(),LockerListoperativos );
		
	}

	@Test
	public void testGetPackageLockerListoutofservicenull() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PackageLocker PackageLockertry = new PackageLocker(1,"1",GPSCoordinates);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		ArrayList<PackageLocker> LockerListoutofservice = new ArrayList<PackageLocker>();
		assertEquals(PickingPointsSystemtry.getPackageLockerListoutofservice(),LockerListoutofservice);
	}
	
	
	@Test
	public void testGetPackageLockerListempty() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PackageLocker PackageLockertry = new PackageLocker(1,"1",GPSCoordinates);
		PackageLocker PackageLockertry2 = new PackageLocker(1,"2",GPSCoordinates);
		PackageLocker PackageLockertry3 = new PackageLocker(2,"3",GPSCoordinates);
		Package Package = new Package();
		PackageLockertry.addPackage(1,Package);
		Package Package2 = new Package();
		PackageLockertry3.addPackage(2, Package2);
		ArrayList<PackageLocker> LockerListempty = new ArrayList<PackageLocker>();
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry2);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry3);
		LockerListempty.add(PackageLockertry2);
		LockerListempty.add(PackageLockertry3);
		assertEquals(PickingPointsSystemtry.getPackageLockerListempty(),LockerListempty);
	}
	
	
	@Test
	public void testGetPackageLockerListemptyStateFalse() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PackageLocker PackageLockertry2 = new PackageLocker(1,"2",GPSCoordinates);
		PackageLocker PackageLockertry3 = new PackageLocker(2,"3",GPSCoordinates);
		PackageLockertry2.setstate(false);
		Package Package2 = new Package();
		PackageLockertry3.addPackage(2, Package2);
		ArrayList<PackageLocker> LockerListempty = new ArrayList<PackageLocker>();
		PickingPointsSystemtry.addPackageLocker(PackageLockertry2);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry3);
		LockerListempty.add(PackageLockertry3);
		assertEquals(PickingPointsSystemtry.getPackageLockerListempty(),LockerListempty);
	}
	
	
	@Test
	public void testGetPackageLockerListemptynull() {
		PickingPointsSystem PickingPointsSystemtry = new PickingPointsSystem();
		GPSCoordinate GPSCoordinates = new GPSCoordinate(0.0,0.0);
		PackageLocker PackageLockertry = new PackageLocker(1,"1",GPSCoordinates);
		Package Package = new Package();
		PackageLockertry.addPackage(1,Package);
		PickingPointsSystemtry.addPackageLocker(PackageLockertry);
		ArrayList<PackageLocker> LockerListempty = new ArrayList<PackageLocker>();
		assertEquals(PickingPointsSystemtry.getPackageLockerListempty(),LockerListempty);
	}

}
