package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class ordreMissionModifyController implements Initializable {

	@FXML
	private TextField tf_reference_am, tf_mission, tf_destination, tf_site, tf_recherche_emp;

	@FXML
	private DatePicker dp_depart, dp_retour, dp_dateCreation;

	@FXML
	private ImageView img_vehicule, img_ajouterEmp;

	@FXML
	private ComboBox<String> cb_client, cb_mat_selected, cb_type_veh, cb_marque_veh, cb_nom_veh;

	@FXML
	private TableView<employe> table_emp;

	@FXML
	private TableColumn<employe, ?> col_code, col_nom, col_prenom, col_poste;

	@FXML
	private Label label_ref_ordreMission, label_code_emp, label_nom_emp, label_prenom_emp, label_poste_emp;

	@FXML
	private Button bt_ajouter, bt_annule;

	// ************************************************************************************************************

	public static Stage primaryStage;
	public static ordreMission old_oredreMission;
	static int id_vehicule;
	static employe employe_a_rajouter;

	// ************************************************************************************************************

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initialiser_liste_clients();
		initialiser_type_veh();

		img_ajouterEmp.setImage(new Image(getClass().getResourceAsStream("/images/add_om.png")));

		recupere_infos();
		recuperer_Listemployes();
		remplirInfos();

		id_vehicule = old_oredreMission.getVehicule().getId_vehicule();
	}

	// ************************************************************************************************************

	void recupere_infos() {
		try {
			String sql = "select mission, destination, id_vehicule , date_creation from ordremission where id_om = ?";
			List<Object> stList = Arrays.asList(old_oredreMission.getId_om());
			ResultSet rs = functions.simple_sql(sql, stList);
			if (rs.next()) {
				old_oredreMission.setMission(rs.getString(1));
				old_oredreMission.setDestinatio(rs.getString(2));
				old_oredreMission.setDate_etabli(rs.getString(4));

				int id_vehicule = rs.getInt(3);
				sql = "select * from vehicule where id_vehicule = ?";
				stList = Arrays.asList(id_vehicule);
				ResultSet rs_veh = functions.simple_sql(sql, stList);
				if (rs_veh.next()) {
					vehicule v = new vehicule(0, rs_veh.getInt("id_vehicule"), rs_veh.getString("matricule"),
							rs_veh.getString("nom"), rs_veh.getString("marque"), rs_veh.getString("type"),
							rs_veh.getString("image_path"));
					old_oredreMission.setVehicule(v);
				}
			}
			functions.conn.close();
		} catch (Exception e) {
			System.out.println("recupere_infos :" + e.getMessage());
		}
	}

	// ***************
	void recuperer_Listemployes() {
		ObservableList<employe> ListEmp = FXCollections.observableArrayList();
		try {
			String sql = "select * from relation_om_emp where id_om = ? ";
			List<Object> stList = Arrays.asList(old_oredreMission.getId_om());
			ResultSet rs_relation = functions.simple_sql(sql, stList);

			while (rs_relation.next()) {
				sql = "select * from employe where id_emp = ?";
				stList = Arrays.asList(rs_relation.getInt("ID_EMP"));

				ResultSet rs = functions.simple_sql(sql, stList);
				if (rs.next()) {
					employe emp = new employe(rs.getInt("id_emp"), rs.getString("nom"), rs.getString("prenom"),
							rs.getString("poste"), rs.getString("status"), rs.getString("tel"));
					ListEmp.add(emp);
				}
			}
			functions.conn.close();
			old_oredreMission.setListEmployes(ListEmp);
		} catch (Exception e) {
			System.out.println("recuperer_Listemployes :" + e.getMessage());
		}
	}

	// ***************************

	void remplirInfos() {
		label_ref_ordreMission.setText("Ordre de mission N° " + old_oredreMission.getReference());
		tf_reference_am.setText(old_oredreMission.getReference());
		tf_mission.setText(old_oredreMission.getMission());
		cb_client.setValue(old_oredreMission.getClient());
		tf_destination.setText(old_oredreMission.getDestinatio());
		tf_site.setText(old_oredreMission.getSite());
		cb_type_veh.setValue(old_oredreMission.getVehicule().getType());
		cb_nom_veh.setValue(old_oredreMission.getVehicule().getNom());
		cb_marque_veh.setValue(old_oredreMission.getVehicule().getMarque());
		cb_mat_selected.setValue(old_oredreMission.getVehicule().getMatricule());

		img_vehicule.setImage(old_oredreMission.getVehicule().getPhoto().getImage());

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		dp_depart.setValue(LocalDate.parse(old_oredreMission.getDate_dep(), formatter));
		dp_retour.setValue(LocalDate.parse(old_oredreMission.getDate_ret(), formatter));
		dp_dateCreation.setValue(LocalDate.parse(old_oredreMission.getDate_etabli(), formatter));

		table_emp.getItems().clear();
		table_emp.getItems().addAll(old_oredreMission.getListEmployes());

		col_code.setCellValueFactory(new PropertyValueFactory<>("code"));
		col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		col_poste.setCellValueFactory(new PropertyValueFactory<>("poste"));
	}

	// ************************************************************************************************************

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
	// ************************************************************************************************************

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
	void ajouterOrdreMission() {
		String newReferenceOm = tf_reference_am.getText();

		if (verifierExistanceOM(newReferenceOm, old_oredreMission.getReference())) {
			functions.infoBox("La nouvelle référence de l'ordre mission existe déja ! veuillez la modifier S.V.P",
					"Référence existe déja", "ERROR");
		} else {
			if (verificationNewInformations()) {
				if (!supprimerOldOM(old_oredreMission.getId_om())) {
					functions.infoBox("Modification non effectuer", "Erreur de suppression Old_OM", "ERROR");
				} else {
					if (insertNewOM()) {
						functions.infoBox("Insertion avec succés", "Bonne route", "INFORMATION");
					} else {
						functions.infoBox("Echec d'insertion ! Veuillez vérifier les informatons S.V.P !", "Echec",
								"WARNING");
					}
				}
			}
		}
	}

	boolean verifierExistanceOM(String newReferenceOm, String oldRefrenceOm) {
		if (!newReferenceOm.equals(oldRefrenceOm)) {
			try {
				String sql = "select count(*) from ordremission where refrence = ? ";
				List<Object> stList = Arrays.asList(newReferenceOm);
				ResultSet rs = functions.simple_sql(sql, stList);

				if (rs.next()) {
					if (rs.getInt(1) > 0) {
						return true;
					}
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
		return false;
	}

	boolean verificationNewInformations() {
		boolean insert = true;
		String client = "";
		String date_depart = "";
		String date_retour = "";
		String date_creation = "";
		try {
			client = cb_client.getValue().toUpperCase();
			date_depart = dp_depart.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			date_retour = dp_retour.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			date_creation = dp_retour.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		} catch (Exception e) {
			insert = false;

		}
		String reference = tf_reference_am.getText();

		String destination = tf_destination.getText();
		String site = tf_site.getText();
		String mission = tf_mission.getText();

		int nbr_employe = table_emp.getItems().size();
		List<String> listVal = Arrays.asList(reference, destination, site, date_depart, date_retour, mission, client,
				date_creation);

		for (String value : listVal) {
			if (value == null || value.length() == 0) {
				insert = false;
			}
		}
		if (!insert) {
			functions.infoBox("Veuillez remplir toutes les informations S.V.P !", "Remplire les informations", "ERROR");
			return false;
		}
		if (id_vehicule < 0) {
			functions.infoBox("Veuillez choisir un véhicule S.V.P !", "Remplire les informations", "ERROR");
			return false;
		}
		if (nbr_employe < 1) {
			functions.infoBox("Veuillez choisir au moins un employé S.V.P !", "Remplire les informations", "ERROR");
			return false;
		}
		return true;
	}

	boolean supprimerOldOM(int id_om) {
		try {
			String sql = "delete from ordremission where id_om = ?";
			List<Object> stList = Arrays.asList(id_om);
			ResultSet rs = functions.simple_sql(sql, stList);
			if (!rs.next()) {
				return false;
			}
		} catch (Exception e) {
			System.out.println("executerModification : " + e.getMessage());
			return false;
		}
		return true;
	}

	boolean insertNewOM() {
		try {
			String reference = tf_reference_am.getText();

			String destination = tf_destination.getText();
			String site = tf_site.getText();
			String mission = tf_mission.getText();

			String client = cb_client.getValue().toUpperCase();
			String date_depart = dp_depart.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String date_retour = dp_retour.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String date_creation = dp_dateCreation.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

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
						if ( ! rs2.next()) {
							return false;
						}
					}
					functions.conn.close();
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			System.out.println("insertion OM :" + e.getMessage());
			return false;
		}
		return true;
	}

	@FXML
	void annulerOrdreMission() {
		primaryStage.close();
	}

	@FXML
	void rechercherEmpoye(KeyEvent event) {
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
	void supprimerEmploye(KeyEvent event) {
		if (event.getCode().equals(KeyCode.DELETE)) {
			if (table_emp.getSelectionModel().getSelectedItem() != null) {
				table_emp.getItems().remove(table_emp.getSelectionModel().getSelectedItem());
			}
		}
	}

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
