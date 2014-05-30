##Engine-Alpha Android
Eine auf Android geportete Engine-Alpha

##Verwenden in Eclipse

Wichtig: Das Android SDK muss installiert sein, sonst kommt es zu Fehlern.

1. In Eclipse File, dann Import Existing Android Code Into Workspace
2. Bei Root Directory, den Ordner von Github auswählen und auf Finish drücken


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