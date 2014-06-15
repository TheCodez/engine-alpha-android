package ea.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import ea.Bild;
import ea.BoundingRechteck;
import ea.Farbe;
import ea.Manager;
import ea.Punkt;
import ea.Raum;
import ea.Text;
import ea.Ticker;
import ea.Vektor;
import ea.internal.collision.Collider;

public class Button extends Raum
{

	private ButtonAktion b;
	
	private boolean istGedrueckt;
	
	private Bild normalBild;
	private Bild gedruecktBild;
	
	private boolean hatText;
	private Text buttonText;
	
	public Button(float x, float y, ButtonDefinition bd)
	{
		super.position = new Punkt(x, y);
		
		if(bd != null && bd.istNichtNull())
		{
			normalBild = new Bild(x, y, bd.normal);
			gedruecktBild = new Bild(x, y, bd.gedrueckt);
			
			if(bd.text != "")
			{
				buttonText = new Text(position.x, position.y, bd.text);
				hatText = true;
			}
			
		}
		else
		{
			Log.e(getClass().getName(), "Buttondefinition ist unvollstaendig");
			buttonText.textSetzen("Button");
		}
		
	}
	
	public void touch(float x, float y, MotionEvent event)
	{
	    switch (event.getAction()) 
	    {
	    	case MotionEvent.ACTION_DOWN:
	    		if(this.dimension().istIn(new Punkt(x, y)))
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
	public void zeichnen(Canvas g, BoundingRechteck r) 
	{
		if(gedruecktBild.bild() != null && normalBild.bild() != null)
		{
			Paint p = new Paint();
			p.setAntiAlias(istGlatt());
			
			if(istGedrueckt)
			{
				g.drawBitmap(gedruecktBild.bild(), position.x, position.y, p);
			}
			else
			{
				g.drawBitmap(normalBild.bild(), position.x, position.y, p);
			}
			
			if(hatText)
			{
				buttonText.zeichnen(g, r);
			}
			
			//g.drawText(buttonText.textGeben(), position.x + (dimension().breite - buttonText.dimension().breite) / 2, position.y + 18, p );
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
