package com.ample.util;

import java.util.Date;

public class User {
private long	id;
private String	  phNumber;
private String	  src;
private String 	  dest;
private String 	  picktime;
private String 	  currentlocation;
private String	  name;
private String	  isNotified;

public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getPhNumber() {
	return phNumber;
}
public void setPhNumber(String phNumber) {
	this.phNumber = phNumber;
}
public String getSrc() {
	return src;
}
public void setSrc(String src) {
	this.src = src;
}
public String getDest() {
	return dest;
}
public void setDest(String dest) {
	this.dest = dest;
}

public String getCurrentlocation() {
	return currentlocation;
}
public void setCurrentlocation(String currentlocation) {
	this.currentlocation = currentlocation;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getIsNotified() {
	return isNotified;
}
public void setIsNotified(String isNotified) {
	this.isNotified = isNotified;
}

public String getPicktime() {
	return picktime;
}
public void setPicktime(String picktime) {
	this.picktime = picktime;
}

	  
}
