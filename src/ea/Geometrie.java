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
import ea.internal.collision.Collider;
import ea.internal.collision.ColliderGroup;
import ea.internal.gra.Listung;

/**
 * Ein Objekt, das aus n primitiven Geometrischen Formen - <b>Dreiecken</b> - besteht
 * 
 * @author Michael Andonie
 */
@SuppressWarnings("serial")
public abstract class Geometrie extends Raum implements Listung {
	
	/**
	 * Die Dimension des Objektes; zur schnellen Ausgabe
	 */
	protected BoundingRechteck dimension;
	
	/**
	 * Die Farbe, die sich das Objekt merkt, wenn es zu leuchten anfaengt, um wieder die alte herstellen zu koennen.
	 */
	private int alte;// = Color.white;
	
	/**
	 * Konstruktor fuer Objekte der Klasse Geometrie
	 * 
	 * @param anzahlFormen
	 *            Die Anzahl der Dreiecke, aus denen die Form bestehen wird.
	 * @param x
	 *            Die bestimmende X-Koordinate
	 * @param y
	 *            Die bestimmende Y-Koordinate
	 */
	public Geometrie(float x, float y) {
		super.position = new Punkt(x, y);
		dimension = new BoundingRechteck(x, y, 0, 0);
	}
	
	/**
	 * Verschiebt das Objekt.
	 * 
	 * @param v
	 *            Der Vektor, der die Verschiebung des Objekts angibt.
	 * @see Vektor
	 */
	@Override
	public void verschieben(Vektor v) {
		dimension = dimension.verschobeneInstanz(v);
	}
	
	
	/**
	 * Setzt ganzheitlich die Farbe aller Formen auf eine bestimmte Farbe.<br />
	 * Dadurch faerbt sich im Endeffekt das ganze Objekt neu ein.
	 * 
	 * @param farbe
	 *            Der String-Wert der Farbe. Zu der Zuordnung siehe <b>Handbuch</b>
	 */
	public void farbeSetzen(String farbe) {
		//farbeSetzen(zuFarbeKonvertieren(farbe));
	}
	
	
	/**
	 * Zeichnet das Objekt.<br />
	 * heisst in diesem Fall das saemtliche Unterdreiecke gezeichnet werden.
	 */
	public void zeichnen(Canvas g, BoundingRechteck r) {
	
		//for (int i = 0; i < formen.length; i++) {
			//formen[i].zeichnen(g, r);
		//}
		
	}
	
	public BoundingRechteck dimension() {
		return dimension.klon();
	}
	
	/**
	 * Berechnet exakter alle Rechteckigen Flaechen, auf denen dieses Objekt liegt.<br />
	 * Diese Methode wird von komplexeren Gebilden, wie geometrischen oder Listen ueberschrieben.
	 * 
	 * @return Alle Rechtecksflaechen, auf denen dieses Objekt liegt.
	 *         Ist standartisiert ein Array der Groesse 1 mit der <code>dimension()</code> als Inhalt.
	 */
	@Override
	public BoundingRechteck[] flaechen() {
		
		return new BoundingRechteck[] { new BoundingRechteck(0,0,0,0)};
		/*BoundingRechteck[] ret = new BoundingRechteck[formen.length];
		for (int i = 0; i < formen.length; i++) {
			ret[i] = formen[i].dimension();
		}
		return ret;
		*/
	}
	
	/**
	 * aktualisisert die Dreiecke, aus denen die Figur besteht.<br />
	 * Zugrunde liegt eine neue Wertzuweisung des Arrays, es wird <code>neuBerechnen()</code> aufgerufen.
	 */
	protected void aktualisieren() {

	}
	
	/**
	 * aktualisisert die Dreiecke, aus denen die Figur besteht UND weisst sie ein. Diese Methode MUSS am Ende eines jeden Konstruktors einer
	 * Klasse stehen, die sich hieraus ableitet<br />
	 * Zugrunde liegt eine neue Wertzuweisung des Arrays, es wird <code>neuBerechnen()</code> aufgerufen.
	 */
	protected void aktualisierenFirst() {
		aktualisieren();
		farbeSetzen("Weiss");
	}
	
	
	/**
	 * Diese Methode loescht alle eventuell vorhandenen Referenzen innerhalb der Engine auf dieses Objekt, damit es problemlos geloescht werden kann.<br />
	 * <b>Achtung:</b> zwar werden hierdurch alle Referenzen geloescht, die <b>nur innerhalb</b> der Engine liegen (dies betrifft vor allem Animationen etc), jedoch nicht die
	 * innerhalb eines <code>Knoten</code>-Objektes!!!!!!!!!<br />
	 * Das heisst, wenn das Objekt an einem Knoten liegt (was <b>immer der Fall ist, wenn es auch gezeichnet wird (siehe die Wurzel des Fensters)</b>), muss es trotzdem
	 * selbst geloescht werden, <b>dies erledigt diese Methode nicht!!</b>.<br />
	 * Diese Klasse ueberschreibt die Methode wegen des Leuchtens.
	 */
	@Override
	public void loeschen() {
//		super.leuchterAbmelden(this);
		super.loeschen();
	}
	
	/**
	 * {@inheritDoc}
	 * Collider ist eine Gruppierung aus den Collidern der Dreiecke, die dieses Objekt ausmachen.
	 */
	@Override
	public Collider erzeugeCollider() {
	
		return erzeugeLazyCollider();
	}
}
