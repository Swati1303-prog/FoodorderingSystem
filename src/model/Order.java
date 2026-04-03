package model;

import java.sql.Timestamp;

public class Order {
    private int id;
    private int customerId;
    private Timestamp orderDate;
    private double totalAmount;
    private String orderStatus;
    private int processedBy;
    private String customerName;

    public Order() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int v) { this.customerId = v; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp v) { this.orderDate = v; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double v) { this.totalAmount = v; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String v) { this.orderStatus = v; }

    public int getProcessedBy() { return processedBy; }
    public void setProcessedBy(int v) { this.processedBy = v; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String v) { this.customerName = v; }
}