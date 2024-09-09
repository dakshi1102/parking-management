public enum VSize { 
    CarSize, Motorcycle, 
}  
 
  
2. ParkingLot.java  
import java.util.HashMap; 
import java.util.Map; 
import java.util.ArrayList; 
 
public class ParkingLot { 
    private Lvl[] lvls; 
    private final int NUM_LVLS = 2; 
    private int numberSlots; 
 
    Map<String, ArrayList<String>> CompanytoVeh; 
 
    public ParkingLot(int numberSlots) { 
        this.numberSlots = numberSlots; 
        this.CompanytoVeh = new HashMap<String, ArrayList<String>>(); 
        lvls = new Lvl[NUM_LVLS]; 
        for (int j = 0; j < NUM_LVLS; j++) { 
            lvls[j] = new Lvl(j, numberSlots); 
            System.out.println("Level " + j + " created with " + numberSlots + 
" " + "slots"); 
        } 
    } 
 
    public boolean parkVehicle(Vehicle vh) { 
        System.out.println(" ------------------------------------ "); 
 
        for (int i = 0; i < lvls.length; i++) { 
            if (lvls[i].parkVehicle(vh)) { 
                System.out.println(" Level " + i + " with Vehicle Number " + 
vh.licPlate + " from " + vh.companyName); 
                if (this.CompanytoVeh.containsKey(vh.companyName)) { 
                    ArrayList<String> regNoList = 
this.CompanytoVeh.get(vh.companyName); 
                    this.CompanytoVeh.remove(vh.companyName); 
                    regNoList.add(vh.licPlate); 
                    this.CompanytoVeh.put(vh.companyName, regNoList); 
                } else { 
                    ArrayList<String> regNoList = new ArrayList<String>(); 
                    regNoList.add(vh.licPlate); 
16 
 
                    this.CompanytoVeh.put(vh.companyName, regNoList); 
                } 
                return true; 
            } 
        } 
        System.out.println("PARKING IS FULL"); 
        return false; 
    } 
 
    public boolean leave(Vehicle vh, int lvl) { 
        System.out.println(" ------------------------------------------ "); 
        lvls[lvl].slotFreed(); 
        System.out.println("Slot freed from  Level " + lvl + " and exited  " + 
vh.licPlate + " of " + vh.companyName); 
        ArrayList<String> vhList = this.CompanytoVeh.get(vh.companyName); 
 
        if (vhList != null && vhList.contains(vh.licPlate)) { 
            vhList.remove(vh.licPlate); 
        } 
 
        return true; 
    } 
 
    public void ComapnyParked(String companyName) { 
        System.out.println(" ------------------------------------------ "); 
        ArrayList<String> vhList = this.CompanytoVeh.get(companyName); 
        if (vhList != null) { 
            System.out.print("The vehicles of " + companyName + " : "); 
            for (String vl : vhList) { 
                System.out.print(vl + "\t"); 
            } 
            System.out.println(); 
        } else { 
            System.out.println("No vehicles from " + companyName + " are 
parked."); 
        } 
    } 
} 
 
3. ParkingSlot.java 
import java.util.ArrayList; 
 
abstract class Vehicle { 
    protected ArrayList<ParkingSlot> parkingSlots = new 
ArrayList<ParkingSlot>(); 
    protected String licPlate; 
    protected int slotsNeeded; 
    protected VSize siz; 
17 
 
    protected String companyName; 
 
    public int getSlotsNeeded() { 
        return slotsNeeded; 
    } 
 
    public void parkInSlot(ParkingSlot s) { 
        parkingSlots.add(s); 
    } 
 
    public VSize getSize() { 
        return siz; 
    } 
 
    public void clearSlots() { 
        for (int i = 0; i < parkingSlots.size(); i++) { 
            parkingSlots.get(i).removeVehicle(); 
        } 
        parkingSlots.clear(); 
    } 
 
    public abstract boolean canFitInSlot(ParkingSlot spot); 
} 
 
class Car extends Vehicle { 
    public Car(String licPlate, String companyName) { 
        slotsNeeded = 1; 
        siz = VSize.CarSize; 
        this.licPlate = licPlate; 
        this.companyName = companyName; 
    } 
 
    public boolean canFitInSlot(ParkingSlot spot) { 
        return spot.getSize() == VSize.CarSize; 
    } 
} 
 
class Motorcycle extends Vehicle { 
    public Motorcycle(String licPlate, String companyName) { 
        slotsNeeded = 1; 
        siz = VSize.Motorcycle; 
        this.licPlate = licPlate; 
        this.companyName = companyName; 
    } 
 
    public boolean canFitInSlot(ParkingSlot slot) { 
        return slot.getSize() == VSize.CarSize || slot.getSize() == 
VSize.Motorcycle; 
18 
 
    } 
} 
 
class Lvl { 
    private int floor; 
    private ParkingSlot[] slots; 
    private int availableSlots = 0; 
    private static final int SLOT_PER_ROW = 10; 
 
    public Lvl(int flr, int numberSlots) { 
        floor = flr; 
        availableSlots = numberSlots; 
        slots = new ParkingSlot[numberSlots]; 
        int largeSlots = numberSlots / 4; 
        int bikeSlots = numberSlots / 4; 
        int compactSlots = numberSlots - largeSlots - bikeSlots; 
 
        for (int j = 0; j < numberSlots; j++) { 
            VSize siz = VSize.Motorcycle; 
            if (j < largeSlots + compactSlots) { 
                siz = VSize.CarSize; 
            } 
            int row = j / SLOT_PER_ROW; 
            slots[j] = new ParkingSlot(this, row, j, siz); 
        } 
    } 
 
    public int availableSlots() { 
        return availableSlots; 
    } 
 
    public boolean parkVehicle(Vehicle vh) { 
        if (availableSlots() < vh.getSlotsNeeded()) 
            return false; 
 
        int slotNumber = findAvailableSlots(vh); 
        if (slotNumber < 0) 
            return false; 
        System.out.print(", Slot Number " + slotNumber); 
        return parkStartingAtSlot(slotNumber, vh); 
    } 
 
    private boolean parkStartingAtSlot(int num, Vehicle vh) { 
        vh.clearSlots(); 
        boolean success = true; 
        for (int j = num; j < num + vh.slotsNeeded; j++) { 
            success &= slots[j].park(vh); 
        } 
19 
 
        availableSlots = availableSlots - vh.slotsNeeded; 
        return success; 
    } 
 
    private int findAvailableSlots(Vehicle vh) { 
        int slotsNeeded = vh.getSlotsNeeded(); 
        int lastRow = -1; 
        int slotsFound = 0; 
 
        for (int j = 0; j < slots.length; j++) { 
            ParkingSlot spot = slots[j]; 
            if (lastRow != slots[j].getLane()) { 
                slotsFound = 0; 
                lastRow = slots[j].getLane(); 
            } 
            if (slots[j].canFitVehicle(vh)) { 
                slotsFound = slotsFound + 1; 
            } else { 
                slotsFound = 0; 
            } 
            if (slotsFound == slotsNeeded) { 
                if (vh.siz == VSize.CarSize) 
                    System.out.print("It is a Car parked in "); 
                else 
                    System.out.print("It is a Motorcycle parked in "); 
                System.out.print("Lane Number  " + lastRow); 
                return j - (slotsNeeded - 1); 
            } 
        } 
        return -1; 
    } 
 
    public void slotFreed() { 
        availableSlots = availableSlots + 1; 
        System.out.println("Available Slots in the current level :" + 
availableSlots); 
    } 
} 
 
class ParkingSlot { 
    private Vehicle vh; 
    private VSize siz; 
    private int lane; 
    private int slotNumber; 
    private Lvl l; 
 
    public ParkingSlot(Lvl lvl, int r, int num, VSize vs) { 
        l = lvl; 
20 
 
        lane = r; 
        slotNumber = num; 
        siz = vs; 
    } 
 
    public boolean isAvailable() { 
        return vh == null; 
    } 
 
    public boolean canFitVehicle(Vehicle vh) { 
        return isAvailable() && vh.canFitInSlot(this); 
    } 
 
    public boolean park(Vehicle vObj) { 
        if (!canFitVehicle(vObj)) { 
            return false; 
        } 
 
        vh = vObj; 
        vh.parkInSlot(this); 
        return true; 
    } 
 
    public int getLane() { 
        return lane; 
    } 
 
    public int getSlotNumber() { 
        return slotNumber; 
    } 
 
    public VSize getSize() { 
        return siz; 
    } 
 
    public void removeVehicle() { 
        l.slotFreed(); 
        vh = null; 
    } 
} 
 
 
4. Main.java  
public class Main { 
    public static void main(String argvs[]) { 
        ParkingLot pl = new ParkingLot(2); 
 
        Car car1 = new Car("1234", "Microsoft"); 
21 
 
        Motorcycle m1 = new Motorcycle("4016", "Microsoft"); 
        Car car2 = new Car("1609", "Google"); 
        Motorcycle m2 = new Motorcycle("1389", "Google"); 
        Car car3 = new Car("1809", "Microsoft"); 
 
        pl.parkVehicle(car1); 
        pl.parkVehicle(m1); 
        pl.parkVehicle(car2); 
 
        pl.ComapnyParked("Microsoft"); 
        pl.ComapnyParked("Google"); 
 
        pl.leave(m1, 0); // Note that m1 is from Microsoft 
 
        pl.ComapnyParked("Microsoft"); 
 
        pl.parkVehicle(m2); 
        pl.parkVehicle(car3); 
    } 
} 
 