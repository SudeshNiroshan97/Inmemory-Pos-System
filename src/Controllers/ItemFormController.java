package Controllers;

import DB.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import util.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemFormController implements Initializable
{
    public AnchorPane PaneItemForm;
    public Label lblHome;
    public JFXTextField txtItemCode;
    public JFXTextField txtItemDescription;
    public JFXTextField txtItemQtyOnHand;
    public JFXButton btnItemSaveUpdate;
    public JFXTextField txtItemUnitPrice;

    public TableView<ItemTM> tblViewItem;

    public TableColumn<ItemTM,String> colItemCode;
    public TableColumn<ItemTM,String>  colItemDescription;
    public TableColumn<ItemTM,String>  colItemQtyOnHand;
    public TableColumn<ItemTM,String>  colItemUnitPrice;

    public ImageView imgViewHome;
    public JFXButton btnDeleteItem;
    public TextField txtSearch;
    PreparedStatement preparedStatement;
    PreparedStatement preparedStatement1;
    PreparedStatement preparedStatement2;
   // ObservableList obItems= FXCollections.observableList(db.ItemTMArrayList);


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtItemCode.setDisable(true);
        txtItemCode.setText(autoIncrement());

        txtItemDescription.setDisable(true);
        txtItemQtyOnHand.setDisable(true);
       txtItemUnitPrice.setDisable(true);
        btnItemSaveUpdate.setDisable(true);
        btnDeleteItem.setDisable(true);

       // tblViewItem.setItems(obItems);
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colItemDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colItemQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colItemUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        serach();

        tblViewItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM newValue) {

                if (newValue == null) {
                    btnItemSaveUpdate.setText("Save");
                    return;
                }

               ItemTM itm= tblViewItem.getSelectionModel().getSelectedItem();

                btnItemSaveUpdate.setText("Update");
                btnItemSaveUpdate.setDisable(false);
                btnDeleteItem.setDisable(false);

                txtItemCode.setText(itm.getCode());
                txtItemDescription.setText(itm.getDescription());
                txtItemQtyOnHand.setText(itm.getQtyOnHand());
                txtItemUnitPrice.setText(itm.getUnitPrice());

                txtItemDescription.setDisable(false);
                txtItemDescription.requestFocus();
                txtItemQtyOnHand.setDisable(false);
                txtItemUnitPrice.setDisable(false);


            }
        });

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                serach();
            }
        });



    }


    private void serach()
    {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            tblViewItem.getItems().clear();
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/posSystem","root","123");

            String sql = "select * from Item where  code like ? or description like ? or qtyOnHand like ? or unitPrice LIKE ?";

            preparedStatement=connection.prepareStatement(sql);


            preparedStatement.setString(1, "%" + txtSearch.getText() + "%");
            preparedStatement.setString(2, "%" + txtSearch.getText() + "%");
            preparedStatement.setString(3, "%" + txtSearch.getText() + "%");
            preparedStatement.setString(4, "%" + txtSearch.getText() + "%");


            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList obTblView=tblViewItem.getItems();

            while (resultSet.next())
            {
                ItemTM itemTM=new ItemTM(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4));
                obTblView.add(itemTM);
            }

            tblViewItem.setItems(obTblView);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Search exception");
        }
    }    public void imgViewHome_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/views/DashboardForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene homeScene = new Scene(root);

        Stage primaryStage = (Stage) (this.PaneItemForm.getScene().getWindow());

        primaryStage.setScene(homeScene);
        primaryStage.centerOnScreen();
    }

    public void imgViewHome_OnMouseEntered(MouseEvent mouseEvent) {
        lblHome.setText("Home");
    }

    public void imgViewHome_OnMouseExited(MouseEvent mouseEvent) {
        lblHome.setText("");
    }

    public void btnNewItem_OnAction(ActionEvent actionEvent) {

        txtItemCode.setText(autoIncrement());
        btnItemSaveUpdate.setDisable(false);

        txtItemDescription.setDisable(false);
        txtItemDescription.requestFocus();

        txtItemQtyOnHand.setDisable(false);
        txtItemUnitPrice.setDisable(false);
    }

    public void btnItemSaveUpdate_OnAction(ActionEvent actionEvent) {
        String action=btnItemSaveUpdate.getText();

        switch (action)
        {
            case "Save":
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    tblViewItem.getItems().clear();
                    Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/posSystem","root","123");

                    String sql = "INSERT INTO Item values (?,?,?,?)";

                    preparedStatement1=connection.prepareStatement(sql);


                    preparedStatement1.setString(1, txtItemCode.getText());
                    preparedStatement1.setString(2,txtItemDescription.getText());
                    preparedStatement1.setString(3,txtItemQtyOnHand.getText());
                    preparedStatement1.setString(4,txtItemUnitPrice.getText());


                    int value= preparedStatement1.executeUpdate();

                    ObservableList obTblView=tblViewItem.getItems();

                    if (value>0)
                    {
                        Alert insertAlert= new Alert(Alert.AlertType.INFORMATION,"Successfully Inserted");
                        insertAlert.showAndWait();
                        serach();

                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Insert Exception");
                }
                break;

            case "Update":
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    tblViewItem.getItems().clear();
                    Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/posSystem","root","123");

                    String sql = "UPDATE Item SET description=?,qtyOnHand=?,unitPrice=? WHERE code=?";

                    preparedStatement1=connection.prepareStatement(sql);



                    preparedStatement1.setString(1,txtItemDescription.getText());
                    preparedStatement1.setString(2,txtItemQtyOnHand.getText());
                    preparedStatement1.setString(3,txtItemUnitPrice.getText());
                    preparedStatement1.setString(4, txtItemCode.getText());

                    int value= preparedStatement1.executeUpdate();

                    ObservableList obTblView=tblViewItem.getItems();

                    if (value>0)
                    {
                        Alert insertAlert= new Alert(Alert.AlertType.INFORMATION,"Successfully Updated");
                        insertAlert.showAndWait();
                        serach();

                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                    System.out.println("Update Exception");
                }



        }
    }

    public void btnDeleteItem_OnAction(ActionEvent actionEvent) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            tblViewItem.getItems().clear();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posSystem", "root", "123");

            String sql = "DELETE FROM Item WHERE code=?";

            preparedStatement1 = connection.prepareStatement(sql);


            preparedStatement1.setString(1, txtItemCode.getText());


            Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to deleted?", ButtonType.OK, ButtonType.CANCEL);
            Optional infor = deleteAlert.showAndWait();
            if (infor.get() == ButtonType.OK) {

                int value = preparedStatement1.executeUpdate();

                ObservableList obTblView = tblViewItem.getItems();

                if (value > 0) {
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Successfully Deleted");
                    infoAlert.showAndWait();
                    serach();

                }
                else {
                    return;
                }


            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Delete Exception");
        }


    }

    public String autoIncrement()

    {


        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int  max=0;
        try
        {
            Connection connection= DriverManager.getConnection("jdbc:mysql://localhost:3306/posSystem","root","123");

            String sql = "select code from Item order by code desc limit 1 ";

            preparedStatement=connection.prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                max = Integer.parseInt(resultSet.getString(1).substring(2,4));

            }


        }
        catch (Exception e)
        {
            System.out.println("Auto increment");
            e.printStackTrace();
        }

        return "I"+String.format("%03d",max+1);

    }


    public void btnItemReports_OnAction(ActionEvent actionEvent) throws JRException {
        JasperDesign jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream("/Reports/ItemReport.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), DBConnection.getInstance().getConnection());

        JasperViewer.viewReport(jasperPrint);
    }
}
