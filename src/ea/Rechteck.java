/*
 * Engine Alpha ist eine anfaengerorientierte 2D-Gaming Engine.
 * 
 * Copyright (C) 2011 Michael Andonie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ea;


import android.graphics.Canvas;
import android.graphics.Paint;
import ea.internal.collision.Collider;

/**
 * Beschreiben Sie hier die Klasse Rechteck.
 * 
 * @author Michael Andonie
 */
@SuppressWarnings("serial")
public class Rechteck extends Geometrie {
	/**
	 * Die Laenge
	 */
	private float hoehe;
	
	/**
	 * Die Breite
	 */
	private float breite;
	
	/**
	 * Konstruktor fuer Objekte der Klasse Rechteck
	 * 
	 * @param x
	 *            Die X Position (Koordinate der linken oberen Ecke) des Rechtecks
	 * @param y
	 *            Die X Position (Koordinate der linken oberen Ecke) des Rechtecks
	 * @param breite
	 *            Die Breite des Rechtecks
	 * @param hoehe
	 *            Die hoehe des Rechtecks
	 */
	public Rechteck(float x, float y, float breite, float hoehe) {
		super(x, y);
		this.breite = breite;
		this.hoehe = hoehe;
		aktualisierenFirst();
	}
	
	/**
	 * Setzt beide Masse feur dieses Rechteck neu.
	 * 
	 * @param breite
	 *            Die neue Breite des Rechtecks
	 * @param hoehe
	 *            Die neue Hoehe des Rechtecks
	 */
	public void masseSetzen(int breite, int hoehe) {
		this.breite = breite;
		this.hoehe = hoehe;
		aktualisieren();
	}
	
	/**
	 * Setzt die Breite fuer dieses Rechteck neu.
	 * 
	 * @param breite
	 *            Die neue Breite des Rechtecks
	 * @see #hoeheSetzen(int)
	 */
	public void breiteSetzen(int breite) {
		this.breite = breite;
		aktualisieren();
	}
	
	/**
	 * Setzt die Hoehe fuer dieses Rechteck neu.
	 * 
	 * @param hoehe
	 *            Die neue Hoehe des Rechtecks
	 * @see #breiteSetzen(int)
	 */
	public void hoeheSetzen(int hoehe) {
		this.hoehe = hoehe;
		aktualisieren();
	}

	
	/**
	 * Zeichnet das Objekt.
	 * 
	 * @param g
	 *            Das zeichnende Graphics-Objekt
	 * @param r
	 *            Das BoundingRechteck, dass die Kameraperspektive Repraesentiert.<br />
	 *            Hierbei soll zunaechst getestet werden, ob das Objekt innerhalb der Kamera liegt, und erst dann gezeichnet werden.
	 */
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
	
		Paint p = new Paint();
		p.setColor(farbe.alsInt());//(super.formen()[0].getColor());
		
		//if (!r.schneidetBasic(this.dimension()))
		//	return;
		
		g.drawRect(position.x - r.x, position.y - r.y, position.x + breite, position.y + hoehe, p);
	}
	
	/**
	 * {@inheritDoc}
	 * Collider wird direkt aus dem das <code>Raum</code>-Objekt umfassenden <code>BoundingRechteck</code>
	 * erzeugt, dass ueber die <code>dimension()</code>-Methode berechnet wird.
	 */
	@Override
	public Collider erzeugeCollider() {
		return erzeugeLazyCollider();
	}
}
