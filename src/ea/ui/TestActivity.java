package ea.ui;

import ea.*;

public class TestActivity extends GameActivity 
{

	@Override
	public void init() 
	{
        Rechteck box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Gruen);
        
        wurzel.add(box);
		
	}
}
