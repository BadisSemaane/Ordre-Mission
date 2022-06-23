package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class ordreMissionShowController implements Initializable{

	@FXML
    private Label label_ref_ordreMission;

    @FXML
    private TextField tf_mission ,tf_nom ,tf_destination , tf_site ,tf_typeVeh , tf_nomVeh, tf_marqueVeh  ,tf_matriculeVeh;

    @FXML
    private DatePicker dp_depart ,dp_retour;

    @FXML
    private ImageView img_vehicule, img_siganture;


    @FXML
    private TableView<employe> table_emp;

    @FXML
    private TableColumn<employe, ?> col_code,col_nom, col_prenom, col_poste;

    @FXML
    private Button bt_imprimer ,bt_annule;

    public static Stage primaryStage;
    
    static ordreMission this_oredreMission ;
    static String pathSiganture = null;
    
//************************************************************************************************************
    @Override
	public void initialize(URL location, ResourceBundle resources) {
    	recupere_infos();
    	recuperer_Listemployes();
    	remplirInfos();
    	img_siganture.setImage(new Image(getClass().getResourceAsStream("/images/add_image.png")));
	}
  
//************************************************************************************************************

    void recupere_infos() {
    	try {
    		String sql = "select mission, destination, id_vehicule , date_creation from ordremission where id_om = ?";
    		List<Object> stList = Arrays.asList(this_oredreMission.getId_om());
    		ResultSet rs = functions.simple_sql(sql, stList);
    		if(rs.next()) {
    			this_oredreMission.setMission(rs.getString(1));
    			this_oredreMission.setDestinatio(rs.getString(2));
    			this_oredreMission.setDate_etabli(rs.getString(4));
    			
    			int id_vehicule = rs.getInt(3);
    			sql = "select * from vehicule where id_vehicule = ?";
    			stList = Arrays.asList(id_vehicule);
    			ResultSet rs_veh = functions.simple_sql(sql, stList);
    			if(rs_veh.next()) {
	    			vehicule v = new vehicule(0,rs_veh.getInt("id_vehicule"),rs_veh.getString("matricule"), rs_veh.getString("nom"),
	    					rs_veh.getString("marque"),	rs_veh.getString("type"),rs_veh.getString("image_path") );
	    			this_oredreMission.setVehicule(v);
    			}
    		}
    		functions.conn.close();
    	}catch (Exception e) {
			System.out.println("recupere_infos :"+e.getMessage());
		}
    }
    
    void remplirInfos() {
    	label_ref_ordreMission.setText("Ordre de mission N° "+this_oredreMission.getReference());
    	tf_mission.setText(this_oredreMission.getMission());
    	tf_nom.setText(this_oredreMission.getClient());
    	tf_destination.setText(this_oredreMission.getDestinatio());
    	tf_site.setText(this_oredreMission.getSite());
    	tf_typeVeh.setText(this_oredreMission.getVehicule().getType());
    	tf_nomVeh.setText(this_oredreMission.getVehicule().getNom());
    	tf_marqueVeh.setText(this_oredreMission.getVehicule().getMarque());
    	tf_matriculeVeh.setText(this_oredreMission.getVehicule().getMatricule());
    	
    	img_vehicule.setImage(this_oredreMission.getVehicule().getPhoto().getImage());
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	
    	dp_depart.setValue(LocalDate.parse(this_oredreMission.getDate_dep(), formatter));
    	dp_retour.setValue(LocalDate.parse(this_oredreMission.getDate_ret(), formatter));
    	
    	table_emp.getItems().clear();
    	table_emp.getItems().addAll(this_oredreMission.getListEmployes());
    	
    	col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
		col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		col_poste.setCellValueFactory(new PropertyValueFactory<>("poste"));
    }
    
    void recuperer_Listemployes() {
    	ObservableList<employe> ListEmp = FXCollections.observableArrayList();
    	try {
    		String sql = "select * from relation_om_emp where id_om = ? ";
    		List<Object> stList = Arrays.asList(this_oredreMission.getId_om());
    		ResultSet rs_relation= functions.simple_sql(sql, stList);
    		
    		while (rs_relation.next()) {
    			sql = "select * from employe where id_emp = ?";
    			stList = Arrays.asList(rs_relation.getInt("ID_EMP"));
    			
    			ResultSet rs = functions.simple_sql(sql, stList);
    			if(rs.next()) {
    				employe emp = new employe(rs.getInt("id_emp"), rs.getString("nom"), rs.getString("prenom"), rs.getString("poste"), 
						rs.getString("status"), rs.getString("tel") );
    				ListEmp.add(emp);
    			}
    		}
    		functions.conn.close();
    		this_oredreMission.setListEmployes(ListEmp);
    	}catch (Exception e) {
			System.out.println("recuperer_Listemployes :"+e.getMessage());
		}
    }
//************************************************************************************************************
   
    @FXML
    void annulerOrdreMission() {
    	primaryStage.close();
    }

    @FXML
    void imprimerOrdreMission() {
    	impressionPDF.ordreMission = this_oredreMission;
    	impressionPDF.pathSiganture = pathSiganture;
    	impressionPDF.imprimer_OrdreMission();
    	bt_imprimer.setDisable(true);
    }

    @FXML
    void selectionnerSignature() throws MalformedURLException {
    	if(pathSiganture == null ) {
    		final FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Veuillez choisir une image depuis ce dossier SVP");
			File defaultDirectory = new File("C:\\application_ordreMission\\images\\signatures");
			fileChooser.setInitialDirectory(defaultDirectory);
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
			File image_selectionne = fileChooser.showOpenDialog(null);

			if (image_selectionne.exists()) {
				File file = new File(image_selectionne.getPath());
				// --> file:/C:/MyImages/myphoto.jpg
				pathSiganture = file.toURI().toURL().toString();
				img_siganture.setImage(new Image(pathSiganture));

				//String img_name = file.getName();
			} else {
				pathSiganture = null;
				img_siganture.setImage(new Image(getClass().getResourceAsStream("/images/add_image.png")));
			}
    	}else {
    		pathSiganture = null;
    		img_siganture.setImage(new Image(getClass().getResourceAsStream("/images/add_image.png")));
    	}
    }
}
