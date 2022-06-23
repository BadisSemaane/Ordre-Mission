package application;

public class employe {
	int id_emp;
	String code, nom,prenom,poste, status, tel;
	public employe(int id_emp, String nom, String prenom, String poste, String status, String tel) {
		super();
		this.id_emp = id_emp;
		this.nom = nom;
		this.prenom = prenom;
		this.poste = poste;
		this.status = status;
		this.tel = tel;
		if(id_emp<10) {
			this.code = "00"+id_emp;
		}else if(id_emp < 100) {
			this.code = "0"+id_emp;
		}else {
			this.code = ""+id_emp;
		}
	}
	public int getId_emp() {
		return id_emp;
	}
	public void setId_emp(int id_emp) {
		this.id_emp = id_emp;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getPoste() {
		return poste;
	}
	public void setPoste(String poste) {
		this.poste = poste;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	
	
}
