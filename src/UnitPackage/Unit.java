package UnitPackage;

public abstract class Unit implements IUnit {
	
	protected  Caracteristique maCara;
	protected boolean isActive;

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
		maCara = maCara;
	}


}
