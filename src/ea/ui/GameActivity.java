package ea.ui;

import java.util.Random;

import ea.*;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;
import android.R;
import android.app.Activity;
import android.os.Bundle;

/*
 * TODO GameActivity soll abstract werden und als Basic Klasse fuer jedes Spiel gelten
 * 
*/
public abstract class GameActivity extends Activity
{
	
	public Knoten wurzel;
		
	private Zeichner zeichner;
	
	public Kamera cam;
	
	@SuppressWarnings("unused")
	private final Random zufall = new Random();

	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        final int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        
        cam = new Kamera(screenWidth, screenHeight, new Zeichenebene());
		zeichner = new Zeichner(this);
		zeichner.init(screenWidth, screenHeight, cam);
		
		cam.wurzel().add(wurzel = new Knoten());
        
        setContentView(zeichner);   
        
        init();
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
	
	// Diese Methode sollte in unterklassen ueberschrieben werden
	protected abstract void init();
	
}
