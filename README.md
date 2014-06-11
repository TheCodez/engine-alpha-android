##Engine-Alpha Android
Eine auf Android geportete Engine-Alpha

##Verwendung:
1. Die [engine-alpha-android.jar](https://github.com/skatermichi/engine-alpha-android/releases/download/0.8/engine-alpha-android.jar) hier, oder im Release Tab herunterladen.
2. Danach die engine-alpha-android.jar in den "libs" Ordner des Projekts kopieren.

##Grundlegendes Beispiel
```java
import ea.*;
import ea.android.GameActivity;

public class TestActivity extends GameActivity 
{
	@Override
	public void init() 
	{
        Rechteck box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Gruen);
        
        wurzel.add(box);
	}
}
```

##Fortgeschrittenes Beispiel
```java
import ea.*;
import ea.android.GameActivity;
import android.hardware.Sensor;
import android.view.MotionEvent;

public class Test extends GameActivity 
{
	private Rechteck box;
	private float verschX;
	
    @Override
    public void init() 
    {
        box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Gruen);

        wurzel.add(box);
    }
    
    @Override
    public void tick()
    {
    	box.bewegen(new Vektor(verschX, 0));
    }
    
    @Override
    public void touchReagieren(float x, float y, MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			box.sichtbarSetzen(false);
		}
		else if(event.getAction() == MotionEvent.ACTION_UP)
		{
			box.sichtbarSetzen(true);
		}
	}
	
    @Override
	public void sensorReagieren(float x, float y, float z, Sensor sensor)
	{
		verschX = x;
	}
}
```