package UnitPackage;

public interface IUnit {
	
	public abstract void makeDamage(Unit u);
	public abstract void makeheal(Unit u);
	public abstract void gainLife(int val);
	public abstract void takeDamagePercing(int val);
	public abstract void takeDamageBlunt(int val);
	public abstract void takeDamageMagic(int val);
	public abstract void takeDamageSlaching(int val);
}
