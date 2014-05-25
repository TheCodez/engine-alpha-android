package ea.test;

import ea.*;
import ea.ui.GameActivity;

public class TestActivity extends GameActivity 
{
	private Rechteck box;
	private Bild bild;
	
	@Override
	public void init() 
	{
		super.init();
		
		hintergrundFarbeSetzen(Farbe.HimmelBlau);
		
        box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Weiss);
        
        bild = new Bild(140, 140, "logo.png");
        
        wurzel.add(box);
        wurzel.add(bild);
	}
        
	@Override
	public void tick()
	{
		bild.verschieben(1, 0);
	}
}