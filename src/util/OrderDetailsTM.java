package util;



public class OrderDetailsTM
{
    private String id;
    private String date;
    private String total;
    private String CustomerID;
    private  String CustomerName;

    @Override
    public String toString() {
        return "OrderDetailsTM{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", total='" + total + '\'' +
                ", CustomerID='" + CustomerID + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {

        return CustomerName;
    }

    public void setCustomerName(String customerName) {

/*

        for (CustomerTM cust: db.customerTMArrayList ) {
            if (this.getCustomerID().equals(cust.getId())) {

                this.CustomerName = cust.getName();
           }


        }
*/


         this.CustomerName = customerName;
    }

    public OrderDetailsTM(String id, String date, String total, String customerID, String customerName) {
        this.id = id;
        this.date = date;
        this.total = total;
        CustomerID = customerID;
        CustomerName = customerName;
    }

    public OrderDetailsTM() {
    }
}
