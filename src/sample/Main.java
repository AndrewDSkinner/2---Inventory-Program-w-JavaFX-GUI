package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import module.*;

import static sample.Controller.generatePartID;
import static sample.Controller.generateProductID;

/**
 * Main class
 * @author Andrew Skinner
 *
 * Javadoc is located in the Javadoc folder in the zip folder.
 *
 * LOGICAL ERROR - The remove associated part removed multiple parts from the table which was undesirable. Fixed by
 * removing a line of code that deleted the selected associated part from the associated parts table.
 *
 * FUTURE ENHANCEMENT -ADD THE CAPABILITY TO ASSESS THE TOTAL COST OF ASSOCIATED PARTS AND PREVENT
 * ADDING PARTS THAT EXCEED THE PRICE OF THE PRODUCT.
 *
 */

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("");
        primaryStage.setScene(new Scene(root, 920, 450));
        primaryStage.show();
    }



    public static void main(String[] args) {
        Part i1 = new InHouse(generatePartID(), "Brake", 12.00, 12, 1, 100, 123);
        Inventory.addPart(i1);
        Part o1 = new Outsourced(generatePartID(), "Tire", 125.00, 8,8, 88, "Tire Bros.");
        Inventory.addPart(o1);
        Part i2 = new InHouse(generatePartID(), "Wrench", 5.00, 35, 1, 200, 400);
        Inventory.addPart(i2);
        Part o2 = new Outsourced(generatePartID(),"Sprocket",3000.00, 1, 1, 300, "Sprocket Co.");
        Inventory.addPart(o2);
        Part i3 = new InHouse(generatePartID(), "Brake", 12.55, 100, 1, 100, 124);
        Inventory.addPart(i3);

        Product p1 = new Product(generateProductID(), "Gizmo", 100.00, 5, 1, 200);
        Inventory.addProducts(p1);
        Product p2 = new Product(generateProductID(), "McGuffin", 222.50,200, 2, 1000);
        Inventory.addProducts(p2);
        Product p3 = new Product(generateProductID(), "Whisper Drive", 10200.00, 6, 1, 50);
        Inventory.addProducts(p3);
        Product p4 = new Product(generateProductID(), "NOC List", 10200.00, 6, 1, 50);
        Inventory.addProducts(p4);
        Product p5 = new Product(generateProductID(), "Quantum", 50200.00, 1, 1, 50);
        Inventory.addProducts(p5);

        launch(args);
    }
}
