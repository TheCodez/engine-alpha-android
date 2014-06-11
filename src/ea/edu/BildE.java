package ea.edu;

import ea.Bild;
import ea.android.GameActivity;

/**
 * Diese Klasse wrapt die Funktionen der Klasse <code>Bild</code> und
 * stellt sie für die lokale BlueJ-API mieglichst klar bereit.
 * 
 * @author Michael Andonie
 * 
 */
public class BildE extends Bild {
	private static final long serialVersionUID = -3131852267825713616L;

	public BildE(int x, int y, String pfad) {
		super(x, y, pfad);
		GameActivity.get().wurzel.add(this);
	}
}