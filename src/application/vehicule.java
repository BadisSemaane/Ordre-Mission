package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class vehicule {
	
	int id_vehicule, num_table;
	String matricule, nom, marque, type, path_photo;
	ImageView photo;
	
	public vehicule(int num_table , int id_vehicule, String matricule, String nom, String marque, String type, String path_photo) {
		super();
		this.num_table = num_table;
		this.id_vehicule = id_vehicule;
		this.matricule = matricule;
		this.nom = nom;
		this.marque = marque;
		this.type = type;
		if(path_photo == null) {
			this.photo = null;
		}else {
			this.photo = new ImageView(new Image(path_photo,100,100,true,true));
		}
		this.path_photo = path_photo;
	}
	
	public String getPath_photo() {
		return path_photo;
	}

	public void setPath_photo(String path_photo) {
		this.path_photo = path_photo;
	}

	public int getNum_table() {
		return num_table;
	}


	public void setNum_table(int num_table) {
		this.num_table = num_table;
	}


	public int getId_vehicule() {
		return id_vehicule;
	}
	public void setId_vehicule(int id_vehicule) {
		this.id_vehicule = id_vehicule;
	}
	public String getMatricule() {
		return matricule;
	}
	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getMarque() {
		return marque;
	}
	public void setMarque(String marque) {
		this.marque = marque;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ImageView getPhoto() {
		return photo;
	}
	public void setPhoto(ImageView photo) {
		this.photo = photo;
	}

}
