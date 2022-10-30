package com.example.flamezpizzaria.Models;

public class FeedbackDetails {

    String id;
    String image;
    String productId;
    String productName;
    String customerName;
    String customerFeedback;

    public FeedbackDetails() {
    }

    public FeedbackDetails(String id, String image, String productId, String productName, String customerName, String customerFeedback) {
        this.id = id;
        this.image = image;
        this.productId = productId;
        this.productName = productName;
        this.customerName = customerName;
        this.customerFeedback = customerFeedback;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerFeedback() {
        return customerFeedback;
    }

    public void setCustomerFeedback(String customerFeedback) {
        this.customerFeedback = customerFeedback;
    }
}
