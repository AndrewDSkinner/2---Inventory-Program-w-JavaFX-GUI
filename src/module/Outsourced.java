package module;

/**
 * Outsourced Class
 * @author Andrew Skinner
 */

public class Outsourced extends Part {
    private String companyName;

    /**
     *
     * @param id Part ID
     * @param name Part Name
     * @param price Part price
     * @param stock Part stock
     * @param min Part min
     * @param max Part max
     * @param companyName Part's company name
     */

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName)
    {
     super(id, name, price, stock, min, max);
     this.companyName = companyName;
    }

    /**
     *
     * @param companyName sets the company name
     */

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     *
     * @return the company name
     */

    public String getCompanyName() {
        return companyName;
    }
}
