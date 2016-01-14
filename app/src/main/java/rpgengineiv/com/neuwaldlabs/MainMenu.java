package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

public class MainMenu extends DrawableUI{
	
	private Bitmap NewGame;
	private Bitmap LoadGame;
	private Bitmap QuitGame;
	private Rect NewGameRectangle;
	private Rect LoadGameRectangle;
	private Rect QuitGameRectangle;
	private Rect BackgroundReactagle;
	private Paint p = new Paint();
		
	public MainMenu(GameView pGV)
	{
		GV = pGV;
		p.setColor(Color.GREEN);
		BackgroundImage = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_background);
		BackgroundReactagle = new Rect(0,0,GV.getWidth(),GV.getHeight());
		NewGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_new_game);
		NewGameRectangle = new Rect(GV.getWidth()*5/100,20,GV.getWidth()*25/100,70);
		LoadGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_load_game);
		LoadGameRectangle = new Rect(GV.getWidth()*5/100,80,GV.getWidth()*25/100,130);
		QuitGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_quit_game);
		QuitGameRectangle = new Rect(GV.getWidth()*5/100,140,GV.getWidth()*25/100,190);
		backgroundMusic = MediaPlayer.create(GV.getContext(), R.raw.mainmenu_bgmusic);
		backgroundMusic.setLooping(true);
		backgroundMusic.start();		
	}
	
	public void Draw(Canvas c)
	{
		c.drawBitmap(BackgroundImage, null, BackgroundReactagle, p);
		c.drawBitmap(NewGame, null, NewGameRectangle, p);
		c.drawBitmap(LoadGame, null, LoadGameRectangle, p);
		c.drawBitmap(QuitGame, null, QuitGameRectangle, p);
	}
	
	public void Touched(MotionEvent event)
	{
		if((event!=null)&&(event.getAction()==MotionEvent.ACTION_DOWN))
		{
			if(NewGameRectangle.contains((int)event.getX(),(int)event.getY()))
					NewGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_new_game_over);
			if(LoadGameRectangle.contains((int)event.getX(),(int)event.getY()))
				LoadGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_load_game_over);
			if(QuitGameRectangle.contains((int)event.getX(),(int)event.getY()))
				QuitGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_quit_game_over);
		}
		if((event!=null)&&(event.getAction()==MotionEvent.ACTION_UP))
		{
			if(NewGameRectangle.contains((int)event.getX(),(int)event.getY()))
			{
				NewGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_new_game);
				GV.GameObjects.remove(this);
				this.Destroy();
				GV.GameObjects.add(new CharacterCreation(GV));
			}
			if(LoadGameRectangle.contains((int)event.getX(),(int)event.getY()))
			{
				LoadGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_load_game);
				//TODO:Add load screen.		
			}
			if(QuitGameRectangle.contains((int)event.getX(),(int)event.getY()))
			{
				QuitGame = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_quit_game);
				System.exit(0);
			}
		}
		
	}
	

}
