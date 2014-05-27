package ea.test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.view.MotionEvent;
import ea.*;
import ea.ui.GameActivity;

public class TestActivity extends GameActivity 
{
	private Rechteck box;
	private Bild bild;
	private Text text;
		
	private float verschX = 0;
	
	@Override
	public void init() 
	{
		hintergrundFarbeSetzen(Farbe.HimmelBlau);
		
        box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Weiss);
        
        bild = new Bild(140, 140, "logo.png");
        
        text = new Text(120, 640, "Farbig!!");
        text.farbeSetzen(Farbe.Gruen);
        text.setzeGroesse(80);
        
        wurzel.add(box);
        wurzel.add(bild);
        wurzel.add(text);
    }
        
	@Override
	public void tick()
	{
		bild.verschieben(new Vektor(verschX, 0));
	}
	
	@Override
	public void touch(float x, float y, MotionEvent event)
	{
	    BoundingRechteck tr = new BoundingRechteck(x, y, 80, 80);

	    switch (event.getAction()) 
	    {
	    	case MotionEvent.ACTION_DOWN:
	    		if(tr.schneidetBasic(text.dimension()))
	    			text.farbeSetzen(Farbe.Rot);	
	    		break;
	    	case MotionEvent.ACTION_UP:
	    		text.farbeSetzen(Farbe.Gruen);	
	    		break;
	    }
	}
	
	public void sensorBewegung(float x, float y, float z, Sensor sensor)
	{
		float xWert = Math.round(x);
		text.textSetzen(String.valueOf(xWert));
		
		verschX = -xWert;
	}
}