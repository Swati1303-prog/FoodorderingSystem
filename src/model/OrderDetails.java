package model;

public class OrderDetails {
    private int id;
    private int orderId;
    private int menuId;
    private String menuName;
    private double amount;
    private int noOfServing;
    private double totalAmount;

    public OrderDetails() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int v) { this.orderId = v; }

    public int getMenuId() { return menuId; }
    public void setMenuId(int v) { this.menuId = v; }

    public String getMenuName() { return menuName; }
    public void setMenuName(String v) { this.menuName = v; }

    public double getAmount() { return amount; }
    public void setAmount(double v) { this.amount = v; }

    public int getNoOfServing() { return noOfServing; }
    public void setNoOfServing(int v) { this.noOfServing = v; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double v) { this.totalAmount = v; }
}