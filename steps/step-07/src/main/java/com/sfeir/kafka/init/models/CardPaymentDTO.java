package com.sfeir.kafka.init.models;

public class CardPaymentDTO {

  private String id;

  private double paidPrice;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public double getPaidPrice() {
    return paidPrice;
  }

  public void setPaidPrice(double paidPrice) {
    this.paidPrice = paidPrice;
  }

}
