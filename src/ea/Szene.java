package ea;

import android.graphics.Canvas;
import android.hardware.Sensor;
import ea.android.GameSzenenActivity;
import ea.android.TouchEvent;
import ea.internal.collision.Collider;

@SuppressWarnings("serial")
public class Szene extends Raum
{
	public Knoten wurzel;
	private Farbe hintergrundFarbe = Farbe.Schwarz;
	
	private GameSzenenActivity game;
	
	public Szene() 
	{
		super.position = new Punkt(0, 0);
		game = GameSzenenActivity.get();
		
		wurzel = new Knoten();
	}
	
	public void hinzufuegen(Raum raum)
	{
		wurzel.add(raum);
		raum.setzeSzene(this);
	}
	
	public void entfernen(Raum raum)
	{
		wurzel.entfernen(raum);
	}
	
	public void leeren()
	{
		wurzel.leeren();
	}
	
	public void tick()
	{
		
	}
	
	public void touchReagieren(float x, float y, TouchEvent event)
	{
		
	}
	
	public void sensorReagieren(float x, float y, float z, Sensor sensor)
	{
		
	}
	
	public void tasteGedruecktReagieren(int code)
	{
		
	}
	
	public void tasteLosgelassenReagieren(int code)
	{
		
	}
	
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BoundingRechteck dimension() 
	{
		int breite = 0, hoehe = 0;
		
		for(Raum r : wurzel.alleElemente())
		{
			breite += r.dimension().breite;
			hoehe += r.dimension().hoehe;
		}
		return new BoundingRechteck(position.x, position.y, breite, hoehe);
	}

	@Override
	public Collider erzeugeCollider() 
	{
		return erzeugeLazyCollider();
	}
	
	public GameSzenenActivity game()
	{
		return game;
	}

	public Farbe hintergrundFarbeGeben() {
		return hintergrundFarbe;
	}

	public void hintergrundFarbeSetze(Farbe hintergrundFarbe) {
		this.hintergrundFarbe = hintergrundFarbe;
	}

}
