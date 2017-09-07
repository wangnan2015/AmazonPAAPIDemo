package com.amazon.paapidemo.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Cart {

	@XmlElement(name="Request")	
	private Request request;

	@XmlElement(name="CartId")
	private String cartId;

	@XmlElement(name="HMAC")
	private String hmac;

	@XmlElement(name="URLEncodedHMAC")
	private String urlEncodedHMAC;

	@XmlElement(name="PurchaseURL")
	private String purchaseURL;

	@XmlElement(name="SubTotal")
	private Price subTotal;



}
