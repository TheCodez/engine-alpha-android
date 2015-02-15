package ea.android;

import java.util.Random;

import ea.Kamera;
import ea.Manager;
import ea.internal.gra.Zeichner;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;

public class BasisActivity extends Activity
{
	public Zeichner zeichner;	
	public Kamera cam;
	
	protected Manager manager;
	
	protected final Random zufall = new Random();
	
	public int breite;
	public int hoehe;
	
	public boolean tick;
	protected int intervall;
	
	protected SensorManager sensorManager;
	protected Sensor sensor;
	protected Vibrator vibrator;
	
	public MediaPlayer mediaPlayer;
	
}
