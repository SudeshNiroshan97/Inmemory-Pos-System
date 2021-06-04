package Controllers;

import DB.DBConnection;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.OrderDetailsTM;
import util.OrderTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchOrderFormController implements Initializable {

    public AnchorPane PaneSearchOrder;
    public JFXTextField txtSearch;

    public TableColumn<OrderTM, String> colId;
    public TableColumn<OrderTM, String> colDate;
    public TableColumn<OrderTM, String> colTotal;
    public TableColumn<OrderTM, String> colCustomerID;
    public TableColumn<OrderTM, String> colName;

    public TableView<OrderDetailsTM> tblViewSearchOrder;

    public ImageView imgViewHome;
    public Label lblHome;

    PreparedStatement preparedStatementSearch;
    PreparedStatement preparedStatementGetOrderDetails;

    ArrayList<OrderDetailsTM> orderDetailsTM = new ArrayList<>();

    Connection connection;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tblViewSearchOrder.getItems().removeAll();
        tblViewSearchOrder.getItems().clear();


        try {
            connection = DBConnection.getInstance().getConnection();

            preparedStatementGetOrderDetails = connection.prepareStatement("SELECT o.id,o.date,SUM(oi.unitPrice*oi.qty),oi.customerId,c.name FROM Orders o, OrderItem oi,Customer c WHERE o.id=oi.orderId AND oi.customerId=c.id GROUP BY o.id, oi.customerId, c.name ");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));

        SetOrderDetailsValues();
        ObservableList obSearchOrderview = tblViewSearchOrder.getItems();
        tblViewSearchOrder.setItems(obSearchOrderview);

        FilteredList<OrderDetailsTM> orderDetailsFilterList = new FilteredList<>(obSearchOrderview, b -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) ->
        {
            orderDetailsFilterList.setPredicate(OrderDetailsTM -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (OrderDetailsTM.getCustomerName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (OrderDetailsTM.getId().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (OrderDetailsTM.getCustomerID().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return OrderDetailsTM.getTotal().indexOf(lowerCaseFilter) != -1;
            });
        });

        SortedList<OrderDetailsTM> orderDetailsTMSortedList = new SortedList<>(orderDetailsFilterList);
        orderDetailsTMSortedList.comparatorProperty().bind(tblViewSearchOrder.comparatorProperty());


        tblViewSearchOrder.setItems(orderDetailsTMSortedList);


    }

    public void SetOrderDetailsValues() {

        try {
            ResultSet resultSet = preparedStatementGetOrderDetails.executeQuery();
            ObservableList obtblSearchOrder = tblViewSearchOrder.getItems();

            while (resultSet.next()) {
                OrderDetailsTM orderDetailsTM = new OrderDetailsTM(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
                obtblSearchOrder.add(orderDetailsTM);
            }

            tblViewSearchOrder.setItems(obtblSearchOrder);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public void imgViewHome_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/views/DashboardForm.fxml");

        Parent root = FXMLLoader.load(resource);


        Scene homeScene = new Scene(root);

        Stage primaryStage = (Stage) (this.PaneSearchOrder.getScene().getWindow());

        primaryStage.setScene(homeScene);
        primaryStage.centerOnScreen();
    }

    public void imgViewHome_OnMouseEntered(MouseEvent mouseEvent) {
        lblHome.setText("Home");
    }

    public void imgViewHome_OnMouseExited(MouseEvent mouseEvent) {

        lblHome.setText("");
    }


    public void tblViewSearchOrder_OnClicked(MouseEvent mouseEvent) throws IOException {


        if (mouseEvent.getClickCount() == 2) {

            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/OrderForm.fxml"));

            Parent root = loader.load();
            OrderFormController orderForm = loader.getController();
            orderForm.review(tblViewSearchOrder.getSelectionModel().getSelectedItem().getId());
            Scene oderScene = new Scene(root);

            Stage primaryStage = (Stage) (this.PaneSearchOrder.getScene().getWindow());

            primaryStage.setScene(oderScene);
            primaryStage.centerOnScreen();
        }


    }


}
