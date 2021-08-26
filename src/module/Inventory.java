package module;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory Class
 * @author Andrew Skinner
 */


public class Inventory {


    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     *
     * @param part the part to add
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     *
     * @return allParts
     */
    public static ObservableList<Part> getAllParts(){

        return allParts;

    }

    /**
     *
     * @param Name the part name to lookup
     * @return list of parts matching parameter
     */

    public static ObservableList<Part> lookupPart(String Name) {
        ObservableList<Part> findParts = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part currentPart : allParts) {
            if (currentPart.getName().contains(Name)) {
                findParts.add(currentPart);
            }
        }

        return findParts;
    }

    /**
     *
     * @param partID the ID number to lookup
     * @return the parts matching the ID searched.
     */

    public static Part lookupPart(int partID) {

        ObservableList<Part> allParts = Inventory.getAllParts();

        for (Part currentPartByID : allParts) {

            if (currentPartByID.getId() == partID) {
                return currentPartByID;
            }

        }
        return null;
    }

    /**
     * an observable list of all the products
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param product to add to allProducts
     */
    public static void addProducts(Product product) {
        allProducts.add(product);
    }

    /**
     *
     * @return allProducts
     */

    public static ObservableList<Product> getAllProducts(){

        return allProducts;

    }

    /**
     *
     * @param searchedProductName the product name to search
     * @return all products matching the condition.
     */

    public static ObservableList<Product> lookupProduct(String searchedProductName) {
        ObservableList<Product> findProducts = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product currentProduct : allProducts) {
            if (currentProduct.getName().contains(searchedProductName)) {
                findProducts.add(currentProduct);
            }
        }
        return findProducts;
    }

    /**
     *
     * @param searchedProductID the id number to search
     * @return the products meeting the search condition
     */
    public static Product lookupProduct(int searchedProductID) {

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product currentProductByID : allProducts) {

            if (currentProductByID.getId() == searchedProductID) {
                return currentProductByID;
            }

        }
        return null;
    }

    /**
     *
     * @param selectedPart the part to update
     */

    public static void updatePart(Part selectedPart) {
        for (int i = 0; i < allParts.size(); i++) {
            if(allParts.get(i).getId() == selectedPart.getId()){
                allParts.set(i, selectedPart);
                break;
            }
        }
    }

    /**
     *
     * @param selectedProduct the product to update
     */

    public static void updateProduct(Product selectedProduct) {
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getId() == selectedProduct.getId()) {
                allProducts.set(i, selectedProduct);
                break;
            }
        }
    }

    /**
     *
     * @param selectedPart part to delete
     * @return false if selectedPart is null, else return true
     */
    public static boolean deletePart(Part selectedPart) {

        if (selectedPart == null) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param selectedProduct the product to be deleted
     * @return false if selectedProduct is false, otherwise return true
     */

    public static boolean deleteProduct(Product selectedProduct) {

        if (selectedProduct == null) {
            return false;
        }

        return true;
    }
}
