package ea.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import ea.Bild;
import ea.BoundingRechteck;
import ea.Farbe;
import ea.Manager;
import ea.Punkt;
import ea.Raum;
import ea.Ticker;
import ea.Vektor;
import ea.internal.collision.Collider;

public class Button extends Raum
{

	private ButtonAktion b;
	
	private boolean istGedrueckt;
	
	private Bild normalBild;
	private Bild gedruecktBild;
	
	public Button(float x, float y, ButtonDefinition bd)
	{
		super.position = new Punkt(x, y);
		
		if(bd != null && bd.istNichtNull())
		{
			normalBild = new Bild(x, y, bd.normal);
			gedruecktBild = new Bild(x, y, bd.gedrueckt);
		}
		else
		{
			
		}
		
	}
	
	public void touch(float x, float y, MotionEvent event)
	{
		BoundingRechteck br = new BoundingRechteck(x, y, 80, 80);

	    switch (event.getAction()) 
	    {
	    	case MotionEvent.ACTION_DOWN:
	    		if(br.schneidetBasic(this.dimension()))
	    		{
	    			istGedrueckt = true;
	    			if(b != null)
	    				b.knopfGedrueckt();
	    		}
	    		break;
	    	case MotionEvent.ACTION_UP:
	    		istGedrueckt = false;
	    		break;
	    }
	}
	
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
		if(gedruecktBild.bild() != null && normalBild.bild() != null)
		{
			if(istGedrueckt)
			{
				g.drawBitmap(gedruecktBild.bild(), position.x, position.y, new Paint());
			}
			else
			{
				g.drawBitmap(normalBild.bild(), position.x, position.y, new Paint());
			}
		}
			
	}
	
	@Override
	public void verschieben(Vektor v) 
	{
		super.verschieben(v);
		normalBild.verschieben(v);
		gedruecktBild.verschieben(v);
	}

	@Override
	public BoundingRechteck dimension() {
		return istGedrueckt? gedruecktBild.dimension() : normalBild.dimension();
	}
	
	public boolean istGedrueckt()
	{
		return istGedrueckt;
	}

	@Override
	public Collider erzeugeCollider() {
		return erzeugeLazyCollider();
	}

	public void setzeGedruecktEvent(ButtonAktion b) {
		this.b = b;
	}

}