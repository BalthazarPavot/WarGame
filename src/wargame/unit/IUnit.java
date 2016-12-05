package wargame.unit;

public interface IUnit {

	public abstract boolean inflictDamage(Unit u);

	public abstract boolean heal(Unit val);

	public abstract void gainLife(int val);

	public abstract boolean takePercingDamages(int val);

	public abstract boolean takeBluntDamages(int val);

	public abstract boolean takeMagicDamages(int val);

	public abstract boolean takeSlachingDamages(int val);

	public abstract boolean canFly();
}
