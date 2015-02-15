package ea.android;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import ea.Farbe;
import ea.Kamera;
import ea.Knoten;
import ea.Manager;
import ea.Raum;
import ea.Szene;
import ea.Ticker;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;

public abstract class GameSzenenActivity extends BasisActivity implements Ticker, SensorEventListener
{
	private static final String TAG = GameSzenenActivity.class.getSimpleName();
	
	private Szene aktuelleSzene;
	
	public Knoten wurzel;
	
	private static GameSzenenActivity instanz;
	
	public int breite;
	public int hoehe;
	
	public boolean tick;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        breite = getWindowManager().getDefaultDisplay().getWidth();
        hoehe = getWindowManager().getDefaultDisplay().getHeight();
        
        cam = new Kamera(breite, hoehe, new Zeichenebene());
		zeichner = new Zeichner(this);
		zeichner.init(breite, hoehe, cam);
        
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);        
        
        mediaPlayer = new MediaPlayer();
        
        wurzel = new Knoten();
        
        instanz = this; 
		
        intervall = 10;
		manager = new Manager();
		manager.anmelden(this, intervall);
		
		aktuelleSzene = init();
		
		if(aktuelleSzene == null)
		{
			aktuelleSzene = new Szene();
		}
		
		cam.wurzel().add(aktuelleSzene.wurzel);
		
		
		//@todo wurzel von GameSzenenActvity entfernen
		for(Raum r : wurzel.alleElemente())
		{
			if(r != null)
				aktuelleSzene.hinzufuegen(r);
			wurzel.leeren();
		}
		
		setContentView(zeichner);
	}

	
	@Override
	protected void onDestroy()
	{		
		super.onDestroy();
		System.exit(0);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
	    super.onConfigurationChanged(newConfig);
	    
	    final int alteBreite = breite;
	    final int alteHoehe = hoehe;
	    
	    breite = alteHoehe;
	    hoehe = alteBreite;
	    
	    cam.reInit(breite, hoehe);
	    zeichner.reInit(breite, hoehe);
	}
	
	@Override
	protected void onPause() 
	{
	    super.onPause();
	    sensorManager.unregisterListener(this);
	    //if(manager != null)
	    //	manager.anhalten(this);
	}
		
	@Override
	protected void onResume()
	{
	    super.onResume();
	    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	   // if(manager != null)
	    	//manager.starten(this, intervall);
	}
	
	public void tickerIntervallSetzen(int intervall)
	{
		this.intervall = intervall;
		manager.abmelden(this);
		manager.anmelden(this, intervall);
	}
	
	public void tickerStoppen()
	{
		manager.anhalten(this);
	}
	
	public void nachrichtLangeAnzeigen(final String text)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(GameSzenenActivity.this, text, Toast.LENGTH_LONG).show();
				
			}
		});
	}
	
	public void nachrichtKurzAnzeigen(final String text)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(GameSzenenActivity.this, text, Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	
	public void setzeSzene(Szene szene)
	{
		if(szene != null)
		{
			cam.wurzel().leeren();
			aktuelleSzene = szene;
			cam.wurzel().add(aktuelleSzene.wurzel);
		}	
	}
	
	public Szene szeneGeben()
	{
		return aktuelleSzene;
	}
	
	public static GameSzenenActivity get()
	{
		return instanz;
	}
	
	public void titelSetzen(String titel)
	{
		
		setTitle(titel);
	}
	
	public void hintergrundFarbeSetzen(Farbe farbe)
	{
		if(zeichner != null)
			zeichner.hintergrundFarbeSetzen(farbe);
	}
	
	public void vibrieren(long milliSekunden)
	{
		vibrator.vibrate(milliSekunden);
	}
		
	public void setzeBildschirmOrientierung(BildOrientierung orientation)
	{
		if(orientation == BildOrientierung.Landschaft)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	/**
	 *  Diese Methode muss in unterklassen ueberschrieben werden
	 */
	public abstract Szene init();
	
    public void tick()
    {
    	if(aktuelleSzene != null)
    	{
    		aktuelleSzene.tick();
    	}
    }
	
	public void touchReagieren(float x, float y, TouchEvent event)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.touchReagieren(x, y, event);
		}
	}
	
	public void sensorReagieren(float x, float y, float z, Sensor sensor)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.sensorReagieren(x, y, z, sensor);
		}
	}
	
	public void tasteGedruecktReagieren(int code)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.tasteGedruecktReagieren(code);
		}
	}
	
	public void tasteLosgelassenReagieren(int code)
	{
		if(aktuelleSzene != null)
		{
			aktuelleSzene.tasteLosgelassenReagieren(code);
		}
	}
	
	public Zeichner zeichnerGeben()
	{
		return zeichner;
	}
	
	public boolean zufallsBoolean() 
	{
		return zufall.nextBoolean();
	}

	
	public int zufallsZahl(int obergrenze) 
	{
		if (obergrenze < 0) {
			Log.e(TAG, "Achtung!! Fuer eine Zufallszahl muss die definierte Obergrenze (die inklusiv in der Ergebnismenge ist) eine nichtnegative Zahl sein!!");
		}
		return zufall.nextInt(obergrenze + 1);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		Sensor s = event.sensor;
		
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		sensorReagieren(x, y, z, s);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    tasteGedruecktReagieren(keyEventKonvertieren(keyCode, event));

	    return super.onKeyDown(keyCode, event);
	}
	
	private int keyEventKonvertieren(int key, KeyEvent event)
	{
		if (key == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        return Taste.Zurueck;
	    }
		
		return Taste.Keine;
	}
}
