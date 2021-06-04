package util;

public class ItemTM
{

    private String code;
    private String description;
    private String qtyOnHand;
    private String unitPrice;

    public ItemTM(String code, String description, String qtyOnHand, String unitPrice) {
        this.code = code;
        this.description = description;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
    }
    public ItemTM()
    {

    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(String qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }


    @Override
    public String toString() {
        return "ItemTM{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", qtyOnHand='" + qtyOnHand + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                '}';
    }

}
