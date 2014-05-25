package ea;

import android.graphics.Canvas;
import ea.internal.collision.Collider;

@SuppressWarnings("serial")
public class Bild extends Raum {

	
	public Bild(String verzeichnis) {
		this(0, 0, verzeichnis);
	}
	
	public Bild(float x, float y, String verzeichnis) {
		super.position = new Punkt(x, y);
	}
	
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
		// TODO Auto-generated method stub

	}

	@Override
	public BoundingRechteck dimension() {
		return new BoundingRechteck(0, 0, 100, 100);
	}

	@Override
	public Collider erzeugeCollider() {
		return erzeugeLazyCollider();
	}
}
