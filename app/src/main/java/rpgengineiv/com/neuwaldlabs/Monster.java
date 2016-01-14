package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.Random;

/**
 * Created by fneuwald on 18/05/2014.
 */
public class Monster extends DrawableUI {

    private static final int BMP_COLUMNS = 3;
    private static final int BMP_ROWS = 4;
    private static final int[] DIRECTION_TO_ANIMATE_MAP = {3, 1, 0, 2};
    private int width, height, direction;
    private static final int DISTANCETOSPOT = 4;
    private SpawnPoint sp;

   private int dex, con, str, hp, maxGold, minGold;
   private GameView gv;
   private String Name;
   private Position pos;
   private float x,y,incrementX,incrementY;;
    private Map map;

    private boolean isMoving = false;
    private boolean isTarget = false;
    private boolean spottedPlayer = false;
    private int movingCount, delay;
    private int xp;

    private Bitmap annimation;
    private int ANIM_ROWS=6;
    private int ANIM_COLS=5;
    private int CurrentFrame = 0;
    private int Anim_height,Anim_width;
    private int ANIM_DELAY=1;
    private int anim_delay=ANIM_DELAY;


   public Monster(int pDex, int pStr, int pCon, Position pPos, Bitmap pChrImage, GameView pGv, String pName, int GoldMax, int GoldMin,Map pMap, int pXP)
   {
        dex = pDex;
        str = pStr;
        con = pCon;
        pos = pPos;
        BackgroundImage = pChrImage;
        gv = pGv;
        Name = pName;
        maxGold = GoldMax;
        minGold = GoldMin;
        hp = con * 10;
       map = pMap;
       x = ((pos.x-pMap.getCenterColumn())*Configuration.CellSize)+(gv.getWidth()/2-Configuration.CellSize/2);
       y = ((pos.y-pMap.getCenterRow())*Configuration.CellSize)+(gv.getHeight()/2-Configuration.CellSize/2);
       this.width = BackgroundImage.getWidth()/ BMP_COLUMNS;
       this.height = BackgroundImage.getHeight() / BMP_ROWS;
       direction = 0;
       xp = pXP;
        //TODO: Add a loot list of items.

       GV = gv;
       annimation = BitmapFactory.decodeResource(gv.getResources(), R.drawable.darkness_001);
       this.Anim_height = annimation.getWidth()/ ANIM_COLS;
       this.Anim_width = annimation.getHeight() / ANIM_ROWS;

   }

   public long DistanceToPlayer()
   {
       // vector distance d^2  = (xa-xb)^2 + (ya-yb)^2
       double d = Math.pow(((double)map.getCenterColumn()-(double)pos.x),2.0 );
       d +=  Math.pow(((double)map.getCenterRow()-(double)pos.y),2.0 );
       d = Math.sqrt(d);
       long dis = Math.round(d);
       if(dis<0)
           dis = dis*-1;
       return dis;
   }

    public int getXP()
    {
        return xp;
    }

    public int getGold()
    {
        Random rnd = new Random(System.currentTimeMillis() );
        return (rnd.nextInt(maxGold-minGold)+minGold);
    }

    public void SetOriginSpawnPoint(SpawnPoint s)
    {
        sp = s;
    }
   public void Update()
   {
       if(this.hp <=0) {
           map.MarkToRemove(this);
           if(sp!=null) // If Monster came from Spawn Point. Notify that it is dead.
                sp.DecreaseActiveMonsterCount();
           gv.player.StopAttacking();
           return;
        }
       if(isTarget)
       {
           if(anim_delay==0) {
               anim_delay = ANIM_DELAY;
               CurrentFrame++;
               if (CurrentFrame >= ANIM_ROWS)
                   CurrentFrame = 0;
           }
           else
               anim_delay--;
       }

       int action= 1; //If action = 0 then walk, action = 1 attack player, else do nothing
       long distance;
       if(!isMoving)
       {
           distance = DistanceToPlayer();
            if(distance <=DISTANCETOSPOT)
            {
                if (distance==1)
                {

                    AttackProcessor.processMonsterAttack(this,gv.player);
                }
                else {
                    action=0;
                    if(pos.y <map.getCenterRow())
                        direction = 0;
                    else
                        if(pos.y > map.getCenterRow())
                            direction = 3;
                        else
                            if(pos.x <map.getCenterColumn())
                                direction = 2;
                            else
                                if(pos.x > map.getCenterColumn())
                                    direction = 1;
                    //TODO: Move towards the player: improve the movement when finds a barrer or is on the diagonal
                }
            }
           else
            {
                if(delay>0)
                {
                    delay--;
                    return;
                }
                Random rnd = new Random(System.nanoTime());
                action = rnd.nextInt() % 5;
                if(action==0)
                    direction = rnd.nextInt() % 4;
                if(direction<0)
                {
                    direction  = -1*direction;
                }

            }

           if(action==0)
           {

               int nextX = pos.x;
               int nextY = pos.y;
               switch (direction)
               {
                   case 3:
                       nextY--;
                       incrementY = -1*Configuration.CellSize/3;
                       incrementX = 0;
                       break;
                   case 1:
                       nextX--;
                       incrementX = -1*Configuration.CellSize/3;
                       incrementY = 0;
                       break;
                   case 0:
                       nextY++;
                       incrementY = Configuration.CellSize/3;
                       incrementX = 0;
                       break;
                   case 2:
                       nextX++;
                       incrementX = Configuration.CellSize/3;
                       incrementY = 0;
                       break;
               }
               if(map.IsWalkable(nextY, nextX))
               {
                   isMoving = true;
                   movingCount = 0;
                   pos.y = nextY;
                   pos.x = nextX;
               }
           }
           else {//do nothing the Monster just stands
           }
       }
       else
       {
           movingCount++;
           this.x += incrementX;
           this.y += incrementY;
               if (movingCount >= 3) {
                   movingCount = 0;
                   isMoving = false;
                   incrementX = 0;
                   incrementY = 0;
                   delay = 40;
               }
       }



   }

    public void Draw(Canvas c)
    {
        int srcy = direction * height;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        Rect src = new Rect(0+width*movingCount, srcy,  width+width*movingCount, srcy + height);
        Rect dst = new Rect(Math.round(x) , Math.round(y-5) , Math.round(x + (Configuration.CellSize)), Math.round(y-5 + (Configuration.CellSize)+3));
        c.drawBitmap(BackgroundImage, src, dst, p);


        p.setColor(Color.YELLOW);
        Rect HPcomplement = new Rect(Math.round(x + (Configuration.CellSize*1/10)), Math.round(y-7 + (Configuration.CellSize*1/10)-2), Math.round(x + (Configuration.CellSize)), Math.round(y - 7 + (Configuration.CellSize*1/10) - 7));
        c.drawRect(HPcomplement,p);


        p.setColor(Color.RED);
        Rect CurHP = new Rect(Math.round(x + (Configuration.CellSize*1/10)), Math.round(y-7 + (Configuration.CellSize*1/10)-2), Math.round(x + (Configuration.CellSize *this.hp/(con*10))), Math.round(y-7 + (Configuration.CellSize*1/10) - 7));
        c.drawRect(CurHP ,p);

        if(isTarget)
        {
            srcy =  Anim_height*CurrentFrame;
            int srcx = Anim_width*4;
            Rect src_anim = new Rect(srcx, srcy, srcx + Anim_width, srcy + Anim_height);
            c.drawBitmap(annimation, src_anim, dst, p);
        }

    }

    public Monster clone(Position p)
    {
        Monster m;
        m = new Monster(this.dex, this.str, this.con, p, BackgroundImage, gv, Name, maxGold, minGold,map,xp);
        return m;
    }

    public void UpdateX(float Increment)
    {
        x -= Increment;
    }

    public void UpdateY(float Increment)
    {
        y -= Increment;
    }

    public Position GetPosition()
    {
        return pos;
    }

    public void setTarget(boolean pIsTarget)
    {
        isTarget = pIsTarget;
    }

    public int GetDex()
    {
        return dex;
    }
    public int GetStr()
    {
        return str;
    }
    public int GetCon()
    {
        return con;
    }

    public int GetArmor() {return con;}

    public void takeDamage (int damage)
    {
        hp = hp - damage;
    }

    public Rect getMosnterRectangle()
    {
        Rect dst = new Rect(Math.round(x) , Math.round(y-5) , Math.round(x + (Configuration.CellSize)), Math.round(y-5 + (Configuration.CellSize)+3));
        return dst;
    }

    @Override
    public void Touched(MotionEvent event)
    {

    }

}