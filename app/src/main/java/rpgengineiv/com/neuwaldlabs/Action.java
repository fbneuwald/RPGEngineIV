package rpgengineiv.com.neuwaldlabs;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class Action
{
    int x;
    int y;

    public boolean IsActionFinished(){return true;}
    public void TouchAction(MotionEvent event){}
    public void Draw(Canvas canvas){}
    public int GetX(){return x;}
    public int GetY(){return y;}

    public static Action CreateAction(String action, int TriggerPosX, int TriggerPosY, String ResourceName)
    {
        return null;
    }

}