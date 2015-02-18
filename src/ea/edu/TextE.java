package ea.edu;

import ea.*;
import ea.android.GameActivity;
import ea.android.Spiel;
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
		
		if(Spiel.instanz() instanceof GameActivity)
		{
			((GameActivity)(Spiel.instanz())).wurzel.add(this);
		}	
		else
		{
			((GameSzenenActivity)(Spiel.instanz())).szeneGeben().hinzufuegen(this);
		}
	}
}