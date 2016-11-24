package UnitPackage;

import wargame.basic_types.Position;

public abstract class Unit implements IUnit {
	
	protected  Caracteristique maCara;
	protected boolean isActive;
	protected Position mapos;
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public  Caracteristique getMaCara() {
		return maCara;
	}

	public  void setMaCara(Caracteristique maCara) {
		this.maCara = maCara;
	}


}
