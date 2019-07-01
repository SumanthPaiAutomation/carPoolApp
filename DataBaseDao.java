package com.demo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.ample.CabDetail;

import com.ample.util.Credential;
import com.ample.util.Notification;
import com.ample.util.User;



public class DataBaseDao {
	
	static DataBaseDao dataBaseDao;
	public static DataBaseDao getDataBaseDao(){
		try{
			ApplicationContext context =  new ClassPathXmlApplicationContext("database.xml");
				if(dataBaseDao==null){
					 dataBaseDao=(DataBaseDao)context.getBean("dataBaseDao");
						System.out.println("JdbacTemplete "+dataBaseDao.getJdbcTemplate().toString());
				}
			return dataBaseDao;
		}catch(Exception t ){
			t.printStackTrace();
			return null;
		}
	}
	
	
	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		System.out.println("jdbcTemplate "+jdbcTemplate.toString());
	}
	
	public int registerUser(final Driver driver,final String adharnumber){
		
		String sql = "SELECT * FROM adhardetails";
	 
		List<Adhar> adharlist  = getJdbcTemplate().query(sql,new BeanPropertyRowMapper<Adhar>(Adhar.class));
			boolean isAdharMatching=false;
			for(Adhar adhar: adharlist){
				if(adhar.getAdharnumber().equals(adharnumber)){
					isAdharMatching=true;
					break;
				}
			}
		if(!isAdharMatching){
				final String INSERT_adhar = "insert into adhardetails (adharnumber) values(?)";
				 
				jdbcTemplate.update(new PreparedStatementCreator() { 
					public PreparedStatement createPreparedStatement( Connection connection) throws SQLException { 
						PreparedStatement ps = connection.prepareStatement(INSERT_adhar,Statement.RETURN_GENERATED_KEYS); 
						ps.setString(1,adharnumber);
						return ps; 
						} 
						}); 
			  final String INSERT_SQL = "insert into driver (drivername,mobilenumber,vechiletype,vechilenumber) values(?,?,?,?)";
				KeyHolder keyHolder = new GeneratedKeyHolder(); 
				jdbcTemplate.update(new PreparedStatementCreator() { 
					public PreparedStatement createPreparedStatement( Connection connection) throws SQLException { 
						PreparedStatement ps = connection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS); 
						ps.setString(1, driver.getDrivername());
						ps.setString(2, driver.getMobilenumber());
						ps.setString(3, driver.getVechiletype());
						ps.setString(4, driver.getVechilenumber());
						return ps; 
						} 
						}, keyHolder); 
					return keyHolder.getKey().intValue(); 
			
			}else{
				return -2;
			}
	}
	
	
	public int updateRecords(final Driver driver){
		 String SQL = "update driver set password = ? where mobilenumber = ?";
		 return jdbcTemplate.update(SQL,new Object[] {driver.getPassword(),driver.getMobilenumber()});
		 
	}
	
	
	
	public int updatelocation(String mobilenumber,String longitude,String latitude,String location){
		//SET SQL_SAFE_UPDATES=0; execute on clod to set value in where clause without primary key
		 String SQL = "update driver set latitude = ?, longitude =?, location=? where mobilenumber = ?";
		  if(jdbcTemplate.update(SQL,new Object[] {latitude,longitude,location,mobilenumber})>0){
			  String updateTime= "update driver set starttime=NOW() where mobilenumber='"+mobilenumber+"'";
				 return jdbcTemplate.update(updateTime);
			}
		  return -1;
		 //,timesnaps=?
	} 
	
	
	
	
	public int updatesrcdest(String src,String dest,String mobilenumber){
		  String SQL = "update driver set source = ?, destination =? where mobilenumber = ?";
		  try{
			  return jdbcTemplate.update(SQL,new Object[] {src,dest,mobilenumber});  
		  }catch(Exception t ){
			  t.printStackTrace();
			  return -1;
		  }
		  
	} 
	
	
	
	
	/*public int updatemessage(String phoneNumber,String message){
			  String updateTime= "update cloudgps set shareinfo='"+message+"'"+" where phnumber='"+phoneNumber+"'";
				 return jdbcTemplate.update(updateTime);
	}*/ 
	
	
	public int updatePeopleincabnow(String phoneNumber,int maxseatincab ,int peopleincabnow){
		 String update= "update driver set peopleincabnow="+peopleincabnow+","+"maxseatincab ="+maxseatincab+" where mobilenumber='"+phoneNumber+"'";
		System.out.println("update "+update);
	   return jdbcTemplate.update(update);
     } 
	
	
	public boolean authUser(String phNumber,String passWord){
			try{	
			Credential credential = jdbcTemplate.queryForObject( "select mobilenumber, password from driver where password = ?", new Object[]{passWord},
			        new RowMapper<Credential>() {
			            public Credential mapRow(ResultSet rs, int rowNum) throws SQLException {
			            	Credential tempcredential = new Credential();
			            	tempcredential.setUserName(rs.getString("mobilenumber"));
			            	tempcredential.setPassWord(rs.getString("password"));
			                return tempcredential;
			            }
			        });
				if(credential.getUserName().equals(phNumber) && credential.getPassWord().equals(passWord)){
					return true;
				}
			}catch(EmptyResultDataAccessException dataAccessException){
				dataAccessException.printStackTrace();
				return false;
			}
		return false;
	}

	
	/*public Driver getLatLong(String phnumber){
		return jdbcTemplate.queryForObject( "select latitude, longitude from driver where mobilenumber = ?", new Object[]{phnumber},
		        new RowMapper<Driver>() {
		            public Driver mapRow(ResultSet rs, int rowNum) throws SQLException {
		            	Driver driver = new Driver();
		            	driver.setLatitude(rs.getString("latitude"));
		            	driver.setLongitude(rs.getString("longitude"));
		                return driver;
		            }
		        });
	}
	*/
	
	
	public int updateUserInfo(final String src,
			final String dest,
			final String phonenumber,
			final String currentloc,
			final String name,
			final String isnotified){
		final String INSERT_SQL = "insert into user (phNumber,src,dest,picktime,currentlocation,name,isNotified) values(?,?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		jdbcTemplate.update(new PreparedStatementCreator() { 
			public PreparedStatement createPreparedStatement( Connection connection) throws SQLException { 
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS); 
				Date myDate=null;
				try{
					String pattern = "yyyy-MM-dd hh:mm:ss";
					SimpleDateFormat formatter = new SimpleDateFormat(pattern);
					Date today = new Date();
					String output = formatter.format(today);
					myDate= formatter.parse(output);
				}catch(Exception t){
					t.printStackTrace();
				}
				java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
				ps.setString(1, phonenumber);
				ps.setString(2, src);
				ps.setString(3, dest);
				ps.setDate(4,sqlDate);
				ps.setString(5, currentloc);
				ps.setString(6, name);
				ps.setString(7, isnotified);
				
				return ps; 
				} 
				}, keyHolder); 
			return keyHolder.getKey().intValue(); 
		}

	
	
			public List<CabDetail> getVechile(String src,String dest){
				final List<CabDetail> driverDetails= new ArrayList<CabDetail>();
				String sql="select * from driver where source ="+"'"+src+"'"+"and destination="+"'"+dest+"'";
				List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
		    	for (Map row : rows) {
		    		CabDetail cabdetail = new CabDetail();
		    		cabdetail.setDrivername((String)row.get("drivername"));
		    		cabdetail.setMobilenumber((String)row.get("mobilenumber"));
		    		cabdetail.setVechiletype((String)row.get("vechiletype"));
		    		cabdetail.setPeopleincabnow((Integer)row.get("peopleincabnow"));
		    		cabdetail.setMaxseatincab((Integer)row.get("maxseatincab"));
		    		cabdetail.setSource((String)row.get("source"));
		    		cabdetail.setDestination((String)row.get("destination"));
		    		cabdetail.setStarttime((Date)row.get(("starttime")));
		    		cabdetail.setLocation((String)row.get(("location")));
		    		cabdetail.setLatitude((String)row.get(("latitude")));
		    		cabdetail.setLongitude((String)row.get(("longitude")));
		        	driverDetails.add(cabdetail);
		    	}
		    	return driverDetails;
		    }

		
			
			public  void updateDriverUserPickDetailsByUSer(final String phnumberdriver,final String phnumberuser){
				
				String query="insert into driveruser(phNumberdriver,phNumberuser,pickstatus,driversentnotificationtime) values("
										  +"'"+phnumberdriver+"',"
				 						  +"'"+phnumberuser+"',"
				 						  +"'"+1+"',"
				 						  +"Now()"+")";
				getJdbcTemplate().execute(query);
				
			}
			
			
			public  int updateDriverUserPickDetailsByDriver(final String phnumberdriver,final String phnumberuser){
				
			 String SQL = "update driveruser set pickstatus = ? where phNumberuser = ?";
	          return jdbcTemplate.update(SQL,new Object[] {0,phnumberuser});
				
			}
			
			
			
			public   List<Notification> getAllTheRequesteduserForCab(String phnumberdriver){
				//pickstatus 1 is for pick me 0 means picked
				String user_ph_number_query= "select phNumberuser from driveruser where phNumberdriver="+"'"+phnumberdriver+"'"+" and pickstatus='1'";
				
				List<String> user_ph_number_query_db= getJdbcTemplate().queryForList(user_ph_number_query, String.class);
				final List<Notification> notificationlist= new ArrayList<Notification>();
				for(String phnumb:user_ph_number_query_db){
				String sql="select * from user where phNumber ="+"'"+phnumb+"'";
				List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
		    	for (Map row : rows) {
		    		String phNumber=(String)row.get("phNumber");
		    		String src=(String)row.get("src");
		    		String dest=(String)row.get("dest");
		    		Date picktime=(Date)row.get("picktime");
		    		String currentlocation=(String)row.get("currentlocation");;
		    		String name=(String)row.get("name");
		    		String isNotified_ste=(String)row.get("name");
		    		notificationlist.add(new Notification(name, phNumber, src,dest,picktime,currentlocation,Boolean.parseBoolean(isNotified_ste)));
		    	}
		    	
				}
		    	return notificationlist;
			}
			
			
			
		public	List<User>	getSelectedUserForDriver(String phnumberdriver){
				String user_ph_number_query= "select phNumberuser from driveruser where phNumberdriver="+"'"+phnumberdriver+"'"+" and pickstatus='0'";
				final List<User> users=new ArrayList<User>();
				List<String> user_ph_number_query_db= getJdbcTemplate().queryForList(user_ph_number_query, String.class);
				for(String phnumb:user_ph_number_query_db){
				String sql="select * from user where phNumber ="+"'"+phnumb+"'";
				/*List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
		    	for (Map row : rows) {
		    		User user= new User();
		    		user.setCurrentlocation((String)row.get("currentlocation"));
		    		user.setDest((String)row.get("dest"));
		    		user.setId((Long)row.get("id"));
		    		user.setIsNotified((String)row.get("isNotified"));
		    		user.setName((String)row.get("name"));
		    		user.setPhNumber((String)row.get("phNumber"));
		    		user.setPicktime((String)row.get("picktime"));
		    		user.setSrc((String)row.get("src"));
		    		users.add(user);
		    	   }*/
				
				
				getJdbcTemplate().query(sql,new RowMapper<User>(){  
				    @Override  
				    public User mapRow(ResultSet rs, int rownumber) throws SQLException {  
				    	User user= new User();
				    	user.setCurrentlocation((rs.getString("currentlocation")));
				    	user.setDest(rs.getString("dest"));
				    	user.setIsNotified(rs.getString("isNotified"));
							user.setName(rs.getString("name"));
							user.setId(rs.getInt("id"));
							user.setPhNumber(rs.getString("phNumber"));
							user.setSrc(rs.getString("src"));
							user.setPicktime((((java.util.Date)rs.getTimestamp(("picktime"))).toString()));
				    	    users.add(user);
				    		return user;
				    } });  
				}
				return users; 
				
			}
			
			
		
		
		public	List<Driver>	driverAccepteduserreq(String phnumberuser){
			String user_ph_number_query= "select phNumberdriver from driveruser where phNumberuser="+"'"+phnumberuser+"'"+" and pickstatus='0'";
			final List<Driver> drivers=new ArrayList<Driver>();
			List<String> driver_ph_number_query_db= getJdbcTemplate().queryForList(user_ph_number_query, String.class);
			for(String phnumb:driver_ph_number_query_db){
			String sql="select * from driver where mobilenumber ="+"'"+phnumb+"'";
			/*List<Map<String, Object>> rows = getJdbcTemplate().queryForList(sql);
	    	for (Map row : rows) {
	    		   Driver cabdetail = new Driver();
		    		cabdetail.setDrivername((String)row.get("drivername"));
		    		cabdetail.setMobilenumber((String)row.get("mobilenumber"));
		    		cabdetail.setVechiletype((String)row.get("vechiletype"));
		    		cabdetail.setPeopleincabnow((Integer)row.get("peopleincabnow"));
		    		cabdetail.setMaxseatincab((Integer)row.get("maxseatincab"));
		    		cabdetail.setSource((String)row.get("source"));
		    		cabdetail.setDestination((String)row.get("destination"));
		    		cabdetail.setStarttime((String)row.get(("starttime")));
		    		cabdetail.setLocation((String)row.get(("location")));
		    		cabdetail.setLatitude((String)row.get(("latitude")));
		    		cabdetail.setLongitude((String)row.get(("longitude")));
		    		cabdetail.setVechilenumber((String)row.get("vechilenumber"));
		    		drivers.add(cabdetail);
	    		}
			}*/
			
			
			getJdbcTemplate().query(sql,new RowMapper<Driver>(){  
			    @Override  
			    public Driver mapRow(ResultSet rs, int rownumber) throws SQLException {  
			    	  Driver cabdetail = new Driver();
			    	    cabdetail.setDrivername(rs.getString("drivername"));
			    		cabdetail.setMobilenumber(rs.getString("mobilenumber"));
			    		cabdetail.setVechiletype(rs.getString("vechiletype"));
			    		cabdetail.setPeopleincabnow(rs.getInt("peopleincabnow"));
			    		cabdetail.setMaxseatincab(rs.getInt("maxseatincab"));
			    		cabdetail.setSource(rs.getString("source"));
			    		cabdetail.setDestination(rs.getString("destination"));
			    	    cabdetail.setStarttime(((java.util.Date)rs.getTimestamp(("starttime"))).toString());
			    		cabdetail.setLocation(rs.getString(("location")));
			    		cabdetail.setLatitude(rs.getString(("latitude")));
			    		cabdetail.setLongitude(rs.getString(("longitude")));
			    		cabdetail.setVechilenumber(rs.getString("vechilenumber"));
			    		drivers.add(cabdetail);
			    		return cabdetail;
			    }  
			    });  
			}
			
			return drivers; 
			
		}
		
		
		
		
		
			public List<User> findAllUser(){
				 
				String sql = "SELECT * FROM user";
				
				final List<User> users= new ArrayList<User>();
				getJdbcTemplate().query(sql,new RowMapper<User>(){  
				    @Override  
				    public User mapRow(ResultSet rs, int rownumber) throws SQLException {  
				    	User user=new User();  
				    	user.setPhNumber(rs.getString("phNumber"));
				    	user.setSrc(rs.getString("src"));
				    	user.setDest(rs.getString("dest"));
				    	user.setPicktime(((java.util.Date)rs.getTimestamp("picktime")).toString());
				    	user.setCurrentlocation(rs.getString("currentlocation"));
				    	user.setName(rs.getString("name"));
				    	user.setIsNotified(rs.getString("isNotified"));
				    	users.add(user);
				    	return user;
				    }  
				    });  
				
				return users;
			}
			
			
			public List<Driver> findAllDriver(){
				 
				String sql = "SELECT * FROM driver";
			 
				List<Driver> driver  = getJdbcTemplate().query(sql,new BeanPropertyRowMapper<Driver>(Driver.class));
				return driver;
			}
			
			
			
			
			
			
			
			
			/*public static void main(String[] args) {
				getAllTheRequesteduserForCab("8147574889");
			}*/
			
			
	 }//class End

	

	

 













	/*	
		
	public boolean storeDocuments(final byte[]doc,final String deptName,final String fileName,final String userName){
		final String INSERT_SQL = "insert into document (teachername,docname,branch,docdata) values(?,?,?,?)";
		  jdbcTemplate.update(INSERT_SQL, new PreparedStatementSetter() {
			  public void setValues(PreparedStatement ps) throws SQLException {
				ps.setBinaryStream(4,new ByteArrayInputStream(doc),doc.length);
				ps.setString(3, deptName);
				ps.setString(2, fileName);
				ps.setString(1, userName);
				}
				});
		  return true;
	}
		  
	public List<String> getStudentEmails(String role,String deptName){
		
		 String sql = "SELECT email FROM student where role="+"'"+role+"'"+" and "+"deptName= "+"'"+deptName+"'";
		 
	     List<String> mails = jdbcTemplate.queryForList(sql,String.class);
	
		 return mails;
	}
	
	
	public int insertStudentRecord(final String name,final String email,final String collegeName,final String deptName,final String contactNumber,final String role ){
		final String INSERT_SQL = "insert into student (name,email,collegeName,deptName,contactNumber,role) values(?,?,?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		jdbcTemplate.update(new PreparedStatementCreator() { 
			public PreparedStatement createPreparedStatement( Connection connection) throws SQLException { 
				PreparedStatement ps = connection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS); 
				ps.setString(1, name);
				ps.setString(2, email);
				ps.setString(3, collegeName);
				ps.setString(4, deptName);
				ps.setString(5, contactNumber);
				ps.setString(6, role);
				return ps; 
				} 
				}, keyHolder); 
			return keyHolder.getKey().intValue(); 
	
		}
	
	
		public int updateStudent(final String userName,final String passWord,final Integer id){
			 final String SQL = "update student set userName =? where id = ?";
			 jdbcTemplate.update(SQL,new Object[] {userName,id});
			 final String SQLpassword = "update student set passWord =? where id = ?";
			 return jdbcTemplate.update(SQLpassword,new Object[] {passWord,id});
			
		}
	
		public int updateDocNameForStudent(String docName,String email){
			 String SQL = "update student set docsend = ?  where email = ?";
			return  jdbcTemplate.update(SQL, docName, email);
		}
		
		
		
		
		public String getDocNameByEmails(String userName){
			
		
			
			 String sql = "SELECT docsend FROM student where userName="+"'"+userName+"'";
			 
		     String docName = jdbcTemplate.queryForObject(sql, String.class);
		
			 return docName;
		}
	*/
	
	/*public List<DataBaseColumnValue> getAllFriendsRowMapper(){  
		 
	//	final List<DataBaseColumnValue> list= new ArrayList<DataBaseColumnValue>();
		 return getJdbcTemplate().query("select * from cloudgps",new RowMapper<DataBaseColumnValue>(){  
		    @Override  
		    public DataBaseColumnValue mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	DataBaseColumnValue columnvalue=new DataBaseColumnValue();  
		    	columnvalue.setId(rs.getLong("id"));
		    	columnvalue.setAddress(rs.getString("address"));
		    	columnvalue.setLatitude(rs.getString("latitude"));
		    	columnvalue.setLongitude(rs.getString("longitude"));
		    	columnvalue.setPassword(rs.getString("password"));
		    	columnvalue.setPhnumber(rs.getString("phnumber"));
		    	columnvalue.setShareinfo(rs.getString("shareinfo"));
		    	columnvalue.setTimesnaps(rs.getTimestamp("timesnaps"));
		    	columnvalue.setUsername(rs.getString("username"));
		    	return columnvalue;
		    }  
		    });  
		
		//return list;
		}  
	*/
	
	

