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

import ea.internal.gui.*;
import ea.internal.phy.Physik;
import ea.internal.util.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Random;

/**
 * Diese Klasse ist für die sofortige, einfache Verwendung der Engine verantwortlich.<br />
 * Aus ihr sollte die Klasse abgleitet werden, die die Spielorganisation beinhaltet.
 * 
 * @author Michael Andonie
 */
public abstract class Game implements TastenReagierbar {
	static {
		System.setProperty("sun.java2d.opengl", "true"); // ok
		System.setProperty("sun.java2d.d3d", "false"); // ok
		System.setProperty("sun.java2d.noddraw", "true"); // set false if possible, linux
		System.setProperty("sun.java2d.pmoffscreen", "false"); // set true if possible, linux
		System.setProperty("sun.java2d.ddoffscreen", "true"); // ok, windows
		System.setProperty("sun.java2d.ddscale", "true"); // ok, hardware accelerated image scaling on windows
	}
	
	/**
	 * Das Spielfenster
	 */
	private final Fenster fenster;
	
	/**
	 * Der Font für die Fenstertexte
	 */
	private Font font;
	
	/**
	 * Der Tickermanager. An ihm werden die Ticker angemeldet.
	 */
	public final Manager manager = new Manager();
	
	/**
	 * Der Animations-Manager. Er kann Animationen organisieren.
	 */
	public final AnimationsManager animationsManager = AnimationsManager.getAnimationsManager();
	
	/**
	 * Der Wurzel-Knoten. An ihm muessen direkt oder indirekt (ueber weitere Knoten) alle <code>Raum</code>-Objekte
	 * angemeldet werden, die auch (normal) gezeichnet werden sollen.
	 */
	public final Knoten wurzel;
	
	/**
	 * An diesem Knoten angelegte Objekte werden immer im Vordergrund sein.<br />
	 * Dies wird zB fuer einen Abblendbildschirm verwendet.
	 */
	@SuppressWarnings("unused")
	private final Knoten superWurzel;
	
	/**
	 * Die statische Wurzel.<br />
	 * Objekte, die an diesem Knoten angemeldet werden, werden ebenfalls gezeichnet, jedoch mit einem essentiellen Unterschied
	 * bei Verschiebung der <code>Kamera</code> werden diese nicht verschoben gezeichnet, sondern bleiben weiter (<b>statisch</b>)
	 * auf ihrer festen Position. Dies bietet sich zum Beispiel fuer eine Punkte-Anzeige etc an.
	 */
	public final Knoten statischeWurzel;
	
	/**
	 * Die <code>Kamera</code> des Spiels.<br />
	 * Dieser kann ueber <code>setzeFokus(Raum m)</code> ein bestimmtes Raum-Objekt immer im Zentrum zeigen. Es gibt auch weitere interessante Methoden dieser Klasse.<br />
	 * Hierzu siehe <b>Handbuch oder Doku</b>.
	 */
	public final Kamera cam;
	
	/**
	 * Gibt an, ob bei Escape-Druck das Spiel beendet werden soll.
	 */
	private final boolean exitOnEsc;
	
	/**
	 * Der Zufallsgenerator des Spiels. Basiert nicht auf echtem Zufall.
	 */
	private final Random zufall = new Random();
	
	/**
	 * Dieser String ist <b>immer das korrekte, Systemabhaengige Pfadtrenner</b>-Literal,
	 * das ganz einfach bei Pfadangaben verwendet werden kann:<br />
	 * <br />
	 * <code>
	 * String verzeichnis = "meinOrdner" + "meinDarinLiegenderUnterordner" + "meineDatei.eaf";
	 * </code>
	 */
	public final String pfadtrenner = DateiManager.sep;
	
	/**
	 * Die eventuell aktive Blende; diese ueberblendet das Spielgeschehen.<br />
	 * Dieses Rechteck sollte nicht in seinen Massen veraendert werden. Ansonsten koennte es die Wirkung als Blende verfehlen.
	 */
	private final Rechteck blende;
	
	/**
	 * Groesster Konstruktor fuer Objekte der Klasse Game.
	 * 
	 * @param x
	 *            Die Breite des Fensters
	 * @param y
	 *            Die Hoehe des Fensters
	 * @param titel
	 *            Der Titel des Spielfensters
	 * @param vollbild
	 *            Ob das Fenster ein echtes Vollbild sein soll.<br />
	 *            Manche Computer unterstuetzen kein Vollbild, in diesem Fall wird ein moeglichst grosses Fenster erzeugt.<br />
	 *            Im Vollbildmodus nimmt das Fenster den gesamten Bildschirm ein, die Eingabeparameter <code>x</code> und <code>y</code> beschreiben dann
	 *            die Masse dieses Fensters, <b>der Bildschirm wird also an die gewuenschte X-Y-Groesse angepasst!!!</b>
	 * @param exitOnEsc
	 *            Ist dieser Wert <code>true</code>, so wird das Spiel automatisch beendet, wenn die "Escape"-Taste gedrueckt
	 *            wurde. Dies bietet sich vor allem an, wenn das Spiel ein Vollbild ist oder die Maus aufgrund der Verwendung einer Maus im Spiel nicht auf das "X"-Symbol
	 *            des Fensters geklickt werden kann, wodurch der Benutzer im Spiel "gefangen" waere und <b>dies ist etwas unbeschreiblich aergerliches fuer den Spielbenutzer!!!!!!!!!!!</b>
	 * @param fensterX
	 *            Die X-Koordinate der linken oberen Ecke des Fensters auf dem Computerbildschirm
	 * @param fensterY
	 *            Die Y-Koordinate der linken oberen Ecke des Fensters auf dem Computerbildschirm
	 */
	public Game(int x, int y, String titel, boolean vollbild, boolean exitOnEsc, int fensterX, int fensterY) {
		fenster = new Fenster(x, y, titel, vollbild, fensterX, fensterY);
		this.exitOnEsc = exitOnEsc;
		
		cam = fenster.getCam();
		cam.wurzel().add(wurzel = new Knoten(), superWurzel = new Knoten());
		
		blende = fenster.fenstermasse().ausDiesem();
		blende.farbeSetzen(new Farbe(255, 255, 255, 190));
		blende.sichtbarSetzen(false);
		
		statischeWurzel = fenster.getStatNode();
		statischeWurzel.add(blende);
		
		fenster.anmelden(this);
		
		try {
			fenster.setIconImage(ImageIO.read(getClass().getResourceAsStream("/ea/assets/favicon.png")));
		} catch (Exception e) {
			Logger.warning("Standard-Icon konnte nicht geladen werden.");
		}
	}
	
	/**
	 * Konstruktor fuer Objekte der Klasse Game. Erstellt unter anderem ein Fenster, das harmonisch im Spielbildschirm liegt.
	 * 
	 * @param x
	 *            Die Breite des Fensters
	 * @param y
	 *            Die Hoehe des Fensters
	 * @param titel
	 *            Der Titel des Spielfensters
	 * @param vollbild
	 *            Ob das Fenster ein echtes Vollbild sein soll.<br />
	 *            Manche Computer unterstuetzen kein Vollbild, in diesem Fall wird ein moeglichst grosses Fenster erzeugt.<br />
	 *            Im Vollbildmodus nimmt das Fenster den gesamten Bildschirm ein, die Eingabeparameter <code>x</code> und <code>y</code> beschreiben dann
	 *            die Masse dieses Fensters, <b>der Bildschirm wird also an die gewuenschte X-Y-Groesse angepasst!!!</b>
	 * @param exitOnEsc
	 *            Ist dieser Wert <code>true</code>, so wird das Spiel automatisch beendet, wenn die "Escape"-Taste gedrueckt
	 *            wurde. Dies bietet sich vor allem an, wenn das Spiel ein Vollbild ist oder die Maus aufgrund der Verwendung einer Maus im Spiel nicht auf das "X"-Symbol
	 *            des Fensters geklickt werden kann, wodurch der Benutzer im Spiel "gefangen" waere und <b>dies ist etwas unbeschreiblich aergerliches fuer den Spielbenutzer!!!!!!!!!!!</b>
	 */
	public Game(int x, int y, String titel, boolean vollbild, boolean exitOnEsc) {
		this(x, y, titel, vollbild, exitOnEsc, -1, -1);
	}
	
	/**
	 * Etwas vereinfachter Konstruktor.<br />
	 * Hierbei gilt automatisch, dass das Fenster <b>beim Tastendruck auf "Escape" beendet wird</b>.<br />
	 * Ist dies nicht gewuenscht, so muss der komplexere Konstruktor aufgerufen werden.
	 * 
	 * @param x
	 *            Die Breite des Fensters
	 * @param y
	 *            Die Hoehe des Fenstsers
	 * @param titel
	 *            Der Titel des Spielfensters
	 * @param vollbild
	 *            Ob das Fenster ein echtes Vollbild sein soll.<br />
	 *            Manche Computer unterstuetzen kein Vollbild, in diesem Fall wird ein moeglichst grosses Fenster erzeugt.<br />
	 *            Im Vollbildmodus nimmt das Fenster den gesamten Bildschirm ein, die Eingabeparameter <code>x</code> und <code>y</code> beschreiben dann
	 *            die Masse dieses Fensters, <b>der Bildschirm wird also an die gewuenschte X-Y-Groesse angepasst!!!</b>
	 */
	public Game(int x, int y, String titel, boolean vollbild) {
		this(x, y, titel, vollbild, true);
	}
	
	/**
	 * Erstellt ein Spiel, bei dem automatisch beim Drücken von ESC alles beendet wird und ohne Titel.
	 * 
	 * @param x
	 *            Die Breite des Fensters
	 * @param y
	 *            Die Hoehe des Fensters
	 * @param vollbild
	 *            Ob das Fenster ein echtes Vollbild sein soll.<br />
	 *            Manche Computer unterstuetzen kein Vollbild, in diesem Fall wird ein moeglichst grosses Fenster erzeugt.<br />
	 *            Im Vollbildmodus nimmt das Fenster den gesamten Bildschirm ein, die Eingabeparameter <code>x</code> und <code>y</code> beschreiben dann
	 *            die Masse dieses Fensters, <b>der Bildschirm wird also an die gewuenschte X-Y-Groesse angepasst!!!</b>
	 */
	public Game(int x, int y, boolean vollbild) {
		this(x, y, "", vollbild, true);
	}
	
	/**
	 * Vereinfachter Konstruktor.<br />
	 * Hierbei gilt automatisch, dass das Fenster <b>beim Tastendruck auf "Escape" beendet wird</b>, sowie dass kein Vollbild
	 * aktiviert wird.<br />
	 * Ist dies nicht gewuenscht, so muss der komplexere Konstruktor aufgerufen werden.
	 * 
	 * @param x
	 *            Die Breite des Fensters
	 * @param y
	 *            Die Hoehe des Fenstsers
	 * @param titel
	 *            Der Titel des Spielfensters
	 */
	public Game(int x, int y, String titel) {
		this(x, y, titel, false, true);
	}
	
	/**
	 * Vereinfachter Konstruktor.<br />
	 * Hierbei gilt automatisch, dass das Fenster <b>beim Tastendruck auf "Escape" beendet wird</b>, sowie dass kein Vollbild
	 * aktiviert wird.<br />
	 * Ist dies nicht gewuenscht, so muss der komplexere Konstruktor aufgerufen werden. Es wird kein Fenstertitel erstellt.
	 * 
	 * @param x
	 *            Die Breite des Fensters
	 * @param y
	 *            Die Hoehe des Fenstsers
	 */
	public Game(int x, int y) {
		this(x, y, "");
	}
	
	/**
	 * Parameterloser Alternativkonstruktor.<br />
	 * Hierbei wird automatisch ein Spielfenster der Groesse 500 auf 500 (kein Vollbild) erstellt, das bei Tastendruck auf "Escape" beendet wird.
	 */
	public Game() {
		this(500, 500, "", false);
	}
	
	/**
	 * Setzt das übergebene Bild als Icon des Fensters
	 * 
	 * @param icon
	 *            zu setzendes Icon
	 */
	public void iconSetzen(Bild icon) {
		fenster.setIconImage(icon.bild());
	}
	
	/**
	 * Die aus <code>TastenReagierbar</code> implemetierte Methode zum Reagieren auf einen einfachen, einmaligen Tastendruck.
	 * 
	 * @param code
	 *            Der Code dieser Taste zu den Codes:<br />
	 *            Siehe http://engine-alpha.org/wiki/Tastaturtabelle für eine vollständige Tabelle
	 * @see ea.Taste
	 */
	public void reagieren(int code) {
		if (exitOnEsc && code == Taste.ESCAPE) {
			beenden();
		}

		tasteReagieren(code);
	}
	
	/**
	 * Gibt ein <b>zufaelliges</b> <code>boolean</code>-Attribut zurueck.<br />
	 * Mit relativ zuverlaessiger Sicherheit sind die Wahrscheinlichkeiten fuer <code>false</code> und <code>true</code> gleich gross.
	 * 
	 * @return <code>false</code> oder <code>true</code>, mit gleich hoher Wahrscheinlichkeit.
	 */
	public boolean zufallsBoolean() {
		return zufall.nextBoolean();
	}
	
	/**
	 * Gibt ein <b>zufaelliges</b> <code>int</code>-Attribut zwischen <code>0</code> und einer festgelegten Obergrenze zurueck.<br />
	 * Mit relativ zuverlaessiger Sicherheit sind die Wahrscheinlichkeiten fuer die Werte zwischen <code>0</code> und der Obergrenze gleich gross.
	 * 
	 * @param obergrenze
	 *            Die hoechste Zahl, die vorkommen kann.<br />
	 *            <b>Die Zahl kann also ebenfalls ergebnis der Rueckgabe sein!!!!!!</b>
	 * @return Eine Zahl zwischen 0 (inklusiv) und der Obergrenze (inklusiv).<br />
	 *         Bei eingabe einer negativen Zahl ist das Ergebnis 0.
	 */
	public int zufallsZahl(int obergrenze) {
		if (obergrenze < 0) {
			System.err.println("Achtung!! Fuer eine Zufallszahl muss die definierte Obergrenze (die inklusiv in der Ergebnismenge ist) eine nichtnegative Zahl sein!!");
		}
		return zufall.nextInt(obergrenze + 1);
	}
	
	/**
	 * Diese Methode beendet das Spiel gaenzlich.<br />
	 * Das heisst, dass das Fenster geschlossen, alle belegten Ressourcen freigegeben und auch
	 * die virtuelle Maschine von JAVA beendet wird.<br />
	 * Also <b>beendet diese Methode die gesamte Applikation</b>!
	 * 
	 * @see #schliessen()
	 */
	public void beenden() {
		fenster.loeschen();
	}
	
	/**
	 * Beendet dieses Game auf softe weise:<br />
	 * - Das Fenster wird geschlossen<br />
	 * - Die Physik wird beendet (alle bestehenden Raum-Objekte werden automatisch
	 * neutral<br />
	 * - Alle Animationen werden beendet<br />
	 * - Der
	 * <b>ABER:</b><br />
	 * -> Die virtuelle Maschine wird <b>nicht</b> beendet.
	 */
	public void schliessen() {
		manager.kill();
		fenster.loeschen();
	}
	
	/**
	 * Dieser Methodenaufruf dauert eine bestimmte Zeit. So kann man sozusagen eine gewisse Zeit
	 * "Pause machen" und warten.<br />
	 * <br />
	 * <b>ACHTUNG!</b><br />
	 * Der Aufruf dieser Methode haelt technisch gesehen diesen <i>Thread</i> an, das bedeutet zum Beispiel, dass - sollte
	 * dies in einer Tick-Methode ausgefuehrt werden, <b>alle anderen Ticker nicht aufgerufen werden, bevonr diese Methode beendet ist</b>. <br />
	 * <br />
	 * <i>Daher sollte diese Methode nur mit Bedacht verwendet werden!</i>
	 * 
	 * @param millisekunden
	 *            Die Anzahl an Millisekunden, die dieser Methodenaufruf dauert. So lange "wartet" man also durch den Aufruf dieser
	 *            Methode.
	 */
	public void warten(int millisekunden) {
		try {
			Thread.sleep(millisekunden);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Setzt den Font, der ab sofort von den Fenstern standartmaessig verwendet wird.
	 * 
	 * @param fontname
	 *            Der Name des zu verwendenden Fonts. <br />
	 *            Ein Blick auf das <b>Fontprotokoll</b> (in der Klasse <code>Text</code> ist empfehlenswert!
	 * @param schriftgroesse
	 *            Die Schriftgroesse, in der die texte dargestellt werden sollen.
	 * @see Text
	 */
	public void fensterFontSetzen(String fontname, int schriftgroesse) {
		this.font = Text.holeFont(fontname).deriveFont(0, schriftgroesse);
	}
	
	/**
	 * Setzt den Font, der ab sofort von den Fenstern standartmaessig verwendet wird mit einer Standartgroesse von 12.
	 * 
	 * @param fontname
	 *            Der Name des zu verwendenden Fonts. <br />
	 *            Ein Blick auf das <b>Fontprotokoll</b> (in der Klasse <code>Text</code> ist empfehlenswert!
	 * @see Text
	 */
	public void fensterFontSetzen(String fontname) {
		fensterFontSetzen(fontname, 12);
	}
	
	/**
	 * Minimiert das Fenster.<br />
	 * Dadurch wird es in die Taskleiste hinein minimiert.
	 */
	public void fensterMinimieren() {
		fenster.minimieren();
	}
	
	/**
	 * Maximiert das Fenster.<br />
	 * Dadurch wird es - sofern es sich in der Taskleiste minimiert befindet -
	 * wieder maximiert.
	 */
	public void fensterMaximieren() {
		fenster.maximieren();
	}
	
	/**
	 * Meldet ein "<code>TastenReagierbar</code>"-Objekt an. Ab diesem Moment wird seine "<code>reagieren</code>"-Methode immer
	 * dann aufgerufen, wenn eine Taste heruntergedrueckt wird.
	 * 
	 * @param g
	 *            Das anzumeldende <code>TastenReagierbar</code>-Objekt
	 */
	public void tastenReagierbarAnmelden(TastenReagierbar g) {
		fenster.anmelden(g);
	}
	
	/**
	 * Prüft, ob eine bestimmte Taste gerade jetzt heruntergedrückt wird.
	 *
	 * @param code
	 *          der Code der zu prüfenden Taste.
	 * @return 	<code>true</code>, falls die gewählte Taste gerade jetzt heruntergedrückt
	 * 			wird. Sonst <code>false</code>.
	 */
	public boolean tasteGedrueckt(int code) {
		return fenster.istGedrueckt(code);
	}
	
	/**
	 * Meldet ein <code>TastenLosgelassenReagierbar</code>-Objekt an. Ab diesem Moment wird seine "<code>tasteGedrueckt</code>"-Methode immer
	 * aufgerufen, wenn eine Taste losgelassen wird.
	 * 
	 * @param g
	 *            Das anzumeldende <code>TastenLosgelassenReagierbar</code>-Objekt.
	 */
	public void tastenLosgelassenReagierbarAnmelden(TastenLosgelassenReagierbar g) {
		fenster.tastenLosgelassenAnmelden(g);
	}
	
	/**
	 * Meldet ein Mausobjekt an.<br />
	 * Ab sofort wird die anzumeldende Maus im Fenster dargestellt und Klicks werden auf die Maus uebertragen.
	 * 
	 * @param maus
	 *            Die anzumeldende Maus
	 * @param listenerUebernehmen
	 *            Ist dieser Wert <code>true</code>, so uebernimmt die neue Maus <b>alle Listener der alten
	 *            Maus</b>
	 * @see Maus
	 */
	public void mausAnmelden(Maus maus, boolean listenerUebernehmen) {
		if (maus == null) {
			System.err.println("Die anzumeldende Maus war ein nicht instanziertes Objekt (sprich: null)!");
		}
		Maus alteMaus = fenster.getMaus();
		fenster.mausLoeschen();
		if (alteMaus != null && listenerUebernehmen) {
			maus.uebernehmeAlleListener(alteMaus);
		}
		fenster.anmelden(maus);
	}
	
	/**
	 * Meldet ein <code>KollisionsReagierbar</code>-Interface an. Ab sofort wird es mit dem spezifizierten
	 * <code>code</code> aufgerufen, sollten sich die <code>Raum</code>-Objekte <code>r1</code> und <code>r2</code>
	 * schneiden.
	 * @param reagierbar	Das anzumeldende <code>KollisionsReagierbar</code>-Interface, das ab sofort von
	 * Kollisionen von <code>r1</code> und <code>r2</code> informiert werden soll.
	 * @param r1			Ein <code>Raum</code>-Objekt
	 * @param r2			Ein zweites <code>Raum</code>-Objekt
	 * @param code			Ein beliebiger Code. Dieser kann verwendet werden, um mit einem Interface mehrere
	 * 						Kollisionen <i>unterscheidbar</i> zu behandeln. Er wird im Aufruf der
	 * 						<code>kollision(int)</code> als Parameter übergeben.
	 */
	public void kollisionsReagierbarAnmelden(KollisionsReagierbar reagierbar, Raum r1, Raum r2, int code) {
		Physik.getPhysik().anmelden(reagierbar, r1, r2, code);
	}
	
	/**
	 * Meldet ein Mausobjekt an.<br />
	 * Ab sofort wird die anzumeldende Maus im Fenster dargestellt und Klicks werden auf die Maus uebertragen.
	 * 
	 * @param maus
	 *            Die anzumeldende Maus
	 * @see Maus
	 */
	public void mausAnmelden(Maus maus) {
		mausAnmelden(maus, false);
	}
	
	/**
	 * Gibt ein BoundingRechteck zurueck, dass die Masse des Fensters beschreibt.<br />
	 * Die Hoehe und Breite geben die Hoehe und Breite des Fensters wieder. Die Position ist immer (0|0), da dies nicht relevant ist
	 * fuer die Masse des Fensters.
	 * 
	 * @return Das besagte BoundingRechteck mit den Fenstermassen.
	 */
	public BoundingRechteck fensterGroesse() {
		return fenster.fenstermasse();
	}
	
	/**
	 * Setzt einen Hintergrund fuer das Spiel.<br />
	 * Das kann ein beliebiges <code>Raum</code>-Objekt sein, vorwiegend bieten sich jedoch Bilder an.<br />
	 * Dieses Objekt wird dann immer im absoluten Hintergrund sein und wird auch weiter entfernt wirken, wenn Bewegungen stattfinden.
	 * Daher sollte dieses Objekt nicht in Berechnungen, wie Kollisionstests oder den Physik-Modus, eingebunden werden!<br />
	 * <br />
	 * <br />
	 * <br />
	 * Soll der Hintergrund etwas besonderes sein, das aus vielen Objekten besteht, so bietet es sich an, all diese an einem Knoten zu binden
	 * und diesen Knoten hier anzumelden.
	 * 
	 * @param m
	 *            Das Raum-Objekt, das ab jetzt der Hintergrund sein wird.
	 */
	public void hintergrundSetzen(Raum m) {
		fenster.hintergrundAnmelden(m);
	}
	
	/**
	 * Setzt, ob das Spiel eine allueberstehende Ueberblende ausfuehren soll, die bis zum widerruf
	 * alles ueberdeckt.<br />
	 * Diese ist niemals gaenzlich durchsichtig, da sie nur ueberblendet und nicht ueberdeckt.
	 * 
	 * @param aktiv
	 *            ob die Blende aktiviert oder deaktiviert werden soll
	 * @param farbe
	 *            Die neue Farbe der Blende
	 * @see #ueberblendeSetzen(boolean)
	 */
	public void ueberblendeSetzen(boolean aktiv, Farbe farbe) {
		if (farbe.undurchsichtig()) {
			farbe = farbe.halbesAlpha();
		}
		blende.farbeSetzen(farbe);
		ueberblendeSetzen(aktiv);
	}
	
	/**
	 * Setzt, ob das Spiel eine allueberstehende Ueberblende ausfuehren soll, die bis zum widerruf
	 * alles ueberdeckt.<br />
	 * Diese ist niemals gaenzlich durchsichtig, da sie nur ueberblendet und nicht ueberdeckt.
	 * 
	 * @param aktiv
	 *            ob die Blende aktiviert oder deaktiviert werden soll
	 */
	public void ueberblendeSetzen(boolean aktiv) {
		blende.sichtbarSetzen(aktiv);
	}
	
	/**
	 * Setzt, ob die gesamte Engine Alpha rechenintensiv, oder ressourcensparsam arbeiten soll.
	 *
	 * <b>IM REGELFALL GILT FOLGENDES:</b>
	 *
	 * Die Engine arbeitet auf einem Niveau, das laufzeittechnisch bei normalen Computern standardmaessig funktioniert. <i>Sollte es
	 * jedoch Probleme mit der Laufzeit geben (haengender Bildschirm; Zeitverzögerungen, die klar an der Engine und nicht am eigenen Projekt
	 * liegen usw.), so sollte mit dieser Methode die rechenintensive Arbeit ausgeschaltet werden.</i>
	 *
	 * <b>Standardmaessig ist die Arbeit rechenintensiv</b>.
	 * 
	 * @param rechenintensiv
	 *            Ist dieser Wert <code>true</code> (Standard), so ist die Arbeit der Engine exakt und rechenintensiv. Ist dieser
	 *            Wert <code>false</code> (fuer langsame Computer zu empfehlen - aber immer erst testen!!!!), so ist die Arbeit ressourcensparender und
	 *            ungenauer.
	 */
	public void rechenintensiveArbeitSetzen(boolean rechenintensiv) {
		Raum.heavyComputingSetzen(rechenintensiv);
	}
	
	/**
	 * Macht vom aktuell sichtbaren Bereich (also dem von der Kamera derzeit
	 * erfassten Bereich) einen Screenshot.
	 * 
	 * @param pfad
	 *            Der Pfad, in dem das Bild gespeichert werden soll. Ein Wert
	 *            wie "screenshot.jpg" speichert den Screenshot im Projektordner. Fuer
	 *            eingabeabhaengige Pfade kann <code>pfadAuswaehlen(String[])</code> benutzt werden.<br />
	 * <br />
	 *            <b> ACHTUNG!! </b><br />
	 */
	public void screenshot(String pfad) {
		screenshot(pfad, cam.position());
	}
	
	/**
	 * Macht einen Screenshot von einem bestimmten Bildbereich und speichert diesen
	 * ab,
	 * 
	 * @param pfad
	 *            Der Pfad, in dem das Bild gespeichert werden soll. Ein Wert
	 *            wie "screenshot.jpg" speichert den Screenshot im Projektordner. Fuer
	 *            eingabeabhaengige Pfade kann <code>pfadAuswaehlen(String[])</code> benutzt werden.<br />
	 * <br />
	 *            <b> ACHTUNG!! </b><br />
	 *            Als Endung wird bisher nur ".jpg" unterstuetzt!
	 * @param x
	 *            Die X-Koordinate der oberen linken Ecke des Bildausschnitts.
	 * @param y
	 *            Die Y-Koordinate der oberen linken Ecke des Bildausschnitts.
	 * @param breite
	 *            Die gewuenschte Breite des Bildes
	 * @param hoehe
	 *            Die gewuenschte Laenge des Bildes
	 * @see #pfadAuswaehlen(java.lang.String[])
	 * @see #screenshot(java.lang.String, ea.BoundingRechteck)
	 */
	public void screenshot(String pfad, int x, int y, int breite, int hoehe) {
		screenshot(pfad, new BoundingRechteck(x, y, breite, hoehe));
	}
	
	/**
	 * Macht einen Screenshot von einem bestimmten Bildbereich und speichert diesen
	 * ab,
	 * 
	 * @param pfad
	 *            Der Pfad, in dem das Bild gespeichert werden soll. Ein Wert
	 *            wie "screenshot.jpg" speichert den Screenshot im Projektordner. Fuer
	 *            eingabeabhaengige Pfade kann <code>pfadAuswaehlen(String[])</code> benutzt werden.<br />
	 * <br />
	 *            <b> ACHTUNG!! </b><br />
	 *            Als Endung wird bisher nur ".jpg" unterstuetzt!
	 * @param ausschnitt
	 *            Der Ausschnitt aus der Zeichenebene, der als Bild
	 *            gespeichert werden soll als <code>BoundingRechteck</code>.
	 * @see #pfadAuswaehlen(java.lang.String[])
	 * @see #screenshot(java.lang.String, int, int, int, int)
	 */
	public void screenshot(String pfad, BoundingRechteck ausschnitt) {
		BufferedImage img = new BufferedImage((int)ausschnitt.breite, (int)ausschnitt.hoehe, BufferedImage.TYPE_INT_RGB);
		cam.wurzel().zeichnen(img.createGraphics(), ausschnitt);
		if (pfad.toLowerCase().endsWith(".jpg"))
			try {
				ImageIO.write(img, "jpg", new File(pfad));
			} catch (IOException ex) {
				Logger.error("Schreib-/Lesefehler beim Speichern des Screenshots!");
				ex.printStackTrace();
			}
	}
	
	/**
	 * Oeffnet einen Such-Dialog, der die Auswahl eines Pfades ermoeglicht.
	 * 
	 * @param akzeptierteEndungen
	 *            Eine Reihe beliebig vieler akzeptierter Endungen (Gross/Kleinschreibung vollkommen egal)<br />
	 *            z.B. : <code>pfadAuswaehlen("jpg", "bmp", "gif");</code><br />
	 *            Wird <code>null</code> als Parameter gegeben, so sind saemtliche Dateien waehlbar.<br />
	 *            z.B. : <code>pfadAuswaehlen(null);</code>
	 * @return Der Pfad der ausgewaehlten Datei als String. Ist "null", wenn kein Pfad ausgewaehlt wurde, sondern das Fenster manuell
	 *         geschlossen wurde
	 */
	public String pfadAuswaehlen(final String... akzeptierteEndungen) {
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				if (akzeptierteEndungen == null) {
					return true;
				} else if (pathname.isDirectory()) {
					return true;
				} else {
					for (int i = 0; i < akzeptierteEndungen.length; i++) {
						if (pathname.getName().toLowerCase().endsWith("." + akzeptierteEndungen[i].toLowerCase())) {
							return true;
						}
					}
					return false;
				}
			}
			
			@Override
			public String getDescription() {
				if (akzeptierteEndungen == null) {
					return "Alle Dateien wählbar";
				} else {
					String sel = "";
					for (int i = 0; i < akzeptierteEndungen.length; i++) {
						sel += "." + akzeptierteEndungen[i].toLowerCase() + " ";
					}
					return "Ausgewählte Formate (" + sel + ")";
				}
			}
		};
		JFileChooser ch = new JFileChooser();
		ch.setFileFilter(filter);
		int erg = ch.showOpenDialog(fenster);
		if (erg == JFileChooser.APPROVE_OPTION) {
			return ch.getSelectedFile().getPath();
		} else {
			return null;
		}
	}
	
	/**
	 * Diese Methode kopiert eine beliebige Datei von einem Pfad in einen neuen.
	 * 
	 * @param von
	 *            Das Verzeichnis der Datei, die kopiert werden soll
	 * @param nach
	 *            Das Verzeichnis, in das die Datei kopiert werden soll
	 * @param nameNeu
	 *            Der Name der neuen Datei, die entstehen soll (z.B. "neuedatei.pdf")
	 * @return <code>true</code>, wenn das kopieren vollends erfolgreich war, sonst <code>false</code>.
	 */
	public boolean kopieren(String von, String nach, String nameNeu) {
		try {
			FileReader re = new FileReader(new File(von));
			FileWriter wr = new FileWriter(File.createTempFile(nameNeu.split(".")[0], nameNeu.split(".")[1], new File(nach)));
			
			int r;
			
			while ((r = re.read()) != -1)
				wr.write(r);
			
			wr.close();
			re.close();
		} catch (FileNotFoundException ex) {
			Logger.error("Das Von-File konnte nicht gefunden werden");
			return false;
		} catch (IOException ex) {
			Logger.error("Fehler beim Lesen.");
			ex.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Diese Methode wird von der Klasse automatisch aufgerufen, sobald eine Taste einfach gedrueckt wurde.<br />
	 * Sie wird dann erst wieder aufgerufen, wenn die Taste erst losgelassen und dann wieder gedreuckt wurde.<br />
	 * Sollte allerdings eine Methode vonnoeten sein, die immer wieder in Regelmaessigen abstaenden aufgerufen wird, solange die Taste <b>heruntergedrueckt
	 * ist, so bietet sich dies im Interface <code>TasteGedruecktReagierbar</code> an</b>.
	 *
	 * @see ea.TastenReagierbar
	 */
	public abstract void tasteReagieren(int code);
}
