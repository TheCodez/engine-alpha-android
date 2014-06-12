package ea.edu;

import ea.*;
import ea.android.GameActivity;

public class TextE extends Text {

	/**
	 * Konstruktor erstellt einen fertig sichtbaren Text.
	 * Seine Position lässt sich leicht über die geerbten Methoden ändern.
	 * @param content	Der Inhalt des Texts.
	 */
	public TextE(String content) {
		super(140, 100, content);
		farbeSetzen("Gruen");
		GameActivity.get().wurzel.add(this);
	}
}