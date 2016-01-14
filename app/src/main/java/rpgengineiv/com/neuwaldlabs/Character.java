package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by fneuwald on 24/12/13.
 */
public class Character extends DrawableUI
{
    private ArrayList<Position> CharMovingPath;
    private static final int BMP_COLUMNS = 3;
    private static final int BMP_ROWS = 4;
    private static final int[] DIRECTION_TO_ANIMATE_MAP = {3, 1, 0, 2};

    private int width,height;
    private int x,y;
    private int currentFrame = 1;
    private int animationRow = 0;
    private int direction;
    private boolean animationStarted = false;

    private int Dexterity, Strength, Constitution, HP, Level, Experience, Armor, Gold;
    private String Name;
    private Position CurrentPosition;

    //Character Items
    public Item Head;
    public Item Chest;
    public Item Legs;
    public Item LeftHand;
    public Item RightHand;
    public Item[] Inventory;
    private Monster AttackingMonster;


    public Character (int pDex, int pStr, int pCon, Position pPos, Bitmap pChrImage, GameView pGv, String pName)
    {
        Dexterity = pDex;
        Strength = pStr;
        Constitution = pCon;
        CurrentPosition = pPos;
        BackgroundImage = pChrImage;
        Name = pName;
        this.width = BackgroundImage.getWidth()/ BMP_COLUMNS;
        this.height = BackgroundImage.getHeight() / BMP_ROWS;
        GV = pGv;
        x = GV.getWidth()/2-Configuration.CellSize/2;
        y = GV.getHeight()/2- Configuration.CellSize/2;


        HP = this.GetMaximumHP();
        //TODO: Set the Inventory size
        Inventory = new Item[16];

        //TODO: Set Experience
        //TODO: Set next level experience

        Inventory[0] = new Item(0, 3, 0, 1, 0, "shield", "Wooden Shield",Item.LEFTHAND, GV);
        RightHand = new Item(3, 0, 1, 0, 2,"axe","Hand Axe",Item.RIGHTHAND, GV);
    }

   /*************************************************************
     * GAME DINAMICS
     */
    public void SetPlayerDirection(int direction)
    {
        if(direction != -1)
            animationRow = DIRECTION_TO_ANIMATE_MAP[direction];
    }


    public void setDirection(int d )
    {
        direction = d;
    }
    private void RegenHP()
    {
        //HP Regenerated is accumulated and added to HP when round generate a integer greater than 0;

        HP += Math.round(Configuration.StandardGenerationRate);

        if(HP>this.GetMaximumHP())
            HP = this.GetMaximumHP();
    }

    public void Touched(MotionEvent event)
    {
        /*
        if((event!=null)&&(event.getAction()==MotionEvent.ACTION_DOWN))
            direction = Tools.GetDirection(event, GV);
        if((event!=null)&&(event.getAction()==MotionEvent.ACTION_UP))
            direction = -1;
            */
    }

    public void Update()
    {

       RegenHP();

        //Direction = 0 up, 1 left, 2 down, 3 right
        //AnimationRow = 3 up, 1 left, 0 down, 2 right
        if((direction >= 0)&&(!animationStarted))
        {
            currentFrame = -1;
            animationStarted = true;
            SetPlayerDirection(direction);
        }
        else
            if((AttackingMonster != null)&&(AttackingMonster.DistanceToPlayer() ==1))
            {
                animationStarted = true;
                AttackProcessor.processPlayerAttack(this, AttackingMonster);
            }

        if(currentFrame == 2)
            animationStarted = false;

        if(animationStarted)
            currentFrame++;

    }

    public void Draw(Canvas c)
    {
        int srcx = currentFrame * width;
        int srcy = animationRow * height;
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        Rect src = new Rect(srcx, srcy, srcx + width, srcy + height);
        Rect dst = new Rect(x , y-5 , x + (Configuration.CellSize), y-5 + (Configuration.CellSize)+3);
        c.drawBitmap(BackgroundImage, src, dst, p);

        Paint myPaint = new Paint();
        myPaint.setColor(Color.YELLOW);
        Rect HPcomplement = new Rect(x + (Configuration.CellSize*1/10), y-7 + (Configuration.CellSize*1/10)-2, x + (Configuration.CellSize), y - 7 + (Configuration.CellSize*1/10) - 7);
        c.drawRect(HPcomplement,myPaint);


        myPaint.setColor(Color.BLUE);
        Rect CurHP = new Rect(x + (Configuration.CellSize*1/10), y-7 + (Configuration.CellSize*1/10)-2, x + (Configuration.CellSize *this.HP/this.GetMaximumHP()), y-7 + (Configuration.CellSize*1/10) - 7);
        c.drawRect(CurHP ,myPaint);
    }

    //GETTERS
    public int GetMaximumHP()
    {
        int MaximumHp = Constitution * 10 ;
        if(Level > 0 )
            MaximumHp = MaximumHp * Level;
        return MaximumHp;}
    public int GetArmor()
    { int totalArmor = Armor;
        if(LeftHand !=null)
            totalArmor += LeftHand.GetArmor();
        if(Head!=null)
            totalArmor += Head.GetArmor();
        if(RightHand !=null)
            totalArmor += RightHand.GetArmor();
        if(Chest!=null)
            totalArmor += Chest.GetArmor();
        if(Legs!=null)
            totalArmor += Legs.GetArmor();
        return totalArmor;
    }

    public void addGold(int pGold)
    {
        Gold += pGold;
    }
    public void removeGold(int pGold)
    {
        Gold -= pGold;
    }

    public int GetStrength()
    {
        int totalStrength = Strength;
        if(LeftHand !=null)
            totalStrength += LeftHand.GetStrIncrement();
        if(Head!=null)
            totalStrength += Head.GetStrIncrement();
        if(RightHand !=null)
            totalStrength += RightHand.GetStrIncrement();
        if(Chest!=null)
            totalStrength += Chest.GetStrIncrement();
        if(Legs!=null)
            totalStrength += Legs.GetStrIncrement();

        return totalStrength;
    }
    public int GetDexterity()
    {
        int totalDexterity = Dexterity;
        if(LeftHand !=null)
            totalDexterity += LeftHand.GetDexIncrement();
        if(Head!=null)
            totalDexterity += Head.GetDexIncrement();
        if(RightHand !=null)
            totalDexterity += RightHand.GetDexIncrement();
        if(Chest!=null)
            totalDexterity += Chest.GetDexIncrement();
        if(Legs!=null)
            totalDexterity += Legs.GetDexIncrement();

        return totalDexterity;
    }
    public int GetConstitution()
    {
        int totalConstitution = Constitution;
        if(LeftHand !=null)
            totalConstitution += LeftHand.GetConIncrement();
        if(Head!=null)
            totalConstitution += Head.GetConIncrement();
        if(RightHand !=null)
            totalConstitution += RightHand.GetConIncrement();
        if(Chest!=null)
            totalConstitution += Chest.GetConIncrement();
        if(Legs!=null)
            totalConstitution += Legs.GetConIncrement();

        return totalConstitution;
    }
    public String GetName()
    {return Name;}

    public int GetCurrentHP()
    {return HP;}

    public int GetLevel()
    {return Level;}

    public int GetExperience()
    {return Experience;}

    public void addXP(int pXP)
    {
        Experience+= pXP;
        //TODO: Check if Level UP
    }

    public Bitmap GetCharacterImage()
    {return BackgroundImage;}

    public void applyDamage(int damage)
    {
        HP = HP -damage;
    }

    public void AttackMonster(Monster m )
    {
        AttackingMonster = m;
    }

    public void StopAttacking()
    {
        AttackingMonster = null;
    }

    public void setNewPath(ArrayList<Position> p)
    {
        CharMovingPath = p;
    }

    public ArrayList<Position> getCharMovingPath()
    {
        return CharMovingPath;
    }

    public Position getNextPathPosition() {
        Position p = CharMovingPath.get(0);
        CharMovingPath.remove(0);
        return p;

    }


}
