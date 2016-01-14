package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by fneuwald on 19/12/2015.
 */
public class Effect extends DrawableUI {


    private final int ROWS = 4;
    private final int COLS = 5;
    private int width, height;
    private int currentFrame = 1;
    private int animationRow = 0;
    private Position position;
    private float x,y;
    private GameView GV;
    private int AnimationFrames = 5;

    public Effect(GameView gv, Position pPosition, Map currentMap)
    {
        GV = gv;
        BackgroundImage = BitmapFactory.decodeResource(gv.getResources(),R.drawable.magic_007);
        this.width = BackgroundImage.getWidth()/ COLS;
        this.height = BackgroundImage.getHeight() / ROWS;
        position = pPosition;
        x = ((position.x-currentMap.getCenterColumn())*Configuration.CellSize)+(gv.getWidth()/2-Configuration.CellSize/2);
        y = ((position.y-currentMap.getCenterRow())*Configuration.CellSize)+(gv.getHeight()/2-Configuration.CellSize/2);
    }

    @Override
    public void Draw(Canvas c){
        int srcy =  height;
        int srcx = width*currentFrame;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        Rect src = new Rect(srcx, srcy, srcx + width, srcy + height);
        Rect dst = new Rect(Math.round(x) , Math.round(y-5) , Math.round(x + (Configuration.CellSize)), Math.round(y-5 + (Configuration.CellSize)+3));
        c.drawBitmap(BackgroundImage, src, dst, p);
    }

    @Override
    public void Update(){
        if(currentFrame<AnimationFrames)
            currentFrame++;

    }

    public boolean IsFinished()
    {
        if(currentFrame>=AnimationFrames)
            return true;
        else
            return false;
    }
    public void UpdateX(float Increment)
    {
        x -= Increment;
    }

    public void UpdateY(float Increment)
    {
        y -= Increment;
    }
}
