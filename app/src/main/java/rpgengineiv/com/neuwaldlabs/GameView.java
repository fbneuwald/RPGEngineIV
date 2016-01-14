package rpgengineiv.com.neuwaldlabs;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView{

	private SurfaceHolder holder;
    private boolean PauseGame = false;
    private GamePause pause;
	private GameLoopThread gameLoop;
    private List<Action> ActiveActions = new ArrayList<Action>();
	//UI objects
	public List<DrawableUI> GameObjects = new ArrayList<DrawableUI>();
    public List<Effect> Effects = new ArrayList<Effect>();

    public Character player;
    public Map currentMap;
	
		
	//--------------------METHODS-----------------------------------//
	
	/*************
	 * Constructor
	 * @param context
	 */
	public GameView(Context context) {
		super(context);
		setLongClickable(true);
		gameLoop = new GameLoopThread(this);
		holder = getHolder();
		holder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				gameLoop.setRunning(false);
				Destroy();
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				Initialize();
				gameLoop.setRunning(true);
				gameLoop.start();
				
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
			}
		});
	}
	
	/******************************
	 * Runs when surface is created.
	 */
	public void Initialize()
	{
		GameObjects.add(new MainMenu(this));
	}
	
	/**********************************
	 * Runs when surface is destroyed.
	 */
	public void Destroy()
	{
		for (DrawableUI a : GameObjects)
		{
			a.Destroy();
		}
	}
	
	
	/**************************
	 * Runs right before Draw on every frame
	 */
	public void Update()
	{


        if(PauseGame)
           return;
        if(ActiveActions.isEmpty())
        {
            for (DrawableUI a : GameObjects)
            {
                if(!a.Disabled)
                    a.Update();
            }
        }
        else
            for (Action a : ActiveActions)
            {
                if(a.IsActionFinished())
                    ActiveActions.remove(a);
            }

        if(!Effects.isEmpty())
        {
            for (Effect a : Effects)
            {
                if(a.IsFinished())
                    Effects.remove(a);
                else
                    a.Update();
            }
        }
	}


    public void AddActiveAction(Action a)
    {
        ActiveActions.add(a);
    }
	/**************************
	 * Controls and Draw all Game drawing activities
	 */
	public void Draw(Canvas c)
	{

        if(PauseGame)
        {
            pause.onDraw(c);
            return;
        }
        for (DrawableUI a : GameObjects)
		{
			a.Draw(c);
		}
        for (Action a : ActiveActions)
        {
            a.Draw(c);
        }
        for (Effect a : Effects)
        {
            a.Draw(c);
        }

	}
	
	/*******************************
	 * Touch logic
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{			
		synchronized (getHolder()) 
		{

            if(PauseGame)
            {
                pause.OnTouch(event);
                return super.onTouchEvent(event);
            }
            CreatePlayerPath(event);
            if(ActiveActions.isEmpty())
            {
                for (DrawableUI a : GameObjects)
                {
                    if(!a.Disabled)
                        a.Touched(event);
                }
            }
            else
                for (Action a : ActiveActions)
                {
                    a.TouchAction(event);
                }
		}
		
		return super.onTouchEvent(event);
	}

    //When Hardware back Button is pressed Activity will call this method.
    public void backButtonPressed()
    {
        pause = new GamePause(this,player);
        PauseGame = true;
    }

    public boolean IsPaused()
    {return PauseGame;}
    public void UnPause()
    {
        PauseGame = false;
    }


    private void CreatePlayerPath(MotionEvent event)
    {
        ArrayList<Position> Path = null;
        if(currentMap!=null) {
            if((event!=null)&&(event.getAction()==MotionEvent.ACTION_UP)) {
                Position playerPosition = new Position(currentMap.getCenterColumn(), currentMap.getCenterRow());
                Position targetPosition = GetMapTouchedPosition(event);

                if(currentMap.IsWalkable(targetPosition.y,targetPosition.x)) {
                    Path = GetPlayerPath(playerPosition, targetPosition);
                    player.setNewPath(Path);
                    synchronized(this.getHolder()) {
                        Effects.add(new Effect(this, targetPosition, currentMap));
                    }

                }
            }
        }

        if(Path!=null)
            for(int i = 0; i<Path.size();i++)
            {
                Log.d("PATH: ", Path.get(i).x + ", " + Path.get(i).y );
            }

    }

    private Position GetMapTouchedPosition(MotionEvent event)
    {
        Position clicked = null;
        float distanceFromCenterX, distanceFromCenterY;
        float incrementX, incrementY;
        distanceFromCenterX = event.getX() - this.getWidth()/2;
        if(distanceFromCenterX<0)
            incrementX = Configuration.CellSize/2;
        else
            incrementX = Configuration.CellSize/2 *-1;
        int Column= Math.round((distanceFromCenterX + incrementX)/Configuration.CellSize)+currentMap.getCenterColumn();
        distanceFromCenterY = event.getY() - this.getHeight()/2;
        if(distanceFromCenterY>0)
            incrementY = Configuration.CellSize/2;
        else
            incrementY = Configuration.CellSize/2 *-1;
        int Row = Math.round(distanceFromCenterY + incrementY)/Configuration.CellSize + currentMap.getCenterRow();
        clicked = new Position(Column, Row);
        return clicked;
    }

    private ArrayList<Position> GetPlayerPath(Position playerCurrentPos, Position targetPos)
    {
        ArrayList<Position> p = new ArrayList<Position>();
        Position next = null;
        int directionX, directionY;
        int distanceX, distanceY;
        distanceX = playerCurrentPos.x - targetPos.x;
        if (distanceX<0)
        {
            directionX = 1;
            distanceX = distanceX * -1;
        }
        else
            directionX = -1;
        distanceY = playerCurrentPos.y - targetPos.y;
        if (distanceY<0) {
            distanceY = distanceY * -1;
            directionY = 1;
        }
        else
            directionY = -1;

        if((distanceX > distanceY)&&(currentMap.IsWalkable(playerCurrentPos.y, playerCurrentPos.x + directionX))) {
            next = new Position(playerCurrentPos.x + directionX, playerCurrentPos.y);
        }
        else {
            if(currentMap.IsWalkable(playerCurrentPos.y+directionY, playerCurrentPos.x))
                next = new Position(playerCurrentPos.x, playerCurrentPos.y+directionY);
            else
                if((currentMap.IsWalkable(playerCurrentPos.y, playerCurrentPos.x + directionX)))
                    next = new Position(playerCurrentPos.x + directionX, playerCurrentPos.y);

        }
        if(next!=null) {
            if (!((next.x == targetPos.x) && (next.y == targetPos.y)))
                p = GetPlayerPath(next, targetPos);
            p.add(0, next);
        }
        return p;

    }


}
