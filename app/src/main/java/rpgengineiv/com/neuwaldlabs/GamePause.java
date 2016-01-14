package rpgengineiv.com.neuwaldlabs;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;


/**
 * Created by fneuwald on 18/05/2014.
 */
public class GamePause {



    private Rect Inventory;
    private Rect Head;
    private Rect RightHand;
    private Rect LeftHand;
    private Rect Chest;
    private Rect Legs;
    private Rect[] Backpack;
    private Rect Back, Exit, Save;
    private GameView gv;
    private Character chr;
    private boolean active = false;
    private Rect Drag;
    private Item DraggedItem;
    private int CurX, CurY;
    private final int HEAD=0;
    private final int CHEST=1;
    private final int LEGS=2;
    private final int LEFTHAND=3;
    private final int RIGHTHAND=4;
    private final int INVENTORY=5;
    private int inventoryIndex;
    private int sourceSlot;

    public GamePause(GameView pGv, Character pChr)
    {
        Inventory = new Rect(5,15,45,55);
        gv = pGv;
        chr = pChr;

        //slot width and height = 30% of a third of gameview width
        int slotsize = gv.getWidth()/3/10*3;
        int left,top;
        // left head, chest and legs = 30% of a third of Gameview width;
        left = gv.getWidth()/3/10*4;
        top = gv.getHeight()/15;
        Head  = new Rect(left,top,left+slotsize,top+slotsize);
        top = gv.getHeight()/30*11;
        Chest = new Rect(left,top,left+slotsize,top+slotsize);
        top = gv.getHeight()/15*8;
        Legs  = new Rect(left,top,left+slotsize,top+slotsize);
        top = gv.getHeight()/15*7;
        left = gv.getWidth()/3/20*17;
        LeftHand = new Rect(left,top,left+slotsize,top+slotsize);
        left = gv.getWidth()/3/20;
        RightHand = new Rect(left,top,left+slotsize,top+slotsize);
        Exit = new Rect(gv.getWidth()-slotsize,gv.getHeight()-slotsize,gv.getWidth(),gv.getHeight());
        Save = new Rect(gv.getWidth()-slotsize*2,gv.getHeight()-slotsize,gv.getWidth()-slotsize,gv.getHeight());
        Back = new Rect(gv.getWidth()-slotsize*3,gv.getHeight()-slotsize,gv.getWidth()-slotsize*2,gv.getHeight());
        Backpack = new Rect[chr.Inventory.length];

        int multiplier = 0;
        int mod = 0;
        for (int i = 0; i < chr.Inventory.length; i++)
        {
            multiplier = i / 4;
            mod = i % 4;
            Backpack[i] = new Rect( gv.getWidth()/2 + slotsize*mod+2,
                    gv.getHeight()/10 + slotsize*multiplier+2,
                    gv.getWidth()/2+slotsize + slotsize*mod,
                    gv.getHeight()/10+slotsize + slotsize*multiplier);
        }


    }

    public boolean HasAction(int x, int y)
    {
        if(Inventory.contains(x,y))
        {
            active = true;
            return true;
        }
        return false;
    }

    public void onDraw(Canvas c)
    {
        Paint p = new Paint();

        p.setColor(Color.BLACK);
        c.drawRect(0, 0, gv.getWidth(), gv.getHeight(), p);
        Bitmap character =  chr.GetCharacterImage();
        c.drawBitmap(character,
                new Rect(character.getWidth()/3,0,character.getWidth()/3*2,character.getHeight()/4),
                new Rect(5,5,gv.getWidth()/3-5,gv.getHeight()/3*2-5) ,p);

        Bitmap square = BitmapFactory.decodeResource(gv.getResources(), R.drawable.border);
        c.drawBitmap(square, null, Head,p);
        c.drawBitmap(square, null, Chest,p);
        c.drawBitmap(square, null, Legs,p);
        c.drawBitmap(square, null, RightHand,p);
        c.drawBitmap(square, null, LeftHand,p);


        for (int i = 0; i < chr.Inventory.length; i++)
        {
            c.drawBitmap(square, null, Backpack[i], p);
            if(chr.Inventory[i]!=null)c.drawBitmap(chr.Inventory[i].GetImage(), null, createItemRect(Backpack[i]),p);
        }

        if(chr.RightHand!=null)c.drawBitmap(chr.RightHand.GetImage(), null, createItemRect(RightHand),p);
        if(chr.LeftHand!=null)c.drawBitmap(chr.LeftHand.GetImage(), null, createItemRect(LeftHand),p);
        if(chr.Head!=null)c.drawBitmap(chr.Head.GetImage(), null, createItemRect(Head),p);
        if(chr.Chest!=null)c.drawBitmap(chr.Chest.GetImage(), null, createItemRect(Chest),p);
        if(chr.Legs!=null)c.drawBitmap(chr.Legs.GetImage(), null, createItemRect(Legs),p);
        if(DraggedItem!=null)c.drawBitmap(DraggedItem.GetImage(), null, createItemRect(Drag),p);

        Bitmap bmpInventory = BitmapFactory.decodeResource(gv.getResources(), R.drawable.play);
        c.drawBitmap(bmpInventory, null, Back,p);
        bmpInventory = BitmapFactory.decodeResource(gv.getResources(), R.drawable.save);
        c.drawBitmap(bmpInventory, null, Save,p);
        bmpInventory = BitmapFactory.decodeResource(gv.getResources(), R.drawable.exit);
        c.drawBitmap(bmpInventory, null, Exit,p);


        p.setColor(Color.WHITE);
        c.drawText("Con: " + chr.GetConstitution(), 5, gv.getHeight()/4*3+10, p);
        c.drawText("Dex: " + chr.GetDexterity(), gv.getWidth()/18*3, gv.getHeight()/4*3+10, p);
        c.drawText("Str: " + chr.GetStrength(), gv.getWidth()/18*6, gv.getHeight()/4*3+10, p);
        c.drawText("HP: " + chr.GetCurrentHP() + "/" + chr.GetMaximumHP(), 5, gv.getHeight()/4*3 + 30, p);
        c.drawText("Armor: " + chr.GetArmor(), gv.getWidth()/9*2+5, gv.getHeight()/4*3 + 30, p);
        c.drawText("Level: " + chr.GetLevel(),5, gv.getHeight()/4*3 + 50, p);
        c.drawText("Exp: " + chr.GetExperience(), gv.getWidth()/9*2+5, gv.getHeight()/4*3 + 50, p);


    }

    public Rect createItemRect(Rect Slot)
    {
        Rect item = new Rect(Slot.left+7, Slot.top+7, Slot.right-7, Slot.bottom-7);
        return item;
    }
    public boolean IsActive()
    {
        return active;
    }

    public void OnTouch(MotionEvent event)
    {
        if(event.getActionMasked()==MotionEvent.ACTION_DOWN) {
            if (Back.contains((int) event.getX(), (int) event.getY())) {
                gv.UnPause();
                return;
            }
            if (Exit.contains((int) event.getX(), (int) event.getY())) {
                gv.GameObjects.clear();
                gv.GameObjects.add(new MainMenu(gv));
                gv.UnPause();
                return;
            }
            if (Save.contains((int) event.getX(), (int) event.getY())) {
                   return;
            } else {

                if ((RightHand.contains((int) event.getX(), (int) event.getY())) && (chr.RightHand != null)) {
                    DraggedItem = chr.RightHand;
                    Drag = new Rect(RightHand);
                    sourceSlot = this.RIGHTHAND;
                }
                if ((LeftHand.contains((int) event.getX(), (int) event.getY())) && (chr.LeftHand != null)) {
                    DraggedItem = chr.LeftHand;
                    Drag = new Rect(LeftHand);
                    sourceSlot = this.LEFTHAND;
                }
                if ((Chest.contains((int) event.getX(), (int) event.getY())) && (chr.Chest != null)) {
                    DraggedItem = chr.Chest;
                    Drag = new Rect(Chest);
                    sourceSlot = this.CHEST;
                }
                if ((Head.contains((int) event.getX(), (int) event.getY())) && (chr.Head != null)) {
                    DraggedItem = chr.Head;
                    Drag = new Rect(Head);
                    sourceSlot = this.HEAD;
                }
                if ((Legs.contains((int) event.getX(), (int) event.getY())) && (chr.Legs != null)) {
                    DraggedItem = chr.Legs;
                    Drag = new Rect(Legs);
                    sourceSlot = this.LEGS;
                }
                for (int i = 0; i < chr.Inventory.length; i++) {
                    if ((Backpack[i].contains((int) event.getX(), (int) event.getY())) && (chr.Inventory[i] != null)) {
                        DraggedItem = chr.Inventory[i];
                        Drag = new Rect(Backpack[i]);
                        sourceSlot = this.INVENTORY;
                        inventoryIndex = i;
                    }

                }

                CurX = (int) event.getX();
                CurY = (int) event.getY();
            }
        }
        else
        if(event.getActionMasked()==MotionEvent.ACTION_UP)
        {
            if(Drag!=null)
            {	if(LeftHand.contains((int)event.getX(), (int) event.getY()))
            {
                if(DraggedItem.GetType() == Item.LEFTHAND)
                {
                    if(SwitchSlots(chr.LeftHand))
                        chr.LeftHand = DraggedItem;
                }
            }
            else
            if(RightHand.contains((int)event.getX(), (int) event.getY()))
            {
                if(DraggedItem.GetType() == Item.RIGHTHAND)
                {
                    if(SwitchSlots(chr.RightHand))
                        chr.RightHand = DraggedItem;
                }

            }
            else
            if(Chest.contains((int)event.getX(), (int) event.getY()))
            {
                if(DraggedItem.GetType() == Item.CHEST)
                {
                    if(SwitchSlots(chr.Chest))
                        chr.Chest = DraggedItem;
                }
            }
            else
            if(Legs.contains((int)event.getX(), (int) event.getY()))
            {
                if(DraggedItem.GetType() == Item.LEGS)
                {
                    if(SwitchSlots(chr.Legs))
                        chr.Legs = DraggedItem;
                }
            }
            else
            if(Head.contains((int)event.getX(), (int) event.getY()))
            {
                if(DraggedItem.GetType() == Item.HEAD)
                {
                    if(SwitchSlots(chr.Head))
                        chr.Head= DraggedItem;
                }
            }
            else
                for (int i= 0; i<Backpack.length;i++)
                    if(Backpack[i].contains((int)event.getX(), (int) event.getY()))
                    {
                        SwitchSlots(chr.Inventory[i]);
                        chr.Inventory[i]= DraggedItem;
                        break;
                    }
            }
            Drag=null;
            DraggedItem = null;
            sourceSlot = -1;
            inventoryIndex = -1;
        }
        else
        {
            if(Drag!=null)
            {
                Drag.left += (int)event.getX()-CurX;
                Drag.right+= (int)event.getX()-CurX;
                Drag.top += (int)event.getY()-CurY;
                Drag.bottom += (int)event.getY()-CurY;
                CurX = (int)event.getX();
                CurY = (int)event.getY();
            }

        }

    }

    private boolean SwitchSlots(Item dstItem)
    {

        switch (sourceSlot) {
            case HEAD:
                if(dstItem==null)
                {
                    chr.Head = null;
                    return true;
                }
                if(dstItem.GetType()==Item.HEAD)
                    chr.Head = dstItem;
                else
                    return false;
                break;
            case CHEST:
                if(dstItem==null)
                {
                    chr.Chest= null;
                    return true;
                }
                if(dstItem.GetType()==Item.CHEST)
                    chr.Chest= dstItem;
                else
                    return false;
                break;
            case LEGS:
                if(dstItem==null)
                {
                    chr.Legs = null;
                    return true;
                }
                if(dstItem.GetType()==Item.LEGS)
                    chr.Legs= dstItem;
                else
                    return false;
                break;
            case LEFTHAND:
                if(dstItem==null)
                {
                    chr.LeftHand= null;
                    return true;
                }
                if(dstItem.GetType()==Item.LEFTHAND)
                    chr.LeftHand = dstItem;
                else
                    return false;
                break;
            case RIGHTHAND:
                if(dstItem==null)
                {
                    chr.RightHand= null;
                    return true;
                }
                if(dstItem.GetType()==Item.RIGHTHAND)
                    chr.RightHand= dstItem;
                else
                    return false;
                break;
            case INVENTORY:
                chr.Inventory[inventoryIndex]= dstItem;
                break;
            default:
                break;

        }
        return true;
    }
}
