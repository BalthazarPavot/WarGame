package UnitPackage;

public class Soldier extends Terrestre {
	
	public Soldier()
	{
		this.getMaCara().setAtkBlunt(0);
		this.getMaCara().setAtkMagic(0);
		this.getMaCara().setAtkSlashing(1.5);
		
		this.getMaCara().setDefBlunt(0.5);
		this.getMaCara().setDefPercing(1.5);
		
		
	}
	

}
