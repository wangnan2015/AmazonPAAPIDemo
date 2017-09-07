package com.amazon.paapidemo.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Error {

	@XmlElement(name="Code")
	private String code;

	@XmlElement(name="Message")
	private String message;

}
