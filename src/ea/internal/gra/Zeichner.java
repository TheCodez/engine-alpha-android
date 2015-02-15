/*
 * Engine Alpha ist eine anfaengerorientierte 2D-Gaming Engine.
 * 
 * Copyright (c) 2011-2014 Michael Andonie and Contributors
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

package ea.internal.gra;

import ea.*;
import ea.android.GameActivity;
import ea.android.GameSzenenActivity;
import ea.android.TouchEvent;
import ea.ui.Button;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Dies ist das Panel, in dem die einzelnen Dinge gezeichnet werden
 * 
 * @author Michael Andonie, Niklas Keller <me@kelunik.com>
 */
public class Zeichner extends View
{
	/**
	 * Das Intervall, in dem das Fenster upgedated wird.
	 */
	public static final int UPDATE_INTERVALL = 20;
	
	/**
	 * Die Kamera.<br />
	 * Letzendlich wird das gezeichnet, was sich in ihr befindet
	 */
	private Kamera cam;
	
	/**
	 * Das BoundingRechteck, dass das Panel in seiner Groesse beschreibt.
	 */
	private BoundingRechteck groesse;
	
	/**
	 * Der Knoten, der die statischen Objekte beinhaltet.
	 */
	private Knoten statNode = new Knoten();
	
	/**
	 * Gibt an, ob der Thread noch arbeiten soll.
	 */
	//private boolean work = true;

	private Farbe hintergrundFarbe = Farbe.Schwarz;

	
	public Zeichner(Context context) {
		super(context);
	}
	
	public void reInit(int sizeX, int sizeY)
	{
		groesse = new BoundingRechteck(0, 0, sizeX, sizeY);
	}
	
	
	/**
	 * Konstruktor fuer Objekte der Klasse Zeichner
	 * 
	 * @param x
	 *            Die Groesse des Einflussbereichs des Panels in Richtung X.
	 * @param y
	 *            Die Groesse des Einflussbereichs des Panels in Richtung Y.
	 * @param c
	 *            Die Kamera, deren Sicht grafisch dargestellt werden soll.
	 */
	public void init(int x, int y, Kamera c) {
		
		this.groesse = new BoundingRechteck(0, 0, x, y);
		this.cam = c;
	}
	
	public void hintergrundFarbeSetzen(Farbe farbe) {
		hintergrundFarbe = farbe;
	}
	
	
	/**
	 * @return Die Kamera, die dieser Zeichner aufruft
	 */
	public Kamera cam() {
		return cam;
	}
	
	/**
	 * @return Der statische Basisknoten
	 */
	public Knoten statNode() {
		return statNode;
	}
	
	/**
	 * Meldet einen Vordergrund an.
	 * 
	 * @param vordergrund
	 *            Der neue Vordergrund
	 */
	public void anmelden(Raum vordergrund) {
		//this.vordergrund = vordergrund;
	}
	
	/**
	 * Meldet den zu zeichnenden Hintergrund an.
	 * 
	 * @param hintergrund
	 *            Der neue Hintergrund
	 */
	public void hintergrundAnmelden(Raum hintergrund) {
		//this.hintergrund = hintergrund;
	}
	
	/**
	 * Loescht den absoluten Vordergrund
	 */
	void vordergrundLoeschen() {
		//vordergrund = null;
	}
	
	/**
	 * @return Ein BoundingRechteck, dass die BReite und Hoehe des Fensters hat.
	 */
	public BoundingRechteck masse() {
		return groesse;
	}
	
	/**
	 * Android Methode zum Zeichnen
	 */
	@Override
	public void onDraw(Canvas g)
	{
		super.onDraw(g);
		
		g.drawColor(hintergrundFarbe.alsInt());
		
		if(cam != null)
			cam.zeichne(g);
		
		invalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		super.onTouchEvent(event);
		
		
		if(getContext() instanceof GameActivity)
		{
			((GameActivity)getContext()).touchReagieren(event.getX(), event.getY(), touchEventConvertieren(event));
			
			// Testet ein touch event fuer Knöpfe
			for(Raum b : ((GameActivity)getContext()).uiWurzel.alleElemente())
			{
				if(b instanceof Button)
				{
					((Button)b).touch(event.getX(), event.getY(), event);
				}
			}
		}
		else if(getContext() instanceof GameSzenenActivity)
		{
			((GameSzenenActivity)getContext()).touchReagieren(event.getX(), event.getY(), touchEventConvertieren(event));
			
			// Testet ein touch event fuer Knöpfe
			for(Raum b : ((GameSzenenActivity)getContext()).szeneGeben().wurzel.alleElemente())
			{
				if(b instanceof Button)
				{
					((Button)b).touch(event.getX(), event.getY(), event);
				}
			}
		}
		
		
		
		
		return true;
	}
	
	private TouchEvent touchEventConvertieren(MotionEvent event)
	{
		TouchEvent e = TouchEvent.Keins;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN)
			e = TouchEvent.Gedrueckt;
		else if(event.getAction() == MotionEvent.ACTION_UP)
			e = TouchEvent.Losgelassen;
		
		return e;
	}
	
}
