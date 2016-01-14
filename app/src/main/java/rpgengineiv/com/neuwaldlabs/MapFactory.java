package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
/**
 * Created by fneuwald on 25/12/13.
 */
public class MapFactory
{



        public static Map LoadMap(String mapFileName,GameView gv)
        {
            return LoadMap(mapFileName, gv,-1,-1);
        }
        public static Map LoadMap(String mapFileName,GameView gv, int initialPosRow, int initialPosCol)
        {
            String BMPName, walkMatrixLine;
            int Columns, Rows, InitPosCol, InitPosRow, i,j;

            int id = gv.getResources().getIdentifier(mapFileName, "raw", "rpgengineiv.com.neuwaldlabs");
            InputStream is = gv.getResources().openRawResource(id);
            BufferedInputStream bis = null;
            DataInputStream dis = null;
            try {

                bis = new BufferedInputStream(is);
                dis = new DataInputStream(bis);
                BMPName= dis.readLine().toString();
                Bitmap bmp = BitmapFactory.decodeResource(gv.getResources(), gv.getResources().getIdentifier(BMPName, "drawable", "rpgengineiv.com.neuwaldlabs"));
                Columns = Integer.parseInt(dis.readLine().toString());
                Rows = Integer.parseInt(dis.readLine().toString());
                InitPosCol = Integer.parseInt(dis.readLine().toString());
                InitPosRow = Integer.parseInt(dis.readLine().toString());
                InitPosCol = initialPosCol == -1 ? InitPosCol : initialPosCol;
                InitPosRow = initialPosRow == -1 ? InitPosRow : initialPosRow;

                Position p = new Position (InitPosCol, InitPosRow);
                Map m =  new Map (bmp, Columns,Rows, gv, p,mapFileName);
                i=0;

                while (i<Rows) {
                    walkMatrixLine = dis.readLine().toString();
                    j=0;
                    for(String s : walkMatrixLine.split(","))
                    {
                        if(Integer.parseInt(s)==1)
                            s="1";
                        m.SetWalkable(j, i,Integer.parseInt(s)==1? true : false);
                        j++;
                    }
                    i++;
                }
                if(!dis.readLine().toString().equalsIgnoreCase("[NPC]"))
                    return null;

                int col, row, initcol, initrow;
                String speech, resourceName, s;
                s = dis.readLine().toString();
                while (!s.equalsIgnoreCase("[ACTIONS]"))
                {
                    row = Integer.parseInt(s);
                    col = Integer.parseInt(dis.readLine().toString());
                    resourceName = dis.readLine().toString();
                    speech= dis.readLine().toString();
                    m.AddNPC( col,row, resourceName, speech, 0);
                    s = dis.readLine().toString();
                }
                s = dis.readLine().toString();
                while (!s.equalsIgnoreCase("[SPAWNPOINTS]"))
                {
                    resourceName = s;
                    row = Integer.parseInt(dis.readLine().toString());
                    col = Integer.parseInt(dis.readLine().toString());
                    initcol = Integer.parseInt(dis.readLine().toString());
                    initrow = Integer.parseInt(dis.readLine().toString());
                    m.AddLoadMapAction(row, col, resourceName, initrow, initcol);
                    s = dis.readLine().toString();
                }


                int interval, max, initial, x, y, radius;
                int dex, str, con, maxgold, mingold, xp;
                String Name, image, line;
                String a[];
                Monster mons;
                SpawnPoint sp;
                while (dis.available() != 0)
                {
                    interval = Integer.parseInt(dis.readLine().toString());
                    max = Integer.parseInt(dis.readLine().toString());
                    initial = Integer.parseInt(dis.readLine().toString());
                    x = Integer.parseInt(dis.readLine().toString());
                    y = Integer.parseInt(dis.readLine().toString());
                    radius = Integer.parseInt(dis.readLine().toString());
                    line = dis.readLine().toString();
                    a = line.split(",");
                    dex = Integer.parseInt(a[0]);
                    str = Integer.parseInt(a[1]);
                    con = Integer.parseInt(a[2]);
                    Name = a[3];
                    image = a[4];
                    maxgold = Integer.parseInt(a[5]);
                    mingold = Integer.parseInt(a[6]);
                    xp = Integer.parseInt(a[7]);
                    mons = new Monster(dex,str,con,new Position(0,0),
                            BitmapFactory.decodeResource(gv.getResources(), gv.getResources().getIdentifier(image, "drawable", "rpgengineiv.com.neuwaldlabs")),
                            gv,Name,maxgold,mingold,m,xp);
                    sp = new SpawnPoint(new Position(x,y),mons,max, initial,interval,radius,gv,m);
                    m.addSpawnPoint(sp);
                }
                is.close();
                bis.close();
                dis.close();

                return m;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;


        }



}
