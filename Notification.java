package com.ample.util;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="Notification")
public class Notification {
	private String phNumber;
	private String src;
	private String dest;
	private Date picktime;
	private String currentlocation;
	private String name;
	//private ImageView image;
	private Boolean isNotified;
	
	public Boolean getIsNotified() {
		return isNotified;
	}

	public void setIsNotified(Boolean isNotified) {
		this.isNotified = isNotified;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Date getPicktime() {
		return picktime;
	}

	public void setPicktime(Date picktime) {
		this.picktime = picktime;
	}

	public String getCurrentlocation() {
		return currentlocation;
	}

	public void setCurrentlocation(String currentlocation) {
		this.currentlocation = currentlocation;
	}

	/*public ImageView getImage() { 
		return image;
	}

	public void setImage(ImageView image) {
		this.image = image;
	}
*/
		public Notification(String name, String phNumber, String src,String dest,Date picktime,String currentlocation,boolean isNotified) {
		this.name=name;
		this.phNumber=phNumber;
		this.src=src;
		this.dest=dest;
		this.picktime=picktime;
		this.currentlocation=currentlocation;
		this.isNotified=isNotified;
		
    }
    
	public Notification(){
		
	}
    @Override
    public String toString() {
        return "PassengerName    "+name + "\n" + 
        		"PhoneNumber     "+phNumber+ "\n"+
        		"Source          "+src+ "\n" +
        		"Destination     "+dest +"\n" + 
        		"PickUpTime      "+picktime+"\n"+ 
        		"CurrentLocation "+currentlocation+"\n"+
        		"IsNotified      "+isNotified;
    }
}
