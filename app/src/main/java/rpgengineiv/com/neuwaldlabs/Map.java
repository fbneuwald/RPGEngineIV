package rpgengineiv.com.neuwaldlabs;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
        import android.view.MotionEvent;

        import java.util.ArrayList;
        import java.util.List;

public class Map extends DrawableUI{
    private Bitmap bmp;
    private int mapWidth;
    private int mapHeight;
    private int columns;
    private int rows;
    private int ViewPortSizeColumns;
    private int ViewPortSizeRows;
    private int centerColumn;
    private int centerRow;
    private int NewCenterRow;
    private int NewCenterColumn;
    private float centerX;
    private float centerY;
    private float halfViewportWidth;
    private float halfViewportHeight;
    private boolean animationStarted = false;
    private int numberAnimationFrames = 3;
    private int currentFrame = 0;
    private int currentNPCAnimationFrame;
    private float columnWidth;
    private float columnHeight;
    private float incrementX;
    private float incrementY;
    private boolean walkingMatrix[][];
    private int direction = -1;
    private String MapFileName;
    private List<NPC> NPCs = new ArrayList<NPC>();
    private float incrementNPCsX;
    private float incrementNPCsY;
    private List<Action> Actions = new ArrayList<Action>();
    private List<SpawnPoint> Spawns = new ArrayList<SpawnPoint>();
    private List<Monster> MonsterList = new ArrayList<Monster>();
    private List<Monster> toRemove= new ArrayList<Monster>();




    /********************
     * Map Setup
     */
    public Map(Bitmap bm, int Columns, int Rows, GameView g, Position pCenter, String pMapFileName)
    {
        this.bmp = bm;
        this.MapFileName = pMapFileName;
        this.mapHeight = bm.getHeight();
        this.mapWidth = bm.getWidth();
        updateViewPort(g, Configuration.CellSize, Configuration.CellSize);
        this.columns = Columns;
        this.rows = Rows;
        GV= g;
        this.centerColumn = pCenter.x;
        this.centerRow = pCenter.y;
        this.columnWidth = ((float)mapWidth/(float)columns);
        this.columnHeight = ((float)mapHeight/(float)rows);
        centerX = (centerColumn * columnWidth)- columnWidth/2;
        centerY = (centerRow * columnHeight) - columnHeight/2;
        halfViewportWidth = (ViewPortSizeColumns * columnWidth)/2;
        halfViewportHeight = (ViewPortSizeRows * columnHeight)/2;
        this.numberAnimationFrames = Configuration.NumberofAnimationFrames;


        walkingMatrix = new boolean[Rows][Columns];
        for(int i = 0; i<Rows;i++)
            for(int j=0; j<Columns; j++)
                walkingMatrix[i][j] = true;
    }
    public void updateViewPort(GameView g, int cellWidth, int cellHeight)
    {
        this.ViewPortSizeColumns = g.getWidth()/cellWidth;
        this.ViewPortSizeRows = g.getHeight()/cellHeight;
    }


    public void AddNPC(int row, int column, String BMPResouceName, String Speech, int Direction)
    {
        int id = GV.getResources().getIdentifier(BMPResouceName, "drawable", "rpgengineiv.com.neuwaldlabs");
        Bitmap bmp = BitmapFactory.decodeResource(GV.getResources(), id);
        NPCs.add(new NPC(bmp, row, column, Direction, Speech, this,this.GV));
    }


    public void AddLoadMapAction(int row, int column, String MapFileName, int initPosRow, int initPosCol)
    {
       Actions.add(new LoadMap(MapFileName, GV, row, column, initPosRow, initPosCol, this));
    }



    /*********************************
     * Map Updating Logic
     */
    private void setUpdateVariables(int direction)
    {
        if(!animationStarted)
        {	if(direction!= -1)
        {
            animationStarted = true;
            currentFrame = 0;
            //Direction = 0 up, 1 left, 2 down, 3 right
            switch(direction)
            {
                case 0:
                    incrementY = - columnHeight / numberAnimationFrames;
                    incrementX = 0;
                    break;
                case 1:
                    incrementX = - columnWidth / numberAnimationFrames;
                    incrementY = 0;
                    break;
                case 2:
                    incrementY = columnHeight / numberAnimationFrames;
                    incrementX = 0;
                    break;
                case 3:
                    incrementX = columnWidth / numberAnimationFrames;
                    incrementY = 0;
                    break;
            }
            NewCenterRow = GetNewCenterRow(direction);
            NewCenterColumn = GetNewCenterColumn(direction);
            if(!Tools.IsValidMovement(direction,this))
            {
                direction = -1;
                animationStarted = false;
            }
        }
        }
    }

    public void Touched(MotionEvent event)
    {

            if((event!=null)&&(event.getAction()==MotionEvent.ACTION_DOWN))
            {

                for(Monster m : MonsterList)
                   if(m.getMosnterRectangle().contains(Math.round(event.getX()),Math.round(event.getY()))) {
                       GV.player.AttackMonster(m);
                       m.setTarget(true);
                   }
                   else
                       m.setTarget(false);
//                direction = Tools.GetDirection(event, GV);

            }
//            if((event!=null)&&(event.getAction()==MotionEvent.ACTION_UP))
//                direction = -1;


    }

    public void Update()
    {

        if(!animationStarted) {
            if (GV.player.getCharMovingPath() == null)
                direction = -1;
            else {
                if (GV.player.getCharMovingPath().size() == 0)
                    direction = -1;
                else
                    direction = Tools.GetDirectionBasedOnPosition(new Position(this.centerColumn, this.getCenterRow()), GV.player.getNextPathPosition());
            }
            GV.player.setDirection(direction);
            setUpdateVariables(direction);
        }
        if(animationStarted)
        {
            //set incremental
            currentFrame++;
            centerY = centerY + incrementY;
            centerX = centerX + incrementX;
            updateNPCs(direction);
            //check if animation ended
            if(currentFrame == numberAnimationFrames)
            {
                animationStarted = false;
                incrementX = 0;
                incrementY = 0;
                centerRow = NewCenterRow;
                centerColumn = NewCenterColumn;
            }
        }

        for (Monster m : MonsterList)
            m.Update();
        for(Monster m1: toRemove) {
            GV.player.addXP(m1.getXP());
            GV.player.addGold(m1.getGold());
            MonsterList.remove(m1);

        }
        toRemove.clear();

        for(SpawnPoint sp : Spawns)
            sp.update();
    }



    public boolean IsWalkable(int row, int Column)
    {
        if((row<=0)||(row>walkingMatrix.length))
            return false;
        else
            if((Column<=0)||(Column>walkingMatrix[row-1].length))
                return false;
        if(walkingMatrix[row-1][Column-1]) {

                for (Monster m : MonsterList) {
                    if ((m.GetPosition().x == Column) && (m.GetPosition().y == row)) // there is a monster on that square
                        return false;
                }
            if((row==centerRow)&&(Column==centerColumn)) /// player is on that square
                return false;
        }
        return walkingMatrix[row-1][Column-1];
    }

    public void SetWalkable(int col, int row, boolean value)
    {
        walkingMatrix[row][col] = value;
    }

    private void updateNPCs(int direction)
    {
        // calculate the animation direction and screen increment for NPCs.
        if((currentNPCAnimationFrame==0))
        {
            if((direction!= -1))
            {
                currentNPCAnimationFrame++;
                //Direction = 0 up, 1 left, 2 down, 3 right
                switch(direction)
                {
                    case 0:
                        incrementNPCsY = - (float)Configuration.CellSize / Configuration.NumberofAnimationFrames;
                        incrementNPCsX= 0;
                        break;
                    case 1:
                        incrementNPCsX= - (float)Configuration.CellSize / Configuration.NumberofAnimationFrames;
                        incrementNPCsY = 0;
                        break;
                    case 2:
                        incrementNPCsY= (float)Configuration.CellSize / Configuration.NumberofAnimationFrames;
                        incrementNPCsX = 0;
                        break;
                    case 3:
                        incrementNPCsX = (float)Configuration.CellSize / Configuration.NumberofAnimationFrames;
                        incrementNPCsY= 0;
                        break;
                }

            }
        }
        else
            currentNPCAnimationFrame++;

        //Adjust NPCs on the screen during movement animations
        for (NPC sprite : NPCs) {
            sprite.UpdateX(incrementNPCsX);
            sprite.UpdateY(incrementNPCsY);

        }

        for (Monster m : MonsterList) {
            m.UpdateX(incrementNPCsX);
            m.UpdateY(incrementNPCsY);
        }

        for (Effect a: GV.Effects)
        {
            a.UpdateX(incrementNPCsX);
            a.UpdateY(incrementNPCsY);
        }
        //check if animation ended and reset variable
        if (currentNPCAnimationFrame == Configuration.NumberofAnimationFrames) {
            currentNPCAnimationFrame = 0;
            incrementNPCsX = 0;
            incrementNPCsY = 0;
        }

    }

    /*******
     * GETTERs
     */
    public int getCenterRow()
    {
        return centerRow;
    }
    public int getCenterColumn()
    {
        return centerColumn;
    }

    public int getMaxRow()
    {return rows;}

    public int getMaxColumn()
    {return columns;}

    public int GetNewCenterRow(int direction)
    {
        //Direction = 0 up, 1 left, 2 down, 3 right
        switch(direction)
        {
            case 0:
                return centerRow - 1;
            case 1:
            case 3:
                return centerRow;
            case 2:
                return centerRow + 1;
        }
        return centerRow;
    }

    public int GetNewCenterColumn(int direction)
    {
        //Direction = 0 up, 1 left, 2 down, 3 right
        switch(direction)
        {
            case 0:
            case 2:
                return centerColumn;
            case 3:
                return centerColumn +1;
            case 1:
                return centerColumn -1;
        }
        return centerColumn;
    }

    public List<NPC> GetNPCs()
    {
        return NPCs;
    }

    public List<Action> getActions()
    {
        return Actions;
    }

    /***********
     * Map Drawing Logic
     */
    public void Draw(Canvas canvas)
    {
        Paint myPaint = new Paint();
        myPaint.setColor(Color.BLACK);

       // created full map Bitmap rectangle
        Rect src = new Rect(Math.round(centerX - halfViewportWidth), Math.round(centerY - halfViewportHeight) ,
                Math.round(centerX + halfViewportWidth), Math.round(centerY + halfViewportHeight));

        //create the rectangle that will be show on screen
        Rect dst = new Rect(0, 0, GV.getWidth(), GV.getHeight());

        canvas.drawRect(0,0,GV.getWidth(), GV.getHeight(),myPaint);
        canvas.drawBitmap(bmp, src, dst, myPaint);

        //Print Position on the top left corner
        myPaint.setColor(Color.YELLOW);
        canvas.drawText(Integer.toString(centerColumn) , 10.0f, 10.0f, myPaint);
        canvas.drawText(Integer.toString(centerRow) , 30.0f, 10.0f, myPaint);

        //Draw Map NPC
        for (NPC sprite : NPCs)
            sprite.Draw(canvas);

        //Draw Map active Monsters
        for(Monster m : MonsterList)
            m.Draw(canvas);
    }

    public void addSpawnPoint(SpawnPoint sp)
    {
        this.Spawns.add(sp);
    }

    public void MarkToRemove(Monster m)
    {
        toRemove.add(m);
    }

    public void addMonster(Monster m)
    {
        MonsterList.add(m);
    }
}
