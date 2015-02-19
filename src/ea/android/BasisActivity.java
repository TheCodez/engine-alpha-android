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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import ea.Farbe;
import ea.Kamera;
import ea.Manager;
import ea.Ticker;
import ea.internal.gra.Zeichenebene;
import ea.internal.gra.Zeichner;

public class BasisActivity extends Activity implements Ticker, SensorEventListener
{
	private static final long serialVersionUID = 2024202201493570532L;

	protected Zeichner zeichner;	
	public Kamera cam;
	
	protected Manager manager;
	
	protected final Random zufall = new Random();
	
	protected int breite;
	protected int hoehe;
	
	public boolean tick;
	protected int intervall;
	
	protected SensorManager sensorManager;
	protected Sensor sensor;
	protected Vibrator vibrator;
	
	protected MediaPlayer mediaPlayer;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        
        // Vollbild erzwingen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        
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

        intervall = 10;
		manager = new Manager();
		manager.anmelden(this, intervall);		
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
	}
		
	@Override
	protected void onResume()
	{
	    super.onResume();
	    sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
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
				Toast.makeText(BasisActivity.this, text, Toast.LENGTH_LONG).show();
			}
		});
	}
	
	public void nachrichtKurzAnzeigen(final String text)
	{
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(BasisActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	protected int keyEventKonvertieren(int key, KeyEvent event)
	{
		if (key == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	        return Taste.Zurueck;
	    }
		
		return Taste.Keine;
	}
	
	public Zeichner zeichnerGeben()
	{
		return zeichner;
	}
	
	public MediaPlayer mediaPlayerGeben()
	{
		return mediaPlayer;
	}
	
	public int breite()
	{
		return breite;
	}
	
	public int hoehe()
	{
		return hoehe;

	}
	
	public boolean zufallsBoolean() 
	{
		return zufall.nextBoolean();
	}

	
	public int zufallsZahl(int obergrenze) 
	{
		if (obergrenze < 0) {
			Log.e(getClass().getSimpleName(), "Achtung!! Fuer eine Zufallszahl muss die definierte Obergrenze (die inklusiv in der Ergebnismenge ist) eine nichtnegative Zahl sein!!");
		}
		return zufall.nextInt(obergrenze + 1);
	}
	public void titelSetzen(String titel)
	{	
		setTitle(titel);
	}
	
	public void vibrieren(long milliSekunden)
	{
		vibrator.vibrate(milliSekunden);
	}
		
	public void setzeBildschirmOrientierung(BildOrientierung orientation)
	{
		if(orientation == BildOrientierung.Landschaft)
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		else
		{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
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
	
	public void onSensorChanged(SensorEvent event) {
		Sensor s = event.sensor;
		
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		
		sensorReagieren(x, y, z, s);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	public void tick() {
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    tasteGedruecktReagieren(keyEventKonvertieren(keyCode, event));

	    return super.onKeyDown(keyCode, event);
	}
}
