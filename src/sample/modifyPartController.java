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
import module.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Modify Part Controller
 * @author  Andrew Skinner
 */

public class modifyPartController implements Initializable {

    public RadioButton inHouseModifyPart;
    public RadioButton outsourcedModifyPart;
    public Button modifyPartCancel;
    public Label modifyMachineIDLabel;

    public static Part receivedPart = null;
   /*public static InHouse inHouseReceived = null;
    public static Outsourced outsourcedReceived = null;
*/
    public TextField modifyPartMachineIDField;
    public TextField modifyPartMinField;
    public TextField modifyPartMaxField;
    public TextField modifyPartPriceField;
    public TextField modifyPartStockField;
    public TextField modifyPartNameField;
    public TextField modifyPartIDField;
    public Button partModifySaveButton;
    public Label stockErrorLabel;
    public Label maxErrorLabel;
    public Label machineIDErrorLabel;

    /**
     *
     * @param part receives part from Main controller to modify
     */
    public static void receivePart(Part part) {
        receivedPart = part;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTheState();
    }

    /**
     *
     * @param actionEvent Exits the modify part form without saving changes.
     * @throws IOException throws exception from FXMLLoader.
     */

    public void onModifyPartCancel(ActionEvent actionEvent) throws IOException {
        System.out.println("On Modify Part clicked.");

        Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 920, 450);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param actionEvent changes the Machine ID Label to Machine ID when the InHouse radio button
     *                    is selected.
     */

    public void onModifyInHouse(ActionEvent actionEvent) {
        modifyMachineIDLabel.setText("Machine ID");
    }

    /**
     *
     * @param actionEvent changes the machine ID label to Company Name when the Outsourced radio button is
     *                    selected.
     */

    public void onModifyOutsourced(ActionEvent actionEvent) {
        modifyMachineIDLabel.setText("Company Name");
    }

    /**
     *
     * @param actionEvent Saves the changes entered into the modify part form.
     * @throws IOException throws Exception from FMXLLoader.
     */

    public void onModifyPartSave(ActionEvent actionEvent) throws IOException {

        System.out.println("Modify Part Save clicked.");

        try {
            String partName = modifyPartNameField.getText();
            int partStockNumber = Integer.parseInt(modifyPartStockField.getText());
            double partPriceNumber = Double.parseDouble(modifyPartPriceField.getText());
            int partMaxNumber = Integer.parseInt(modifyPartMaxField.getText());
            int partMinNumber = Integer.parseInt(modifyPartMinField.getText());
            int partIDNumber = Integer.parseInt(modifyPartIDField.getText());

            if (partMaxNumber < partMinNumber) {
                maxErrorLabel.setText("Max must be greater or equal to Min.");
                return;
            }

            if (partStockNumber > partMaxNumber || partStockNumber < partMinNumber) {
                stockErrorLabel.setText("Inv must be between Max and Min.");
                return;
            }

            if (inHouseModifyPart.isSelected()) {
                int partMachineID = Integer.parseInt(modifyPartMachineIDField.getText());

                InHouse inHouse = new InHouse(partIDNumber, partName, partPriceNumber, partStockNumber, partMinNumber, partMaxNumber, partMachineID);
                Inventory.updatePart(inHouse);

            } else if (outsourcedModifyPart.isSelected()) {
                String partMachineID = modifyPartMachineIDField.getText();

                if (partMachineID.isEmpty()) {
                    machineIDErrorLabel.setText("Company Name cannot be empty.");
                    return;
                }

                Outsourced outsourced = new Outsourced(partIDNumber, partName, partPriceNumber, partStockNumber, partMinNumber, partMaxNumber, partMachineID);
                Inventory.updatePart(outsourced);
            }


            Parent root = FXMLLoader.load(getClass().getResource("/sample/sample.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 920, 450);
            stage.setTitle("");
            stage.setScene(scene);
            stage.show();
        }
        catch(NumberFormatException e) {
            modifyPartAlert();
        }
    }

    /**
     * Enters data into proper fields from main controller.
     */

    private void setTheState() {

        modifyPartNameField.setText(receivedPart.getName());
        modifyPartMaxField.setText(Integer.toString(receivedPart.getMax()));
        modifyPartMinField.setText(Integer.toString(receivedPart.getMin()));
        modifyPartPriceField.setText(Double.toString(receivedPart.getPrice()));
        modifyPartIDField.setText(Integer.toString(receivedPart.getId()));
        modifyPartStockField.setText(Integer.toString(receivedPart.getStock()));

        if (receivedPart instanceof InHouse) {
            inHouseModifyPart.fire();
            modifyPartMachineIDField.setText(Integer.toString(((InHouse) receivedPart).getMachineID()));
        }
        else if (receivedPart instanceof  Outsourced) {
            outsourcedModifyPart.fire();
            modifyPartMachineIDField.setText(((Outsourced) receivedPart).getCompanyName());
        }
    }

    /**
     * Alerts the user to enter the correct data type into each field.
     */

    private void modifyPartAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Alert");
        alert.setContentText("Enter a valid value for each field!\n" +
                "Name/Company Name fields accept a string or character only. \n" +
                "Inv/Max/Min/Price/Machine ID fields accept numbers only.");
        alert.showAndWait();
    }
}
