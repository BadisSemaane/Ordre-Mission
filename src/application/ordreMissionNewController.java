package application;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ordreMissionNewController implements Initializable {

	@FXML
	private ComboBox<String> cb_client, cb_mat_selected, cb_type_veh, cb_marque_veh, cb_nom_veh;

	@FXML
	private TextField tf_destination, tf_site, tf_recherche_emp, tf_reference_am, tf_mission;

	@FXML
	private DatePicker dp_depart, dp_retour, dp_dateCreation;

	@FXML
	private ImageView img_vehicule, img_ajouterEmp;

	@FXML
	private TableView<employe> table_emp;

	@FXML
	private TableColumn<employe, ?> col_code, col_nom, col_prenom, col_poste;

	@FXML
	private Button bt_ajouter, bt_annule;

	@FXML
	private Label label_code_emp, label_nom_emp, label_prenom_emp, label_poste_emp;

	// ***********************************************************************************************************

	static employe employe_a_rajouter;
	static int id_vehicule = -999;
	public static Stage primaryStage;

	// ***********************************************************************************************************

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LocalDate date = LocalDate.now();
		dp_dateCreation.setValue(date);
		
		initialiser_reference();
		initialiser_liste_clients();
		tf_destination.setText("Alger - - Alger");
		
		initialiser_type_veh();
		type_selected();
		marque_selected();
		nom_selected();
		matricule_selected();
		img_ajouterEmp.setImage(new Image(getClass().getResourceAsStream("/images/add_om.png")));
	}

	// ***********************************************************************************************************

	void initialiser_reference() {
		try {
			int anneeActuelle = LocalDate.now().getYear();
			int num_annee = 1;
			String sql = "select max(num_annee) from ordreMission where annee = ? order by num_annee";
			List<Object> stList = Arrays.asList(anneeActuelle);
			ResultSet rs = functions.simple_sql(sql, stList);
			if (rs.next()) {
				num_annee = rs.getInt(1) + 1;
			}
			tf_reference_am.setText(num_annee + "/" + anneeActuelle);
			functions.conn.close();
		} catch (Exception e) {
			System.out.println("initialiser_reference : " + e.getMessage());
		}

	}

	void initialiser_liste_clients() {
		cb_client.getItems().clear();
		try {
			String sql = "select distinct client from ordreMission order by client";
			List<Object> stList = Arrays.asList();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_client.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_client.getSelectionModel().selectFirst();
		} catch (Exception e) {
			System.out.println("initialiser_reference : " + e.getMessage());
		}
	}

	void initialiser_type_veh() {
		cb_type_veh.getItems().clear();
		try {
			String sql = "select distinct type from vehicule order by type";
			List<Object> stList = Arrays.asList();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_type_veh.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_type_veh.getSelectionModel().selectFirst();
		} catch (Exception e) {
			System.out.println("initialiser_reference : " + e.getMessage());
		}
	}

	// ***********************************************************************************************************

	@FXML
	void type_selected() {
		try {
			cb_marque_veh.getItems().clear();
			String type = cb_type_veh.getValue();
			String sql = "select distinct marque from vehicule where type = ? order by marque";
			List<Object> stList = Arrays.asList(type);
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_marque_veh.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_marque_veh.getSelectionModel().selectFirst();
		} catch (Exception e) {
			System.out.println("type_selected :" + e.getMessage());
		}
	}

	@FXML
	void marque_selected() {

		try {
			cb_nom_veh.getItems().clear();
			String marque = cb_marque_veh.getValue();
			String sql = "select distinct nom from vehicule where marque = ? order by nom";
			List<Object> stList = Arrays.asList(marque);
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_nom_veh.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_nom_veh.getSelectionModel().selectFirst();
		} catch (Exception e) {
			System.out.println("marque_selected :" + e.getMessage());
		}
	}

	@FXML
	void nom_selected() {
		try {
			cb_mat_selected.getItems().clear();
			String nom = cb_nom_veh.getValue();
			String sql = "select distinct matricule from vehicule where nom = ? order by matricule";
			List<Object> stList = Arrays.asList(nom);
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_mat_selected.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_mat_selected.getSelectionModel().selectFirst();
		} catch (Exception e) {
			System.out.println("nom_selected :" + e.getMessage());
		}
	}

	@FXML
	void matricule_selected() {
		try {
			Image img = new Image(getClass().getResourceAsStream("/images/noPicFound.jpg"));
			String matricule = cb_mat_selected.getValue();
			String sql = "select id_vehicule , image_path from vehicule where matricule = ? ";
			List<Object> stList = Arrays.asList(matricule);
			ResultSet rs = functions.simple_sql(sql, stList);
			if (rs.next()) {
				id_vehicule = rs.getInt(1);
				if (rs.getString(2).length() > 10) {
					img = new Image(rs.getString(2));
				}
			} else {
				id_vehicule = -999;
			}
			functions.conn.close();
			img_vehicule.setImage(img);
		} catch (Exception e) {
			System.out.println("matricule_selected :" + e.getMessage());
		}
	}

	// ***********************************************************************************************************
	@FXML
	void rechercherEmpoye() {
		try {
			String nom = tf_recherche_emp.getText().toUpperCase() + "%";
			String sql = "select id_emp, nom, prenom , poste from employe where nom like ? or prenom like ? ";
			List<Object> stList = Arrays.asList(nom, nom);
			ResultSet rs = functions.simple_sql(sql, stList);
			if (rs.next()) {
				employe_a_rajouter = new employe(rs.getInt("id_emp"), rs.getString("nom"), rs.getString("prenom"),
						rs.getString("poste"), "", "");
			} else {
				employe_a_rajouter = null;
			}
			if (employe_a_rajouter != null) {
				label_code_emp.setText(employe_a_rajouter.getCode());
				label_nom_emp.setText(employe_a_rajouter.getNom());
				label_prenom_emp.setText(employe_a_rajouter.getPrenom());
				label_poste_emp.setText(employe_a_rajouter.getPoste());
			}
		} catch (Exception e) {
			System.out.println("rechercherEmpoye :" + e.getMessage());
		}
	}

	@FXML
	void ajouterEmp() {
		if (employe_a_rajouter != null) {
			boolean exist = false;
			for (employe emp : table_emp.getItems()) {
				if (emp.getCode().equals(employe_a_rajouter.getCode())) {
					exist = true;
					break;
				}
			}
			if (exist) {
				functions.infoBox("Employé existe déja", "Impossible de rajouter l'empyé", "WARNING");
			} else {
				table_emp.getItems().add(employe_a_rajouter);
				initialiser_rechecheEmp();
			}
		}
	}

	@FXML
	void supprimerEmploye(KeyEvent event) {
		if (event.getCode().equals(KeyCode.DELETE)) {
			if (table_emp.getSelectionModel().getSelectedItem() != null) {
				table_emp.getItems().remove(table_emp.getSelectionModel().getSelectedItem());
			}
		}
	}

	// ***********************************************************************************************************

	@FXML
	void ajouterOrdreMission() {
		boolean insert = true;

		String client = "";
		String date_depart = "";
		String date_retour = "";
		String date_creation = "";
		try {
			client = cb_client.getValue().toUpperCase();
			date_depart = dp_depart.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			date_retour = dp_retour.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			date_creation = dp_dateCreation.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception e) {
			insert = false;
			functions.infoBox("Veuillez remplir le nom du client, date départ, date retour et Date ordre mission S.V.P !",
					"Remplire les informations", "ERROR");
		}
		String reference = tf_reference_am.getText();

		String destination = tf_destination.getText();
		String site = tf_site.getText();
		String mission = tf_mission.getText();

		int nbr_employe = table_emp.getItems().size();
		List<String> listVal = Arrays.asList(reference, destination, site, date_depart, date_retour, mission, client,date_creation);

		for (String value : listVal) {
			if (value == null || value.length() == 0) {
				insert = false;
				break;
			}
		}
		if (!insert) {
			functions.infoBox("Veuillez remplir toutes les informations S.V.P !", "Remplire les informations", "ERROR");
		}
		if (id_vehicule < 0) {
			insert = false;
			functions.infoBox("Veuillez choisir un véhicule S.V.P !", "Remplire les informations", "ERROR");
		}
		if (nbr_employe < 1) {
			insert = false;
			functions.infoBox("Veuillez choisir au moins un employé S.V.P !", "Remplire les informations", "ERROR");
		}

		// Arrays.asList(reference,client,destination,site,date_depart,date_retour);
		/*
		 * ID_OM REFRENCE ANNEE NUM_ANNEE MISSION CLIENT SITE DESTINATION DATE_DEP
		 * DATE_RETOUR ID_VEHICULE
		 */
		// 0 1 2 3 4 5 6 7 8 9 10
		if (insert) {
			try {
				String[] ref = reference.split("/");
				List<Object> listValues = Arrays.asList(1, reference, Integer.valueOf(ref[1]), Integer.valueOf(ref[0]),
						mission, client, site, destination, date_depart, date_retour, id_vehicule, date_creation);

				String sql = "insert into ordreMission values (?,?,?,?,?,?,?,?,?,?,?,?)";
				ResultSet rs = functions.simple_sql(sql, listValues);
				if (rs.next()) {
					// recuperer lidentiend de lordre de mission

					sql = "select id_om from ordremission where refrence = ? ";
					listValues = Arrays.asList(reference);
					ResultSet rs_id_om = functions.simple_sql(sql, listValues);

					if (rs_id_om.next()) {
						String id_om = rs_id_om.getString(1);
						for (employe emp : table_emp.getItems()) {
							sql = "insert into relation_om_emp values (?,?,?)";
							listValues = Arrays.asList(1, id_om, emp.getId_emp());
							ResultSet rs2 = functions.simple_sql(sql, listValues);
							if (rs2.next()) {
								System.out.println("employé " + emp.getNom() + " ajouté avec succée");
							} else {
								System.out.println("employé " + emp.getNom() + " non ajouté");
							}
						}
						functions.conn.close();
						primaryStage.close();
						functions.infoBox("Insertion avec succés", "Bonne route", "INFORMATION");
					} else {
						functions.infoBox("Echec d'insertion ! Veuillez vérifier les informatons S.V.P !", "Echec",
								"WARNING");
					}
				}
			} catch (Exception e) {
				System.out.println("insertion OM :" + e.getMessage());
			}
		}
	}

	@FXML
	void annulerOrdreMission() {
		primaryStage.close();
	}

	void initialiser_rechecheEmp() {
		employe_a_rajouter = null;
		tf_recherche_emp.clear();
		label_code_emp.setText("");
		label_nom_emp.setText("");
		label_poste_emp.setText("");
		label_prenom_emp.setText("");

		col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
		col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		col_poste.setCellValueFactory(new PropertyValueFactory<>("poste"));

	}
}
