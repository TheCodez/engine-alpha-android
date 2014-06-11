package ea;

import android.graphics.Color;

public final class Farbe 
{
	public int r, g, b, a;
	
	/**
	 * Basierend auf Werten von : https://kb.iu.edu/data/aetf.html
	 */
	public static final Farbe Blau = new Farbe(0, 0, 255); 
	public static final Farbe Rot = new Farbe(255, 0, 0); 
	public static final Farbe Gruen = new Farbe(0, 128, 0);
	public static final Farbe Schwarz = new Farbe(0, 0, 0); 
	public static final Farbe Weiss = new Farbe(255, 255, 255);
	public static final Farbe Braun = new Farbe(165, 42, 42);
	public static final Farbe HimmelBlau = new Farbe(135, 206, 235); 
	public static final Farbe Rosa = new Farbe(255, 192, 203);
	public static final Farbe Orange = new Farbe(255, 165, 0);
	public static final Farbe Olive = new Farbe(128, 128, 0); 
	public static final Farbe Gold = new Farbe(255, 215, 0);
	public static final Farbe Schokolade = new Farbe(210, 105, 30);
	public static final Farbe Beige = new Farbe(245, 245, 220);
	public static final Farbe GelbGruen = new Farbe(154, 205, 50);
	public static final Farbe KoenigsBlau = new Farbe(65, 105, 225);
	public static final Farbe Aquamarin = new Farbe(127, 255, 212);
	public static final Farbe Mokka = new Farbe(255, 228, 181);
	public static final Farbe Weizen = new Farbe(245, 222, 179);
	public static final Farbe Tomate = new Farbe(255, 99, 71);
	
	public Farbe(int r, int g, int b, int a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Farbe(int r, int g, int b)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 255;
	}
	
	public int alsInt()
	{
		return Color.rgb(r, g, b);
	}
	
	public static Farbe vonString(String farbe)
	{
		final String f = farbe.toLowerCase();
		
		switch(f)
		{
			case "blau":
				return Farbe.Blau;
			case "rot":
				return Farbe.Rot;
			case "gruen":
				return Farbe.Gruen;
			case "weiss":
				return Farbe.Weiss;
			case "schwarz":
				return Farbe.Schwarz;
			case "orange":
				return Farbe.Orange;
			case "braun":
				return Farbe.Braun;
			case "rosa":
				return Farbe.Rosa;
			case "aquamarin":
				return Farbe.Aquamarin;
			default:
				return Farbe.Weiss;
		}
	}
	
	/**
	 * Gibt eine Farbe mit dem Halben Alpha-Wert dieser zurueck.
	 * 
	 * @return eine Farbe desselbe Farbtons wie diese, jedoch doppelt so durchsichtig wie diese.
	 */
	public Farbe halbesAlpha() {
		return new Farbe(r, g, b, a / 2);
	}

	/**
	 * Gibt an, ob diese Farbe ueberhaupt nicht durchsichtig ist.
	 * 
	 * @return <code>true</code>, wenn der Alpha-Wert der Farbe <b>nicht 255</b> ist, sonst automatisch <code>false</code>.
	 */
	public boolean undurchsichtig() {
		return a == 255;
	}
}
