package com.amazon.paapidemo.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="CartAddResponse", namespace = "http://webservices.amazon.com/AWSECommerceService/2013-08-01")
public class CartAddResponse {

	@XmlElement(name="OperationRequest")
	private OperationRequest operationRequest;

	@XmlElement(name="Cart")
	private Cart cart;

}
