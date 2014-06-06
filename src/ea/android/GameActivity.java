package ea.android;

import java.util.Random;

import ea.*;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;

enum BildOrientation
{
	Portrait,
	Landschaft
}

/*
 * TODO GameActivity soll abstract werden und als Basic Klasse fuer jedes Spiel gelten
 * 
*/
public abstract class GameActivity extends Activity implements Ticker, SensorEventListener
{
	
	public Knoten wurzel;
		
	private Zeichner zeichner;	
	public Kamera cam;
	
	protected Manager manager;
	
	private final Random zufall = new Random();
	
	private static GameActivity instanz;
	
	public int breite;
	public int hoehe;
	
	public boolean tick;

	
	private SensorManager sensorManager;
	private Sensor sensor;
	private Vibrator vibrator;
	
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
        
        setContentView(zeichner);   
        
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL);
        
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        
        instanz = this; 
		
		manager = new Manager();
		manager.anmelden(this, 10);
		
		init();
	}

	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		
		if(wurzel != null)
			wurzel.leeren();
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
	    super.onConfigurationChanged(newConfig);
	    
	    final int alteBreite = breite;
	    final int alteHoehe = hoehe;
	    
	    breite = alteHoehe;
	    hoehe = alteBreite;
	}
	
	@Override
	protected void onPause() 
	{
	    super.onPause();
	    sensorManager.unregisterListener(this);
	    //manager.anhalten(this);
	}
		
	@Override
	protected void onResume()
	{
	    super.onResume();
	    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	    //manager.starten(this, 20);
	}
	
	public void tickerIntervallSetzen(int intervall)
	{
		manager.abmelden(this);
		manager.anmelden(this, intervall);
	}
	
	public void tickerStoppen()
	{
		manager.anhalten(this);
	}

	public static GameActivity get()
	{
		if(instanz != null)
			return instanz;
		return null;
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
	
	
	public void setzeOrientation(BildOrientation orientation)
	{
		if(orientation == BildOrientation.Landschaft)
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		else
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	/**
	 *  Diese Methode muss in unterklassen ueberschrieben werden
	 */
	public abstract void init();
	
    public void tick()
    {
    	
    }
	
	public void touchReagieren(float x, float y, MotionEvent event)
	{
		
	}
	
	public void sensorReagieren(float x, float y, float z, Sensor sensor)
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
			System.err.println("Achtung!! Fuer eine Zufallszahl muss die definierte Obergrenze (die inklusiv in der Ergebnismenge ist) eine nichtnegative Zahl sein!!");
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
	
}
