package ea.test;

import ea.*;
import ea.ui.GameActivity;

public class TestActivity extends GameActivity 
{
	private Rechteck box;
	
	@Override
	public void init() 
	{
		super.init();
		
		hintergrundFarbeSetzen(Farbe.HimmelBlau);
		
        box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Weiss);
        
        wurzel.add(box);
	}
	
	@Override
	public void tick()
	{
		box.verschieben(5, 0);
	}
}