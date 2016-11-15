package UnitPackage;

public class test {

	public static void main()
	{
		Healer h;
		Soldier s;
		
		h=new Healer();
		s=new Soldier();
		
		System.out.print("vie du soldat: "+s.maCara.getPv()+"\n");
		h.makedamage(s);
		System.out.print("vie du soldat apres combat :"+s.maCara.getPv()+"\n");
		h.makeheal(s);
		System.out.print("vie du soldat apres soin :"+s.maCara.getPv()+"\n");
	}
}
