package application;

import java.net.URL;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ordreMissionController implements Initializable {

	@FXML
	private ImageView img_refrech;

	@FXML
	private Button bt_new, bt_modify, bt_delete;

	@FXML
	private TableView<ordreMission> tableOrdreMission;

	@FXML
	private TableColumn<ordreMission, ?> col_num, col_ref, col_dateDep, col_dateRetour, col_client, col_site;

	@FXML
	private TextField tf_reference, tf_site;

	@FXML
	private ComboBox<String> cb_clients, cb_annee, cb_mois;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialiser_images();
		initialiserComboBox();
		recupererListeOrdreMission();
	}

	// *****************************************************************************************************************

	@FXML
	void modifierOrdreMission() {
		if (tableOrdreMission.getSelectionModel().getSelectedItem() != null) {
			ordreMission ordreMission = tableOrdreMission.getSelectionModel().getSelectedItem();
			ordreMissionModifyController.old_oredreMission = ordreMission;
			try {
				AnchorPane root = new AnchorPane();
				root = FXMLLoader.load(getClass().getResource("/Views/ordreMissionModify.fxml"));
				Scene scene = new Scene(root, 765, 700);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

				Stage primaryStage = new Stage();
				primaryStage.setTitle("Modification OM : " + ordreMission.getReference());
				// primaryStage.setResizable(false);
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				ordreMissionModifyController.primaryStage = primaryStage;
				primaryStage.show();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@FXML
	void nouveauOrdreMission() {
		try {
			AnchorPane root = new AnchorPane();
			root = FXMLLoader.load(getClass().getResource("/Views/ordreMissionNew.fxml"));
			Scene scene = new Scene(root, 765, 700);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Stage primaryStage = new Stage();
			primaryStage.setTitle("Nouvel ordre de mission");
			// primaryStage.setResizable(false);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			ordreMissionNewController.primaryStage = primaryStage;
			primaryStage.show();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	void supprimerOrdreMission() {
		ordreMission om = tableOrdreMission.getSelectionModel().getSelectedItem();
		if (om == null) {
			functions.infoBox("Aucun ordre de mission n'est selectionné pour etre supprimé", "NULL", "WARNING");
		} else {
			Alert alert = new Alert(AlertType.CONFIRMATION,
					"Voulez-Vous supprimer l'ordre de mission " + om.getReference() + " ?", ButtonType.YES,
					ButtonType.CANCEL);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.YES) {
				boolean est_supprime = supprimerOM_BDD(om.getId_om());

				if (est_supprime) {
					tableOrdreMission.getItems().remove(om);
					functions.infoBox(" Supression OM " + om.getReference() + " avec succés ! ", "Succes",
							"INFORMATION");
				} else {
					functions.infoBox(" Supression OM " + om.getReference() + " Non effectuée ! ", "ERREUR",
							"INFORMATION");
				}
			} else {
				functions.infoBox(" Supression OM " + om.getReference() + " Annulée ! ", "Annuler", "INFORMATION");
			}
		}
	}

	boolean supprimerOM_BDD(int id_om) {
		try {
			String sql = "delete from ordremission where id_om = ? ";
			List<Object> stList = Arrays.asList(id_om);

			ResultSet rs = functions.simple_sql(sql, stList);

			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@FXML
	void afficherOrdreMission(MouseEvent event) {
		if (event.getClickCount() == 2) {
			if (tableOrdreMission.getSelectionModel().getSelectedItem() != null) {
				ordreMission ordreMission = tableOrdreMission.getSelectionModel().getSelectedItem();
				ordreMissionShowController.this_oredreMission = ordreMission;
				try {
					AnchorPane root = new AnchorPane();
					root = FXMLLoader.load(getClass().getResource("/Views/ordreMissionShow.fxml"));
					Scene scene = new Scene(root, 600, 700);
					scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

					Stage primaryStage = new Stage();
					primaryStage.setTitle("Ordre de mission " + ordreMission.getReference());
					// primaryStage.setResizable(false);
					primaryStage.setResizable(false);
					primaryStage.setScene(scene);
					ordreMissionShowController.primaryStage = primaryStage;
					primaryStage.show();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}

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

	@FXML
	void recupererListeOrdreMission() {
		tableOrdreMission.getItems().clear();

		String reference = tf_reference.getText().length() == 0 ? "%" : tf_reference.getText() + "%";
		String client = cb_clients.getValue().equals("Tous...") ? "%" : "%" + cb_clients.getValue().toUpperCase() + "%";
		String site = tf_site.getText().length() == 0 ? "%" : "%" + tf_site.getText().toUpperCase() + "%";

		String annee = cb_annee.getValue().equals("Tous...") ? "%" : cb_annee.getValue();
		int mois_index = cb_mois.getSelectionModel().getSelectedIndex();
		String mois = "";
		if (mois_index == 0) {
			mois = "%";
		} else if (mois_index < 10) {
			mois = "0" + mois_index;
		} else {
			mois = "" + mois_index;
		}

		String date_recherche = "%/" + mois + "/" + annee;

		try {
			String sql = "select * from ordremission where refrence like ? and client like ? and upper(site) like ? and "
					+ "date_creation like ? order by id_om DESC";
			List<Object> stList = Arrays.asList(reference, client, site, date_recherche);
			ResultSet rs = functions.simple_sql(sql, stList);

			int num_om = 0;
			while (rs.next()) {
				num_om++;
				ordreMission om = new ordreMission(rs.getInt(1), num_om, rs.getString("refrence"),
						rs.getString("date_dep"), rs.getString("date_retour"), rs.getString("client"),
						rs.getString("site"));
				tableOrdreMission.getItems().add(om);
			}
		} catch (Exception e) {
			System.out.println("recupererListeOrdreMission => " + e.getMessage());
		}

		col_num.setCellValueFactory(new PropertyValueFactory<>("num_om"));
		col_ref.setCellValueFactory(new PropertyValueFactory<>("reference"));
		col_dateDep.setCellValueFactory(new PropertyValueFactory<>("date_dep"));
		col_dateRetour.setCellValueFactory(new PropertyValueFactory<>("date_ret"));
		col_client.setCellValueFactory(new PropertyValueFactory<>("client"));
		col_site.setCellValueFactory(new PropertyValueFactory<>("site"));
	}

	void initialiserComboBox() {

		cb_mois.getItems().clear();
		cb_mois.getItems().addAll("Tous...", "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout",
				"Septembre", "Octobre", "Novembre", "December");
		cb_mois.getSelectionModel().selectFirst();

		cb_clients.getItems().clear();
		cb_clients.getItems().add("Tous...");

		try {
			String sql = "select distinct client from ordreMission order by client";
			List<Object> stList = Arrays.asList();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_clients.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_clients.getSelectionModel().selectFirst();
		} catch (Exception e) {
			System.out.println("initialiserComboBox / clients : " + e.getMessage());
		}

		cb_annee.getItems().clear();
		cb_annee.getItems().add("Tous...");

		try {
			String sql = "select distinct annee from ordreMission order by annee";
			List<Object> stList = Arrays.asList();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_annee.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_annee.getSelectionModel().selectLast();
		} catch (Exception e) {
			System.out.println("initialiserComboBox / annee : " + e.getMessage());
		}

	}
}
