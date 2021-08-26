package module;

/**
 * InHouse class
 * @author Andrew Skinner
 */
public class InHouse extends Part {

    private int machineID;

    /**
     *
     * @param id Part ID
     * @param name Part Name
     * @param price Part price
     * @param stock Part stock
     * @param min Part stock minimum
     * @param max Part stock maximum
     * @param machineID Part machine ID
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineID) {
        super (id, name, price, stock, min, max);
        this.machineID = machineID;

    }

    /**
     *
     * @param machineID sets the machine id
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     *
     * @return the machine id
     */
    public int getMachineID() {
        return machineID;
    }
}
