package ea;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import ea.android.GameInstanz;

public class Sound 
{

	private final MediaPlayer player;
	
	public Sound(String datei) 
	{
		player = GameInstanz.get().mediaPlayerGeben();
        
		setzeSound(datei);
	}
	
	public void setzeSound(String datei)
	{
        try 
        {
        	AssetFileDescriptor descriptor = GameInstanz.get().getAssets().openFd(datei);
        	long start = descriptor.getStartOffset();
        	long end = descriptor.getLength();
        	
        	player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(descriptor.getFileDescriptor(), start, end);
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
	
	public void pause()
	{
		player.pause();
	}
	
	public void unpause()
	{
		player.start();
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
