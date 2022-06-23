package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class mainWindowController implements Initializable {

	@FXML
	private AnchorPane paneMenu, paneContenu;

	@FXML
	private ImageView img_logo;

	@FXML
	private Button bt_ordreMission, bt_employes, bt_vehicules;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		initialser_icones();
		afficherOrdreMission();
		//nouveauClientProbel();
	}

	public void initialser_icones() {
		img_logo.setImage(new Image(getClass().getResourceAsStream("/images/logo.png")));

		ImageView img = new ImageView(new Image(getClass().getResourceAsStream("/images/ordreMission.png"), 35, 35, false, false));
		bt_ordreMission.setGraphic(img);

		img = new ImageView(new Image(getClass().getResourceAsStream("/images/employe.png"), 35, 35, false, false));
		bt_employes.setGraphic(img);

		img = new ImageView(new Image(getClass().getResourceAsStream("/images/vehicule.png"), 35, 35, false, false));
		bt_vehicules.setGraphic(img);
	}

	// ***********************************************************************************
	
	@FXML
	void afficherOrdreMission() {
		try {
		modifier_style_bouton("ordre mission");
    	
    	AnchorPane pane =  FXMLLoader.load(getClass().getResource("/Views/ordreMissionMain.fxml"));
    	paneContenu.getChildren().setAll(pane);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@FXML
	void afficherEmployes() throws IOException{
		modifier_style_bouton("employes");
    	
    	AnchorPane pane =  FXMLLoader.load(getClass().getResource("/Views/employesMain.fxml"));
    	paneContenu.getChildren().setAll(pane);
	}

	@FXML
	void afficherVehicules() throws IOException{
		modifier_style_bouton("vehicules");
    	
    	AnchorPane pane =  FXMLLoader.load(getClass().getResource("/Views/vehiculesMain.fxml"));
    	paneContenu.getChildren().setAll(pane);
	}

	public void modifier_style_bouton(String nom_button) {
		bt_ordreMission.getStyleClass().remove("buttonSelected");
		bt_employes.getStyleClass().remove("buttonSelected");
		bt_vehicules.getStyleClass().remove("buttonSelected");
		
		bt_ordreMission.getStyleClass().add("buttonNotSelected");
		bt_employes.getStyleClass().add("buttonNotSelected");
		bt_vehicules.getStyleClass().add("buttonNotSelected");
		
		if(nom_button == "ordre mission") {
			bt_ordreMission.getStyleClass().add("buttonSelected");
		}else if (nom_button == "employes") {
			bt_employes.getStyleClass().add("buttonSelected");
		}else if (nom_button == "vehicules") {
			bt_vehicules.getStyleClass().add("buttonSelected");
		}
	}
	
	void nouveauClientProbel() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","wc_facturation","orcl1");
			String sql = "select * from article where id_client = 142 or id_client = 143 order by ID_ARTICLE_GLOBAL";
			PreparedStatement pst = conn.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			
			int id_art = 3659;
			int num_art_client = 49;
			while(rs.next()) {
				id_art++;
				
				String sql2 = "insert into article values (?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement pst2 = conn.prepareStatement(sql2);
				
				pst2.setInt(1, id_art);
				pst2.setInt(2, num_art_client);
				pst2.setInt(3, 149);
				pst2.setString(4, rs.getString(4));
				pst2.setString(5, rs.getString(5));
				pst2.setString(6, rs.getString(6));
				pst2.setString(7, rs.getString(7));
				pst2.setString(8, rs.getString(8));
				pst2.setFloat(9, rs.getFloat(9));
				pst2.setString(10, rs.getString(10));
				
				ResultSet rs2 = pst2.executeQuery();
				if(rs2.next()) {
					System.out.println("yes");
				}else{
					System.out.println("----------------No");
				}
			}
			conn.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
