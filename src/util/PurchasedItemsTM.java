package util;

public class PurchasedItemsTM
{
    private String purchasedItemCode;
    private String purchasedQty;
    private String purchasedItemUnitPrice;
    private String purchasedItemDescription;
    private String purchasedItemQtyOnHand;
    private String purchasedItemTotal;



    @Override
    public String toString() {
        return "PurchasedItemsTM{" +
                "purchasedItemCode='" + purchasedItemCode + '\'' +
                ", purchasedQty='" + purchasedQty + '\'' +
                ", purchasedItemUnitPrice='" + purchasedItemUnitPrice + '\'' +
                ", purchasedItemDescription='" + purchasedItemDescription + '\'' +
                ", purchasedItemQtyOnHand='" + purchasedItemQtyOnHand + '\'' +
                '}';
    }

    public String getPurchasedItemDescription() {
        return purchasedItemDescription;
    }

    public void setPurchasedItemDescription(String purchasedItemDescription) {
        this.purchasedItemDescription = purchasedItemDescription;
    }

    public String getPurchasedItemQtyOnHand() {
        return purchasedItemQtyOnHand;
    }

    public void setPurchasedItemQtyOnHand(String purchasedItemQtyOnHand) {
        this.purchasedItemQtyOnHand = purchasedItemQtyOnHand;
    }

    public String getPurchasedItemTotal() {
        return purchasedItemTotal;
    }

    public void setPurchasedItemTotal(String purchasedItemTotal) {
        purchasedItemTotal = purchasedItemTotal;
    }



    public PurchasedItemsTM(String purchasedItemCode, String purchasedQty, String purchasedItemUnitPrice) {
        this.purchasedItemCode = purchasedItemCode;
        this.purchasedQty = purchasedQty;
        this.purchasedItemUnitPrice = purchasedItemUnitPrice;
    }

    public PurchasedItemsTM(String purchasedItemCode, String purchasedQty, String purchasedItemUnitPrice, String purchasedItemDescription, String purchasedItemQtyOnHand, String purchasedItemTotal) {
        this.purchasedItemCode = purchasedItemCode;
        this.purchasedQty = purchasedQty;
        this.purchasedItemUnitPrice = purchasedItemUnitPrice;
        this.purchasedItemDescription = purchasedItemDescription;
        this.purchasedItemQtyOnHand = purchasedItemQtyOnHand;
        this.purchasedItemTotal = purchasedItemTotal;
    }

    public PurchasedItemsTM() {
    }

    public String getPurchasedItemCode() {
        return purchasedItemCode;
    }

    public void setPurchasedItemCode(String purchasedItemCode) {
        this.purchasedItemCode = purchasedItemCode;
    }

    public String getPurchasedQty() {
        return purchasedQty;
    }

    public void setPurchasedQty(String purchasedQty) {
        this.purchasedQty = purchasedQty;
    }

    public String getPurchasedItemUnitPrice() {
        return purchasedItemUnitPrice;
    }

    public void setPurchasedItemUnitPrice(String purchasedItemUnitPrice) {
        this.purchasedItemUnitPrice = purchasedItemUnitPrice;
    }
}
