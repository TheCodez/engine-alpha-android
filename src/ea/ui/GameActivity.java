package ea.ui;

import java.util.Random;

import ea.*;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;
import android.R;
import android.app.Activity;
import android.os.Bundle;

/* TODO!!
 * Hauptklasse fuer Android Anwendungen jede App sollte hiervon erben.
 * 
*/
public class GameActivity extends Activity
{
	
	public Knoten wurzel;
	
	@SuppressWarnings("unused")
	private Knoten superWurzel;
	
	private Zeichner zeichner;
	
	public Kamera cam;
	
	//private final Random zufall = new Random();

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        final int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        
        cam = new Kamera(screenWidth, screenHeight, new Zeichenebene());
		zeichner = new Zeichner(this);
		zeichner.init(screenWidth, screenHeight, cam);
		
		wurzel = new Knoten();
		cam.wurzel().add(wurzel);
        
        setTitle("Engine Alpha Android");
        setContentView(zeichner);
        
        wurzel.add(new Kreis(50 , 120, 40));
        wurzel.add(new Rechteck(100, 250, 120, 120));
        
    }
	
}
