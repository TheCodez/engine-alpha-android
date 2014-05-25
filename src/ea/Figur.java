package ea;

import java.util.ArrayList;

import android.graphics.Canvas;
import ea.internal.collision.Collider;

@SuppressWarnings("serial")
public class Figur extends Raum {

	private static ArrayList<Figur> figuren;
	private boolean spiegelX;
	private boolean spiegelY;
	
	static 
	{
		figuren = new ArrayList<Figur>();
		
		Manager.standard.anmelden((new Ticker() {
			int runde = 0;

			@Override
			public void tick() {
				runde++;
				try {
					/*
					for (Figur f : liste) {
						if (f.animiert()) {
							f.animationsSchritt(runde);
						}
					}
					*/
				} catch (java.util.ConcurrentModificationException e) {
					//
				}
			}
		}), 1);
	}
	
	public Figur(float x, float y, String verzeichnis)
	{
		super();
		super.position = new Punkt(x, y);
		figuren.add(this);
	}
	
	public Figur(String verzeichnis)
	{
		this(0, 0, verzeichnis);
	}
	
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
		// TODO Auto-generated method stub

	}

	@Override
	public BoundingRechteck dimension() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collider erzeugeCollider() {
		// TODO Auto-generated method stub
		return null;
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

}
