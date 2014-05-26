package ea.ui;

import java.util.Random;

import ea.*;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/*
 * TODO GameActivity soll abstract werden und als Basic Klasse fuer jedes Spiel gelten
 * 
*/
@SuppressWarnings("serial")
public abstract class GameActivity extends Activity implements Ticker
{
	
	public Knoten wurzel;
		
	private Zeichner zeichner;	
	public Kamera cam;
	
	private Manager manager = new Manager();
	
	private final Random zufall = new Random();
	
	private static GameActivity instanz;
	
	public int breite;
	public int hoehe;

	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        breite = getWindowManager().getDefaultDisplay().getWidth();
        hoehe = getWindowManager().getDefaultDisplay().getHeight();
        
        cam = new Kamera(breite, hoehe, new Zeichenebene());
		zeichner = new Zeichner(this, this);
		zeichner.init(breite, hoehe, cam);
		
		cam.wurzel().add(wurzel = new Knoten());
        
        setContentView(zeichner);   
        
        manager.anmelden(this, 10);
        
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		init();
	}
	
	@Override
	protected void onDestroy()
	{
		if(wurzel != null)
			wurzel.leeren();
		
		super.onDestroy();
	}

	public static GameActivity get()
	{
		if(instanz != null)
			return instanz;
		return null;
	}
	
	public void titelSetzen(String titel)
	{
		setTitle(titel);
	}
	
	public void hintergrundFarbeSetzen(Farbe farbe)
	{
		if(zeichner != null)
			zeichner.hintergrundFarbeSetzen(farbe);
	}
	
	/**
	 *  Diese Methode sollte in unterklassen ueberschrieben werden
	 *  Es sollte immer super.init(); aufgerufen werden sonst kann es zu fehlern kommen
	 */
	protected void init()
	{
		instanz = this;
	}
	
	public void tick() 
	{
	}
	
	public void touch(MotionEvent event)
	{
		
	}
	
	public Zeichner zeichnerGeben()
	{
		return zeichner;
	}
	
	public boolean zufallsBoolean() 
	{
		return zufall.nextBoolean();
	}

	
	public int zufallsZahl(int obergrenze) 
	{
		if (obergrenze < 0) {
			System.err.println("Achtung!! Fuer eine Zufallszahl muss die definierte Obergrenze (die inklusiv in der Ergebnismenge ist) eine nichtnegative Zahl sein!!");
		}
		return zufall.nextInt(obergrenze + 1);
	}
	
}
