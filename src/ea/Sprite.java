package ea;

import java.util.ArrayList;

import android.graphics.Canvas;
import ea.internal.collision.BoxCollider;
import ea.internal.collision.Collider;
import ea.internal.collision.ColliderGroup;

public class Sprite extends Raum
{
	private static ArrayList<Sprite> sprites;
	private int intervall = 100;
	private int aktuelle;
	private Bild[] bilder;
	
	private boolean animiert = true;
	
	static 
	{
		sprites = new ArrayList<Sprite>();
		
		Manager.standard.anmelden((new Ticker() {
			int runde = 0;

			@Override
			public void tick() {
				runde++;
				try {
					
					for (Sprite s : sprites) {
						if (s.animiert()) {
							s.animationsSchritt(runde);
						}
					}
					
				} catch (java.util.ConcurrentModificationException e) {
				}
			}
		}), 1);
	}
	
	public Sprite(float x, float y, String ...bilder)
	{
		super.position = new Punkt(x, y);
		
		this.bilder = new Bild[bilder.length];
		
		animiert = bilder.length > 1;
		
		for(int i = 0; i < bilder.length; i++)
		{
			this.bilder[i] = new Bild(x, y, bilder[i]);
		}
	}
	
	public Sprite(String ...bilder)
	{
		this(0, 0, bilder);
	}

	public boolean animiert()
	{
		return animiert;
	}
	
	public void animationsSchritt(int runde)
	{
		if (runde % intervall  != 0) {
			return;
		}
		if (aktuelle == bilder.length - 1) {
			aktuelle = 0;
		} else {
			aktuelle++;
		}
	}
	
	public void intervallSetzen(int intervall)
	{
		this.intervall = intervall;
	}
	
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
		bilder[aktuelle].zeichnen(g, r);

	}

	@Override
	public BoundingRechteck dimension() {
		if (bilder != null && bilder[aktuelle] != null) {
			return new BoundingRechteck(position.x, position.y, bilder[0].dimension().breite, bilder[0].dimension().hoehe);
		} else {
			return new BoundingRechteck(position.x, position.y, bilder[aktuelle].dimension().breite, bilder[aktuelle].dimension().hoehe);
		}
	}

	@Override
	public Collider erzeugeCollider() {
		ColliderGroup gc = new ColliderGroup();
		for(BoundingRechteck r : flaechen()) {
			gc.addCollider(BoxCollider.fromBoundingRechteck(new Vektor(r.x, r.y), r));
		}
		return gc;
	}
}
