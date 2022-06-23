package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class functions {

	static Connection conn = null;
	
	
	// Sql select 
	public static ResultSet simple_sql(String sql, List<Object> stList) {
		ResultSet rs = null;
		try{	  
   		 Class.forName("oracle.jdbc.driver.OracleDriver");  
   		 conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","ordreMission","orcl1");	
   
   		 PreparedStatement pst = conn.prepareStatement(sql);
   		 int i = 0;
   		 for(Object st:stList) {
   			 i++;
   			 pst.setObject(i, st);
   		 }
   		 rs = pst.executeQuery();
   		// conn.close();
		}catch (Exception e) {
			System.out.println("simple_sql =====> "+e.getMessage());
		}
		return rs;
	}

	//********************************************************************************
	public static void infoBox(String infoMessage, String headerMessage, String type_alert) {
		Alert alert = null;
		if(type_alert.toUpperCase().equals("ERROR")) {
			alert = new Alert(AlertType.ERROR);
		}else if(type_alert.toUpperCase().equals("CONFIRMATION")) {
			alert = new Alert(AlertType.CONFIRMATION);
		}else if(type_alert.toUpperCase().equals("INFORMATION")) {
			alert = new Alert(AlertType.INFORMATION);
		}else if(type_alert.toUpperCase().equals("WARNING")) {
			alert = new Alert(AlertType.WARNING);
		}
		
		alert.setTitle(type_alert);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}
	
}
