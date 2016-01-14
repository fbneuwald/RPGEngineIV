package rpgengineiv.com.neuwaldlabs;

import android.graphics.Canvas;

/****
 * 
 * @author Felipe Neuwald
 *
 */


public class GameLoopThread extends Thread{

	private GameView view;
	private boolean running = false;
	
	public GameLoopThread(GameView v)
	{
		this.view = v;
	}
	
	public void setRunning(boolean run)
	{
		running = run;
	}
	
	@Override
	public void run()
	{
		long ticketsPS = 1000/Configuration.FPS;
		long startTime;
		long sleepTime;
		while(running)
		{
			startTime = System.currentTimeMillis();
			
			Canvas c = null;
			try
			{
				c = view.getHolder().lockCanvas();
				synchronized(view.getHolder())
				{
					view.Update();
					view.Draw(c);
				}
			}
			finally 
			{
				if(c!=null)
					view.getHolder().unlockCanvasAndPost(c);
			}
			
			sleepTime = ticketsPS - (System.currentTimeMillis() - startTime);
			try
			{
				if(sleepTime > 0)
					sleep(sleepTime);
				else
					sleep(10);
				
			}catch (Exception e){}
			
		}
	}
}
