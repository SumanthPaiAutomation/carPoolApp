package com.ample;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="CabDetails")
@XmlAccessorType(XmlAccessType.FIELD)
public class CabDetails {
	@XmlElement(name="CabDetail")
	List<CabDetail> cabdetail= new ArrayList<CabDetail>();

	public List<CabDetail> getCabdetail() {
		return cabdetail;
	}

	public void setCabdetail(List<CabDetail> cabdetail) {
		this.cabdetail = cabdetail;
	}
	
}
