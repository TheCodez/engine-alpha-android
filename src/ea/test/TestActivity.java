package ea.test;

import android.view.MotionEvent;
import ea.*;
import ea.ui.GameActivity;

public class TestActivity extends GameActivity 
{
	private Rechteck box;
	private Bild bild;
	private Text f;
	
	private Vektor verschiebung = new Vektor(140, 140);
	
	@Override
	public void init() 
	{
		super.init();
		
		hintergrundFarbeSetzen(Farbe.HimmelBlau);
		
        box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Weiss);
        
        bild = new Bild(140, 140, "logo.png");
        
        Text t = new Text(160, 550, "Test");
        t.setzeGroesse(80);
        t.unterstrichenSetzen(true);
        
        f = new Text(120, 640, "Farbig!!");
        f.farbeSetzen(Farbe.Gruen);
        f.setzeGroesse(80);
        
        wurzel.add(box);
        wurzel.add(bild);
        wurzel.add(t);
        wurzel.add(f);
	}
        
	@Override
	public void tick()
	{
		bild.positionSetzen(verschiebung.x, verschiebung.y);
	}
	
	@Override
	public void touch(MotionEvent event)
	{
		float eventX = event.getX();
	    float eventY = event.getY();
	    BoundingRechteck r = new BoundingRechteck(eventX, eventY, 180, 180);
	    BoundingRechteck tr = new BoundingRechteck(eventX, eventY, 80, 80);

	    switch (event.getAction()) 
	    {
	    	case MotionEvent.ACTION_DOWN:
	    		eventX = event.getX();
	    	    eventY = event.getY();
	    		if(tr.schneidetBasic(f.dimension()))
	    			f.farbeSetzen(Farbe.Rot);	
	    		break;
	    	case MotionEvent.ACTION_UP:
    			f.farbeSetzen(Farbe.Gruen);	
	    		break;
	    	case MotionEvent.ACTION_MOVE:
	    		eventX = event.getX();
	    	    eventY = event.getY();
	    		if(r.schneidetBasic(bild.dimension()))
	    			verschiebung = new Vektor(event.getX(), event.getY());
	    		break;
	    }
	}
}