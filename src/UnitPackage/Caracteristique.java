package UnitPackage;

public class Caracteristique {
	
	private static int pvMax;
	private int pv;
	
	private boolean flying;
	
	private int atkSlashing;
	private int defSlashing;
	
	private int atkPercing;
	private int defPercing;
	
	private int atkBlunt;
	private int defBlunt;
	
	private int atkMagic;
	private int defMagic;
	
	private int range;
	
	
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
		
		pvMax=pv=100;
		
		range=1;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getAtkMagic() {
		return atkMagic;
	}

	public void setAtkMagic(int atkMagic) {
		this.atkMagic = atkMagic;
	}

	public int getDefMagic() {
		return defMagic;
	}

	public void setDefMagic(int defMagic) {
		this.defMagic = defMagic;
	}

	public static int getPvMax() {
		return pvMax;
	}

	public static void setPvMax(int pvMax) {
		Caracteristique.pvMax = pvMax;
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

	public int getAtkSlashing() {
		return atkSlashing;
	}

	public void setAtkSlashing(int atkSlashing) {
		this.atkSlashing = atkSlashing;
	}

	public int getDefSlashing() {
		return defSlashing;
	}

	public void setDefSlashing(int defSlashing) {
		this.defSlashing = defSlashing;
	}

	public int getAtkPercing() {
		return atkPercing;
	}

	public void setAtkPercing(int atkPercing) {
		this.atkPercing = atkPercing;
	}

	public int getDefPercing() {
		return defPercing;
	}

	public void setDefPercing(int defPercing) {
		this.defPercing = defPercing;
	}

	public int getAtkBlunt() {
		return atkBlunt;
	}

	public void setAtkBlunt(int atkBlunt) {
		this.atkBlunt = atkBlunt;
	}

	public int getDefBlunt() {
		return defBlunt;
	}

	public void setDefBlunt(int defBlunt) {
		this.defBlunt = defBlunt;
	}
	
}
