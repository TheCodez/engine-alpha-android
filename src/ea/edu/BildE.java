package ea.edu;

import ea.Bild;
import ea.android.GameActivity;
import ea.android.GameInstanz;
import ea.android.GameSzenenActivity;

/**
 * Diese Klasse wrapt die Funktionen der Klasse <code>Bild</code> und
 * stellt sie für die lokale BlueJ-API mieglichst klar bereit.
 * 
 * @author Michael Andonie
 * 
 */
@Deprecated
public class BildE extends Bild {
	private static final long serialVersionUID = -3131852267825713616L;

	public BildE(int x, int y, String pfad) {
		super(x, y, pfad);

		if(GameInstanz.get() instanceof GameActivity)
			((GameActivity)(GameInstanz.get())).wurzel.add(this);
		else if(((GameSzenenActivity)(GameInstanz.get())).szeneGeben() == null)
			((GameSzenenActivity)(GameInstanz.get())).wurzel.add(this);
		else
			((GameSzenenActivity)(GameInstanz.get())).szeneGeben().hinzufuegen(this);
	}
}