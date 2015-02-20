package ea.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import ea.Bild;
import ea.BoundingRechteck;
import ea.Punkt;
import ea.Text;
import ea.Vektor;
import ea.internal.collision.Collider;
import ea.internal.util.Logger;

public class Button extends UIElement
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
				buttonText = new Text(mittelPunkt().x - 82, mittelPunkt().y, bd.text, 82);
				hatText = true;
			}
		}
		else
		{
			Logger.error("Buttondefinition ist unvollstaendig");
		}
		
	}
	
	@Override
	public void touch(float x, float y, MotionEvent event)
	{
		if(sichtbar())
		{
		
		    switch (event.getAction()) 
		    {
		    	case MotionEvent.ACTION_DOWN:
		    		if(this.dimension().istIn(new Punkt(x, y)))
		    		{
		    			istGedrueckt = true;
		    			if(b != null)
		    				b.knopfGedrueckt(this);
		    		}
		    		break;
		    	case MotionEvent.ACTION_UP:
		    		istGedrueckt = false;
		    		break;
		    }
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
			
			Paint pt = new Paint();
			pt.setTextSize(buttonText.groesseGeben());
			
			if(hatText)
			{
				//g.drawText(buttonText.textGeben(), mittelPunkt().x + (dimension().breite - buttonText.dimension().breite) / 2, mittelPunkt().y + 18, pt );
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
