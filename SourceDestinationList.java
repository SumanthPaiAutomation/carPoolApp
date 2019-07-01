package com.ample.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SourceDestinationList")
public class SourceDestinationList {
	List<String> srcdest= new ArrayList<String>();

	public List<String> getSrcdest() {
		return srcdest;
	}

	public void setSrcdest(List<String> srcdest) {
		this.srcdest = srcdest;
	}
	
	
}
