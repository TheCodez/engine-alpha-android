package ea.android;

public class GameInstanz 
{
	//private GameInstanz instanz = new GameInstanz();
	
	public static BasisActivity get()
	{
		GameActivity ga = GameActivity.get();
		GameSzenenActivity gsa = GameSzenenActivity.get();
		
		if(ga != null)
			return ga;
		else if(gsa != null)
			return gsa;
		else
			return null;
	}
}
