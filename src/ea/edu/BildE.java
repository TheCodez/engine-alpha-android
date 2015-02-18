package ea.edu;

import ea.Bild;
import ea.android.GameActivity;
import ea.android.Spiel;
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

		if(Spiel.instanz() instanceof GameActivity)
		{
			((GameActivity)(Spiel.instanz())).wurzel.add(this);
		}	
		else if(Spiel.instanz() instanceof GameActivity && ((GameSzenenActivity)(Spiel.instanz())).szeneGeben() != null)
		{
			((GameSzenenActivity)(Spiel.instanz())).szeneGeben().hinzufuegen(this);
		}
	}
}