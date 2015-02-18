package ea.android;

public class Spiel 
{
	public static BasisActivity instanz()
	{
		GameActivity ga = GameActivity.get();
		GameSzenenActivity gsa = GameSzenenActivity.get();
		
		if(ga != null)
		{
			return ga;
		}
		else if(gsa != null)
		{
			return gsa;
		}
		else
		{
			return null;
		}
	}
}
