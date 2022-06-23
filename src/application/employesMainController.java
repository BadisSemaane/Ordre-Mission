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

public class employesMainController implements Initializable {

	@FXML
	private ImageView img_refrech;

	@FXML
	private TextField tf_codeEmp, tf_nomPrenomEmp;

	@FXML
	private ComboBox<String> cb_posteEmp, cb_statusEmp;

	@FXML
	private Button bt_new, bt_modify, bt_delete;

	@FXML
	private TableView<employe> tableEmployes;

	@FXML
	private TableColumn<employe, ?> col_codeEmp, col_nomEmp, col_prenomEmp, col_posteEmp, col_statusEmp, col_telEmp;

//*********************************************************************************************

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialiser_images();
		initialiserComboBox();
		recupererListeEmpoles();
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

//*********************************************************************************************

	public void initialiserComboBox() {
		cb_statusEmp.getItems().clear();
		cb_statusEmp.getItems().add("Tous...");
		cb_statusEmp.getItems().add("En Fonction");
		cb_statusEmp.getItems().add("En Arret");
		cb_statusEmp.getSelectionModel().select(1);

		cb_posteEmp.getItems().clear();
		cb_posteEmp.getItems().add("Tous...");
		try {
			String sql = "select unique(poste) from employe order by poste";
			List<Object> stList = new ArrayList<Object>();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_posteEmp.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_posteEmp.getSelectionModel().selectFirst();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void recupererListeEmpoles() {
		tableEmployes.getItems().clear();
		try {
			String id_emp = tf_codeEmp.getText().length() == 0 ? "%"
					: "%" + Integer.valueOf(tf_codeEmp.getText()) + "%";
			String nom_prenom = tf_nomPrenomEmp.getText().length() == 0 ? "%"
					: "%" + tf_nomPrenomEmp.getText().toUpperCase() + "%";
			String poste = cb_posteEmp.getValue().equals("Tous...") ? "%"
					: "%" + cb_posteEmp.getValue().toUpperCase() + "%";
			String status = cb_statusEmp.getValue().equals("Tous...") ? "%" : cb_statusEmp.getValue();

			String sql = "select * from employe where id_emp like ? and ( nom like ? or prenom like ? ) and poste like ? and status like ? ";
			List<Object> stList = Arrays.asList(id_emp, nom_prenom, nom_prenom, poste, status);

			ResultSet rs = functions.simple_sql(sql, stList);

			while (rs.next()) {
				employe emp = new employe(rs.getInt("id_emp"), rs.getString("nom"), rs.getString("prenom"),
						rs.getString("poste"), rs.getString("status"), rs.getString("tel"));
				tableEmployes.getItems().add(emp);
			}
			functions.conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		col_codeEmp.setCellValueFactory(new PropertyValueFactory<>("code"));
		col_nomEmp.setCellValueFactory(new PropertyValueFactory<>("nom"));
		col_prenomEmp.setCellValueFactory(new PropertyValueFactory<>("prenom"));
		col_posteEmp.setCellValueFactory(new PropertyValueFactory<>("poste"));
		col_statusEmp.setCellValueFactory(new PropertyValueFactory<>("status"));
		col_telEmp.setCellValueFactory(new PropertyValueFactory<>("tel"));
	}

//*********************************************************************************************

	@FXML
	void modifierEmploye() {
		employe emp = tableEmployes.getSelectionModel().getSelectedItem();
		if (emp != null) {
			try {
				employeModifyController.emp = emp;
				AnchorPane root = new AnchorPane();
				root = FXMLLoader.load(getClass().getResource("/Views/employesModify.fxml"));
				Scene scene = new Scene(root, 400, 350);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

				Stage primaryStage = new Stage();
				primaryStage.setTitle("Modification employé : " + emp.getNom() + " " + emp.getPrenom());
				primaryStage.setResizable(false);
				primaryStage.setScene(scene);
				employeModifyController.primaryStage = primaryStage;
				primaryStage.show();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			functions.infoBox("Veuillez selection un employé S.V.P !", "Aucun employé selectionné", "WARNING");
		}
	}

	@FXML
	void nouveauEmploye() {
		try {
			AnchorPane root = new AnchorPane();
			root = FXMLLoader.load(getClass().getResource("/Views/employesNew.fxml"));
			Scene scene = new Scene(root, 400, 350);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			Stage primaryStage = new Stage();
			primaryStage.setTitle("Nouvel employé");
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			employesNewController.primaryStage = primaryStage;
			primaryStage.show();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@FXML
	void supprimerEmploye() {
		employe emp = tableEmployes.getSelectionModel().getSelectedItem();
		if (emp != null) {
			try {
				String sql = "delete from employe where id_emp = ?";
				List<Object> stList = Arrays.asList(emp.getId_emp());
				ResultSet rs = functions.simple_sql(sql, stList);

				if (rs.next()) {
					functions.infoBox("Employé : "+emp.getNom() +  " " + emp.getPrenom() +" Supprimé avec succés !" ,"Employé Supprimeé", "INFORMATION");
					recupererListeEmpoles();
				}else {
					functions.infoBox("Employé : "+emp.getNom() +  " " + emp.getPrenom() +" non supprimé !" ,"Ereur Suppréssion", "ERROR");
				}
			} catch (Exception e) {
				System.out.println("erreur supprression :"+e.getMessage());
			}
		} else {
			functions.infoBox("Veuillez selectionner un employé à supprimé S.V.P !" ,"NULL", "warning");
		}
	}

}
