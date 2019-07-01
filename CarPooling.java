package com.ample;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.ample.util.AESencrp;
import com.ample.util.Credential;
import com.ample.util.Drivers;
import com.ample.util.Notification;
import com.ample.util.Notifications;
import com.ample.util.SourceDestinationList;
import com.ample.util.Users;
import com.demo.db.DataBaseDao;
import com.demo.db.Driver;


@Path("/")

public class CarPooling {
	@GET
	@Path("/check")
	@Produces(MediaType.APPLICATION_XML)
	public Object checkLive(){
		Credential cred= new Credential();
		cred.setUserName("amar");
		cred.setPassWord("amar");
		return cred;
	}
	
	
	@GET
	@Path("/srcdest")
	@Produces(MediaType.APPLICATION_XML)
	public Object getSrcdest(){
		SourceDestinationList scrdes= new SourceDestinationList();
		scrdes.getSrcdest().add("Majestick");
		scrdes.getSrcdest().add("Matikere");
		scrdes.getSrcdest().add("Hebbal");
		scrdes.getSrcdest().add("Silkboard");
		scrdes.getSrcdest().add("Kormangala");
		scrdes.getSrcdest().add("Yeshwantpur");
		scrdes.getSrcdest().add("Yelanka");
		/*int id = DataBaseDao.getDataBaseDao().registerUser(userName, phoneNumber);
		if(id>0){
			String passsWord= userName+"@"+id;
			if(DataBaseDao.getDataBaseDao().updateRecords(passsWord, phoneNumber)>0){
				Credential cred= new Credential();
				cred.setUserName(phoneNumber);
				cred.setPassWord(passsWord);
				return cred;
			}
			Credential cred= new Credential();
			cred.setUserName(phoneNumber);
			cred.setPassWord("Error occured in server while update passWord");
			return cred;
		}*/
		System.out.println("Inside find source destination");
		return scrdes;
	
	}
	
	@GET
	@Path("/updateseat/{phnumber}/{peopleincabnow}/{maxseatavilincab}")
	@Produces(MediaType.APPLICATION_XML)
	public Object updateseatInfo(@PathParam("phnumber") String phnumber,@PathParam("peopleincabnow") Integer peopleincabnow,@PathParam("maxseatavilincab") Integer maxseatavilincab){
		System.out.println("People in cab now "+peopleincabnow);
		System.out.println("Mobile Number "+phnumber);
		System.out.println("Inside udate seat info");
		if(DataBaseDao.getDataBaseDao().updatePeopleincabnow(phnumber, maxseatavilincab,peopleincabnow)>0){
			return "updated succsessfully";
		}
		return "Error from server while updating seat info";
	
	
	}
	
	
	
	@GET
	@Path("/updatesrcdest/{src}/{dest}/{phnumber}")
	@Produces(MediaType.APPLICATION_XML)
	public Object updatesrcdest(@PathParam("src") String src,@PathParam("dest") String dest,@PathParam("phnumber") String phnumber){
		System.out.println("Mobile Number "+phnumber);
		System.out.println("Inside updatesrcdest");
		if(DataBaseDao.getDataBaseDao().updatesrcdest(src, dest, phnumber)>0){
			return "updated succsessfully";
		}
		return "Error from server while updating src dest";
	
	
	}
	
	//get Cab driver notification from user
	@GET
	@Path("/notification/{phnumber}")
	@Produces(MediaType.APPLICATION_XML)
	public Object getNotificationToCabDriver(@PathParam("phnumber") String phnumber){
		List<Notification> list=DataBaseDao.getDataBaseDao().getAllTheRequesteduserForCab(phnumber);
		Notifications notifications= new Notifications();
		for(Notification notification:list){
			notifications.getNotification().add(notification);
		}
		/*for(int i=0;i<2;i++){
			notifications.getNotification().add(new Notification("Amar", "7847932020", "matikere","Hebbal",cal.getTime(),"silkboard",false));
		}
		*/
		System.out.println("Inside getNotificationToCabDriver ");
		return notifications;
	
	}
	
	
	
	
	@GET
	@Path("/user_cab/{phnumber}")
	@Produces(MediaType.APPLICATION_XML)
	public Object getUserListRequestdForCab(@PathParam("phnumber") String phnumber){
		return DataBaseDao.getDataBaseDao().getSelectedUserForDriver(phnumber);
	
	}
	
	@GET
	@Path("/register/{drivername}/{driverphonenumber}/{vechiletype}/{vechilenumber}/{adharcard}")
	@Produces(MediaType.APPLICATION_XML)
	public Object getRegistered(@PathParam("drivername")String drivername,@PathParam("driverphonenumber")String driverphonenumber,@PathParam("vechiletype")String vechiletype,@PathParam("vechilenumber")String vechilenumber,@PathParam("adharcard")String adharnumber){
		System.out.println("Inseide Register");
		try{
		
		String driver_name_decrypt = AESencrp.decrypToString(drivername);
		String vechile_type_decrypt=  AESencrp.decrypToString(vechiletype);
		String vechile_number_decrypt= AESencrp.decrypToString(vechilenumber);
	
		Driver driver= new Driver();
		driver.setDrivername(driver_name_decrypt);
		driver.setVechiletype(vechile_type_decrypt);
		driver.setVechilenumber(vechile_number_decrypt);
		driver.setMobilenumber(driverphonenumber);
		
		int id = DataBaseDao.getDataBaseDao().registerUser(driver,adharnumber);
		
		if(id>0){
			String passsWord= driver.getDrivername()+"@"+id;
			driver.setPassword(passsWord);
			if(DataBaseDao.getDataBaseDao().updateRecords(driver)>0){
				Credential cred= new Credential();
				cred.setUserName(driver.getMobilenumber());
				cred.setPassWord(passsWord);
				return cred;
			}
			Credential cred= new Credential();
			cred.setUserName("Error");
			cred.setPassWord("Error occured in server while update passWord");
			return cred;
		}
		
		if(id==-1){
			Credential cred= new Credential();
			cred.setUserName("-1");
			cred.setPassWord("-1");
			return cred;
		}
		
		if(id==-2){
			Credential cred= new Credential();
			cred.setUserName("Adhar Details already registerd with our DataBase");
			cred.setPassWord("-2");
			cred.setLoginSuccess(false);
			return cred;
		}
		Credential cred= new Credential();
		cred.setUserName("Error");
		cred.setPassWord("Error occured in server  updating passWord");
		return cred;
		}catch(Exception t){
			t.printStackTrace();
			Credential cred= new Credential();
			cred.setUserName("Error");
			cred.setPassWord("Error occured in server  catch block");
			return cred;
		}
	}
	
	@GET
	@Path("/login/{phoneNumber}/{passWord}")
	@Produces(MediaType.APPLICATION_XML)
	public Object getlogin(@PathParam("phoneNumber")String phoneNumber,@PathParam("passWord")String passWord){
		System.out.println("inside Login");
		String passWord_decrypt=AESencrp.decrypToString(passWord);
		if(DataBaseDao.getDataBaseDao().authUser(phoneNumber,passWord_decrypt)){
			Credential cred= new Credential();
			cred.setLoginSuccess(true);
			cred.setUserName(phoneNumber);
			cred.setPassWord(passWord);
			return cred;
		}
		Credential cred= new Credential();
		cred.setLoginSuccess(false);
		cred.setUserName(phoneNumber);
		cred.setPassWord(passWord);
		return cred;
		
	}
	
	
	//SENT CONFIRMATION TO USER THAT I WILL PICK U AT UR LOCATION
	@GET
	@Path("/confirm/{phoneNumberPassenger}/{phNumberDriver}")
	@Produces(MediaType.APPLICATION_XML)
	public Object setConfirmation(@PathParam("phoneNumberPassenger")String phoneNumberPassenger,@PathParam("phNumberDriver")String phNumberDriver){
		System.out.println("phoneNumberPassenger "+phoneNumberPassenger);
		System.out.println("phNumberDriver "+phNumberDriver);
		
		if(DataBaseDao.getDataBaseDao().updateDriverUserPickDetailsByDriver(phNumberDriver,phoneNumberPassenger)>0){
			return "Please pick the passenger you selected";
		}
		else{
			return "Error happend from server";
		}
	}

	
	
	
	
	
	
	//List of All the User Accpted by driver
	@GET
	@Path("/driveraccepteduser/{phnumberdriver}")
	@Produces(MediaType.APPLICATION_XML)
	public Object userListAccpetedbyDriver(@PathParam("phnumberdriver") String phnumberdriver){
				try{
					System.out.println("Insided driveraccepteduser");
				    Users users= new Users();
				     users.setUser(DataBaseDao.getDataBaseDao().getSelectedUserForDriver(phnumberdriver));
				     return users;
				}
				catch(Exception t){
					t.printStackTrace();
					return "Error from server sending request to cab Driver";
				}
		    }
	
	
	
	//Get list fo Driver Accepted the req for a  User
	@GET
	@Path("/driveraccepteduserreq/{phnumberUser}")
	@Produces(MediaType.APPLICATION_XML)
	public Object driverListAccptedUserReq(@PathParam("phnumberUser") String phnumberUser){
				try{
					Drivers drivers= new Drivers();
					drivers.setDriver(DataBaseDao.getDataBaseDao().driverAccepteduserreq(phnumberUser));
					return drivers;
				}
				catch(Exception t){
					t.printStackTrace();
					return "Error from server sending request to cab Driver";
				}
		    }
	
	
	
	
	
	
	//UPDATE DRIVER LOCATION ON BASE OF LAT LONG
	@GET
	@Path("/locupdate/{phoneNumber}/{lognitude}/{latitude}/{address}")
	@Produces(MediaType.APPLICATION_XML)
	public Object updateDriverLocation(@PathParam("phoneNumber")String phoneNumber,@PathParam("lognitude")String lognitude,@PathParam("latitude")String latitude,@PathParam("address")String address){
		try{
			System.out.println("phoneNumber "+phoneNumber);
			System.out.println("lognitude "+lognitude);
			System.out.println("latitude "+latitude);
			byte[] decodedaddress = org.apache.commons.codec.binary.Base64.decodeBase64(address.getBytes());      
			String newaddressforupdate=new String(decodedaddress);
			System.out.println("address "+newaddressforupdate);
			if(DataBaseDao.getDataBaseDao().updatelocation(phoneNumber, lognitude, latitude, newaddressforupdate)>0){
				return "Updated Driver location";	
			}
			return "Update Driver location Error from server";
			
		}catch(Exception t){
			t.printStackTrace();
			return "Error Exception from server";
		}
		
	}
	
	

	/*
	@GET
	@Path("/shareinfo/{phoneNumber}/{message}")
	@Produces(MediaType.APPLICATION_XML)
	public Object shareinfo(@PathParam("phoneNumber")String phoneNumber,@PathParam("message")String message){
		try{
			System.out.println("phoneNumber "+phoneNumber);
			byte[] decodemessage = org.apache.commons.codec.binary.Base64.decodeBase64(message.getBytes());      
			String messagesforupdate=new String(decodemessage);
			System.out.println("message "+messagesforupdate);
			if(DataBaseDao.getDataBaseDao().updatemessage(phoneNumber, messagesforupdate)>0){
				return "Messages shared";
			}
			return "Messages shared Error from server";
			
		}catch(Exception t){
			t.printStackTrace();
			return "Error from server";
		}
		
		return null;
	}*/
	
//This methos is used to regitsre user and find cab in a specified direction
	@GET
	@Path("/updateuserinfo/{src}/{dest}/{phonenumber}/{currentloc}/{name}/{isnotified}")
	@Produces(MediaType.APPLICATION_XML)
	public Object userInfo(
			@PathParam("src")String src,
			@PathParam("dest")String dest,
			@PathParam("phonenumber")String phonenumber,
			@PathParam("currentloc")String currentloc,
			@PathParam("name")String name,
			@PathParam("isnotified")String isnotified
			){
		
		try{
			String decrypt_name = AESencrp.decrypToString(name);
			String decrypt_currentloc=AESencrp.decrypToString(currentloc); 
			if(DataBaseDao.getDataBaseDao().updateUserInfo(src, dest, phonenumber, decrypt_currentloc, decrypt_name, isnotified)>0){
				List<CabDetail> cabDetail= DataBaseDao.getDataBaseDao().getVechile(src,dest);
				CabDetails cabDetails= new CabDetails();
				cabDetails.setCabdetail(cabDetail);
				
				return cabDetails;
			}
			else{
				return "some problem with Mysql";
			}
		}
		catch(Exception t){
			t.printStackTrace();
			return "Error from server";
		}
		
	}
	
	//Used to Accept request by cab driver from user 
	@GET
	@Path("/usercabrequest/{driverphnumber}/{userphnumber}")
	@Produces(MediaType.APPLICATION_XML)
	public Object userCabRequest(
			@PathParam("driverphnumber")String driverphnumber,
			@PathParam("userphnumber")String userphnumber
			
			){
				try{
					System.out.println("Insided UserCabRequest");
				    DataBaseDao.getDataBaseDao().updateDriverUserPickDetailsByUSer(driverphnumber, userphnumber);
					return "Request sent to the Driver at "+driverphnumber+"Driver will be notifed once he recive your request";
				}
				catch(Exception t){
					t.printStackTrace();
					return "Error from server sending request to cab Driver";
				}
		    }
		
	
	
	//Used to Accept request by cab driver from user 
		@GET
		@Path("/findalluser")
		@Produces(MediaType.APPLICATION_XML)
		public Object findAllUSer(){
					try{
						System.out.println("Insided findAll user");
					    Users users= new Users();
					     users.setUser(DataBaseDao.getDataBaseDao().findAllUser());
					     return users;
					}
					catch(Exception t){
						t.printStackTrace();
						return "Error from server sending request to cab Driver";
					}
			    }
		
		@GET
		@Path("/findalldriver")
		@Produces(MediaType.APPLICATION_XML)
		public Object findAllDriver(){
					try{
						System.out.println("Insided findAll driver");
						Drivers drivers= new Drivers();
						List<Driver> list=DataBaseDao.getDataBaseDao().findAllDriver();
						drivers.setDriver(list);
					    return drivers;
					}
					catch(Exception t){
						t.printStackTrace();
						return "Error from server sending request to cab Driver";
					}
			    }
		
		
}
