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

import ea.internal.util.Logger;

/**
 * Ein Vektor bezeichnet eine relative Punktangabe.<br />
 * Ansonsten unterscheidet er sich hier nicht weiter von einem Punkt.<br />
 * Vektoren werden meist fuer die BEschreibung einer bewegung benutzt.
 * 
 * @author Michael Andonie
 */
public final class Vektor
{
	/**
	 * Konstante fuer einen Bewegungslosen Vektor (Werte 0|0)
	 */
	public static final Vektor NULLVEKTOR = new Vektor(0, 0);
	
	/**
	 * Konstante fuer eine einfache Verschiebung nach rechts (Werte 1|0)
	 */
	public static final Vektor RECHTS = new Vektor(1, 0);
	
	/**
	 * Konstante fuer eine einfache Verschiebung nach links (Werte -1|0)
	 */
	public static final Vektor LINKS = new Vektor(-1, 0);
	
	/**
	 * Konstante fuer eine einfache Verschiebung nach oben (Werte 0|-1)
	 */
	public static final Vektor OBEN = new Vektor(0, -1);
	
	/**
	 * Konstante fuer eine einfache Verschiebung nach unten (Werte 0|1)
	 */
	public static final Vektor UNTEN = new Vektor(0, 1);
	
	/**
	 * Konstante, die widerspiegelt, das keine Bewegung vollzogen wird.
	 */
	public static final int KEINE_BEWEGUNG = -1;
	
	/**
	 * Die Konstante fuer die Richtung Westen
	 */
	public static final int W = 0;
	
	/**
	 * Die Konstante fuer die Richtung Osten
	 */
	public static final int O = 1;
	
	/**
	 * Die Konstante fuer die Richtung Norden
	 */
	public static final int N = 2;
	
	/**
	 * Die Konstante fuer die Richtung Sueden
	 */
	public static final int S = 3;
	
	/**
	 * Die Konstante fuer die Richtung Suedwesten
	 */
	public static final int NW = 4;
	
	/**
	 * Die Konstante fuer die Richtung Suedosten
	 */
	public static final int NO = 5;
	
	/**
	 * Die Konstante fuer die Richtung Suedwesten
	 */
	public static final int SW = 6;
	
	/**
	 * Die Konstante fuer die Richtung Suedosten
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
	 * Konstruktor fuer Objekte der Klasse Vektor. Werte werden hier als <code>int</code> angegeben.
	 * Intern wird trotzdem mit genaueren Fließkommazahlen gerechnet.
	 * 
	 * @param x
	 *            Der Bewegungsanteil in Richtung X
	 * @param y
	 *            Der Bewegungsanteil in Richtung Y
	 */
	public Vektor(int x, int y) {
		this.x = (float) x;
		this.y = (float) y;
	}
	
	/**
	 * Konstruktor fuer Objekte der Klasse Vektor
	 * 
	 * @param x
	 *            Der Bewegungsanteil in Richtung X
	 * @param y
	 *            Der Bewegungsanteil in Richtung Y
	 */
	public Vektor(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Zweite Variante des Konstruktors fuer die Klasse Vektor.<br />
	 * Hierbei wird er erzeugt als die noetige Bewegung von einem Punkt, um zu einem zweiten zu kommen.
	 * 
	 * @param start
	 *            Der Ausgangspunkt der Bewegung dieses Vektors, der zu dem Ziel hinfuehrt.
	 * @param ziel
	 *            Der Zielpunkt der Bewegung
	 */
	public Vektor(Punkt start, Punkt ziel) {
		this.x = ziel.x - start.x;
		this.y = ziel.y - start.y;
	}
	
	/**
     * Gibt eine <b>Normierung</b> des Vektors aus.
     * Dies ist ein Vektor, der<br />
     * <ul>
     * <li>In die selbe Richtung wie der ursprüngliche Vektor zeigt.</li>
     * <li>Eine Länge von (möglichst) exakt 1 hat.</li>
     * </ul>
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
    	//Nimm euklidische Norm
    	return (float)Math.sqrt(x*x + y*y);
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
	 * Berechnet die effektive Bewegung, die dieser Vektor und ein weiterer zusammen ausueben.
	 * 
	 * @param v
	 *            Der zweite bewegende Vektor
	 * @return Ein neues Vektor-Objekt, das die Summe der beiden urspruenglichen Bewegungen darstellt.
	 */
	public Vektor summe(Vektor v) {
		return new Vektor(this.x + v.x, this.y + v.y);
	}
	
	/**
     * Berechnet die Differenz zwischen diesem und einem weiteren Vektor.
     * @param v	ein weiterer Vektor.
     * @return	Die Differenz "this - v" der beiden Vektoren.
     */
    public Vektor differenz(Vektor v) {
    	return new Vektor(this.x-v.x, this.y-v.y);
	}
    
    /**
	 * Teilt die effektive Laenge des Vektors durch eine ganze Zahl und kuerzt dadurch seine Effektivitaet.
	 * 
	 * @param divisor
	 *            Hierdurch wird die Laenge des Vektors auf der Zeichenebene geteilt.
	 * @return Ein Vektor-Objekt, das eine Bewegung in dieselbe Richtung beschreibt, allerdings in der
	 *         Laenge gekuerzt um den angegebenen Divisor.<br />
	 *         <b>Achtung!</b><br />
	 * Ist <code>(0,0)</code>, wenn die Eingabe == 0 war!
	 * @see #multiplizieren(int)
	 */
    public Vektor teilen(float divisor) {
        if(divisor == 0) {
            //throw new ArithmeticException("Der Divisor für das Teilen war 0!");
        	Logger.error("Der Divisor für das Teilen war 0!");
        	return NULLVEKTOR;
		}
		return new Vektor(x / divisor, y / divisor);
	}
	
	/**
	 * Multipliziert die effektiven Laengen beider Anteile des Vektors (X und Y) mit einem festen Faktor.
	 * Dadurch entsteht ein neuer Vektor mit anderen Werten (es sei denn, der Faktor ist 1); dieser wird dann zurueck gegeben.
	 * 
	 * @param faktor
	 *            Der Faktor, mit dem die X- und Y-Werte des Vektors multipliziert werden
	 * @return Der Vektor mit den multiplizierten Werten
	 * @see #teilen(float)
	 */
	public Vektor multiplizieren(int faktor) {
		return new Vektor(x * faktor, y * faktor);
	}
	
	/**
	 * Multipliziert die effektiven Längen beider Anteile des Vektors (X und Y) mit einem festen Faktor.
	 * Dadurch entsteht ein neuer Vektor mit anderen Werten (es sei denn, der Faktor ist 1); dieser wird dann zurueck gegeben.
	 * 
	 * @param faktor
	 *            Der Faktor, mit dem die X- und Y-Werte des Vektors multipliziert werden
	 * @return Der Vektor mit den multiplizierten Werten
	 * @see #teilen(float)
	 */
	public Vektor multiplizieren(float faktor) {
		return new Vektor(x * faktor, y * faktor);
	}
	
    /**
     * Berechnet das <b>Skalarprodukt</b> von diesem Vektor mit einem weiteren. Das Skalarprodukt für zweidimensionale
     * Vektoren ist: <br/>
     * (a,b) o (c, d) = a*b + c*d
     * @param v	Ein zweiter Vektor.
     * @return	Das Skalarprodukt dieses Vektoren mit dem Vektor <code>v</code>.
     */
    public float skalarprodukt(Vektor v) {
    	return this.x * v.x + this.y * v.y;
    }

    /**
	 * Berechnet, ob dieser Vektor keine Wirkung hat, dies ist der Fall, wenn beide Komponenten, X und Y gleich 0 sind.
	 * 
	 * @return <code>true</code>, wenn dieser keine Auswirkungen als Bewegender Vektor machen wuerde.
	 */
	public boolean unwirksam() {
		return (this.x == 0 && this.y == 0);
	}
	
	/**
	 * Berechnet die Richtung des Vektors, in die er wirkt.<br />
	 * Der Rueckgabewert basiert auf den Konstanten der eigenen Klasse und sind entweder die Basiswerte
	 * (<code>N/S/O/W</code>) oder die Kombiwerte (<code>NO/NW/...</code>). Alle diese sind Konstanten dieser Klasse.
	 * 
	 * @return Der Wert der Konstanten, die diese Bewegung wiederspiegelt.
	 */
	public int richtung() {
		if (x == 0) {
			if (y == 0) {
				return KEINE_BEWEGUNG;
			}
			if (y > 0) {
				return S;
			}
			return N;
		}
		if (x > 0) {
			if (y == 0) {
				return O;
			}
			if (y > 0) {
				return SO;
			}
			return NO;
		}
		if (y == 0) {
			return W;
		}
		if (y > 0) {
			return SW;
		}
		return NW;
	}
	
	/**
	 * Berechnet einen einfachen Vektor (maximale auslenkung bei jeder Achse 1 (positiov wie negativ)), der
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
	 * @return Der Vektor, der mit einer einfachen Auslenkung (d. h. für X und Y je ein Wertebereich von {-1, 0, 1})
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
				System.err.println("Achtung! Die eingegebene Konstante hatte keinen der moeglichen Werte!");
				return null;
		}
	}
	
	/**
	 * Gibt zurück, ob dieser Vektor <i>echt ganzzahlig</i> ist, also ob seine <b>tatsächlichen Delta-Werte</b>
	 * beide Ganzzahlen sind.
	 * 
	 * @return <code>true</code>, wenn <b>beide</b> Delta-Werte dieses Punktes ganzzahlig sind, sonst <code>false</code>.
	 */
	public boolean istEchtGanzzahlig() {
		return this.x == (float) Math.floor(x) && this.y == (float) Math.floor(y);
	}
	
	/**
	 * Gibt die X-Verschiebung dieses Vektors wieder.
	 * 
	 * @return Die X-Verschiebung dieses Vektors. Positive Werte verschieben nach
	 *         rechts, negative Werte verschieben nach links.
	 * @see #realX()
	 */
    public float realX() {
		return x;
	}
	
	/**
	 * Gibt die Y-Verschiebung dieses Vektors wieder.
	 * 
	 * @return Die Y-Verschiebung dieses Vektors. Positive Werte verschieben nach
	 *         unten, negative Werte verschieben nach oben.
	 * @see #realY()
	 */
    public float realY() {
		return y;
	}
	
	/**
	 * Gibt einen einfachen Vektor zurück, dessen Richtungskomponenten nur -1, 0 oder 1 annehmen (-1 bei werten < 0; 0 bei Werten = 0; 1
	 * bei Werten > 0).
	 * 
	 * @return Ein Einfacher Vektor, der die Richtung des Urspruenglichen mit einfachen Werten beschreibt beschreibt.
	 */
	public Vektor einfacher() {
		return vonKonstante(richtung());
	}
	
	/**
	 * Gibt die String-Repraesentation dieses Objektes aus.
	 * 
	 * @return Die String-Repraesentation dieses Strings
	 */
	@Override
	public String toString() {
		return "Vektor: (" + x + "|" + y + ")";
	}
	
	/**
	 * {@inheritDoc} Prueft, ob ein beliebiges Objekt gleich diesem Vektor ist. Ueberschrieben aus der Superklasse <code>Object</code>.<br />
	 * 2 Vektoren gelten als gleich, wenn sie in ihrem Delta-X und ihrem Delta-Y (und zwar <b>reell exakt</b>) uebereinstimmen.
	 * 
	 * @param o
	 *            Das auf gleichheit mit diesem zu ueberpruefende Objekt.
	 * @return <code>true</code>, wenn beide Vektoren das gleiche dX und dY haben, sonst <code>false</code>.
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Vektor) {
			Vektor v = (Vektor) o;
			return (this.x == v.x && this.y == v.y);
		}
		return false;
	}

    /**
     * Gibt die X-Verschiebung dieses Vektors mit Ganzzahlen wieder.
     * @return  Die X-Verschiebung dieses Vektors. Positive Werte verschieben nach
     * rechts, negative Werte verschieben nach links.
     * @see dY()
     */
	public int dX() {
		return (int)x;
	}
	
	/**
     * Gibt die Y-Verschiebung dieses Vektors mit Ganzzahlen wieder.
     * @return  Die Y-Verschiebung dieses Vektors. Positive Werte verschieben nach
     * unten, negative Werte verschieben nach oben.
     * @see dX()
     */
	public int dY() {
		return (int)y;
	}
}
