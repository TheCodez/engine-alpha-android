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
	public Knoten uiWurzel;
		
	private static GameActivity instanz;
	
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
	
	public void hintergrundFarbeSetzen(Farbe farbe)
	{
		if(zeichner != null)
		{
			zeichner.hintergrundFarbeSetzen(farbe);
		}
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

	@Override
	public ArrayList<UIElement> uiElemente() {
		ArrayList<UIElement> ui = new ArrayList<UIElement>();
		
		for(Raum u : uiWurzel.alleElemente())
		{
			if(u instanceof UIElement)
			{
				ui.add((UIElement)u);
			}
		}
		
		return ui;
	}
}
