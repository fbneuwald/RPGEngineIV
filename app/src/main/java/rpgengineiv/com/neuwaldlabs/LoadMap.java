package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
/**
 * Created by fneuwald on 26/03/14.
 */


public class LoadMap extends Action{


    GameView gv;
    String fileName;
    int initPosX, initPosY;
    Map currentMap;

    public LoadMap(String MapFileName, GameView gameview, int TriggerPosX, int TriggerPosY, int initPosX, int initPosY, Map pCurrentMap)
    {
        fileName = MapFileName;
        gv = gameview;
        x = TriggerPosX;
        y = TriggerPosY;
        this.initPosX = initPosX;
        this.initPosY = initPosY;
        this.currentMap = pCurrentMap;
    }

    @Override
    public boolean IsActionFinished()
    {
        Map m = MapFactory.LoadMap(fileName, gv, initPosX, initPosY);
        if(m!=null)
        {
            gv.GameObjects.remove(currentMap);
            gv.GameObjects.add(0,m);
            gv.currentMap = m;
        }
        return true;
    }
    @Override
    public void TouchAction(MotionEvent event){}
    @Override
    public void Draw(Canvas canvas)
    {
        Paint p = new Paint();
        p.setColor(Color.YELLOW);
        Rect r = new Rect(0,0,gv.getWidth(),gv.getHeight());
        Bitmap bmp = BitmapFactory.decodeResource(gv.getResources(),R.drawable.kepler22b);
        canvas.drawBitmap(bmp,null,r,p);
        canvas.drawText("Loading...", gv.getWidth()/2-40,gv.getHeight()/2-40,p);


    }
}
