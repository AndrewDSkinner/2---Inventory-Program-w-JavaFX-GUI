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
import module.Inventory;
import module.Part;
import module.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Modify Product Controller
 * @author Andrew Skinner
 */

public class modifyProductController implements Initializable {

    public Button modifyProductCancelButton;
    public TableView partsTable3;
    public TableColumn modifyProductPartIDCol;
    public TableColumn modifyProductPartNameCol;
    public TableColumn modifyProductPartStockCol;
    public TableColumn modifyProductPartPriceCol;
    public TextField modProdPartSearchField;

    public static Product receivedProduct;
    public TextField modifyProdIDField;
    public TextField modifyProdNameField;
    public TextField modifyProdStockField;
    public TextField modifyProdPriceField;
    public TextField modifyProdMaxField;
    public TextField modifyProdMinField;
    public Button modifyProdSaveButton;
    public TableColumn modAssocPartsIDCol;
    public TableColumn modAssocPartsNameCol;
    public TableColumn modAssocPartsStockCol;
    public TableColumn modAssocPartsPriceCol;
    public TableView assocProdTable;
    public Label stockErrorLabel;
    public Label maxErrorLabel;
    private ObservableList<Part> relatedParts = FXCollections.observableArrayList();

    /**
     *
     * @param product received the selected product from the main controller to modify.
     */
    public static void receiveProduct(Product product) {
        receivedProduct = product;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTheState();


    }

    /**
     *
     * @param actionEvent Closes the modify product form without saving changes.
     * @throws IOException throws exception from FMXLLoader.
     */

    public void onModifyProdCancel(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify Prod Cancel clicked");

        Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 920, 450);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param actionEvent Searches the top table of parts.
     */

    public void onModProdPartSearch(ActionEvent actionEvent) {
        System.out.println("Part Searched 3!");

        String query = modProdPartSearchField.getText();
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

        partsTable3.setItems(parts);
        modProdPartSearchField.setText("");
    }

    /**
     * This fills the fields for the product details to be modified.
     */

    private void setTheState() {
        modifyProdIDField.setText(Integer.toString(receivedProduct.getId()));
        modifyProdNameField.setText(receivedProduct.getName());
        modifyProdPriceField.setText(Double.toString(receivedProduct.getPrice()));
        modifyProdMaxField.setText(Integer.toString(receivedProduct.getMax()));
        modifyProdMinField.setText(Integer.toString(receivedProduct.getMin()));
        modifyProdStockField.setText(Integer.toString(receivedProduct.getStock()));

        partsTable3.setItems(Inventory.getAllParts());

        modifyProductPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        relatedParts = receivedProduct.getAllAssociatedParts();
        assocProdTable.setItems(relatedParts);

        modAssocPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modAssocPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modAssocPartsStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modAssocPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     *
     * @param actionEvent Saves the details from the product fields and the associated parts table and closes
     *                    the form.
     * @throws IOException Throws exception from FMXLLoader.
     */
    public void onModProdSave(ActionEvent actionEvent) throws IOException {
        System.out.println("Mod Prod Save clicked.");

        String name = modifyProdNameField.getText();
        int stock = Integer.parseInt(modifyProdStockField.getText());
        int max = Integer.parseInt(modifyProdMaxField.getText());
        int min = Integer.parseInt(modifyProdMinField.getText());
        double  price = Double.parseDouble(modifyProdPriceField.getText());
        int id = Integer.parseInt(modifyProdIDField.getText());

        if (max < min) {
            maxErrorLabel.setText("Max must be greater or equal to min.");
            return;
        }

        if (stock > max || stock < min) {
            stockErrorLabel.setText("Inv cannot be greater than max or less than Min.");
            return;
        }



        Product product = new Product(id, name, price, stock, min, max);
        
        Inventory.updateProduct(product);

        for (Part relatedPart : relatedParts) {
            product.addAssociatedPart(relatedPart);
        }
        

        Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 920, 450);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param actionEvent Removes the associated product on button click.
     */

    public void onRemoveAssocPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) assocProdTable.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            System.out.println("selected part is null");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you certain you want to remove this associated part?");

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){

            assocProdTable.getItems().remove(selectedPart);
            //relatedParts.remove(selectedPart);
        }


    }

    /**
     *
     * @param actionEvent Adds the selected part from the top table and places it into the
     *                    associated parts table.
     */

    public void onAddAssocPart(ActionEvent actionEvent) {

        Part selectedPart = (Part) partsTable3.getSelectionModel().getSelectedItem();

        if (selectedPart == null) {
            System.out.println("selected part is null");
            return;
        }

        relatedParts.add(selectedPart);
        assocProdTable.setItems(relatedParts);


    }
}
