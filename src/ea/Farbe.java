package ea;

import android.graphics.Color;

public class Farbe 
{
	public int r, g, b, a;
	
	public static final Farbe Blau = new Farbe(0, 0, 255); 
	public static final Farbe Rot = new Farbe(255, 0, 0); 
	public static final Farbe Gruen = new Farbe(0, 255, 0);
	public static final Farbe Schwarz = new Farbe(0, 0, 0); 
	public static final Farbe Weiss = new Farbe(255, 255, 255); 
	
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
	
	public int farbeZuInt()
	{
		return Color.rgb(r, g, b);
	}
}
