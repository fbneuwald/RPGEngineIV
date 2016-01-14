package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by fneuwald on 25/03/14.
 */
public class NPC extends DrawableUI {
    private static final int BMP_COLUMNS = 3;
    private static final int BMP_ROWS = 4;
    private static final int[] DIRECTION_TO_ANIMATE_MAP = {3, 1, 0, 2};

    private Position CurrentPosition;
    private int width, height;
    private float x,y;
    private int CurrentDirection;
    private String Conversation;
    private Map currentMap;

    public NPC(Bitmap pNPCImage, int pColumn, int pRow, int pDirection, String pConversation, Map pMap, GameView pGV)
    {
        this.BackgroundImage = pNPCImage;
        CurrentPosition = new Position(pColumn,pRow);
        CurrentDirection = pDirection;
        Conversation = pConversation;
        currentMap = pMap;
        GV=pGV;
        this.width = BackgroundImage.getWidth()/ BMP_COLUMNS;
        this.height = BackgroundImage.getHeight() / BMP_ROWS;

        x = ((pColumn-currentMap.getCenterColumn())*Configuration.CellSize)+(pGV.getWidth()/2-Configuration.CellSize/2);
        y = ((pRow-currentMap.getCenterRow())*Configuration.CellSize)+(pGV.getHeight()/2-Configuration.CellSize/2);


    }

    public void Draw(Canvas c)
    {
        int srcy = CurrentDirection * height;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        Rect src = new Rect(0, srcy,  width, srcy + height);
        Rect dst = new Rect(Math.round(x) , Math.round(y-5) , Math.round(x + (Configuration.CellSize)), Math.round(y-5 + (Configuration.CellSize)+3));
        c.drawBitmap(BackgroundImage, src, dst, p);

    }

    public int GetColumn()
    {return CurrentPosition.x;}

    public int GetRow()
    {return CurrentPosition.y;}

    public void UpdateX(float Increment)
    {
        x -= Increment;
    }

    public void UpdateY(float Increment)
    {
        y -= Increment;
    }

    public String GetConversation()
    {
        return Conversation;
    }
}
