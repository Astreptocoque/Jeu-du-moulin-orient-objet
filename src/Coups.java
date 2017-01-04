
public class Coups {
	
	int coupCaseDepart;
	int coupCaseArrivee;
	int valeur;
	
	public Coups(){
		this.coupCaseArrivee = 0;
		this.coupCaseDepart = 0;
	}
	
	public Coups(int coupCaseDepart,  int valeur, int coupCaseArrivee){
		this.coupCaseArrivee = coupCaseArrivee;
		this.coupCaseDepart = coupCaseDepart;
		this.valeur = valeur;
	}
	
	public Coups(int valeur, int coupCaseArrivee){
		this.coupCaseArrivee = coupCaseArrivee;
		this.coupCaseDepart = 0;
		this.valeur = valeur;
	}
	
	public void setCoupCaseDepart(int coupCaseDepart){
		this.coupCaseDepart = coupCaseDepart;
	}
	
	public void setCoupCaseArrivee(int coupCaseArrivee){
		this.coupCaseArrivee = coupCaseArrivee;
	}
	
	public void setValeur(int valeur){
		this.valeur = valeur;
	}
	
	public int getCoupCaseDepart(){
		return this.coupCaseDepart;
	}
	
	public int getCoupCaseArrivee(){
		return this.coupCaseArrivee;
	}
	
	public int getValeur(){
		return this.valeur;
	}
}

