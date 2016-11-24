package UnitPackage;

import wargame.basic_types.Position;

public abstract class Unit implements IUnit {
	
	protected  Caracteristique caracteristique;
	protected boolean isActive;
	protected Position mapos;
	public Position getMapos() {
		return mapos;
	}

	public Caracteristique getCaracteristique() {
		return caracteristique;
	}

	public void setCaracteristique(Caracteristique caracteristique) {
		this.caracteristique = caracteristique;
	}

	public void setMapos(Position mapos) {
		this.mapos = mapos;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}




}
