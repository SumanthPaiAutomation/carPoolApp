package com.ample.admin;

import com.opensymphony.xwork2.ActionSupport;

public class Admin{

	private static final long serialVersionUID = 1L;

		private String shopDetails;
		private String trace;
		private String comments;
		private Byte floorimage;	
		private String floorNo;
		public String getFloorNo() {
			return floorNo;
		}


		public void setFloorNo(String floorNo) {
			this.floorNo = floorNo;
		}


		public String getShopDetails() {
			return shopDetails;
		}


		public void setShopDetails(String shopDetails) {
			this.shopDetails = shopDetails;
		}


		public String getTrace() {
			return trace;
		}


		public void setTrace(String trace) {
			this.trace = trace;
		}


		public String getComments() {
			return comments;
		}


		public void setComments(String comments) {
			this.comments = comments;
		}


		public Byte getFloorimage() {
			return floorimage;
		}

		public void setFloorimage(Byte floorimage) {
			this.floorimage = floorimage;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		
		public String  createQRAndUploadMap(){
			return "success";
		}
		
		public String showAdminPage(){
			return "show_admin_page";
		}
}
