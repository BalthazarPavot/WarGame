package UnitPackage;

public class Caracteristique {
	
	private int pvMax;
	private int pv;
	
	private boolean flying;
	
	private double atkSlashing;
	private double defSlashing;
	
	private double atkPercing;
	private double defPercing;
	
	private double atkBlunt;
	private double defBlunt;
	
	private double atkMagic;
	private double defMagic;
	
	private int range;
	private int nbCaseDep;
	
	Caracteristique()
	{
		flying=false;
		
		atkBlunt=1;
		atkPercing=1;
		atkSlashing=1;
		atkMagic=1;
		
		defBlunt=1;
		defPercing=1;
		defSlashing=1;
		defMagic=1;
		
		pvMax=100;
		pv=100;
		
		range=1;
		nbCaseDep=2;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public double getAtkMagic() {
		return atkMagic;
	}

	public int getNbCaseDep() {
		return nbCaseDep;
	}

	public void setNbCaseDep(int nbCaseDep) {
		this.nbCaseDep = nbCaseDep;
	}

	public void setAtkMagic(double atkMagic) {
		this.atkMagic = atkMagic;
	}

	public double getDefMagic() {
		return defMagic;
	}

	public void setDefMagic(double defMagic) {
		this.defMagic = defMagic;
	}

	public  int getPvMax() {
		return pvMax;
	}

	public  void setPvMax(int pvMax) {
		this.pvMax = pvMax;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public boolean isFlying() {
		return flying;
	}

	public void setFlying(boolean flying) {
		this.flying = flying;
	}

	public double getAtkSlashing() {
		return atkSlashing;
	}

	public void setAtkSlashing(double atkSlashing) {
		this.atkSlashing = atkSlashing;
	}

	public double getDefSlashing() {
		return defSlashing;
	}

	public void setDefSlashing(double defSlashing) {
		this.defSlashing = defSlashing;
	}

	public double getAtkPercing() {
		return atkPercing;
	}

	public void setAtkPercing(double atkPercing) {
		this.atkPercing = atkPercing;
	}

	public double getDefPercing() {
		return defPercing;
	}

	public void setDefPercing(double defPercing) {
		this.defPercing = defPercing;
	}

	public double getAtkBlunt() {
		return atkBlunt;
	}

	public void setAtkBlunt(double atkBlunt) {
		this.atkBlunt = atkBlunt;
	}

	public double getDefBlunt() {
		return defBlunt;
	}

	public void setDefBlunt(double defBlunt) {
		this.defBlunt = defBlunt;
	}
	
}
