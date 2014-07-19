package ea;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import ea.internal.collision.BoxCollider;
import ea.internal.collision.Collider;
import ea.internal.collision.ColliderGroup;

public class Sprite extends Raum
{
	private static ArrayList<Sprite> sprites;
	private int intervall = 100;
	private int aktuelle;
	private Bild[] bilder;
	
	private boolean animiert = true;
	
	private boolean spiegelX;
	private boolean spiegelY;
	
	private boolean istSpriteSheet;
	private Bild spriteSheet;
	
	private int spriteBreite;
	private int spriteHoehe;
	private int zeilen, spalten;
	private int aktuellesFrame;
	private long frameTicker;
	private int framePeriode;
	private Rect sourceRect;
	private int anzahlBilder;
	
	static 
	{
		sprites = new ArrayList<Sprite>();
		
		Manager.standard.anmelden((new Ticker() {
			int runde = 0;

			@Override
			public void tick() {
				runde++;
				try {
					
					for (Sprite s : sprites) {
						if (s.animiert()) {
							s.animationsSchritt(runde);
						}
					}
					
				} catch (java.util.ConcurrentModificationException e) {
				}
			}
		}), 1);
	}
	
	public Sprite(float x, float y, String ...bilder)
	{
		super.position = new Punkt(x, y);
		
		this.bilder = new Bild[bilder.length];
		
		animiert = bilder.length > 1;
		istSpriteSheet = false;
		
		for(int i = 0; i < bilder.length; i++)
		{
			this.bilder[i] = new Bild(x, y, bilder[i]);
		}
		
		sprites.add(this);
	}
	
	public Sprite(String ...bilder)
	{
		this(0, 0, bilder);
	}

	public Sprite(String spriteSheet, float x, float y, int fps, int anzahlBilder)
	{
		super.position = new Punkt(x, y);
		
		istSpriteSheet = true;
		this.spriteSheet = new Bild(x, y, spriteSheet);
		
		this.anzahlBilder = anzahlBilder;
		spriteBreite = this.spriteSheet.bild().getWidth() / anzahlBilder;
		spriteHoehe = this.spriteSheet.bild().getHeight();
		sourceRect = new Rect(0, 0, spriteBreite, spriteHoehe);
		framePeriode = 1000 / fps;
		frameTicker = 0l;
		
		animiert = true;
		
		sprites.add(this);
	}
	
	public boolean animiert()
	{
		return animiert;
	}
	
	public void animationsSchritt(int runde)
	{
		if(!istSpriteSheet)
		{
			if (runde % intervall  != 0) {
			return;
			}
			if (aktuelle == bilder.length - 1) {
				aktuelle = 0;
			} else {
				aktuelle++;
			}
		}
		else
		{
			if (System.currentTimeMillis() > frameTicker + framePeriode) 
			{
				frameTicker = System.currentTimeMillis();
				aktuellesFrame++;
				if (aktuellesFrame >= anzahlBilder) {
					aktuellesFrame = 0;
				}
			}
			
			this.sourceRect.left = aktuellesFrame * spriteBreite;
			this.sourceRect.right = this.sourceRect.left + spriteBreite;
		}
	}
	
	public void intervallSetzen(int intervall)
	{
		this.intervall = intervall;
	}
	
	@Override
	public void zeichnen(Canvas g, BoundingRechteck r) 
	{
	
		if(!istSpriteSheet)
		{
			Matrix m = new Matrix();
			Paint p = new Paint();
			Bitmap bild = bilder[aktuelle].bild();
			
			if(spiegelX && spiegelY) {
				m.preScale(-1, -1);
			} else if(spiegelX) {
				m.preScale(-1, 1);
			} else if(spiegelY) {
				m.preScale(1, -1);
			} else {
				m.preScale(1, 1);
			}
		    
			Bitmap endBild = Bitmap.createBitmap(bild, 0, 0, bild.getWidth(), bild.getHeight(), m, false);
		    
			g.drawBitmap(endBild, position.x, position.y, p);
		}
		
		else
		{
			Rect destRect = new Rect(position.x(), position.y(), position.x() + spriteBreite, position.y() + spriteHoehe);
			
			Matrix m = new Matrix();
			Paint p = new Paint();
			
			if(spiegelX && spiegelY) {
				m.preScale(-1, -1);
			} else if(spiegelX) {
				m.preScale(-1, 1);
			} else if(spiegelY) {
				m.preScale(1, -1);
			} else {
				m.preScale(1, 1);
			}
		    
			Bitmap endBild = Bitmap.createBitmap(spriteSheet.bild(), 0, 0, spriteSheet.bild().getWidth(), spriteSheet.bild().getHeight(), m, false);
			
			g.drawBitmap(endBild, sourceRect, destRect, null);
		}
	}

	@Override
	public BoundingRechteck dimension() {
		if (bilder != null && bilder[aktuelle] != null) {
			return new BoundingRechteck(position.x, position.y, bilder[0].dimension().breite, bilder[0].dimension().hoehe);
		} else {
			return new BoundingRechteck(position.x, position.y, bilder[aktuelle].dimension().breite, bilder[aktuelle].dimension().hoehe);
		}
	}

	@Override
	public Collider erzeugeCollider() {
		ColliderGroup gc = new ColliderGroup();
		for(BoundingRechteck r : flaechen()) {
			gc.addCollider(BoxCollider.fromBoundingRechteck(new Vektor(r.x, r.y), r));
		}
		return gc;
	}
	
	public void setzeSpiegelX(boolean s)
	{
		spiegelX = s;
	}
	
	public void setzeSpiegelY(boolean s)
	{
		spiegelY = s;
	}
	
	public int aktuellesFrameGeben()
	{
		return aktuellesFrame;
	}
}
