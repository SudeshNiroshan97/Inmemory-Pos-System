package Controllers;

import DB.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CustomerTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;


public class CustomerFormController implements Initializable {
    public AnchorPane PaneCustomerForm;
    public JFXTextField txtCustomerID;
    public JFXTextField txtCustomerName;
    public JFXTextField txtCustomerAddress;
    public ImageView imgViewHome;
    public Label lblHome;
    public JFXButton btnSaveUpdate;
    public TextField txtSearch;
    public TableColumn<CustomerTM, String> colCustomerId;
    public TableColumn<CustomerTM, String> colCustomerName;
    public TableColumn<CustomerTM, String> colCustomerAddress;
    public TableView<CustomerTM> tblViewCustomer;
    PreparedStatement preparedStatement;
    PreparedStatement preparedStatement1;
    PreparedStatement preparedStatement2;
    PreparedStatement preparedStatementInsert;
    PreparedStatement preparedStatementUpdate;
    PreparedStatement preparedStatementDelete;
    PreparedStatement preparedStatementSearch;
    PreparedStatement preparedStatementAutoIncrement;
    private Connection connection;

    //the changes are applied to obCustomer it will be applied to customerArray too not backward

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtCustomerName.setDisable(true);
        txtCustomerAddress.setDisable(true);



        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCustomerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));


        try {
            connection= DBConnection.getInstance().getConnection();


            preparedStatementSearch = connection.prepareStatement("select * from Customer where  id like ? or name like ? or address like ?");
            preparedStatementInsert = connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?)");
            preparedStatementAutoIncrement = connection.prepareStatement("select id from Customer order by id desc limit 1 ");
            preparedStatementDelete = connection.prepareStatement("DELETE FROM Customer WHERE id=?");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                serach();
            }
        });
        tblViewCustomer.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM newValue) {
                if (newValue == null) {
                    return;
                } else {
                    txtCustomerID.setText(newValue.getId());
                    txtCustomerName.setText(newValue.getName());
                    txtCustomerAddress.setText(newValue.getAddress());
                }
            }
        });

        //loading values
        serach();
        txtCustomerID.setText(autoIncrement());

    }

    public void imgViewHome_OnMouseClicked(MouseEvent mouseEvent) throws IOException {

        URL resource = this.getClass().getResource("/views/DashboardForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene homeScene = new Scene(root);

        Stage primaryStage = (Stage) (this.PaneCustomerForm.getScene().getWindow());

        primaryStage.setScene(homeScene);
        primaryStage.centerOnScreen();


    }

    private void serach() {


        try {
           tblViewCustomer.getItems().clear();

           preparedStatementSearch.setString(1,"%"+txtSearch.getText()+"%");
            preparedStatementSearch.setString(2,"%"+txtSearch.getText()+"%");
            preparedStatementSearch.setString(3,"%"+txtSearch.getText()+"%");



            ResultSet resultSet = preparedStatementSearch.executeQuery();

            ObservableList obTblView = tblViewCustomer.getItems();

            while (resultSet.next()) {
                CustomerTM customerTM = new CustomerTM(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
                obTblView.add(customerTM);
            }

            tblViewCustomer.setItems(obTblView);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Search exception");
        }
    }

    public void imgViewHome_OnMouseEntered(MouseEvent mouseEvent) {
        lblHome.setText("Home");
    }

    public void imgViewHome_OnMouseExited(MouseEvent mouseEvent) {

        lblHome.setText("");
    }

    public String autoIncrement() {

        int max = 0;
        try {

            ResultSet resultSet = preparedStatementAutoIncrement.executeQuery();

            while (resultSet.next()) {
                max = Integer.parseInt(resultSet.getString(1).substring(2, 4));

            }


        } catch (Exception e) {
            System.out.println("Auto increment");
            e.printStackTrace();
        }

        return "C" + String.format("%03d", max + 1);

    }

    public void btnSaveUpdate_OnAction(ActionEvent actionEvent) {


        String customerNameText = txtCustomerName.getText();
        String customerAddressText = txtCustomerAddress.getText();

        if (customerNameText==null)
        {

                Alert qtyEpmtyAlert=new Alert(Alert.AlertType.INFORMATION,"Please Enter Quantity.",ButtonType.OK);

                qtyEpmtyAlert.showAndWait();

                txtCustomerName.selectAll();
                txtSearch.requestFocus();

        }

        else if (customerAddressText==null)
        {

            Alert qtyEpmtyAlert=new Alert(Alert.AlertType.INFORMATION,"Please Enter Quantity.",ButtonType.OK);

            qtyEpmtyAlert.showAndWait();

            txtCustomerAddress.selectAll();
            txtCustomerAddress.requestFocus();

        }

        boolean matchesName = customerNameText.matches("^[A-Z[a-z]+]$");



        try {
            tblViewCustomer.getItems().clear();


            preparedStatementInsert.setString(1, txtCustomerID.getText());
            preparedStatementInsert.setString(2, txtCustomerName.getText());
            preparedStatementInsert.setString(3, txtCustomerAddress.getText());


            int value = preparedStatementInsert.executeUpdate();

            ObservableList obTblView = tblViewCustomer.getItems();

            if (value > 0) {
                Alert insertAlert = new Alert(Alert.AlertType.INFORMATION, "Successfully Inserted");
                insertAlert.showAndWait();
                serach();

            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Insert Exception");
        }

    }


    public void btnDeleteCustomer_OnAction(ActionEvent actionEvent) {


        try {
            tblViewCustomer.getItems().clear();


            preparedStatementDelete.setString(1, txtCustomerID.getText());


            Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to deleted?", ButtonType.OK, ButtonType.CANCEL);
            Optional infor = deleteAlert.showAndWait();
            if (infor.get() == ButtonType.OK) {

                int value = preparedStatementDelete.executeUpdate();

                ObservableList obTblView = tblViewCustomer.getItems();

                if (value > 0) {
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Successfully Deleted");
                    infoAlert.showAndWait();
                    serach();

                } else {
                    return;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Exception");
        }

    }

    public void btnNewCustomer_OnAction(ActionEvent actionEvent) {
        txtCustomerID.setText(autoIncrement());
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        txtCustomerName.requestFocus();

    }


    public void btnReport_OnAction(ActionEvent actionEvent) throws JRException {

       JasperDesign jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream("/Reports/CustomerReport.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> params = new HashMap<>();
        params.put("CustomerName",txtSearch.getText());

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(tblViewCustomer.getItems()));

        JasperViewer.viewReport(jasperPrint);

    }
}
