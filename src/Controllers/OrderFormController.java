package Controllers;

import DB.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.ItemTM;
import util.PurchasedItemsTM;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;


public class OrderFormController implements Initializable {
    public ImageView imgViewHome;
    public TableView<PurchasedItemsTM> tblViewOrderView;

    public TableColumn<PurchasedItemsTM, String> colItemID;
    public TableColumn<PurchasedItemsTM, String> ColItemDescription;
    public TableColumn<PurchasedItemsTM, String> ColUnitPrice;
    //public TableColumn<PurchasedItemsTM,String> colQtyOnHand;
    public TableColumn<PurchasedItemsTM, String> colItemQty;
    public TableColumn<PurchasedItemsTM, String> colItemTotal;
    public TableColumn<PurchasedItemsTM, Button> colDelete;

    public Label lblMessage;
    public Label lblHome;

    public JFXTextField txtjDate;
    public JFXTextField txtTotal;
    public JFXTextField txtCustomerAddress;
    public JFXTextField txtCustomerName;
    public JFXTextField txtOrderID;
    public JFXTextField txtDescription;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtQty;
    public JFXTextField txtUnitPrice;

    public JFXComboBox cmbCustomerID;
    public JFXComboBox cmbItemID;

    public JFXButton btnAdd;
    public AnchorPane OrderPane;
    public JFXButton btnPlaceOrder;
    public JFXButton btnAddNew;

    double sum;

    PreparedStatement preparedStatementGetItems;
    PreparedStatement preparedStatementGetSelectedItems;
    PreparedStatement preparedStatementGetSelectedCustomer;
    PreparedStatement preparedStatementGetPurchasedItemDetails;

    PreparedStatement preparedStatementInsert;
    PreparedStatement preparedStatementUpdateItem;

    PreparedStatement preparedStatementAutoIncrement;
    PreparedStatement preparedStatementGetCustomer;
    PreparedStatement preparedStatementInsertPurchasedItemDetails;
    PreparedStatement preparedStatementGetItemDetails;
    PreparedStatement preparedStatementGetReviewDetails;

    Connection connection;

  //  private ArrayList<ItemTM> tempItem = new ArrayList<>();
    private ArrayList<PurchasedItemsTM> tempPurchasedItemsTMS = new ArrayList<>();
    private ObservableList obTblView = FXCollections.observableList(tempPurchasedItemsTMS);


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //Mapping values to table columns
        colItemID.setCellValueFactory(new PropertyValueFactory<>("purchasedItemCode"));
        ColItemDescription.setCellValueFactory(new PropertyValueFactory<>("purchasedItemDescription"));
        colItemQty.setCellValueFactory(new PropertyValueFactory<>("purchasedQty"));
        ColUnitPrice.setCellValueFactory(new PropertyValueFactory<>("purchasedItemUnitPrice"));
        colItemTotal.setCellValueFactory(new PropertyValueFactory<>("purchasedItemTotal"));


        colDelete.setCellFactory(ActionButtonTableCell.forTableColumn("Remove", (PurchasedItemsTM p) -> {

            btnAdd.setText("Add");
            for (PurchasedItemsTM purchasedItemsTM : tempPurchasedItemsTMS) {
                if (p.getPurchasedItemCode().equals(purchasedItemsTM.getPurchasedItemCode())) {
                    String newPurchaedItemOnHand = Integer.parseInt(p.getPurchasedItemQtyOnHand()) + Integer.parseInt(purchasedItemsTM.getPurchasedItemQtyOnHand()) + "";
                    purchasedItemsTM.setPurchasedItemQtyOnHand(newPurchaedItemOnHand);
                    break;
                }
            }
            obTblView.remove(p);
            tblViewOrderView.refresh();


            return p;
        }));


        //show date
        txtjDate.setText(LocalDate.now() + "");
        //Auto generate order id and show in the text
        txtOrderID.setText(autoIncrement());

        btnAdd.setDisable(true);
        btnPlaceOrder.setDisable(true);
        cmbItemID.setDisable(true);
        try {
            connection = DBConnection.getInstance().getConnection();
            preparedStatementGetCustomer = connection.prepareStatement("select id from Customer");
            preparedStatementGetSelectedCustomer = connection.prepareStatement("select * from Customer where id=?");
            preparedStatementAutoIncrement = connection.prepareStatement("select id from Orders order by id desc limit 1 ");
            preparedStatementGetItems = connection.prepareStatement("SELECT code FROM Item");
            preparedStatementGetSelectedItems = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
            preparedStatementInsertPurchasedItemDetails = connection.prepareStatement("INSERT INTO OrderItem value (?,?,?,?,?)");
            preparedStatementUpdateItem = connection.prepareStatement("UPDATE Item SET qtyOnHand=? WHERE code=?");
            preparedStatementInsert = connection.prepareStatement("INSERT INTO Orders value(?,?)");
            preparedStatementGetPurchasedItemDetails = connection.prepareStatement("SELECT * FROM OrderItem WHERE orderId=? ");
            preparedStatementGetItemDetails = connection.prepareStatement("SELECT * FROM Item WHERE code=?");
            preparedStatementGetReviewDetails = connection.prepareStatement("SELECT o.id,o.date,oi.customerId,oi.itemCode,oi.unitPrice,oi.qty,c.name,c.address,i.description,i.qtyOnHand,(oi.unitPrice*oi.qty)\n" +
                    "FROM Orders o,OrderItem oi, Customer c, Item i\n" +
                    "\n" +
                    "WHERE\n" +
                    "\n" +
                    "      o.id=oi.orderId\n" +
                    "            AND\n" +
                    "      oi.customerId=c.id\n" +
                    "            AND\n" +
                    "      oi.itemCode=i.code\n" +
                    "\n" +
                    "            AND\n" +
                    "      o.id=?\n" +
                    "\n");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtOrderID.setText(autoIncrement());
        //Adding customer ids to the combo
        try {

            ResultSet resultSet = preparedStatementGetCustomer.executeQuery();
            ObservableList obCustomerId = cmbCustomerID.getItems();

            while (resultSet.next()) {
                obCustomerId.add(resultSet.getString(1));

            }


        } catch (Exception e) {
            System.out.println("Cmb Customer load");
            e.printStackTrace();
        }


        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null) {
                    return;
                } else {


                    try {


                        String cid = cmbCustomerID.getValue() + "";
                        preparedStatementGetSelectedCustomer.setString(1, cid);

                        ResultSet resultSet = preparedStatementGetSelectedCustomer.executeQuery();


                        while (resultSet.next()) {

                            txtCustomerName.setText(resultSet.getString(2));
                            txtCustomerAddress.setText(resultSet.getString(3));
                        }
                        cmbItemID.setDisable(false);

                    } catch (Exception e) {
                        System.out.println("Cmb Customer load");
                        e.printStackTrace();
                    }
                }
            }
        });

        //adding item ids to the combo
        try {
            ResultSet resultSet = preparedStatementGetItems.executeQuery();
            ObservableList obCmbItemIds = cmbItemID.getItems();
            while (resultSet.next()) {
                obCmbItemIds.add(resultSet.getString(1));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        cmbItemID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null) {
                    return;
                } else {


                    String selectedItemId = cmbItemID.getSelectionModel().getSelectedItem() + "";


                    try {


                        if ((tblViewOrderView.getItems().isEmpty())) {
                            preparedStatementGetSelectedItems.setString(1, selectedItemId);
                            ResultSet resultSet = preparedStatementGetSelectedItems.executeQuery();
                            while (resultSet.next()) {

                                txtDescription.setText(resultSet.getString(2));
                                txtQtyOnHand.setText(resultSet.getString(3));
                                txtUnitPrice.setText(resultSet.getString(4));


                            }
                        } else if (!(tblViewOrderView.getItems().isEmpty())) {
                            for (PurchasedItemsTM purchasedItemsTM : tempPurchasedItemsTMS) {
                                if (purchasedItemsTM.getPurchasedItemCode().equals(selectedItemId)) {
                                    txtDescription.setText(purchasedItemsTM.getPurchasedItemDescription());
                                    txtQtyOnHand.setText(purchasedItemsTM.getPurchasedItemQtyOnHand());
                                    txtUnitPrice.setText(purchasedItemsTM.getPurchasedItemUnitPrice());

                                } else {
                                    preparedStatementGetSelectedItems.setString(1, selectedItemId);
                                    ResultSet resultSet = preparedStatementGetSelectedItems.executeQuery();
                                    while (resultSet.next()) {

                                        txtDescription.setText(resultSet.getString(2));
                                        txtQtyOnHand.setText(resultSet.getString(3));
                                        txtUnitPrice.setText(resultSet.getString(4));


                                    }
                                }
                            }

                        }
                        txtQty.requestFocus();
                        btnAdd.setDisable(false);


                    } catch (SQLException e) {

                        System.out.println("cmb Item details load");
                        e.printStackTrace();
                    }


                }

            }
        });

        btnPlaceOrder.setDisable(true);


        tblViewOrderView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<PurchasedItemsTM>() {
            @Override
            public void changed(ObservableValue<? extends PurchasedItemsTM> observable, PurchasedItemsTM oldValue, PurchasedItemsTM newValue) {
                if (newValue == null) {
                    return;
                } else {

                    try {


                        btnAdd.setText("Update");
                        preparedStatementGetSelectedItems.setString(1, newValue.getPurchasedItemCode());
                        ResultSet resultSet = preparedStatementGetSelectedItems.executeQuery();

                        while (resultSet.next()) {
                            cmbItemID.getSelectionModel().select(newValue.getPurchasedItemCode());
                            txtDescription.setText(resultSet.getString(2));
                            txtQtyOnHand.setText(resultSet.getString(3));
                            txtUnitPrice.setText(resultSet.getString(4));

                            txtQty.requestFocus();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();

                    }

                }
            }
        });

    }


    public void btnAdd_OnAction(ActionEvent actionEvent) {

        if (txtQty.getText()==null)
        {
            Alert qtyEpmtyAlert=new Alert(Alert.AlertType.INFORMATION,"Please Enter Quantity.",ButtonType.OK);

            qtyEpmtyAlert.showAndWait();

            txtQty.selectAll();
            txtQty.requestFocus();
        }

        if (btnAdd.getText().equals("Add")) {
            for (PurchasedItemsTM purchasedItemsTM : tempPurchasedItemsTMS) {


                if (purchasedItemsTM.getPurchasedItemCode().equals(cmbItemID.getValue())) {

                    String qty = Integer.parseInt(txtQty.getText()) + Integer.parseInt(purchasedItemsTM.getPurchasedQty()) + "";
                    purchasedItemsTM.setPurchasedQty(qty);

                    updateQtyOnHand(txtQty.getText(), purchasedItemsTM.getPurchasedItemCode());


                    purchasedItemsTM.setPurchasedItemTotal(calculateTotal(txtUnitPrice.getText(), qty));
                    txtTotal.setText(calculateSumofTotal() + "");
                    tblViewOrderView.refresh();

                    cmbItemID.getSelectionModel().clearSelection();
                    txtDescription.clear();
                    txtQtyOnHand.clear();
                    txtUnitPrice.clear();
                    txtQty.clear();
                    btnAdd.setDisable(true);
                    btnPlaceOrder.setDisable(false);
                    return;
                }
            }

            PurchasedItemsTM purchasedItems = new PurchasedItemsTM(cmbItemID.getValue() + "", txtQty.getText(), txtUnitPrice.getText(), txtDescription.getText(), txtQtyOnHand.getText(), calculateTotal(txtUnitPrice.getText(), txtQty.getText()));

            obTblView.add(purchasedItems);
            tblViewOrderView.setItems(obTblView);


            txtTotal.setText(calculateSumofTotal() + "");
            updateQtyOnHand(txtQty.getText(), cmbItemID.getSelectionModel().getSelectedItem() + "");


            cmbItemID.getSelectionModel().clearSelection();
            txtDescription.clear();
            txtQtyOnHand.clear();
            txtUnitPrice.clear();
            txtQty.clear();
            btnAdd.setDisable(true);
            btnPlaceOrder.setDisable(false);

        } else if (btnAdd.getText().equals("Update")) {
            for (PurchasedItemsTM purchasedItemsTM : tempPurchasedItemsTMS) {


                if (purchasedItemsTM.getPurchasedItemCode().equals(cmbItemID.getSelectionModel().getSelectedItem())) {


                    purchasedItemsTM.setPurchasedQty(txtQty.getText());

                    updateQtyOnHand(txtQty.getText(), cmbItemID.getValue() + "");


                    purchasedItemsTM.setPurchasedItemTotal(calculateTotal(txtUnitPrice.getText(), txtQty.getText()));
                    txtTotal.setText(calculateSumofTotal() + "");
                    tblViewOrderView.refresh();

                    cmbItemID.getSelectionModel().clearSelection();
                    txtDescription.clear();
                    txtQtyOnHand.clear();
                    txtUnitPrice.clear();
                    txtQty.clear();
                    btnAdd.setDisable(true);
                    btnPlaceOrder.setDisable(false);
                    btnAdd.setText("Add");
                    return;
                }
            }
        }
    }


    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) {
        int noOfRows = 0;
        try {

            preparedStatementInsert.setString(1, txtOrderID.getText());
            preparedStatementInsert.setString(2, txtjDate.getText());

            noOfRows = preparedStatementInsert.executeUpdate();
            if (noOfRows > 0) {
                Alert orderDetailAlert = new Alert(Alert.AlertType.INFORMATION, " Successfully Inserted", ButtonType.OK);
            }
        } catch (Exception e) {
            if (noOfRows > 0) {
                Alert orderDetailAlert = new Alert(Alert.AlertType.INFORMATION, " Error in order Insert", ButtonType.OK);
            }
            e.printStackTrace();
        }
        try {
            for (PurchasedItemsTM purchasedItemsTM : tempPurchasedItemsTMS) {
                preparedStatementInsertPurchasedItemDetails.setString(1, txtOrderID.getText());
                preparedStatementInsertPurchasedItemDetails.setString(2, purchasedItemsTM.getPurchasedItemCode());
                preparedStatementInsertPurchasedItemDetails.setString(3, purchasedItemsTM.getPurchasedQty());
                preparedStatementInsertPurchasedItemDetails.setString(4, purchasedItemsTM.getPurchasedItemUnitPrice());
                preparedStatementInsertPurchasedItemDetails.setString(5, cmbCustomerID.getValue() + "");
                preparedStatementInsertPurchasedItemDetails.executeUpdate();

                try {
                    preparedStatementUpdateItem.setString(1, purchasedItemsTM.getPurchasedItemQtyOnHand());
                    preparedStatementUpdateItem.setString(2, purchasedItemsTM.getPurchasedItemCode());
                    preparedStatementUpdateItem.executeUpdate();
                } catch (Exception ex) {

                    Alert orderDetailAlert = new Alert(Alert.AlertType.INFORMATION, "Error in update Item", ButtonType.OK);
                }

            }


        } catch (Exception e) {
            System.out.println("Error in add orderDetail");
            Alert orderDetailAlert = new Alert(Alert.AlertType.INFORMATION, "Error in add orderDetail", ButtonType.OK);
            e.printStackTrace();
        }

        generateBill();

        reset();


    }

    private void reset() {
        // Initialize controls to their default states


        btnPlaceOrder.setDisable(true);
        btnAdd.setDisable(true);
        cmbCustomerID.setDisable(true);

        txtCustomerAddress.setDisable(true);

        txtCustomerName.setEditable(false);

        txtDescription.setEditable(false);
        txtUnitPrice.setEditable(false);
        txtQtyOnHand.setEditable(false);
        txtQty.setEditable(false);

        txtCustomerName.clear();
        cmbCustomerID.getSelectionModel().clearSelection();
        cmbItemID.getSelectionModel().clearSelection();
        tblViewOrderView.getItems().clear();
        txtTotal.setText("Total : 0.00");
    }

    public void imgViewHome_OnMouseClicked(MouseEvent mouseEvent) throws IOException {
        URL resource = this.getClass().getResource("/views/DashboardForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene homeScene = new Scene(root);

        Stage primaryStage = (Stage) (this.OrderPane.getScene().getWindow());

        primaryStage.setScene(homeScene);
        primaryStage.centerOnScreen();
    }

    public void imgViewHome_OnMouseEntered(MouseEvent mouseEvent) {
        lblHome.setText("Home");
    }

    public void imgViewHome_OnMouseExited(MouseEvent mouseEvent) {
        lblHome.setText("");
    }


    public double calculateSumofTotal() {
        sum = 0.0;
        ObservableList<PurchasedItemsTM> purchasedItems = tblViewOrderView.getItems();
        for (PurchasedItemsTM pitm : purchasedItems) {

            sum = sum + Double.parseDouble(pitm.getPurchasedItemTotal());

        }
        return sum;
    }

    public void txtUnitPrice_OnAction(ActionEvent actionEvent) {
        //btnAdd.fireEvent( actionEvent);

    }

    public void txtQty_OnKeyPressed(KeyEvent keyEvent) {

    }


    public String autoIncrement() {

        int max = 0;
        try {


            ResultSet resultSet = preparedStatementAutoIncrement.executeQuery();

            {
                while (resultSet.next()) {
                    max = Integer.parseInt(resultSet.getString(1).substring(4, 7));

                }
            }

        } catch (NullPointerException ex) {
            return "ODR-" + String.format("%03d", max + 1);
        } catch (Exception e) {
            System.out.println("Auto increment");
            e.printStackTrace();
        }

        return "ODR-" + String.format("%03d", max + 1);

    }

    private String calculateTotal(String unitPrice, String qty) {


        return (Double.parseDouble(unitPrice) * Double.parseDouble(qty) + "");
    }

    private void updateQtyOnHand(String qty, String itemCode) {

        for (PurchasedItemsTM purchasedItemsTM : tempPurchasedItemsTMS) {

            if (purchasedItemsTM.getPurchasedItemCode().equals(itemCode)) {
                String originalQtyonHand = "0";

                try {


                    if (btnAdd.getText().equals("Update")) {
                        String cid = cmbItemID.getValue() + "";
                        preparedStatementGetSelectedItems.setString(1, itemCode);

                        ResultSet resultSet = preparedStatementGetSelectedItems.executeQuery();


                        while (resultSet.next()) {

                            originalQtyonHand = resultSet.getString(3);
                        }

                    } else if (btnAdd.getText().equals("Add")) {
                        originalQtyonHand = purchasedItemsTM.getPurchasedItemQtyOnHand();
                    }

                } catch (Exception e) {
                    System.out.println("Cmb Customer load");
                    e.printStackTrace();
                }


                String qtyOnHand = Integer.parseInt(originalQtyonHand) - Integer.parseInt(qty) + "";

                purchasedItemsTM.setPurchasedItemQtyOnHand(qtyOnHand);
                return;
            }
        }


    }

    public void review(String selectedItemID) {

        try {
            preparedStatementGetReviewDetails.setString(1, selectedItemID);
            ResultSet resultSet = preparedStatementGetReviewDetails.executeQuery();


            while (resultSet.next()) {
                txtjDate.setEditable(false);
                txtOrderID.setEditable(false);
                cmbCustomerID.setDisable(true);
                txtCustomerName.setDisable(true);
                txtCustomerAddress.setDisable(true);
                cmbItemID.setDisable(true);

                txtDescription.setDisable(true);
                txtQtyOnHand.setDisable(true);
                txtUnitPrice.setDisable(true);
                txtQty.setDisable(true);
                tblViewOrderView.setEditable(false);
                btnAdd.setDisable(true);
                btnAddNew.setDisable(true);
                btnPlaceOrder.setDisable(true);
                txtTotal.setEditable(false);


                txtOrderID.setText(selectedItemID);
                txtjDate.setText(resultSet.getString(2));
                cmbCustomerID.getSelectionModel().select(resultSet.getString(3));
                txtCustomerName.setText(resultSet.getString(7));
                txtCustomerAddress.setText(resultSet.getString(8));



                ObservableList obTblview = tblViewOrderView.getItems();


                PurchasedItemsTM purchasedItemsTM = new PurchasedItemsTM(resultSet.getString(4), resultSet.getString(6), resultSet.getString(5), resultSet.getString(9), resultSet.getString(10), resultSet.getString(11));
                obTblview.add(purchasedItemsTM);

                txtTotal.setText(calculateSumofTotal() + "");
                tblViewOrderView.setItems(obTblview);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void generateBill()  {

      //  try {
           /* JasperDesign  jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream("/Reports/Purchased_Bill.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

            Map<String,Object> params=new HashMap<>();
            params.put("billNo",txtOrderID.getText());
            params.put("billDate",txtjDate.getText());
            params.put("total",txtTotal.getText());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(tblViewOrderView.getItems()));

            JasperViewer.viewReport(jasperPrint);
        } catch (JRException e) {
            e.printStackTrace();
        }*/

           try {
               JasperDesign jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream("/Reports/orderReportSubReport.jrxml"));
               JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

               JasperDesign jasperDesignsub = JRXmlLoader.load(this.getClass().getResourceAsStream("/Reports/SubReport.jrxml"));
               JasperReport jasperReportsub = JasperCompileManager.compileReport(jasperDesignsub);

              Map<String,Object> params=new HashMap<>();
              params.put("OrderIDSub",txtOrderID.getText());
               params.put("subReportP",jasperReportsub);
               JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,params, DBConnection.getInstance().getConnection());

               JasperViewer.viewReport(jasperPrint);
           }catch (Exception e)
           {
               e.printStackTrace();
           }


    }



    public void btnAddNew_OnAction(ActionEvent actionEvent) {

        cmbCustomerID.setDisable(false);
        txtCustomerName.setDisable(false);
        txtCustomerAddress.setDisable(false);
        cmbItemID.setDisable(false);
        txtDescription.setDisable(false);
        txtQtyOnHand.setDisable(false);
        txtUnitPrice.setDisable(false);
        txtQty.setDisable(false);
        txtQty.setEditable(true);
        tblViewOrderView.setDisable(false);
        btnAdd.setDisable(false);

        btnPlaceOrder.setDisable(false);
        txtTotal.setDisable(false);

        txtOrderID.setText(autoIncrement());

    }

    public void btnBillReport_OnAction(ActionEvent actionEvent) {

        generateBill();
    }
}

class ActionButtonTableCell<S> extends TableCell<S, Button> {

    private final Button actionButton;

    public ActionButtonTableCell(String label, Function<S, S> function) {
        this.getStyleClass().add("action-button-table-cell");

        this.actionButton = new Button(label);
        this.actionButton.setOnAction((ActionEvent e) -> {
            function.apply(getCurrentItem());
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
    }

    public static <S> Callback<TableColumn<S, Button>, TableCell<S, Button>> forTableColumn(String label, Function<S, S> function) {
        return param -> new ActionButtonTableCell<>(label, function);
    }

    public S getCurrentItem() {
        return getTableView().getItems().get(getIndex());
    }

    @Override
    public void updateItem(Button item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(actionButton);
        }
    }
}