package application;

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
import javafx.stage.Stage;

public class employesNewController implements Initializable {

	@FXML
	private TextField tf_nom, tf_tel, tf_prenom;

	@FXML
	private ComboBox<String> cb_fonction;

	@FXML
	private Button bt_valider;

	static Stage primaryStage;
//**************************************************************************************************
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initialiserImages();
		initialiserComboBox();
	}

//*************************************************************************************************

	@FXML
	void validerInsertion() {
		String nom = "";
		String prenom = "";
		String poste = "";
		String status = "En Fonction";
		String tel = "";
		try {
			nom = tf_nom.getText().toUpperCase();
			prenom = tf_prenom.getText().toUpperCase();
			poste =cb_fonction.getValue().toUpperCase();
			status = "En Fonction";
			tel = tf_tel.getText();
		} catch (Exception e) {
			functions.infoBox("Erreur! Veuillez remplir tous les informations SVP!", "Erreur", "ERROR");
		}
		if (nom.length() == 0 || prenom.length() == 0 || poste.length() == 0 || status.length() == 0  || tel.length() == 0) {
			functions.infoBox("Erreur! Veuillez remplir tous les informations SVP!", "Erreur", "ERROR");
		} else {
			try {
				String sql_insert = "insert into employe values(?,?,?,?,?,?)";
				List<Object> stList = Arrays.asList(1, nom, prenom, poste, status, tel);
				ResultSet rs = functions.simple_sql(sql_insert, stList);
				if (rs.next()) {
					primaryStage.close();
					functions.infoBox("Insertion avec succés", "Succés", "INFORMATION");
				} else {
					functions.infoBox("Insertion non effectuée", "Erreur", "ERROR");
				}
			} catch (Exception e) {
				System.out.println("validerAjout :" + e.getMessage());
				functions.infoBox("Insertion non effectuée", e.getMessage(), "ERROR");
			}
		}
	}

	public void initialiserImages() {
		bt_valider.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/valider.png"), 35, 35, true, true)));
	}

	public void initialiserComboBox() {
		cb_fonction.getItems().clear();
		try {
			String sql = "select unique(poste) from employe order by poste";
			List<Object> stList = new ArrayList<Object>();
			ResultSet rs = functions.simple_sql(sql, stList);
			while (rs.next()) {
				cb_fonction.getItems().add(rs.getString(1));
			}
			functions.conn.close();
			cb_fonction.getSelectionModel().selectFirst();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
