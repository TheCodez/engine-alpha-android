package ea.test;

import ea.*;
import ea.ui.GameActivity;

public class TestActivity extends GameActivity 
{

	@Override
	public void init() 
	{
		super.init();
		
        Rechteck box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Gruen);
        
        wurzel.add(box);
	}
}