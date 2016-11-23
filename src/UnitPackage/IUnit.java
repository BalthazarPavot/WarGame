package UnitPackage;

public interface IUnit {
	public abstract void move();
	public abstract void makeDamage(Unit u);
	public abstract void makeheal(Unit u);
	public abstract void gainLife(int val);
	public abstract void takedamage(int val,int type );
}
