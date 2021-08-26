package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import module.InHouse;
import module.Inventory;
import module.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static sample.Controller.generatePartID;

/**
 * Add Part Controller class
 *
 * @author Andrew Skinner
 */

public class addPartController implements Initializable {



    public Button addPartCancel;
    public RadioButton inHouseAddPart;
    public RadioButton outSourcedAddPart;
    public Label machineIDLabel;
    public TextField addPartNameField;
    public TextField addPartStockField;
    public TextField addPartPriceField;
    public TextField addPartMaxField;
    public TextField addPartMinField;
    public TextField partAddMIdField;
    public Button addPartSaveButton;
    public Boolean InHouseSelected;
    public Label nameErrorLabel;
    public Label stockErrorLabel;
    public Label priceErrorLabel;
    public Label minErrorLabel;
    public Label maxErrorLabel;
    public Label machineIDErrorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    /**
     *
     * @param actionEvent Cancel is clicked
     * @throws IOException throws input exceptions for the add part button FXMLLoader.
     */

    public void onAddPartCancel(ActionEvent actionEvent) throws IOException {
        System.out.println("On Add Part Cancel clicked");

        Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 920, 450);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param actionEvent for the InHouse radio button.
     */

    public void onInHouseRadio(ActionEvent actionEvent) {
        machineIDLabel.setText("Machine ID");

        InHouseSelected = true;

    }

    /**
     *
     * @param actionEvent for the Outsourced radio button.
     */

    public void onOutsourcedRadio(ActionEvent actionEvent) {
        machineIDLabel.setText("Company Name");

        InHouseSelected = false;
    }


    public void onAddPartSave(ActionEvent actionEvent) throws IOException {
       try {
           System.out.println("Add Part Save Button clicked.");

           String partName = addPartNameField.getText();
           int partStockNumber = Integer.parseInt(addPartStockField.getText());
           double partPriceNumber = Double.parseDouble(addPartPriceField.getText());
           int partMaxNumber = Integer.parseInt(addPartMaxField.getText());
           int partMinNumber = Integer.parseInt(addPartMinField.getText());

           if (partMaxNumber < partMinNumber) {
               maxErrorLabel.setText("Max must be greater or equal to Min.");
               return;
           }

           if (partStockNumber > partMaxNumber || partStockNumber < partMinNumber) {
               stockErrorLabel.setText("Inv must be between Max and Min.");
               return;
           }



           if (inHouseAddPart.isSelected()) {
               int partMachineID = Integer.parseInt(partAddMIdField.getText());

               InHouse inHousePart = new InHouse(generatePartID(), partName, partPriceNumber,
                       partStockNumber, partMinNumber, partMaxNumber, partMachineID);

               Inventory.addPart(inHousePart);
           } else {
               String partMachineID = partAddMIdField.getText().trim();

               if (partMachineID.isEmpty()) {
                   machineIDErrorLabel.setText("Company Name cannot be empty.");
                   return;

               }

               Outsourced outsourcedPart = new Outsourced(generatePartID(), partName, partPriceNumber,
                       partStockNumber, partMinNumber, partMaxNumber, partMachineID);
               Inventory.addPart(outsourcedPart);
           }

           Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
           Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
           Scene scene = new Scene(root, 920, 450);
           stage.setTitle("");
           stage.setScene(scene);
           stage.show();
       }
       catch (NumberFormatException e) {
          savePartAlert();
       }
    }

    /**
     * Displays an alert for blank or incorrect data.
     */

    public void savePartAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Alert");
        alert.setContentText("Enter a valid value for each field!\n" +
                "Name/Company Name fields accept a string or character only. \n" +
                "Inv/Max/Min/Machine/Price fields accept numbers only.");
        alert.showAndWait();
    }
}
