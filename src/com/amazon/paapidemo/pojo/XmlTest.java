package com.amazon.paapidemo.pojo;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlTest {

	public static void main(String[] args) throws Exception {

		Cart cart = new Cart();
		cart.setCartId("cartid");
		cart.setHmac("hmac");
		CartCreateResponse response = new CartCreateResponse();
		response.setCart(cart);
		
        JAXBContext context = JAXBContext.newInstance(CartCreateResponse.class);
        Marshaller marshaller = context.createMarshaller();
        //marshaller.marshal(response, System.out);

        String body = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><CartCreateResponse xmlns=\"http://webservices.amazon.com/AWSECommerceService/2011-08-01\"><Cart><CartId>cartid</CartId><HMAC>hmac</HMAC></Cart></CartCreateResponse>";
        Unmarshaller unmarshaller = context.createUnmarshaller();
        CartCreateResponse resp2 = (CartCreateResponse) unmarshaller.unmarshal(new ByteArrayInputStream(body.getBytes()));
        System.out.println("here: " + resp2);
	}

}
