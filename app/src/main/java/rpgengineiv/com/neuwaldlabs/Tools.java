package rpgengineiv.com.neuwaldlabs;

import android.view.MotionEvent;

/**
 * Created by fneuwald on 24/12/13.
 */
public class Tools {
    public static int GetDirection(MotionEvent event, GameView GV)
    {
        int direction=-1;
        try
        {
            //Direction = 0 up, 1 left, 2 down, 3 right
            int tan0 = (int)event.getX()-GV.getWidth()/2;
            int tan1 = (int)event.getY()-GV.getHeight()/2;
            direction = (int) ((Math.atan2(tan0, tan1) / (Math.PI / 2) + 2) % 4);
        }
        catch(Exception e)
        {
            String a = e.getMessage();
        }
        return direction;
    }

    public static boolean IsValidMovement(int direction, Map m)
    {
        int newCenterColumn = m.GetNewCenterColumn(direction);
        int newCenterRow = m.GetNewCenterRow(direction);

        // check if within the map
        if((newCenterColumn>=1)&&(newCenterColumn<=m.getMaxColumn())&&
                (newCenterRow>=1)&&(newCenterRow<=m.getMaxRow()))
        {
            //check if match a NPC
            for (NPC sprite : m.GetNPCs())
            {
                if((sprite.GetColumn()==newCenterColumn)&&
                        (sprite.GetRow()==newCenterRow))
                {
                    Conversation c = new Conversation (sprite, m.GV.getWidth(),m.GV.getHeight(),m.GV);
                    m.GV.AddActiveAction(c);
                    return false;

                }
            }

            for (Action a : m.getActions())
            {
                if((a.GetX()==newCenterColumn)&&
                        (a.GetY()==newCenterRow))
                {
                    m.GV.AddActiveAction(a);
                }
            }
            //Is a walkable place on Current Map
            if(!m.IsWalkable(newCenterRow, newCenterColumn))
                return false;


            return true;
        }
        else
            return false;

    }
    public static int GetDirectionBasedOnPosition(Position PlayerPos, Position NextPos)
    {
        //Direction = 0 up, 1 left, 2 down, 3 right
        if(PlayerPos.x<NextPos.x) //RIGHT
            return 3;
        if(PlayerPos.x>NextPos.x) //LEFT
            return 1;
        if(PlayerPos.y>NextPos.y) // UP
            return 0;
        if(PlayerPos.y<NextPos.y) // DOWN
            return 2;

        return -1;
    }


}
