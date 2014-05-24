package ea.ui;

import java.util.Random;

import ea.*;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;
import android.R;
import android.app.Activity;
import android.os.Bundle;

/*
 * TODO GameActivity soll abstract werden und als Basic Klasse für jedes Spiel gelten
 * 
*/
public class GameActivity extends Activity
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
        
        setTitle("Engine Alpha Android");
        setContentView(zeichner);
        
        Kreis k = new Kreis(200 , 180, 80);
        k.farbeSetzen(Farbe.Gruen);
        
        Rechteck r = new Rechteck(screenHeight / 2 - 240, screenHeight / 2 - 120, 120, 120);
        r.farbeSetzen(Farbe.Weiss);
        
        wurzel.add(k);
        wurzel.add(r);
        
    }
	
	// Diese Methode sollte in unterklassen ueberschrieben werden
	//public abstract void spielStart();
	
}
