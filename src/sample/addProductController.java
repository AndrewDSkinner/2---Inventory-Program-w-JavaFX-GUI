package sample;

import javafx.collections.FXCollections;
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

import static sample.Controller.generateProductID;

/**
 * Add Product Controller Class
 * @author Andrew Skinner
 */

public class addProductController implements Initializable {

    public Button cancelAddProduct;
    public TextField addProductSearchBar;
    public TextField addProdNameField;
    public TextField addProdStock;
    public TextField addProductPrice;
    public TextField addProductMax;
    public TextField addProductMin;
    public TableView partsTable2;
    public TableColumn partIDCol2;
    public TableColumn partNameCol2;
    public TableColumn partStockCol2;
    public TableColumn partPriceCol2;
    public Button addProductSaveButton;
    public Button addAssocPartButton;
    public TableView addAssocPartsTable;
    public TableColumn assocPartsIDCol;
    public TableColumn assocPartsNameCol;
    public TableColumn assocPartsStockCol;
    public TableColumn assocPartsPriceCol;
    public Button removeAssocPartButton;
    public Label maxErrorLabel;
    public Label stockErrorLabel;

    private ObservableList<Part> allAssocParts = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable2.setItems(Inventory.getAllParts());

        partIDCol2.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol2.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    /**
     *
     * @param actionEvent Cancels and exits the add product screen.
     * @throws IOException throws exception for FXMLLoader
     */
    public void onCancelProduct(ActionEvent actionEvent) throws IOException{

            Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 920, 450);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        }

    /**
     *
     * @param actionEvent Saves contents of add product form.
     * @throws IOException throws exception for FMXLLoader.
     */

    public void onAddProdSave(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Product Save Button Clicked.");

        try {
            String productName = addProdNameField.getText();
            int productStock = Integer.parseInt(addProdStock.getText());
            double productPrice = Double.parseDouble(addProductPrice.getText());
            int productMax = Integer.parseInt(addProductMax.getText());
            int productMin = Integer.parseInt(addProductMin.getText());

            if (productMax < productMin) {
                maxErrorLabel.setText("Max must be greater or equal to min.");
                return;
            }

            if (productStock > productMax || productStock < productMin) {
                stockErrorLabel.setText("Inv cannot be greater than max or less than Min.");
                return;
            }

            Product addedProduct = new Product(generateProductID(), productName, productPrice, productStock, productMin, productMax);
            Inventory.addProducts(addedProduct);
            for (Part eachPart : allAssocParts) {

                addedProduct.addAssociatedPart(eachPart);
            }


            Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 920, 450);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        } catch (NumberFormatException e) {
            saveProductAlert();
        }
    }

    /**
     *
     * @param actionEvent searches top parts table.
     */

    public void onPartSearch2(ActionEvent actionEvent) {
        System.out.println("Part searched 2!");

        String query = addProductSearchBar.getText();
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
        partsTable2.setItems(parts);

        addProductSearchBar.setText("");

    }

    /**
     *
     * @param actionEvent adds an associated part to the bottom table
     */

    public void onAddAssociatedPart(ActionEvent actionEvent) {
        System.out.println("Add Associated Part Button clicked.");

        Part selectedPart = (Part) partsTable2.getSelectionModel().getSelectedItem();

        if(selectedPart == null) {
            System.out.println("Selected Part is null.");
            return;
        }
        System.out.println(selectedPart.getName());

        int partId = selectedPart.getId();
        String partName = selectedPart.getName();
        int partStock = selectedPart.getStock();
        double partPrice = selectedPart.getPrice();
        int partMax = selectedPart.getMax();
        int partMin = selectedPart.getMin();

        if (selectedPart instanceof InHouse) {
            int partMachID = selectedPart.getId();

            InHouse part = new InHouse(partId, partName, partPrice, partStock, partMin, partMax, partMachID);

           addAssocPart(part);

        } else if (selectedPart instanceof Outsourced){
            String partMachID = ((Outsourced) selectedPart).getCompanyName();

            Outsourced part = new Outsourced(partId,partName, partPrice, partStock, partMin, partMax, partMachID);

           addAssocPart(part);

        }

        addAssocPartsTable.setItems(allAssocParts);

        assocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        assocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        assocPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        assocPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     *
     * @param newAssocPart adds an associated part to allAssocParts list.
     */

    public void addAssocPart(Part newAssocPart) {

        allAssocParts.add(newAssocPart);

    }

    /**
     *
     * @return returns all Associated parts.
     */

    public ObservableList<Part> getAllAssocParts() {

        return allAssocParts;

    }

    /**
     *
     * @param actionEvent removes the selected associated part on button click.
     */
    public void onRemoveAssocPart(ActionEvent actionEvent) {

        Part selectedPart = (Part)addAssocPartsTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            System.out.println(" Selected Part is null");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you certain you want to remove this associated part?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){

            addAssocPartsTable.getItems().remove(selectedPart);
            //relatedParts.remove(selectedPart);
        }
    }

    /**
     * Creates an error message if incorrect data is entered into the form prior to saving.
     */

    private void saveProductAlert() {

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Alert");
        alert.setContentText("Enter a valid value for each field!\n" +
                "Name fields accept a string or character only. \n" +
                "Inv/Max/Min/Price fields accept numbers only. ");
        alert.showAndWait();
    }

}
