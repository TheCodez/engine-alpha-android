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
import ea.Ticker;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;

enum BildOrientierung
{
	Portrait,
	Landschaft
}
	
public abstract class GameActivity extends Activity implements Ticker, SensorEventListener
{
	private static final String TAG = "GameActivity";
	
	public Knoten wurzel;
	public Knoten uiWurzel;
		
	private Zeichner zeichner;	
	public Kamera cam;
	
	protected Manager manager;
	
	private final Random zufall = new Random();
	
	private static GameActivity instanz;
	
	public int breite;
	public int hoehe;
	
	public boolean tick;
	private int intervall;
	
	private SensorManager sensorManager;
	private Sensor sensor;
	private Vibrator vibrator;
	
	public MediaPlayer mediaPlayer;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        breite = getWindowManager().getDefaultDisplay().getWidth();
        hoehe = getWindowManager().getDefaultDisplay().getHeight();
        
        cam = new Kamera(breite, hoehe, new Zeichenebene());
		zeichner = new Zeichner(this);
		zeichner.init(breite, hoehe, cam);
		
		cam.wurzel().add(wurzel = new Knoten());
		cam.wurzel().add(uiWurzel = new Knoten());
        
        setContentView(zeichner);   
        
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL);
        
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);        
        
        mediaPlayer = new MediaPlayer();
        
        instanz = this; 
		
        intervall = 10;
		manager = new Manager();
		manager.anmelden(this, intervall);
		
		init();
	}

	
	@Override
	protected void onDestroy()
	{
		
		if(wurzel != null)
			wurzel.leeren();
		
		if(uiWurzel != null)
			uiWurzel.leeren();
		
		if(vibrator != null)
			vibrator.cancel();
		
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

	public void nachrichtAnzeigen(final String text, final int dauer)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(GameActivity.this, text, dauer).show();
				
			}
		});
	}
	
	public static GameActivity get()
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
	
	public void uiElementHinzufuegen(Raum m)
	{
		if(!uiWurzel.besitzt(m) && !wurzel.besitzt(m))
		{
			uiWurzel.add(m);
		}
	}
	
	public void raumHinzufuegen(Raum m)
	{
		if(!wurzel.besitzt(m) && !uiWurzel.besitzt(m))
		{
			wurzel.add(m);
		}
	}
	
	/**
	 *  Diese Methode muss in unterklassen ueberschrieben werden
	 */
	public abstract void init();
	
    public void tick()
    {
    	
    }
	
	public void touchReagieren(float x, float y, TouchEvent event)
	{
		
	}
	
	public void sensorReagieren(float x, float y, float z, Sensor sensor)
	{
		
	}
	
	public void tasteGedruecktReagieren(int code)
	{
		
	}
	
	public void tasteLosgelassenReagieren(int code)
	{
		
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
	    tasteGedruecktReagieren(keyEventConvertieren(keyCode, event));

	    return super.onKeyDown(keyCode, event);
	}
	
	private int keyEventConvertieren(int key, KeyEvent event)
	{
		if (key == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        return Taste.Zurueck;
	    }
		
		return Taste.Keine;
	}
}
