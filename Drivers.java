package com.ample.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.demo.db.Driver;

@XmlRootElement(name="Drivers")
public class Drivers {
	List<Driver> driver=new ArrayList<Driver>();

	public List<Driver> getDriver() {
		return driver;
	}

	public void setDriver(List<Driver> list) {
		this.driver = list;
	}
}
