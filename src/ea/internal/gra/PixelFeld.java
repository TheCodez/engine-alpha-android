package ea.internal.gra;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import ea.BoundingRechteck;
import ea.Farbe;

/**
 * Ein PixelFeld ist eine Ansammlzung vieler Pixel, es kann gezeichnet werden.<br />
 * Es besteht aus mehreren Quadraten gleicher Groesse, die aneinandergereiht das
 * Rechteck mit deren Groesse darstellen. <br />
 * <b>Achtung!</b> Pixelfelder leiten sich nicht aus der notwendigen Ueberklasse
 * <code>Raum</code> ab, um direkt grafisch dargestellt werden zu koennen ein
 * einzelnes Pixelfeld kann in einer unanimierten Figur dargestellt werden!
 * 
 * @author Michael Andonie
 */
public class PixelFeld implements java.io.Serializable {
	private static final long serialVersionUID = 78L;


	/* Die Farbinformation der einzelnen Pixel.<br />
	 * Ist einer dieser Werte <code>null</code>, so wird an dieser Position
	 * nicht gezeichnet.
	 */
	private final Farbe[][] farbe;
	
	private Bitmap cache;

	/**
	 * Alternative Farbe fuer das einfarbige Zeichnen
	 */
	private Farbe alternativ = null;

	/**
	 * Der Genauigkeitsfaktor.<br />
	 * Die Groesse eines Unterquadrats ist das Multiplikationsergebnis des
	 * Faktors mit der Pixelgroesse.<br />
	 * Daher <b>muss</b> Der Faktor groesser als 0 sein!
	 */
	private int faktor;

	/**
	 * Gibt an, ob die Laenge sich seit dem letzten Wert geändert haben KÖNNTE.
	 */
	private boolean changed = true;

	/**
	 * Die Pixelanzahl des letzten Kollisionstests.
	 */
	private int pixel = 0;

	/**
	 * Konstruktor fuer Objekte der Klasse PixelFeld
	 * 
	 * @param grX
	 *            Die Breite der Figur in Quadraten
	 * @param grY
	 *            Die Hoehe der Figur in Quadraten
	 * @param faktor
	 *            Der Genauigkeitsfaktor der Figur. <b>MUSS</b> groesser als 0
	 *            zu sein !
	 */
	public PixelFeld(int grX, int grY, int faktor) {
		farbe = new Farbe[grX][grY];

		if (faktor <= 0) {
			throw new IllegalArgumentException(
					"Der Eingabefaktor muss größer als 0 sein. Deine Eingabe: "
							+ faktor);
		}

		this.faktor = faktor;
	}

	/**
	 * Setzt den Groessenfaktor des Feldes.
	 * 
	 * @param faktor
	 *            Der neue Groessenfaktor
	 */
	public void faktorSetzen(int faktor) {
		if (faktor <= 0) {
			throw new IllegalArgumentException(
					"Zoomfaktor muss größer als 0 sein. Deine Eingabe: "
							+ faktor);
		}

		this.faktor = faktor;
	}

	/**
	 * Setzt an einer bestimmten Position eine Farbe.
	 * 
	 * @param x
	 *            Die Relative X-Position des zu aendernden Quadrats
	 * @param y
	 *            Die Relative Y-Position des zu aendernden Quadrats
	 * @param c
	 *            Die neu zu setzende Farbe. Ist dieser Wert null, so wird
	 *            dieses Unterquadrat nicht mitgezeichnet.
	 */
	public void farbeSetzen(int x, int y, Farbe c) {
		farbe[x][y] = c;
	}

	/**
	 * @return die Breite des Feldes in der Zeichenebene.
	 */
	public int breite() {
		return farbe.length * faktor;
	}

	/**
	 * @return die Hoehe des Feldes in der Zeichenebene.
	 */
	public int hoehe() {
		return farbe[0].length * faktor;
	}

	/**
	 * @return die Anzahl an Unterquadraten in Richtung X
	 */
	public int breiteN() {
		return farbe.length;
	}

	/**
	 * @return die Anzahl an Unterquadraten in Richtung Y
	 */
	public int hoeheN() {
		return farbe[0].length;
	}

	/**
	 * @return Größenfaktor, der dieses Bild zeichnet.
	 */
	public int faktor() {
		return faktor;
	}

	/**
	 * Gleicht dieses PixelFeld an ein anderes an, sodass beide genau dieselben
	 * Inhalte haben.
	 *
	 * <b>Achtung</b>: Hierfür müssen beide PixelFelder die selben Maße in Länge und Breite
	 * haben (hierbei zählt nicht der Größenfaktor, sondern die Anzahl an
	 * Unterquadraten in Richtung <code>x</code> und <code>y</code>.
	 */
	public void angleichen(PixelFeld f) {
		if (f.hoeheN() == this.hoeheN() && f.breiteN() == this.breiteN()) {
			for (int i = 0; i < farbe.length; i++) {
				// Deutlich performanter als ein weiterer for-loop.
				System.arraycopy(f.farbe[i], 0, this.farbe[i], 0, farbe[0].length);
			}
		} else {
			throw new IllegalArgumentException("Achtung!\nDie beiden zum Angleich angeführten PixelFelder haben unterschiedliche Masse in Höhe und/oder Breite!");
		}
	}

	/**
	 * Ändert alle Farben des Feldes in ihr Negativ um.
	 */
	public void negativ() {
		for (int i = 0; i < farbe.length; i++) {
			for (int j = 0; j < farbe[0].length; j++) {
				if (this.farbe[i][j] != null) {
					this.farbe[i][j] = new Farbe(255 - farbe[i][j].r,
							255 - farbe[i][j].g, 255 - farbe[i][j].b, farbe[i][j].a);
				}
			}
		}
	}

	/**
	 * Hellt alle Farbwerte auf.
	 */
	public void heller() {
	}

	/**
	 * Dunkelt alle Farbwerte ab.
	 */
	public void dunkler() {
	}

	/**
	 * Transformiert alle Farbwerte um einen entsprechenden Betrag.<br />
	 * Bei Uebertreten des Definitionsbereiches bleibtwird bei den Grenzen (0
	 * bzw. 255) gehalten.
	 * 
	 * @param r
	 *            Der Rot-Aenderungswert
	 * @param g
	 *            Der Gruen-Aenderungswert
	 * @param b
	 *            Der Blau-Aenderungswert
	 */
	public void transformieren(int r, int g, int b) {
		for (int i = 0; i < farbe.length; i++) {
			for (int j = 0; j < farbe[0].length; j++) {
				if (this.farbe[i][j] != null) {
					Farbe c = farbe[i][j];
					farbe[i][j] = new Farbe(zahlenSumme(c.r, r),
							zahlenSumme(c.g, g), zahlenSumme(
									c.b, b));
				}
			}
		}
	}

	/**
	 * Sorgt fuer die einfarbige Darstellung des Feldes
	 * 
	 * @param c
	 *            Diese Farbe ist nun fuer alle farbeigen Quadrate die Farbe
	 */
	public void einfaerben(Farbe c) {
		alternativ = c;
	}

	/**
	 * Sorgt fuer die normale Darstellung des Feldes
	 */
	public void zurueckFaerben() {
		alternativ = null;
	}

	/**
	 * Zeichnet das Feld mit einem bestimmten Verzug.
	 * 
	 * @param g
	 *            Das zeichnende Graphics-Objekt
	 * @param x
	 *            Der Verzug in Richtung X
	 * @param y
	 *            Der Verzug in Richtung Y
	 * @param spiegelX
	 *            Ob dieses Pixelfeld entlang der X-Achse gespiegelt werden soll
	 * @param spiegelY
	 *            Ob dieses Pixelfeld entlang der Y-Achse gespiegelt werden soll
	 */
	public void zeichnen(Canvas g, int x, int y, boolean spiegelX, boolean spiegelY) {
			int width = farbe.length * faktor, height = farbe.length == 0 ? 0 : farbe[0].length * faktor;
			
			//cache = Bitmap.createBitmap(width, height, Config.ARGB_4444);
			//cache = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			//Canvas cacheGraphics = new Canvas(cache);

			for (int i = 0; i < farbe.length; i++) {
				for (int j = 0; j < farbe[i].length; j++) {
					Farbe c = farbe[i][j];

					if (c == null) {
						continue;
					}

					if (alternativ == null) {
				//		cacheGraphics.drawColor(c.alsInt());
					} else {
					//	cacheGraphics.drawColor(alternativ.alsInt());
					}

					//cacheGraphics.drawRect(i * faktor, j * faktor, faktor, faktor, new Paint());
				}
			}

		int w = breite(), h = hoehe();
		
		//g.drawBitmap(col, 0, 4, x + w, y + h, width, height, true, new Paint());
		
		/*
		if(spiegelX && spiegelY) {
			g.drawBitmap(cache, x + w, y + h, new Paint());
		} else if(spiegelX) {
			g.drawBitmap(cache, x + w, y, new Paint());
		} else if(spiegelY) {
			g.drawBitmap(cache, x, y + h, new Paint());
		} else {
			g.drawBitmap(cache, x, y, new Paint());
		}
		*/
	}

	/**
	 * In dieser Methode werden die einzelnen Quadrate von ihrer
	 * Informationsdichte her zurueckgegeben.
	 * 
	 * @return Die Farbinformationen ueber dieses Pixelfeld.
	 */
	public Farbe[][] getPic() {
		return farbe;
	}

	/**
	 * Berechnet die Anzahl an Pixeln, die auf diesem PixelFeld liegen.
	 * 
	 * @return Die Anzahl an tatsaechlichen Pixeln.
	 */
	public int anzahlPixel() {
		if (changed) {
			int neu = 0;

			for (int i = 0; i < farbe.length; i++) {
				for (int j = 0; j < farbe[0].length; j++) {
					if (farbe[i][j] != null) {
						neu++;
					}
				}
			}

			pixel = neu;
			changed = false;
		}

		return pixel;
	}

	/**
	 * Erstellt ein neues PixelFeld mit exakt denselben Eigenschaften wie
	 * dieses.<br />
	 * Diese Methode wird vor allem intern im FigurenEditor verwendet.
	 * 
	 * @return Ein neues PixelFeld-Objekt mit genau demselben Zustand wie
	 *         dieses.
	 */
	public PixelFeld erstelleKlon() {
		PixelFeld ret = new PixelFeld(farbe.length, farbe[0].length, faktor);

		for (int i = 0; i < farbe.length; i++) {
			for (int j = 0; j < farbe[0].length; j++) {
				ret.farbeSetzen(i, j, farbe[i][j]);
			}
		}

		return ret;
	}

	/**
	 * Berechnet <b>EXAKT</b> die Flaechen aus denen dieses Pixel-Feld besteht.
	 * 
	 * @param x
	 *            Die X-Startkoordinate der linken oberen Ecke
	 * @param y
	 *            Die Y-Startkoordinate der linken oberen Ecke
	 * @return Alle Flaechen dieses Pixel-Feldes als Array aus
	 *         Bounding-Rechtecken.
	 */
	public BoundingRechteck[] flaechen(float x, float y) {
		BoundingRechteck[] ret = new BoundingRechteck[anzahlPixel()];
		int cnt = 0;
		for (int i = 0; i < farbe.length; i++) {
			for (int j = 0; j < farbe[0].length; j++) {
				if (farbe[i][j] != null) {
					ret[cnt++] = new BoundingRechteck(x + i * faktor, y + j
							* faktor, faktor, faktor);
				}
			}
		}

		return ret;
	}


	/**
	 * Errechnet aus zwei Zahlen die Summe und setzt das Ergebnis, sofern nicht
	 * im Definitionsbereich der Farbwerte auf den naeheren Grenzwert (0 bzw.
	 * 255)
	 * 
	 * @param a
	 *            Wert 1
	 * @param b
	 *            Wert 2
	 * @return Die Summe, unter Garantie im Definitionsbereich
	 */
	private static final int zahlenSumme(int a, int b) {
		return Math.max(0, Math.min(255, a + b));
	}
}