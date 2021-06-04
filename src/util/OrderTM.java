package util;

import java.util.ArrayList;

public class OrderTM
{
    private String orderId;
    private String date;
    private String customerId;
    private ArrayList<PurchasedItemsTM> purchasedItems=new ArrayList<PurchasedItemsTM>();

    @Override
    public String toString() {
        return "OrderTM{" +
                "orderId='" + orderId + '\'' +
                ", date='" + date + '\'' +
                ", customerId='" + customerId + '\'' +
                ", purchasedItems=" + purchasedItems +
                '}';
    }

    public OrderTM() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public ArrayList<PurchasedItemsTM> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(ArrayList<PurchasedItemsTM> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }


    public OrderTM(String orderId, String date, String customerId, ArrayList<PurchasedItemsTM> purchasedItems) {
        this.orderId = orderId;
        this.date = date;
        this.customerId = customerId;
        this.purchasedItems = purchasedItems;
    }
}
