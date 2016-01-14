package rpgengineiv.com.neuwaldlabs;

import java.util.Random;

/**
 * Created by fneuwald on 18/05/2014.
 */
public class SpawnPoint {
    private int AvgSpawnTime, maxMonsters, radius, timer;
    private Monster monster;
    private Map map;
    private Position startPoint;
    private GameView GV;
    private int NumberOfActiveMonsters;

    public SpawnPoint(Position p, Monster m, int maximumMonsters, int pInitialNumberOfMosnters, int averageSpawnTime, int pRadius, GameView pGV, Map pMap)
    {
        startPoint = p;
        maxMonsters = maximumMonsters;
        AvgSpawnTime = averageSpawnTime;
        monster = m;
        GV = pGV;
        NumberOfActiveMonsters = 0;
        radius = pRadius;
        map = pMap;

        for(int i=0; i<pInitialNumberOfMosnters; i++)
        {
            Monster mons = m.clone(GenerateRandomPosition());
            mons.SetOriginSpawnPoint(this);
            map.addMonster(mons);
            NumberOfActiveMonsters++;
        }

        Random rnd = new Random();
        int i = rnd.nextInt();
        i = i % AvgSpawnTime*2*Configuration.FPS;
        if(i<0) i = i *-1;
        timer = i;
     }


    private Position GenerateRandomPosition()
    {
        boolean isValidPosition  =false;
        Position p = new Position(0,0);
        Random rnd = new Random();
        int x,y;
        int Retry = 10;
        while(!isValidPosition)
        {
            x = rnd.nextInt() % radius + startPoint.x;
            y = rnd.nextInt() % radius + startPoint.y;
            if(map.IsWalkable(y, x)) {
                p = new Position(x,y);
                isValidPosition = true;
            }
            Retry--;
            if(Retry==0)
                return null;

        }
        return p;
    }
    public void update()
    {
        if(timer==0)
        {
            Random rnd = new Random();
            int i = rnd.nextInt();
            i = i % AvgSpawnTime*2*Configuration.FPS; //mutiply by FPS to convert to seconds.
            if(i<0) i = i *-1;
            timer = i;
            if(NumberOfActiveMonsters<maxMonsters) {
                Position p = GenerateRandomPosition();
                if(p!=null) // P will be null if timedout on number of retries for a valida position
                {
                    Monster mons = monster.clone(p);
                    mons.SetOriginSpawnPoint(this);
                    map.addMonster(mons);

                    NumberOfActiveMonsters++;
                }

                // else it will skip this spawn and wait for a clear position.
            }
        }
        else
               timer--;




    }



    public void DecreaseActiveMonsterCount()
    {
        NumberOfActiveMonsters--;
    }
}
