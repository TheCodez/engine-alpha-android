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

/**
 * Ein Vektor bezeichnet eine relative Punktangabe.<br />
 * Ansonsten unterscheidet er sich hier nicht weiter von einem Punkt.<br />
 * Vektoren werden meist für die Beschreibung einer Bewegung benutzt.
 * 
 * @author Michael Andonie
 */
public final class Vektor
implements Cloneable {
	/**
	 * Konstante für einen bewegungslosen Vektor (0, 0)
	 */
	public static final Vektor NULLVEKTOR = new Vektor(0, 0);
	
	/**
	 * Konstante für eine einfache Verschiebung nach rechts (1, 0)
	 */
	public static final Vektor RECHTS = new Vektor(1, 0);
	
	/**
	 * Konstante für eine einfache Verschiebung nach links (-1, 0)
	 */
	public static final Vektor LINKS = new Vektor(-1, 0);
	
	/**
	 * Konstante für eine einfache Verschiebung nach oben (0, -1)
	 */
	public static final Vektor OBEN = new Vektor(0, -1);
	
	/**
	 * Konstante für eine einfache Verschiebung nach unten (0, 1)
	 */
	public static final Vektor UNTEN = new Vektor(0, 1);
	
	/**
	 * Konstante, die widerspiegelt, dass keine Bewegung vollzogen wird.
	 */
	public static final int KEINE_BEWEGUNG = -1;
	
	/**
	 * Die Konstante für die Richtung Westen
	 */
	public static final int W = 0;
	
	/**
	 * Die Konstante für die Richtung Osten
	 */
	public static final int O = 1;
	
	/**
	 * Die Konstante für die Richtung Norden
	 */
	public static final int N = 2;
	
	/**
	 * Die Konstante für die Richtung Süden
	 */
	public static final int S = 3;
	
	/**
	 * Die Konstante für die Richtung Nordwesten
	 */
	public static final int NW = 4;
	
	/**
	 * Die Konstante für die Richtung Nordosten
	 */
	public static final int NO = 5;
	
	/**
	 * Die Konstante für die Richtung Südwesten
	 */
	public static final int SW = 6;
	
	/**
	 * Die Konstante für die Richtung Südosten
	 */
	public static final int SO = 7;
	
	/**
	 * Der kontinuierliche(re) DeltaX-Wert des Punktes. Die anderen Koordinaten sind ggf.
	 * nur gerundet.
	 */
	public final float x;
	
	/**
	 * Der kontinuierliche(re) DeltaY-Wert des Punktes. Die anderen Koordinaten sind ggf.
	 * nur gerundet.
	 */
	public final float y;
	
	/**
	 * Konstruktor fuer Objekte der Klasse Vektor
	 * 
	 * @param x
	 *            Der Bewegungsanteil <code>x</code>.
	 * @param y
	 *            Der Bewegungsanteil <code>y</code>.
	 */
	public Vektor(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Zweite Variante des Konstruktors für die Klasse Vektor.
	 *
	 * Hierbei wird er erzeugt als die noetige Bewegung von einem Punkt, um zu einem zweiten zu kommen.
	 * 
	 * @param start
	 *            Der Ausgangspunkt der Bewegung dieses Vektors, der zu dem Ziel hinführt.
	 * @param ziel
	 *            Der Zielpunkt der Bewegung.
	 */
	public Vektor(Punkt start, Punkt ziel) {
		this.x = ziel.x - start.x;
		this.y = ziel.y - start.y;
	}
	
	/**
     * Gibt eine <b>Normierung</b> des Vektors aus.
     * Dies ist ein Vektor, der
	 *
     * * in die selbe Richtung wie der ursprüngliche Vektor zeigt.</li>
     * * eine Länge von (möglichst) exakt 1 hat.</li>
	 *
     * @return der normierte Vektor zu diesem Vektor.
     */
    public Vektor normiert() {
    	return this.teilen(this.laenge());
    }
    
    /**
     * Gibt die Länge dieses Vektors aus.
     * @return	Die Länge dieses Vektors.
     */
    public float laenge() {
    	return (float) Math.sqrt(x*x + y*y);
    }
    
    /**
	 * Berechnet die Gegenrichtung des Vektors.
	 * 
	 * @return Ein neues Vektor-Objekt, das genau die Gegenbewegung zu dem eigenen beschreibt.
	 */
	public Vektor gegenrichtung() {
		return new Vektor(-this.x, -this.y);
	}
	
	/**
	 * Berechnet die effektive Bewegung, die dieser Vektor und ein weiterer zusammen ausüben.
	 * 
	 * @param v
	 *            Der zweite bewegende Vektor
	 * @return Ein neues Vektor-Objekt, das die Summe der beiden ursprünglichen Bewegungen darstellt.
	 */
	public Vektor summe(Vektor v) {
		return new Vektor(this.x + v.x, this.y + v.y);
	}
	
	/**
     * Berechnet die Differenz zwischen diesem und einem weiteren Vektor.
     * @param v	ein zweiter Vektor.
     * @return	Die Differenz der beiden Vektoren (<code>"this - v"</code>)
     */
    public Vektor differenz(Vektor v) {
    	return new Vektor(this.x-v.x, this.y-v.y);
	}
    
    /**
	 * Teilt die effektive Länge des Vektors durch eine ganze Zahl und kürzt dadurch seine Effektivität.
	 * 
	 * @param divisor
	 *            Hierdurch wird die Länge des Vektors auf der Zeichenebene geteilt.
	 * @return Ein Vektor-Objekt, das eine Bewegung in dieselbe Richtung beschreibt, allerdings in der
	 *         Länge gekürzt um den angegebenen Divisor.<br />
	 *         <b>Achtung!</b><br />
     *
	 * @see #multiplizieren(float)
	 */
    public Vektor teilen(float divisor) {
        if(divisor == 0) {
            throw new ArithmeticException("Der Divisor für das Teilen war 0!");
		}

		return new Vektor(x / divisor, y / divisor);
	}
	
	/**
	 * Multipliziert die effektiven Längen beider Anteile des
	 * Vektors (<code>x</code> und <code>y</code>) mit einem festen Faktor.
	 *
	 * Dadurch entsteht ein neuer Vektor mit anderen Werten, welcher zurückgegeben wird.
	 * 
	 * @param faktor
	 *            Der Faktor, mit dem die <code>x</code>- und <code>y</code>-Werte
	 *            des Vektors multipliziert werden
	 * @return Der Vektor mit den multiplizierten Werten
	 * @see #teilen(float)
	 */
	public Vektor multiplizieren(float faktor) {
		return new Vektor(x * faktor, y * faktor);
	}
	
    /**
     * Berechnet das <b>Skalarprodukt</b> von diesem Vektor mit einem weiteren.
     * Das Skalarprodukt für zweidimensionale Vektoren ist:
     * :<code>(a, b) o (c, d) = a * b + c * d</code>
     *
     * @param v	Ein zweiter Vektor.
     * @return  Das Skalarprodukt dieses Vektoren mit dem Vektor <code>v</code>.
     */
    public float skalarprodukt(Vektor v) {
    	return this.x * v.x + this.y * v.y;
    }

    /**
	 * Berechnet, ob dieser Vektor keine Wirkung hat. Dies ist der Fall,
     * wenn beide Komponenten (<code>x</code> und <code>y</code>) 0 sind.
	 * 
	 * @return <code>true</code>, wenn dieser keine Auswirkungen als bewegender Vektor machen würde.
	 */
	public boolean unwirksam() {
		return this.x == 0 && this.y == 0;
	}
	
	/**
	 * Berechnet die Richtung des Vektors, in die er wirkt.<br />
	 * Der Rückgabewert basiert auf den Konstanten der eigenen Klasse und sind entweder die Basiswerte
	 * (<code>N/S/O/W</code>) oder die Kombiwerte (<code>NO/NW/...</code>). Alle diese sind Konstanten dieser Klasse.
	 * 
	 * @return Der Wert der Konstanten, die diese Bewegung wiederspiegelt.
	 */
	public int richtung() {
		if (x == 0 && y == 0) {
			return KEINE_BEWEGUNG;
		}

		if (x == 0) {
			return y > 0 ? S : N;
		}

		if(y == 0) {
			return x > 0 ? O : W;
		}

		if(x < 0 && y < 0) {
			return NW;
		}

		if(x > 0 && y < 0) {
			return NO;
		}

		if(x > 0 && y > 0) {
			return SO;
		}

		return SW;
	}

	/**
	 * Berechnet einen einfachen Vektor (maximale Auslenkung bei jeder Achse 1 (positiov wie negativ)), der
	 * der entsprechenden Konstante dieser Klasse entspricht möglich sind:
	 *
	 * <code>N</code>,
	 * <code>S</code>,
	 * <code>O</code>,
	 * <code>W</code>,
	 * <code>NO</code>,
	 * <code>NW</code>,
	 * <code>SO</code>,
	 * <code>SW</code>
	 * 
	 * @param konstante
	 *            Die Konstante, die die Bewegungsrichtung beschreibt.
	 * @return Der Vektor, der mit einer einfachen Auslenkung
	 * (d.h. für <code>x</code> und <code>y</code> je ein Wertebereich von {-1, 0, 1})
	 *         die entsprechende Bewegung macht.<br />
	 *         Ist <code>null</code>, wenn die Konstante einen nicht verwendbaren Wert hat!
	 */
	public static Vektor vonKonstante(int konstante) {
		switch (konstante) {
			case N:
				return OBEN;
			case S:
				return UNTEN;
			case O:
				return RECHTS;
			case W:
				return LINKS;
			case NO:
				return new Vektor(1, -1);
			case NW:
				return new Vektor(-1, -1);
			case SO:
				return new Vektor(1, 1);
			case SW:
				return new Vektor(-1, 1);
			default:
				throw new IllegalArgumentException("Die eingegebene Konstante hatte keinen der möglichen Werte!");
		}
	}
	
	/**
	 * Gibt zurück, ob dieser Vektor <i>echt ganzzahlig</i> ist, also ob seine <b>tatsächlichen Delta-Werte</b>
	 * beide Ganzzahlen sind.
	 * 
	 * @return <code>true</code>, wenn <b>beide</b> Delta-Werte dieses Punktes ganzzahlig sind, sonst <code>false</code>.
	 */
	public boolean istEchtGanzzahlig() {
		return x == (float) Math.floor(x) && y == (float) Math.floor(y);
	}
	
	/**
	 * Gibt die <code>x</code>-Verschiebung dieses Vektors wieder.
	 * 
	 * @return Die <code>x</code>-Verschiebung dieses Vektors. Positive Werte verschieben nach
	 *         rechts, negative Werte verschieben nach links.
	 * @see #realX()
	 */
    public float realX() {
		return x;
	}
	
	/**
	 * Gibt die <code>y</code>-Verschiebung dieses Vektors wider.
	 * 
	 * @return Die <code>y</code>-Verschiebung dieses Vektors. Positive Werte verschieben nach
	 *         unten, negative Werte verschieben nach oben.
	 * @see #realY()
	 */
    public float realY() {
		return y;
	}
	
	/**
	 * Gibt einen einfachen Vektor zurück, dessen Richtungskomponenten
	 * nur <code>-1</code>, <code>0</code> oder <code>1</code> annehmen.
	 *
	 * :-1 bei Werten < 0 - 0 bei Werten = 0 - 1 bei Werten > 0
	 * 
	 * @return Ein Einfacher Vektor, der die Richtung des Urspruenglichen mit einfachen Werten beschreibt beschreibt.
	 */
	public Vektor einfacher() {
		return vonKonstante(richtung());
	}
	
	/**
	 * Gibt die String-Repräsentation dieses Objektes aus.
	 * 
	 * @return Die String-Repräsentation dieses Strings
	 */
	@Override
	public String toString() {
		return "ea.Vektor [x = " + x + "; y = " + y + "]";
	}
	
	/**
	 * Prüft, ob ein beliebiges Objekt gleich diesem Vektor ist.Überschrieben aus der Superklasse <code>Object</code>.
	 *
	 * Zwei Vektoren gelten als gleich, wenn <code>x</code> und <code>y</code> der beiden Vektoren übereinstimmen.
	 * 
	 * @param o
	 *            Das auf Gleichheit mit diesem zu überpruefende Objekt.
	 * @return <code>true</code>, wenn beide Vektoren gleich sind, sonst <code>false</code>.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Vektor) {
			Vektor v = (Vektor) o;

			return x == v.x && y == v.y;
		}

		return false;
	}

    /**
     * Gibt die <code>x</code>-Verschiebung dieses Vektors mit Ganzzahlen wider.
     * @return  Die <code>x</code>-Verschiebung dieses Vektors. Positive Werte verschieben nach
     * rechts, negative Werte verschieben nach links.
     * @see #dY()
     */
	public int dX() {
		return (int) x;
	}
	
	/**
     * Gibt die <code>y</code>-Verschiebung dieses Vektors mit Ganzzahlen wider.
     * @return  Die <code>y</code>-Verschiebung dieses Vektors. Positive Werte verschieben nach
     * unten, negative Werte verschieben nach oben.
     * @see #dX()
     */
	public int dY() {
		return (int) y;
	}
	
	/**
	 * Erstellt ein <b>neues <code>Vektor</code>-Objekt mit demselben Zustan</b>.
	 * @return eine neue Instanz von <code>Vektor</code> mit den selben Koordinaten (x|y)
	 */
	@Override
	public Vektor clone() {
		return new Vektor(x,y);
	}
	
	/**
	 * Gibt die Manhattan-L�nge des Vektors zur�ck. Diese ist f�r v=(a,b) definiert
	 * als a+b .
	 * @return	Die Summe von delta X und delta Y des Vektors.
	 */
	public float manhattanLength() {
		float v = x + y;
		return v < 0 ? -v : v;
	}
}
