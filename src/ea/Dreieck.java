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
import android.graphics.Paint.Style;
import android.graphics.Path;
import ea.internal.collision.Collider;
import ea.internal.gra.Listung;

/**
 * Das Dreieck ist die Basiszeichenklasse.<br />
 * Jeder Koerper laesst sich aus solchen darstellen.<br />
 * Daher ist dies die <b>einzige</b> Klasse, die in sich eine Zeichenroutine hat
 * 
 * @author Michael Andonie
 */
@SuppressWarnings("serial")
public class Dreieck extends Geometrie {
	/**
	 * Die X-Koordinaten der Punkte
	 */
	private float[] x = new float[3];

	/**
	 * Die Y-Koordinaten der Punkte
	 */
	private float[] y = new float[3];

	/**
	 * Konstruktor fuer Objekte der Klasse Dreieck
	 * 
	 * @param p1
	 *            Der erste Punkt des Dreiecks
	 * @param p2
	 *            Der zweite Punkt des Dreiecks
	 * @param p3
	 *            Der dritte Punkt des Dreiecks
	 */
	public Dreieck(Punkt p1, Punkt p2, Punkt p3)
	{
		super(0, 0);
		x[0] = p1.x;
		x[1] = p2.x;
		x[2] = p3.x;
		y[0] = p1.y;
		y[1] = p2.y;
		y[2] = p3.y;
		aktualisierenFirst();
	}

	/**
	 * Konstruktor
	 * 
	 * @param x
	 *            Alle X-Koordinaten als Feld
	 * @param y
	 *            Alle Y-Koordinaten als Feld
	 */
	public Dreieck(float[] x, float[] y) {
		super(0, 0);
		if (x.length == 3 && y.length == 3) {
			this.x = x;
			this.y = y;
		} else {
			System.out.println("Läuft nicht, falsche Arraylengen bei Dreiecksbildung!");
		}
	}

	/**
	 * Setzt die drei Punkte dieses Dreiecks neu.
	 * 
	 * @param p1
	 *            Der 1. neue Punkt des Dreiecks
	 * @param p2
	 *            Der 2. neue Punkt des Dreiecks
	 * @param p3
	 *            Der 3. neue Punkt des Dreiecks
	 * @see #punkteSetzen(float[], float[])
	 */
	public void punkteSetzen(Punkt p1, Punkt p2, Punkt p3) {
		x[0] = p1.x;
		x[1] = p2.x;
		x[2] = p3.x;
		y[0] = p1.y;
		y[1] = p2.y;
		y[2] = p3.y;
	}

	/**
	 * Setzt die drei Punkte dieses Dreiecks nue
	 * 
	 * @param x
	 *            Die Koordinaten aller X-Punkte. Der Index gibt den Punkt an (x[0] und y[0] bilden einen Punkt)
	 * @param y
	 *            Die Koordinaten aller Y-Punkte. Der Index gibt den Punkt an (x[0] und y[0] bilden einen Punkt)
	 */
	public void punkteSetzen(float[] x, float[] y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Methode zum Verschieben
	 * 
	 * @param v
	 *            Die Verschiebung als Vektor
	 * @see Raum#verschieben(Vektor)
	 */
	@Override
	public void verschieben(Vektor v) {
		for (int i = 0; i < 3; i++) {
			x[i] += v.x;
			y[i] += v.y;
		}
	}

	/**
	 * Gibt an, ob diese Dreieck sich mit einem anderen schneidet.<br />
	 * Dem Test zugrunde liegt folgene Mathematische Schlussfolgerung als Bedingung fuer das schneiden:<br/ >
	 * <b> 2 Dreiecke schneiden sich,<br />
	 * ->sobald mindestens ein Punkt des einen Dreiecks innerhalb des anderen liegt.</b><br />
	 * Dies ist die Grundlegende Testeinheit fuer alle geometrischen Formen Formen der Engine.
	 * 
	 * @return <code>true</code>, wenn sich die beiden Dreiecke theoretisch schneiden wuerden, sonst <code>false</code>.
	 */
	public boolean schneidetBasic(Dreieck d) {
		return false;
	}

	public boolean schneidetBasic(BoundingRechteck r) {
		return true;
		//return r.schneidet(this);
	}

	/**
	 * Die implementierte dimension()-Methode.
	 * 
	 * @return Das BoundingRechteck, das das Dreieck exakt umschreibt.
	 */
	@Override
	public BoundingRechteck dimension() {
		float kleinstesX = x[0];
		float groesstesX = x[0];
		float kleinstesY = y[0];
		float groesstesY = y[0];

		for (int i = 0; i < 3; i++) {
			if (x[i] > groesstesX) {
				groesstesX = x[i];
			}
			if (x[i] < kleinstesX) {
				kleinstesX = x[i];
			}
			if (y[i] > groesstesY) {
				groesstesY = y[i];
			}
			if (y[i] < kleinstesY) {
				kleinstesY = y[i];
			}
		}

		return new BoundingRechteck(kleinstesX, kleinstesY, (groesstesX - kleinstesX), (groesstesY - kleinstesY));
	}

	/**
	 * @return Ein Punkt-Array der Groesse 3, das die drei das Dreieck beschreibenden Punkte enthaelt.
	 */
	public Punkt[] punkte() {
		Punkt[] ret = new Punkt[3];
		for (int i = 0; i < 3; i++) {
			ret[i] = new Punkt(x[i], y[i]);
		}
		return ret;
	}

	/**
	 * Zeichnet das Objekt.
	 * 
	 * @param g
	 *            Das zutaendige Graphics-Objekt
	 * @param r
	 *            Das BoundingRechteck, das das Kamerabild beschreibt.
	 */
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {

		if (!r.schneidetBasic(this.dimension())) {
			return;
		}

		for (int i = 0; i < 3; i++) {
			x[i] -= r.x;
			y[i] -= r.y;
		}

		Paint p = new Paint();
		p.setColor(farbe.alsInt());
		p.setStyle(Style.FILL);
		
		Path path = new Path();
		path.setFillType(Path.FillType.EVEN_ODD);
		path.moveTo(x[0], y[0]);
		path.lineTo(x[1], y[1]);
		path.lineTo(x[2], y[2]);
		path.lineTo(x[0], y[0]);
		path.close();

		g.drawPath(path, p);
	}

	public Dreieck[] neuBerechnen() {
		Dreieck[] e = { this };
		return e;
	}

	/**
	 * {@inheritDoc}
	 * Collider wird direkt aus dem das <code>Raum</code>-Objekt umfassenden <code>BoundingRechteck</code>
	 * erzeugt, dass über die <code>dimension()</code>-Methode berechnet wird.
	 */
	@Override
	public Collider erzeugeCollider() {
		return erzeugeLazyCollider();
	}
}