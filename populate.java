package hw3_scource;
//Homework 3 for COEN280
//Author: Xin He

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import org.json.*;

public class populate {
	public static void main(String[] args) throws Exception {
		populate pop = new populate();
	    pop.run();
	    System.out.println("Done");
	}
	public void run() throws Exception {
		System.out.println("initialization");
	    Connection con = null;
	    Statement stmt=null;
	    try {
	    	con = openConnection();
	        try {
	        	stmt = con.createStatement();
	            System.out.println("parse from business");
	            parse_business(stmt);
	            System.out.println("parse from yelp_user");
	            build_user(stmt);
	            System.out.println("parse from yelp_review");
	            PreparedStatement review_stmt = con.prepareStatement("insert into review (rid,reviewDate,stars,text,funny,useful,cool,user_id,bid) values (?,?,?,?,?,?,?,?,?)");
	            build_review(con, review_stmt);
	            System.out.println("parse from yelp_checkin");
	            build_checkin(stmt);
	        }
	        catch (SQLException e){
	        	e.printStackTrace();
	        }
	        finally{
	        	//Closing the Statement object
	            try{
	            	if(stmt!=null){
	            		stmt.close();
	                    stmt=null;
	                }
	            }
	            catch (SQLException e){
	            	e.printStackTrace();
	            }
	        }
	    }
	    catch (SQLException e) {
	    	System.err.println("Errors occurs when communicating with the database server: " + e.getMessage());
	    } 
	    catch (ClassNotFoundException e) {
	    	System.err.println("Cannot find the database driver");
	    } 
	    finally {
	    	closeConnection(con);
	    }
	}
	
	static Connection openConnection() throws SQLException, ClassNotFoundException {
		// Load the Oracle database driver
	    DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
	    // Information needed when connecting to a database
	    String host = "localhost";
	    String port = "1521";
	    String dbName = "orcl";
	    String userName = "Scott";
	    String password = "tiger";
	    // JDBC URL construction
	    String dbURL = "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
	    return DriverManager.getConnection(dbURL, userName, password);
	}
	  
	public void closeConnection(Connection con) {
	    try {
	    	con.close();
	    } 
	    catch (SQLException e) {
	    	System.err.println("Cannot close connection: " + e.getMessage());
	    }
	}
	
	public void parse_business(Statement stmt1) throws Exception 
	{	
		String filePath = "yelp_business.json";
	  	int close7_f,open7_f, close1_f,open1_f, close2_f,open2_f, close3_f,open3_f, close4_f,open4_f,close5_f,open5_f, close6_f,open6_f;
	  	try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	  	    for(String line; (line = br.readLine()) != null; ) {
	  	    	JSONObject obj=new JSONObject(line);
	  			String bid=obj.getString("business_id");
	  			String address=obj.getString("full_address");
	  			String address_modify=address.replaceAll("'", "''");
	  			JSONObject hours=obj.getJSONObject("hours");
	  			if (hours.has("Sunday")) {
	  	    	    JSONObject Sunday = hours.getJSONObject("Sunday");
	  	    	    String close7=Sunday.getString("close");
	  	    	    String close7_m=close7.substring(0, close7.length()-3) + close7.substring(close7.length()-2);
		    	    close7_f = Integer.parseInt(close7_m);
	      	    	String open7=Sunday.getString("open");
	      	    	String open7_m=open7.substring(0, open7.length()-3) + open7.substring(open7.length()-2);
	  	    	    open7_f = Integer.parseInt(open7_m);
	  	    	}
	  			else {
	  				close7_f=-1;
	  			    open7_f=-1;
	  			}
	  			if (hours.has("Monday")) {
	  	    	    JSONObject Monday = hours.getJSONObject("Monday");
	  	    	    String close1=Monday.getString("close");
	  	    	    String close1_m=close1.substring(0, close1.length()-3) + close1.substring(close1.length()-2);
	  	    	    close1_f = Integer.parseInt(close1_m);
	    	    	String open1=Monday.getString("open");
	    	    	String open1_m=open1.substring(0, open1.length()-3) + open1.substring(open1.length()-2);
	  	    	    open1_f = Integer.parseInt(open1_m);
	  	    	}
	  			else {
					close1_f=-1;
					open1_f=-1;
				}
	  			if (hours.has("Tuesday")) {
	  	    	    JSONObject Tuesday = hours.getJSONObject("Tuesday");
	  	    	    String close2=Tuesday.getString("close");
	  	    	    String close2_m=close2.substring(0, close2.length()-3) + close2.substring(close2.length()-2);
		    	    close2_f = Integer.parseInt(close2_m);
	    	    	String open2=Tuesday.getString("open");
	    	    	String open2_m=open2.substring(0, open2.length()-3) + open2.substring(open2.length()-2);
	  	    	    open2_f = Integer.parseInt(open2_m);
	  	    	}
	  			else {
	  				close2_f=-1;
					open2_f=-1;
	  			}
	  			if (hours.has("Wednesday")) {
	  	    	    JSONObject Wednesday = hours.getJSONObject("Wednesday");
	  	    	    String close3=Wednesday.getString("close");
	  	    	    String close3_m=close3.substring(0, close3.length()-3) + close3.substring(close3.length()-2);
		    	    close3_f = Integer.parseInt(close3_m);
	    	    	String open3=Wednesday.getString("open");
	    	    	String open3_m=open3.substring(0, open3.length()-3) + open3.substring(open3.length()-2);
	  	    	    open3_f = Integer.parseInt(open3_m);
	  	    	}
	  			else {
					close3_f=-1;
					open3_f=-1;
	  			}
	  			if (hours.has("Thursday")) {
	  	    	    JSONObject Thursday = hours.getJSONObject("Thursday");
	  	    	    String close4=Thursday.getString("close");
	  	    	    String close4_m=close4.substring(0, close4.length()-3) + close4.substring(close4.length()-2);
		    	    close4_f = Integer.parseInt(close4_m);
	    	    	String open4=Thursday.getString("open");
	    	    	String open4_m=open4.substring(0, open4.length()-3) + open4.substring(open4.length()-2);
	  	    	    open4_f = Integer.parseInt(open4_m);
	  	    	}
	  			else {
					close4_f=-1;
					open4_f=-1;
	  			}
	  			if (hours.has("Friday")) {
	  	    	    JSONObject Friday = hours.getJSONObject("Friday");
	  	    	    String close5=Friday.getString("close");
	  	    	    String close5_m=close5.substring(0, close5.length()-3) + close5.substring(close5.length()-2);
		    	    close5_f = Integer.parseInt(close5_m);
	    	    	String open5=Friday.getString("open");
	    	    	String open5_m=open5.substring(0, open5.length()-3) + open5.substring(open5.length()-2);
	  	    	    open5_f = Integer.parseInt(open5_m);
	  	    	}
	  			else {
					close5_f=-1;
					open5_f=-1;
	  			}
	  	    	if (hours.has("Saturday")) {
	  	    	    JSONObject Saturday = hours.getJSONObject("Saturday");
	  	    	    String close6=Saturday.getString("close");
	  	    	    String close6_m=close6.substring(0, close6.length()-3) + close6.substring(close6.length()-2);
		    	    close6_f = Integer.parseInt(close6_m);
	    	    	String open6=Saturday.getString("open");
	    	    	String open6_m=open6.substring(0, open6.length()-3) + open6.substring(open6.length()-2);
	  	    	    open6_f = Integer.parseInt(open6_m);
	  	    	}
	  	    	else {
					close6_f=-1;
					open6_f=-1;
	  	    	}
	  			boolean op=obj.getBoolean("open");
	  			String open=String.valueOf(op);
	  			String city=obj.getString("city");
	  			int review_count=obj.getInt("review_count");
	  			String name=obj.getString("name");
	  			String name_modify=name.replaceAll("'", "''");
	  			String state=obj.getString("state");
	  			double star=obj.getDouble("stars");
	  			StringBuilder temp=new StringBuilder();
	  			temp.append("Insert into business values('"+bid+"','"+address_modify+"',");
	  			temp.append(close7_f+","+open7_f+","+close1_f+","+open1_f+","+close2_f+","+open2_f+","+close3_f+","+open3_f+","+close4_f+","+open4_f+","+close5_f+","+open5_f+","+close6_f+","+open6_f+",");
	  			temp.append("'"+open+"','"+city+"'"+","+review_count+",'"+name_modify+"','"+state+"',"+star+")");
	  			//System.out.println(temp.toString());
	  			stmt1.executeUpdate(temp.toString());
	  		    HashSet<String> mainCategory = new HashSet();
	            mainCategory.add("Active Life");
	            mainCategory.add("Arts & Entertainment");
	            mainCategory.add("Automotive");
	            mainCategory.add("Car Rental");
	            mainCategory.add("Cafes");
	            mainCategory.add("Beauty & Spas");
	            mainCategory.add("Convenience Stores");
	            mainCategory.add("Dentists");
	            mainCategory.add("Doctors");
	            mainCategory.add("Drugstores");
	            mainCategory.add("Department Stores");
	            mainCategory.add("Education");
	            mainCategory.add("Event Planning & Services");
	            mainCategory.add("Flowers & Gifts");
	            mainCategory.add("Food");
	            mainCategory.add("Health & Medical");
	            mainCategory.add("Home Services");
	            mainCategory.add("Home & Garden");
	            mainCategory.add("Hospitals");
	            mainCategory.add("Hotels & Travel");
	            mainCategory.add("Hardware Stores");
	            mainCategory.add("Grocery");
	            mainCategory.add("Medical Centers");
	            mainCategory.add("Nurseries & Gardening");
	            mainCategory.add("Nightlife");
	            mainCategory.add("Restaurants");
	            mainCategory.add("Shopping");
	            mainCategory.add("Transportation");
	  			JSONArray category=obj.getJSONArray("categories");
	  			StringBuilder temp1=new StringBuilder();
	  			for(int i=0;i<category.length();++i) {
	  				String cate=(String) category.get(i);
	  				//Insert into table MainCategory
	  				if(mainCategory.contains(cate)) {
	  					String cate_modify=cate.replaceAll("'", "''");
	  					temp1.append("Insert into MainCategory values(");
	  					temp1.append("'"+bid+"','"+cate_modify+"')");
	  					//System.out.println(temp1);
	  					stmt1.executeUpdate(temp1.toString());
	  					temp1.setLength(0);
	  				}
	  				//Insert into table SubCategory
	  				else {
	  					String cate_modify=cate.replaceAll("'", "''");
	  					temp1.append("Insert into SubCategory values(");
	  					temp1.append("'"+bid+"','"+cate_modify+"')");
	  					//System.out.println(temp1);
	  					stmt1.executeUpdate(temp1.toString());
	  					temp1.setLength(0);
	  				}
	  			}
				//Insert into table attribute
				JSONObject attributes=obj.getJSONObject("attributes");
				StringBuilder temp2=new StringBuilder();
				Set<String> attr_keys = attributes.keySet();
				for (String key : attr_keys) {
					if (attributes.get(key) instanceof JSONObject) {
						//System.out.println("  is object");
						JSONObject child=attributes.getJSONObject(key);
						Set<String> child_keys=child.keySet();
						for(String key1:child_keys) {
							if(child.get(key1) instanceof Boolean) {
								if(child.getBoolean(key1)) {
									//System.out.println(child.getBoolean(key1));
									temp2.append("Insert into attribute values(");
									temp2.append("'"+bid+"','");
									temp2.append(key+"_"+key1+"')");
									//System.out.println(temp2);
									stmt1.executeUpdate(temp2.toString());
									temp2.setLength(0);
								}
								else {
									temp2.append("Insert into attribute values(");
									temp2.append("'"+bid+"','");
									temp2.append(key+"_"+key1+"_false')");
									//System.out.println(temp2);
									stmt1.executeUpdate(temp2.toString());
									temp2.setLength(0);
								}
								//System.out.println(key1);
								//System.out.println(child.get(key1));
							}
							else {
								temp2.append("Insert into attribute values(");
								temp2.append("'"+bid+"','");
								temp2.append(child+"_"+key1+"_"+child.getDouble(key1)+"')");
								//System.out.println("temp2=" + temp2);
								stmt1.executeUpdate(temp2.toString());
								temp2.setLength(0);
							}
						}
					}
					else {
						//System.out.println(attributes.get(key));
						if(attributes.get(key) instanceof Boolean) {
							if(attributes.getBoolean(key)) {
								temp2.append("Insert into attribute values(");
								temp2.append("'"+bid+"','");
								temp2.append(key+"')");
								//System.out.println(temp2);
								stmt1.executeUpdate(temp2.toString());
								temp2.setLength(0);
							}
								else{
									temp2.append("Insert into attribute values(");
									temp2.append("'"+bid+"','");
									temp2.append(key+"_false')");
									//System.out.println(temp2);
									String temp2_str = temp2.toString();
									stmt1.executeUpdate(temp2_str);
									temp2.setLength(0);
							}
						}
						else {
							temp2.append("Insert into attribute values(");
							temp2.append("'"+bid+"','");
							temp2.append(key+"_"+attributes.get(key)+"')");
							//System.out.println(temp2);
							stmt1.executeUpdate(temp2.toString());
							temp2.setLength(0);
						}
				}	
	  	    }
	  	}
	  }
	}
	
	public void build_review(Connection con, PreparedStatement stmt) throws Exception 
	{	
		String filePath = "yelp_review.json";
	  	try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	JSONObject obj=new JSONObject(line);
		    	String review_id=obj.getString("review_id");
				//System.out.println("review_id=" + review_id);
				String reviewDate=obj.getString("date");
				int stars=obj.getInt("stars");
				String text=obj.getString("text");
				String text_modify=text.replaceAll("'", "''");
				JSONObject votes=obj.getJSONObject("votes");
				int funny=votes.getInt("funny");
				int useful=votes.getInt("useful");
				int cool=votes.getInt("cool");
				String user_id=obj.getString("user_id");
				String bid=obj.getString("business_id");
				//String type=obj.getString("type");
				stmt.setString(1, review_id);
				stmt.setString(2, reviewDate);
				stmt.setInt(3, stars);
				Clob text_clob = con.createClob();
				text_clob.setString(4, text);
				stmt.setClob(4, text_clob);
				stmt.setInt(5, funny);
				stmt.setInt(6, useful);
				stmt.setInt(7, cool);
				stmt.setString(8, user_id);
				stmt.setString(9, bid);
				//stmt.setString(10, type);
				stmt.executeUpdate();
		    }
	  	}
	}
	
	public void build_user(Statement stmt1) throws Exception 
	{	
	  	String filePath = "yelp_user.json";
	  	try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	JSONObject obj=new JSONObject(line);
		    	String user_id=obj.getString("user_id");
		    	String name=obj.getString("name");
		    	String name_modify=name.replaceAll("'", "''");
		    	StringBuilder temp=new StringBuilder();
	  			temp.append("Insert into yelp_user values ('"+user_id+"','"+name_modify+"')");
	  			//System.out.println(temp.toString());
	  			stmt1.executeUpdate(temp.toString());
		    }
	  	}
	}
	
	public void build_checkin(Statement stmt) throws Exception 
	{	
		int checkin=0;
	  	String filePath = "yelp_checkin.json";
	  	try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	JSONObject obj=new JSONObject(line);
		    	String business_id=obj.getString("business_id");
		    	JSONObject checkin_info=obj.getJSONObject("checkin_info");
		    	Set<String> checkin_keys = checkin_info.keySet();
		    	for (String key : checkin_keys) {
		    		checkin+=checkin_info.getInt(key);
		    	}
		    	StringBuilder temp=new StringBuilder();
	  			temp.append("Insert into yelp_checkin values ('"+business_id+"','"+checkin+"')");
	  			//System.out.println(temp.toString());
	  			stmt.executeUpdate(temp.toString());
	  			checkin=0;	
		    }
	  	}
	}
}