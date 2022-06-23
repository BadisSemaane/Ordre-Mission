package application;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class vehiculesMainController implements Initializable {

	@FXML
	private ImageView img_refrech;

	@FXML
	private TextField tf_matriculeVeh, tf_nom_Veh;

	@FXML
	private ComboBox<String> cb_marqueVeh, cb_typeVeh;

	@FXML
	private Button bt_new, bt_modify, bt_delete;

	@FXML
	private TableView<vehicule> tableVehicules;

	@FXML
	private TableColumn<vehicule, ?> col_numveh, col_photoVeh, col_matriculeVeh, col_nomVeh, col_marqueVeh, col_typeVeh;

//*********************************************************************************************

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialiser_images();
		initialiserComboBox();
		recuperer_liste_vehicules();
	}

//*********************************************************************************************

	public void initialiser_images() {
		img_refrech.setImage(new Image(getClass().getResourceAsStream("/images/search_om.png"), 50, 50, true, true));

		ImageView img = new ImageView(
				new Image(getClass().getResourceAsStream("/images/add_om.png"), 35, 35, true, true));
		bt_new.setGraphic(img);

		img = new ImageView(new Image(getClass().getResourceAsStream("/images/edit_om.png"), 35, 35, true, true));
		bt_modify.setGraphic(img);

		img = new ImageView(new Image(getClass().getResourceAsStream("/images/delete_om.png"), 35, 35, true, true));
		bt_delete.setGraphic(img);
	}

	public void recuperer_liste_vehicules() {
		tableVehicules.getItems().clear();
		try {
			String matricule = tf_matriculeVeh.getText().length() == 0 ? "%" : "%" + tf_matriculeVeh.getText() + "%";
			String nom = tf_nom_Veh.getText().length() == 0 ? "%" : "%" + tf_nom_Veh.getText().toUpperCase() + "%";
			String marque = cb_marqueVeh.getValue().equals("Tous...") ? "%" : "%" + cb_marqueVeh.getValue() + "%";
			String type = cb_typeVeh.getValue().equals("Tous...") ? "%" : "%" + cb_typeVeh.getValue() + "%";

			String sql = "select * from vehicule where matricule like ? and upper(nom) like ? and marque like ? and type like ? order by id_vehicule";
			List<Object> stList = Arrays.asList(matricule, nom, marque, type);

			ResultSet rs = functions.simple_sql(sql, stList);
			int num_table = 0;
			while (rs.next()) {
				num_table++;
				vehicule v = new vehicule(num_table, rs.getInt("id_vehicule"), rs.getString("matricule"),
						rs.getString("nom"), rs.getString("marque"), rs.getString("type"), rs.getString("image_path"));
				tableVehicules.getItems().add(v);
			}
			functions.conn.close();
		} catch (Exception e) {
			System.out.println("recuperer_liste_vehicules :" + e.getMessage());
		}

		col_numveh.setCellValueFactory(new PropertyValueFactory<>("num_table"));
		col_photoVeh.setCellValueFactory(new PropertyValueFactory<>("photo"));
		col_matriculeVeh.setCellValueFactory(new PropertyValueFactory<>("matricule"));
		col_nomVeh.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_marqueVeh.setCellValueFactory(new PropertyValueFactory<>("marque"));
		col_typeVeh.setCellValueFactory(new PropertyValueFactory<>("type"));

	}

	public void initialiserComboBox() {
		cb_marqueVeh.getItems().clear();
		cb_typeVeh.getItems().clear();
		cb_marqueVeh.getItems().add("Tous...");
		cb_typeVeh.getItems().add("Tous...");

		try {
			String sql = "select unique(marque) from vehicule order by marque";
			List<Object> stList = new ArrayList<Object>();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_marqueVeh.getItems().add(rs.getString(1));
			}

			sql = "select unique(type) from vehicule order by type";
			rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_typeVeh.getItems().add(rs.getString(1));
			}
			functions.conn.close();

			cb_marqueVeh.getSelectionModel().selectFirst();
			cb_typeVeh.getSelectionModel().selectFirst();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

//*********************************************************************************************

	@FXML
	void modifierVehicule() {
		vehicule veh = tableVehicules.getSelectionModel().getSelectedItem();

		if (veh != null) {
			try {
				vehiculeModifyController.vehicule = veh;

				AnchorPane root = new AnchorPane();
				root = FXMLLoader.load(getClass().getResource("/Views/vehiculesModify.fxml"));
				Scene scene = new Scene(root, 400, 500);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

				Stage primaryStage = new Stage();
				primaryStage.setTitle("Modification véhicule");
				// primaryStage.setResizable(false);
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				vehiculeModifyController.primaryStage = primaryStage;
				primaryStage.show();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			functions.infoBox("Veuillez selectionner un véhicule à modifier S.V.P !", "NULL", "WARNNING");
		}
	}

	@FXML
	void nouveauVehicule() {
		try {
			AnchorPane root = new AnchorPane();
			root = FXMLLoader.load(getClass().getResource("/Views/vehiculesNew.fxml"));
			Scene scene = new Scene(root, 400, 500);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Stage primaryStage = new Stage();
			primaryStage.setTitle("Nouveau véhicule");
			// primaryStage.setResizable(false);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			vehiculeNewController.primaryStage = primaryStage;
			primaryStage.show();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	void supprimerVehicule() throws SQLException {
		vehicule vehicule = tableVehicules.getSelectionModel().getSelectedItem();

		if (vehicule == null) {
			functions.infoBox("Aucun véhicule selectionner pour supprimer", "NULL Selection", "WARNING");
		} else {
			try {
				String sql = "delete from vehicule where id_vehicule = ? ";
				List<Object> stList = Arrays.asList(vehicule.getId_vehicule());
				ResultSet rs = functions.simple_sql(sql, stList);
				if (rs.next()) {
					functions.infoBox("Suppression avec succés", "Succés", "INFORMATION");
					recuperer_liste_vehicules();
				} else {
					functions.infoBox("Impossible de supprimerce véhicule", "Ecchec de suppression", "INFORMATION");
				}
			} catch (Exception e) {
				functions.infoBox("Impossible de supprimer car ce véhicule apartient à un ou plusieurs Ordre de mission ! "
						+ "Veuillez supprimer ces ordre de mission à fin de supprimer ce véhicule", "Erreur : "+e.getMessage(), "ERROR");
			}
		}
	}
}
