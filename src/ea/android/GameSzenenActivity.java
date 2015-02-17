package ea.android;

import android.hardware.Sensor;
import android.os.Bundle;
import ea.Knoten;
import ea.Raum;
import ea.Szene;

public abstract class GameSzenenActivity extends BasisActivity
{
	private static final long serialVersionUID = -3983748994386821131L;

	private Szene aktuelleSzene;
	
	public Knoten wurzel;
	
	private static GameSzenenActivity instanz;
	
	public boolean tick;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
		instanz = this;
        
		aktuelleSzene = init();
		
		if(aktuelleSzene == null)
		{
			aktuelleSzene = new Szene();
		}
		
		cam.wurzel().add(aktuelleSzene.wurzel);
		
		
		//@todo wurzel von GameSzenenActvity entfernen
		for(Raum r : wurzel.alleElemente())
		{
			if(r != null)
				aktuelleSzene.hinzufuegen(r);
			wurzel.leeren();
		}
		
		setContentView(zeichner);
	}
	
	public void setzeSzene(Szene szene)
	{
		if(szene != null)
		{
			cam.wurzel().leeren();
			aktuelleSzene = szene;
			cam.wurzel().add(aktuelleSzene.wurzel);
		}	
	}
	
	public Szene szeneGeben()
	{
		return aktuelleSzene;
	}
	
	public static GameSzenenActivity get()
	{
		return instanz;
	}
	
	/**
	 *  Diese Methode muss in unterklassen ueberschrieben werden
	 */
	public abstract Szene init();
	
    public void tick()
    {
    	if(aktuelleSzene != null)
    	{
    		aktuelleSzene.tick();
    	}
    }
	
	public void touchReagieren(float x, float y, TouchEvent event)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.touchReagieren(x, y, event);
		}
	}
	
	public void sensorReagieren(float x, float y, float z, Sensor sensor)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.sensorReagieren(x, y, z, sensor);
		}
	}
	
	public void tasteGedruecktReagieren(int code)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.tasteGedruecktReagieren(code);
		}
	}
	
	public void tasteLosgelassenReagieren(int code)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.tasteLosgelassenReagieren(code);
		}
	}
}
