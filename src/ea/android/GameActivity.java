package ea.android;

import java.util.ArrayList;

import android.os.Bundle;
import ea.Farbe;
import ea.Knoten;
import ea.Raum;
import ea.ui.UIElement;

enum BildOrientierung
{
	Portrait,
	Landschaft
}
	
public abstract class GameActivity extends BasisActivity
{
	private static final long serialVersionUID = -4408577037715253942L;

	public Knoten wurzel;
		
	private static GameActivity instanz;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        cam.wurzel().add(wurzel = new Knoten());
        
        setContentView(zeichner);
        
        instanz = this;
		
		init();
	}
	
	public static GameActivity get()
	{
		return instanz;
	}
	
	public void hintergrundFarbeSetzen(Farbe farbe)
	{
		if(zeichner != null)
		{
			zeichner.hintergrundFarbeSetzen(farbe);
		}
	}
	
	public void hinzufuegen(Raum m)
	{
		if(!wurzel.besitzt(m))
		{
			wurzel.add(m);
		}
	}
	
	/**
	 *  Diese Methode muss in unterklassen ueberschrieben werden
	 */
	public abstract void init();

	@Override
	public ArrayList<UIElement> uiElemente() {
		ArrayList<UIElement> ui = new ArrayList<UIElement>();
		
		for(Raum u : wurzel.alleElemente())
		{
			if(u instanceof UIElement)
			{
				ui.add((UIElement)u);
			}
		}
		
		return ui;
	}
}
