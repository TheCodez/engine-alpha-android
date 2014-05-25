##Engine-Alpha Android
Versuch die Engine-Alpha auf Android zu porten

##Grundlegendes Beispiel
```java
import ea.*;

public class TestActivity extends GameActivity 
{

	@Override
	public void init() 
	{
		super.init();
	
        Rechteck box = new Rechteck(180, 320, 120, 120);
        box.farbeSetzen(Farbe.Gruen);
        
        wurzel.add(box);
		
	}
}
```