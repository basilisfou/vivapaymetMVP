package com.clickmedia.vasilis.vivapayments.model;
import java.io.Serializable;


public class Product implements Serializable {

	private static final long serialVersionUID = 1L; //assigned a version identifier

	private String title;
	private String description;
	private String[] productImage;
	private String buyLink;
	private String price;
	private int MaxInstallment;

	public Product(String title, String description, String[] productImage, String buyLink, String price,int MaxInstallment) {
		this.title = title;
		this.description = description;
		this.productImage = productImage;
		this.buyLink = buyLink;
		this.price = price;
		this.MaxInstallment = MaxInstallment;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getProductImage() {
		return productImage;
	}

	public void setProductImage(String[] productImage) {
		this.productImage = productImage;
	}

	public String getBuyLink() {
		return buyLink;
	}

	public void setBuyLink(String buyLink) {
		this.buyLink = buyLink;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getMaxInstallment() {
		return MaxInstallment;
	}

	public void setMaxInstallment(int maxInstallment) {
		MaxInstallment = maxInstallment;
	}
}
