package ea.android;

import android.os.Bundle;
import ea.Knoten;
import ea.Raum;

enum BildOrientierung
{
	Portrait,
	Landschaft
}
	
public abstract class GameActivity extends BasisActivity
{
	private static final long serialVersionUID = -4408577037715253942L;

	public Knoten wurzel;
	public Knoten uiWurzel;
		
	private static GameActivity instanz;
	
	public boolean tick;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        cam.wurzel().add(wurzel = new Knoten());
		cam.wurzel().add(uiWurzel = new Knoten());
        
        setContentView(zeichner);
        
        instanz = this;
		
		init();
	}
	
	public static GameActivity get()
	{
		return instanz;
	}
	
	public void uiElementHinzufuegen(Raum m)
	{
		if(!uiWurzel.besitzt(m) && !wurzel.besitzt(m))
		{
			uiWurzel.add(m);
		}
	}
	
	public void raumHinzufuegen(Raum m)
	{
		if(!wurzel.besitzt(m) && !uiWurzel.besitzt(m))
		{
			wurzel.add(m);
		}
	}
	
	/**
	 *  Diese Methode muss in unterklassen ueberschrieben werden
	 */
	public abstract void init();
}
