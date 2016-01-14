package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by fneuwald on 18/05/2014.
 */
public class Item {

    private int damage;
    private int armor;
    private int DexIncrement;
    private int StrIncrement;
    private int ConIncrement;
    private Bitmap image;
    private String itemName;
    private int ItemType=-1;
    public static final int HEAD=0;
    public static final int CHEST=1;
    public static final int LEGS=2;
    public static final int LEFTHAND=3;
    public static final int RIGHTHAND=4;
    public static final int NONE=-1;


    public Item(int pDamage, int pArmor, int DexIncr, int StrIncr, int ConIncr, String pImagename, String pName, int pType, GameView gv)
    {

        damage = pDamage;
        armor = pArmor;
        itemName = pName;
        image = BitmapFactory.decodeResource(gv.getResources(), gv.getResources().getIdentifier(pImagename, "drawable", "rpgengineiv.com.neuwaldlabs"));
        DexIncrement = DexIncr;
        StrIncrement = StrIncr;
        ConIncrement = ConIncr;
        ItemType = pType;

    }

    public Item(int pDamage, int pArmor, int DexIncr, int StrIncr, int ConIncr, Bitmap pImage, String pName, int pType)
    {
        damage = pDamage;
        armor = pArmor;
        itemName = pName;
        image = pImage;
        DexIncrement = DexIncr;
        StrIncrement = StrIncr;
        ConIncrement = ConIncr;
        ItemType = pType;

    }

    public int GetDamage()
    {return damage;}
    public int GetArmor()
    {return armor;}
    public int GetDexIncrement()
    {return DexIncrement;}
    public int GetStrIncrement()
    {return StrIncrement;}
    public int GetConIncrement()
    {return ConIncrement;}
    public String GetItemName()
    {return itemName;}
    public int GetItemType()
    {return ItemType;}

    public Item Clone(){
        return new Item(damage,armor, DexIncrement, StrIncrement, ConIncrement, image, itemName, ItemType);
    }

    public void onDraw(Rect dstRect, Canvas c)
    {
        Paint p = new Paint();
        p.setColor(Color.BLACK);
        c.drawBitmap(image, null, dstRect,p);
    }

    public Bitmap GetImage()
    {
        return image;
    }

    public int GetType()
    {
        return ItemType;
    }
}
