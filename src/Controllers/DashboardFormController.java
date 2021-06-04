package Controllers;

import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class DashboardFormController {

    public AnchorPane PaneDashboardForm;
    public ImageView imgViewManageCustomer;
    public ImageView imgViewItemManagement;
    public ImageView imgViewPlaceOrder;
    public Label lblCustomerDescription;
    public ImageView imgviewSearchOrder;

    public void imgViewManageCustomer_OnMouseClicked(MouseEvent mouseEvent) throws IOException {

        URL resource = this.getClass().getResource("/views/ManageCustomerForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene CustomerScene=new Scene(root);

        Stage primaryStage=(Stage)(this.PaneDashboardForm.getScene().getWindow());

        primaryStage.setScene(CustomerScene);
        primaryStage.setTitle("Manage Customer Details");
        primaryStage.centerOnScreen();

    }

    public void imgViewManageCustomer_OnMouseEntered(MouseEvent mouseEvent) {

        imgViewManageCustomer.requestFocus();

        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgViewManageCustomer);
        scaleT.setToX(1.2);
        scaleT.setToY(1.2);
        scaleT.play();

        DropShadow glow = new DropShadow();
        glow.setColor(Color.CORNFLOWERBLUE);
        glow.setWidth(20);
        glow.setHeight(20);
        glow.setRadius(20);
        imgViewManageCustomer.setEffect(glow);
        lblCustomerDescription.setText("Click to Manage Customer Details");

    }

    public void imgViewManageCustomer_OnMouseExited(MouseEvent mouseEvent) {
         ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgViewManageCustomer);
        scaleT.setToX(1);
        scaleT.setToY(1);
        scaleT.play();

        imgViewManageCustomer.setEffect(null);
        lblCustomerDescription.setText("");
    }

    public void imgViewManageItems_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/views/ItemForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene ItemsScene=new Scene(root);

        Stage primaryStage=(Stage)(this.PaneDashboardForm.getScene().getWindow());

        primaryStage.setScene(ItemsScene);
        primaryStage.setTitle("Manage Items Details");
        primaryStage.centerOnScreen();

    }

    public void imgViewManageItems_OnMouseEntered(MouseEvent mouseEvent) {

        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgViewItemManagement);
        imgViewItemManagement.requestFocus();
        scaleT.setToX(1.2);
        scaleT.setToY(1.2);
        scaleT.play();

        DropShadow glow = new DropShadow();
        glow.setColor(Color.CORNFLOWERBLUE);
        glow.setWidth(20);
        glow.setHeight(20);
        glow.setRadius(20);
        imgViewItemManagement.setEffect(glow);
        lblCustomerDescription.setText("Click to Manage Items Details");

    }

    public void imgViewManageItems_OnMouseExited(MouseEvent mouseEvent) {
        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgViewItemManagement);
        scaleT.setToX(1);
        scaleT.setToY(1);
        scaleT.play();

        imgViewItemManagement.setEffect(null);
        lblCustomerDescription.setText("");
    }

    public void imgViewPlaceOrder_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/views/OrderForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene OrderScene=new Scene(root);

        Stage primaryStage=(Stage)(this.PaneDashboardForm.getScene().getWindow());

        primaryStage.setScene(OrderScene);
        primaryStage.setTitle("Manage Order Details");
        primaryStage.centerOnScreen();

    }


    public void imgViewPlaceOrder_OnMouseEntered(MouseEvent mouseEvent) {
        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgViewPlaceOrder);
        imgViewPlaceOrder.requestFocus();
        scaleT.setToX(1.2);
        scaleT.setToY(1.2);
        scaleT.play();

        DropShadow glow = new DropShadow();
        glow.setColor(Color.CORNFLOWERBLUE);
        glow.setWidth(20);
        glow.setHeight(20);
        glow.setRadius(20);
        imgViewPlaceOrder.setEffect(glow);
        lblCustomerDescription.setText("Click to Manage Order Details");

    }

    public void imgViewPlaceOrder_OnMouseExited(MouseEvent mouseEvent) {
        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgViewPlaceOrder);
        scaleT.setToX(1);
        scaleT.setToY(1);
        scaleT.play();

        imgViewPlaceOrder.setEffect(null);
        lblCustomerDescription.setText("");
    }

    public void imgviewSearchOrder_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/views/SearchOrderForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene OrderScene=new Scene(root);

        Stage primaryStage=(Stage)(this.PaneDashboardForm.getScene().getWindow());

        primaryStage.setScene(OrderScene);
        primaryStage.setTitle("Search Orders");
        primaryStage.centerOnScreen();
    }

    public void imgviewSearchOrder_OnMouseEntered(MouseEvent mouseEvent) {
        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgviewSearchOrder);
        imgviewSearchOrder.requestFocus();
        scaleT.setToX(1.2);
        scaleT.setToY(1.2);
        scaleT.play();

        DropShadow glow = new DropShadow();
        glow.setColor(Color.CORNFLOWERBLUE);
        glow.setWidth(20);
        glow.setHeight(20);
        glow.setRadius(20);
        imgviewSearchOrder.setEffect(glow);
        lblCustomerDescription.setText("Click to Search Order Details");
    }

    public void imgviewSearchOrder_OnMouseExited(MouseEvent mouseEvent) {
        ScaleTransition scaleT =new ScaleTransition(Duration.millis(200), imgviewSearchOrder);
        scaleT.setToX(1);
        scaleT.setToY(1);
        scaleT.play();

        imgviewSearchOrder.setEffect(null);
        lblCustomerDescription.setText("");
    }
}
