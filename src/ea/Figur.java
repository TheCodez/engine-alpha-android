package ea;

import java.util.ArrayList;

import android.graphics.Canvas;
import ea.internal.collision.BoxCollider;
import ea.internal.collision.Collider;
import ea.internal.collision.ColliderGroup;
import ea.internal.gra.FigurData;
import ea.internal.gra.PixelFeld;

public class Figur extends Raum {

	private static ArrayList<Figur> figuren;
	private boolean spiegelX;
	private boolean spiegelY;
	
	private PixelFeld[] animation;
	private int intervall = 100;
	private int aktuelle;
	
	private boolean animiert = true;
	
	static 
	{
		figuren = new ArrayList<Figur>();
		
		if(figuren.size() != 0)
		{
			Manager.standard.anmelden((new Ticker() {
				int runde = 0;
	
				@Override
				public void tick() {
					runde++;
					try {
						
						for (Figur f : figuren) {
							if (f.animiert()) {
								f.animationsSchritt(runde);
							}
						}
						
					} catch (java.util.ConcurrentModificationException e) {
					}
				}
			}), 1);
		}
	}
	
	public Figur(float x, float y, String verzeichnis)
	{
		super.position = new Punkt(x, y);

		animation = new PixelFeld[20];
		animation[0] = new PixelFeld(40, 40, 3);
		//this.animation = FigurData.figurEinlesen(verzeichnis).feld;
		
		figuren.add(this);
	}
	
	public Figur(String verzeichnis)
	{
		this(0, 0, verzeichnis);
	}
	
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
		animation[aktuelle].zeichnen(g, (int) (position.x - r.x), (int) (position.y - r.y), spiegelX, spiegelY);

	}

	@Override
	public BoundingRechteck dimension() {
		if (animation != null && animation[aktuelle] != null) {
			return new BoundingRechteck(position.x, position.y, animation[0].breite(), animation[0].hoehe());
		} else {
			return new BoundingRechteck(position.x, position.y, animation[aktuelle].breite(), animation[aktuelle].hoehe());
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

	public boolean spiegelXGeben() {
		return spiegelX;
	}

	public void setzeSpiegelX(boolean spiegelX) {
		this.spiegelX = spiegelX;
	}

	public boolean spiegelYGeben() {
		return spiegelY;
	}

	public void setzeSpiegelY(boolean spiegelY) {
		this.spiegelY = spiegelY;
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
		if (aktuelle == animation.length - 1) {
			aktuelle = 0;
		} else {
			aktuelle++;
		}
	}

}
