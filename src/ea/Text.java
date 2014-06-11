package ea;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import ea.internal.collision.Collider;

public class Text extends Raum
{
	private boolean istUnterstrichen;
	private float groesse;
	private String text;
	public Text(float x, float y, String text, float textGroesse)
	{
		super.position = new Punkt(x, y);
		this.text = text;
		groesse = textGroesse;
		farbeSetzen(Farbe.Schwarz);
	}
	
	public Text(float x, float y, String text)
	{
		this(x, y, text, 24);
	}
	
	public Text(String text)
	{
		this(0, 0, text);
	}
	
	public boolean istUnterstrichen() {
		return istUnterstrichen;
	}

	public void unterstrichenSetzen(boolean istUnterstrichen) {
		this.istUnterstrichen = istUnterstrichen;
	}
	
	public void textSetzen(String text)
	{
		this.text = text;
	}
	
	public String textGeben()
	{
		return text;
	}

	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) {
		Paint p = new Paint();
		p.setUnderlineText(istUnterstrichen);
		p.setTextSize(groesse);
		p.setColor(farbe.alsInt());
		p.setAntiAlias(istGlatt());
		
		if(r.schneidetBasic(this.dimension())) {
			g.drawText(text, position.x - r.x, position.y - r.y, p);
		}
	}

	@Override
	public BoundingRechteck dimension() {
		//TODO Textbreite und Hoehe
		return new BoundingRechteck(position.x, position.y, (text.length() * groesse) / 2.5f , groesse);
	}

	@Override
	public Collider erzeugeCollider() {
		return erzeugeLazyCollider();
	}

	public float groesseGeben() {
		return groesse;
	}

	public void setzeGroesse(float groesse) {
		this.groesse = groesse;
	}
}
