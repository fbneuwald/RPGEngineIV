package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.MotionEvent;

public class DrawableUI {
	
	protected Bitmap BackgroundImage;
	protected GameView GV;
	protected MediaPlayer backgroundMusic;
	protected boolean Disabled = false;
	
	public void Draw(Canvas c){return;}
	public void Update(){return;}
    public void Touched(MotionEvent event){return;}
	public void Destroy()
	{
		if(backgroundMusic!=null)
			backgroundMusic.stop();
			
	}
	

}
