package application;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class vehiculeModifyController implements Initializable {

	@FXML
	private TextField tf_matricule, tf_nom;

	@FXML
	private ComboBox<String> cb_marque, cb_type;

	@FXML
	private ImageView img_photo;

	@FXML
	private Button bt_ajouter;

	static Stage primaryStage;
	static String path_photo;
//*************************************************************************************

	public static vehicule vehicule;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialiserImages();
		initialiserComboBox();
		initialiserInfosVehicule();
	}

//*************************************************************************************
	public void initialiserImages() {
		img_photo.setImage(new Image(getClass().getResourceAsStream("/images/add_image.png"), 100, 100, true, true));
		bt_ajouter.setGraphic(
				new ImageView(new Image(getClass().getResourceAsStream("/images/valider.png"), 35, 35, true, true)));
	}

	public void initialiserComboBox() {
		cb_marque.getItems().clear();
		cb_type.getItems().clear();
		try {
			String sql = "select unique(marque) from vehicule order by marque";
			List<Object> stList = new ArrayList<Object>();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_marque.getItems().add(rs.getString(1));
			}

			sql = "select unique(type) from vehicule order by type";
			rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_type.getItems().add(rs.getString(1));
			}
			functions.conn.close();

			cb_marque.getSelectionModel().selectFirst();
			cb_type.getSelectionModel().selectFirst();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	// *************************************************************************************

	@FXML
	void validerAjout() {
		String matricule = "";
		String nom = "";
		String marque = "";
		String type = "";
		try {
			matricule = tf_matricule.getText();
			nom = tf_nom.getText().toUpperCase();
			marque = cb_marque.getValue().toUpperCase();
			type = cb_type.getValue().toUpperCase();
		} catch (Exception e) {
			functions.infoBox("Erreur! Veuillez remplir tous les informations SVP!", "Erreur", "ERROR");
		}
		if (matricule.length() != 12 && matricule.length() != 13) {
			functions.infoBox("Erreur! Veuillez respecter la forme du matricule \"00000 122 16\" SVP!",
					"Erreur Matricule", "ERROR");
		} else {
			if (nom.length() == 0) {
				functions.infoBox("Erreur! Veuillez remplir le nom SVP!", "Erreur", "ERROR");
			} else {
				if (path_photo == null) {
					functions.infoBox("Erreur! Veuillez choisir une photo SVP!", "Erreur", "ERROR");
				} else {
					try {
						String sql_update = "update vehicule set matricule = ? , nom =  ? , marque = ? , type = ? , "
								+ "image_path = ? where id_vehicule = ?";
						List<Object> stList = Arrays.asList(matricule, nom, marque, type, path_photo,
								vehicule.getId_vehicule());
						ResultSet rs = functions.simple_sql(sql_update, stList);
						if (rs.next()) {
							primaryStage.close();
							functions.infoBox("Modification avec succés", "Succés", "INFORMATION");
							
						} else {
							functions.infoBox("Modification non effectuée", "Erreur", "ERROR");
						}
					} catch (Exception e) {
						System.out.println("validerAjout :" + e.getMessage());
					}
				}
			}
		}
	}

	@FXML
	void ajouter_supprimer_photo(MouseEvent event) {
		if (path_photo != null) {
			path_photo = null;
			img_photo.setImage(
					new Image(getClass().getResourceAsStream("/images/add_image.png"), 100, 100, true, true));
		} else {
			try {
				final FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Veuillez choisir une photo depuis ce dossier SVP");
				File defaultDirectory = new File("C:\\application_ordreMission\\photosVehicules\\");
				fileChooser.setInitialDirectory(defaultDirectory);
				fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg"));
				File image_selectionne = fileChooser.showOpenDialog(null);
				if (image_selectionne.exists()) {
					File file = new File(image_selectionne.getPath());
					path_photo = file.toURI().toURL().toString();
					img_photo.setImage(new Image(path_photo));
				}
			} catch (MalformedURLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	void initialiserInfosVehicule() {
		tf_matricule.setText(vehicule.getMatricule());
		tf_nom.setText(vehicule.getNom());
		cb_marque.getSelectionModel().select(vehicule.getMarque());
		cb_type.getSelectionModel().select(vehicule.getType());

		path_photo = vehicule.getPath_photo();
		if (vehicule.getPhoto() != null) {
			img_photo.setImage(vehicule.getPhoto().getImage());
		}
	}
}
