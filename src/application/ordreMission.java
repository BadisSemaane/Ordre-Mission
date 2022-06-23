package application;

import javafx.collections.ObservableList;

public class ordreMission {
	int id_om ;
	int num_om; // num dans la table
	String reference, date_dep,date_ret, client,site,date_etabli;
	String mission,destinatio;
	vehicule vehicule;
	ObservableList<employe> listEmployes;
	
	public ordreMission(int id_om ,int num_om, String reference, String date_dep, String date_ret, String client, String site) {
		super();
		this.id_om = id_om;
		this.num_om = num_om;
		this.reference = reference;
		this.date_dep = date_dep;
		this.date_ret = date_ret;
		this.client = client;
		this.site = site;
	}
	
	
	public String getDate_etabli() {
		return date_etabli;
	}


	public void setDate_etabli(String date_etabli) {
		this.date_etabli = date_etabli;
	}


	public int getId_om() {
		return id_om;
	}

	public void setId_om(int id_om) {
		this.id_om = id_om;
	}

	public int getNum_om() {
		return num_om;
	}
	public void setNum_om(int num_om) {
		this.num_om = num_om;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getDate_dep() {
		return date_dep;
	}
	public void setDate_dep(String date_dep) {
		this.date_dep = date_dep;
	}
	public String getDate_ret() {
		return date_ret;
	}
	public void setDate_ret(String date_ret) {
		this.date_ret = date_ret;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getMission() {
		return mission;
	}
	public void setMission(String mission) {
		this.mission = mission;
	}
	public String getDestinatio() {
		return destinatio;
	}
	public void setDestinatio(String destinatio) {
		this.destinatio = destinatio;
	}
	public vehicule getVehicule() {
		return vehicule;
	}
	public void setVehicule(vehicule vehicule) {
		this.vehicule = vehicule;
	}
	public ObservableList<employe> getListEmployes() {
		return listEmployes;
	}
	public void setListEmployes(ObservableList<employe> listEmployes) {
		this.listEmployes = listEmployes;
	}
	
}
