package wargame.unit;

public class Caracteristique {

	private int pvMax;
	private int pv;

	private boolean flying;

	private double atkSlashing = 1.0;
	private double defSlashing = 1.0;

	private double atkPercing = 1.0;
	private double defPercing = 1.0;

	private double atkBlunt = 1.0;
	private double defBlunt = 1.0;

	private double atkMagic = 1.0;
	private double defMagic = 1.0;

	private int range = 1;
	private int sigh=3;
	private int nbCaseDep = 2;

	public Caracteristique(int pvmax, double atkSlaching, double defSlaching, double atkBlunt, double defBlunt,
			double atkPercing, double defPercing, double atkMagic, double defMagic) {

		flying = false;

		this.pvMax = pvmax;
		pv = pvmax;

		this.atkPercing = atkPercing;
		this.defPercing = defPercing;

		this.atkBlunt = atkBlunt;
		this.defBlunt = defBlunt;

		this.atkMagic = atkMagic;
		this.defMagic = defMagic;

		this.atkSlashing = atkSlaching;
		this.defSlashing = defSlaching;

	}



	public int getSigh() {
		return sigh;
	}

	public void setSigh(int sigh) {
		this.sigh = sigh;
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

	public int getPvMax() {
		return pvMax;
	}

	public void setPvMax(int pvMax) {
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
