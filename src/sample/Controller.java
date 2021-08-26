package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import module.*;


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Main Controller class
 * @author Andrew Skinner
 */

public class Controller implements Initializable {

    public TableView partsTable;
    public TableColumn allPartsCol;
    public TableColumn partsNameCol;
    public TableColumn partsInventoryCol;
    public TableColumn partsPriceCol;

    public TableView productsTable;
    public TableColumn productsIDCol;
    public TableColumn productsNameCol;
    public TableColumn productsInventoryCol;
    public TableColumn productsPriceCol;
    public Button partAddButton;

    public Button partDeleteButton;
    public Button productsAddButton;
    public Button productsModifyButton;
    public Button productsDeleteButton;
    public Button exitButton;
    
    public Button modifyPartButton;
    public TextField productsSearchField;
    public TextField partSearchField;


    /**
     *
     * @param actionEvent Opens the add part screen.
     * @throws IOException throws exception from FXML.
     */

    public void onAddPart(ActionEvent actionEvent) throws IOException {
        System.out.println("On Add Part clicked");


        Parent root = FXMLLoader.load(getClass().getResource("/sample/addPart.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 600);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();

    }

    /**
     *
     * @param actionEvent deletes the selected part on button click.
     */

    public void onDeletePart(ActionEvent actionEvent) {
        System.out.println("On Delete Part clicked");

        Part selectedPart = (Part)partsTable.getSelectionModel().getSelectedItem();

        if (! Inventory.deletePart(selectedPart)) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you certain you want to delete this product?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){

            partsTable.getItems().remove(selectedPart);
        }

    }

    /**
     *
     * @param actionEvent opens the Part modify screen
     * @throws IOException throws exception from FMXLLoader.
     */

    public void onPartModify(ActionEvent actionEvent) throws IOException {
        System.out.println("On Modify Part clicked");

        try {
            Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();

            modifyPartController.receivePart(selectedPart);

            Parent root = FXMLLoader.load(getClass().getResource("/sample/modifyPart.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 500, 600);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
        catch(Exception e) {
            System.out.println("Caught error");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Alert");
            alert.setContentText("Select a part from the table to modify.");
            alert.showAndWait();
        }

    }

    /**
     *
     * @param actionEvent Opens the add product screen.
     * @throws IOException Throws exception from FXMLLoader
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException {
        System.out.println("On Add Product clicked");

        Parent root = FXMLLoader.load(getClass().getResource("/sample/addProduct.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 850, 550);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param actionEvent opens the Modify Product screen.
     * @throws IOException throws exception from FXMLLoader
     */

    public void onModifyProduct(ActionEvent actionEvent) throws IOException {
        System.out.println("On Modify Product clicked");

        try {
            Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
            modifyProductController.receiveProduct(selectedProduct);

            Parent root = FXMLLoader.load(getClass().getResource("/sample/modifyProduct.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 850, 550);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e) {
            System.out.println("Caught error");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Alert");
            alert.setContentText("Select a product from the table to modify.");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param actionEvent Deletes the selected product.
     */

    public void onDeleteProduct(ActionEvent actionEvent) {
        System.out.println("On Delete Product clicked");

        Product selectedProduct = (Product)productsTable.getSelectionModel().getSelectedItem();

        if (!Inventory.deleteProduct(selectedProduct)) {
            return;
        }

        if (selectedProduct.getAllAssociatedParts().isEmpty()) {
            System.out.println("no parts associated");


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you certain you want to delete this product?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {

                productsTable.getItems().remove(selectedProduct);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Alert");
            alert.setContentText("This product has associated parts and cannot be deleted.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

 

    partsTable.setItems(Inventory.getAllParts());
    productsTable.setItems(Inventory.getAllProducts());


    allPartsCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    partsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    productsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    productsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    /**
     *
     * @param actionEvent Exits the system.
     */

    public void onExitSystem(ActionEvent actionEvent) {

        System.exit(0);
    }

    /**
     *
     * @param actionEvent Searches based on entered text.
     */

    public void onSearchedPart(ActionEvent actionEvent) {
        System.out.println("Part Searched....");

        String query = partSearchField.getText();

        ObservableList<Part> parts = Inventory.lookupPart(query);

        if(parts.size() == 0) {
            try {
                int id = Integer.parseInt(query);
                Part p = Inventory.lookupPart(id);
                if (p != null) {
                    parts.add(p);
                }
            } catch (NumberFormatException error) {
                // ignore
            }
        }

        partsTable.setItems(parts);

        partSearchField.setText("");
    }

    /**
     *
     * @param actionEvent Searches for products based on entered text.
     */

    public void onSearchedProduct(ActionEvent actionEvent) {
        System.out.println("Product Searched. . . .");

        String query = productsSearchField.getText();

        ObservableList<Product> products = Inventory.lookupProduct(query);

        if(products.size() == 0) {
            try {
                int id = Integer.parseInt(query);
                Product p = Inventory.lookupProduct(id);
                if (p != null) {
                    products.add(p);
                }
            } catch (NumberFormatException error) {
                // ignore
            }
        }

        productsTable.setItems(products);

        productsSearchField.setText("");
    }
   public static int partIdNum = 0;
   public static int productIdNum = 0;

    /**
     *
     * @return generates the Part ID.
     */

    public static int generatePartID() {

        partIdNum++;



        return partIdNum;
    }

    /**
     *
     * @return generates the product ID.
     */

    public static int generateProductID() {

        productIdNum++;

        return productIdNum;
    }


}
