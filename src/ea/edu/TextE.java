package ea.edu;

import ea.*;
import ea.android.GameActivity;
import ea.android.GameInstanz;
import ea.android.GameSzenenActivity;

@Deprecated
public class TextE extends Text {

	/**
	 * Konstruktor erstellt einen fertig sichtbaren Text.
	 * Seine Position lässt sich leicht über die geerbten Methoden ändern.
	 * @param content	Der Inhalt des Texts.
	 */
	public TextE(String content) {
		super(140, 100, content);
		farbeSetzen("Gruen");
		
		if(GameInstanz.get() instanceof GameActivity)
		{
			((GameActivity)(GameInstanz.get())).wurzel.add(this);
		}
		else if(((GameSzenenActivity)(GameInstanz.get())).szeneGeben() == null)
		{
			((GameSzenenActivity)(GameInstanz.get())).wurzel.add(this);
		}	
		else
		{
			if(((GameSzenenActivity)(GameInstanz.get())).szeneGeben() != null)
			{
				((GameSzenenActivity)(GameInstanz.get())).szeneGeben().hinzufuegen(this);
			}	
		}
	}
}