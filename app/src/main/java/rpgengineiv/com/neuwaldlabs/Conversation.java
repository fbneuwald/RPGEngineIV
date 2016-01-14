package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;

/**
 * Created by fneuwald on 26/03/14.
 */

public class Conversation extends Action {

    private int TTL = 0;
    private NPC npc;
    private int left;
    private int right;
    private int top;
    private int bottom;
    private int BoxWidth = 300;
    private int BoxHeight = 100;
    private GameView gameView;
    private Bitmap bmp;
    private boolean actionFinished = false;

    public Conversation(NPC pNPC, int width, int height, GameView gv)
    {
        npc = pNPC;
        top = 2;
        bottom = BoxHeight;
        left = width/2 - BoxWidth / 2;
        right = width/2 + BoxWidth / 2;
        gameView = gv;
        bmp = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.border);
    }

    @Override
    public void TouchAction(MotionEvent event)
    {
        if(event.getActionMasked()==MotionEvent.ACTION_DOWN)
        {
            //on first touch complete the full speech
            if(TTL< npc.GetConversation().length())
                TTL = npc.GetConversation().length();

                //on second touch complete the action
            else
                actionFinished = true;
        }
    }

    @Override
    public boolean IsActionFinished()
    {
        TTL++;
        if(actionFinished)
            return true;
        else
            return false;

    }

    @Override
    public void Draw(Canvas canvas)
    {
        int Stringstart = 0;
        int StringEnd = TTL > npc.GetConversation().length() ? npc.GetConversation().length():TTL;
        String text  = npc.GetConversation().substring(Stringstart, StringEnd);
        String Lines[] = text.split("\\|");

        Paint mypaint	= new Paint();
        mypaint.setColor(Color.BLACK);

        Rect dst = new Rect(left,top, right, bottom);
        canvas.drawBitmap(bmp, null, dst, mypaint);
        mypaint.setColor(Color.YELLOW);
        Typeface tf = Typeface.createFromAsset(gameView.getContext().getAssets(), "fonts/hanalei.ttf");
        mypaint.setTypeface(tf);
        mypaint.setTextSize(16);
        for(int i=0; i<Lines.length;i++)
            canvas.drawText(Lines[i], left + 25, top + 15 + 18 * (i+1), mypaint);
    }
}
