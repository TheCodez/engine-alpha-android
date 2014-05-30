package ea.internal.gra;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import android.content.res.AssetManager;
import android.graphics.Color;
import ea.Farbe;
import ea.Punkt;
import ea.android.GameActivity;

public class FigurData 
{
	public Punkt pos;
	public PixelFeld[] feld;
	public boolean animiert;
	
	public FigurData()
	{
		
	}
	
	
	public static FigurData figurEinlesen(String pfad) {
		
		FigurData fig = new FigurData();
		LineNumberReader f = null;
		String line;

		try {
			AssetManager assetManager = GameActivity.get().zeichnerGeben().getContext().getAssets();

			f = new LineNumberReader(new InputStreamReader(assetManager.open(pfad)));
			line = f.readLine();

			if (line.equals(line.compareTo("_fig_") != 0)) { // Format  bestätigen
				//Logger.error("Die Datei ist keine Figur-Datei!" + line);

				return null;
			}

			line = f.readLine();
			final int animationsLaenge = Integer.valueOf(line.substring(3)); // Die Anzahl an PixelFeldern
			// System.out.println("PixelFelder: " + animationsLaenge);
			line = f.readLine();
			final int fakt = Integer.valueOf(line.substring(2)); // Der
																	// Groessenfaktor
			// System.out.println("Der Groessenfaktor: " + fakt);
			line = f.readLine();
			final int x = Integer.valueOf(line.substring(2)); // Die X-Groesse
			line = f.readLine();
			final int y = Integer.valueOf(line.substring(2)); // Die Y-Groesse
			// System.out.println("X-Gr: " + x + "; Y-Gr: " + y);
			line = f.readLine();
			final int px = Integer.valueOf(line.substring(2)); // Die X-Position
			line = f.readLine();
			final int py = Integer.valueOf(line.substring(2)); // Die Y-Position
			// System.out.println("P-X: " + px + " - P-Y: " + py);

			PixelFeld[] ergebnis = new PixelFeld[animationsLaenge];
			for (int i = 0; i < ergebnis.length; i++) { // Felder basteln
				if ((line = f.readLine()).compareTo("-") != 0) { // Sicherheitstest
					//Logger.error("Die Datei ist beschädigt");
				}
				ergebnis[i] = new PixelFeld(x, y, fakt);
				for (int xT = 0; xT < x; xT++) { // X
					for (int yT = 0; yT < y; yT++) { // Y
						line = f.readLine();
						Farbe c = farbeEinlesen(line.split(":")[1]);
						if (c != null) {
							c = Farbe.Weiss;//ausListe(c);
						}
						ergebnis[i].farbeSetzen(xT, yT, c);
					}
				}
			}
			fig.feld = ergebnis;
			fig.pos = new Punkt(px, py);
			fig.animiert = (animationsLaenge != 1);
			f.close();
		} catch (IOException e) {
			//Logger.error("Fehler beim Lesen der Datei. Existiert die Datei mit diesem Namen wirklich?"
				//			+ bruch + verzeichnis);
			e.printStackTrace();
		} finally {
			if(f != null) {
				try {
					f.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return fig;
	}
	
	public static Farbe farbeEinlesen(String s) {
		if (s.compareTo("%%;") == 0) {
			return null;
		//} else if (s.charAt(0) != '&') {
			//return Raum.zuFarbeKonvertieren(s.replace(";", ""));
		} else {
			int[] rgb = new int[3];
			int cnt = 0;
			int temp = 1;

			for (int i = 1; i < s.length(); i++) {
				if (s.charAt(i) == ',' || s.charAt(i) == ';') {
					rgb[cnt++] = Integer.valueOf(s.substring(temp, i));
					temp = i + 1;
				}
			}

			return new Farbe(rgb[0], rgb[1], rgb[2]);
		}
	}
}
