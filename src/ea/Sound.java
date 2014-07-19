package ea;

import java.io.IOException;

import ea.android.GameActivity;
import android.media.MediaPlayer;

public class Sound 
{

	private MediaPlayer player;
	
	public Sound(String datei) 
	{
		player = GameActivity.get().mediaPlayer;
        
        try 
        {
            player.setDataSource(datei);
            player.prepare();
        } 
        catch (IllegalArgumentException e) 
        {
                e.printStackTrace();
        } 
        catch (IllegalStateException e) 
        {
                e.printStackTrace();
        } 
        catch (IOException e)
        {
                e.printStackTrace();
        }
	}
	
	public void play()
	{
		player.start();
	}
	
	public void stop()
	{
		player.stop();
	}
}
