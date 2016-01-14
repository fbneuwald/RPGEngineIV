package rpgengineiv.com.neuwaldlabs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;

public class CharacterCreation extends DrawableUI 
{
	private Rect BackgroundReactagle;
	private Paint p = new Paint();
	private Rect PanelRectangle;
	private Bitmap Panel;
	private Rect DetailedPanelRectangle;
	private Rect BiaRectable,HelenaRectangle,JoanaRectangle,DuduRectangle;
	private Bitmap Bia,Helena,Joana,Dudu;
	private Rect SelectedPhotoRectangle,SelectedNamePerfilRectangle,SelectedDescrRectangle,
	SelectedResistenciaRectangle, SelectedHabilidadeRectangle,SelectedForcaRectangle, PainelRectangle,
	LaunchRectangle,CancelRectangle;
	private Bitmap SelectedPhoto,SelectedNamePerfil,SelectedDescr,
	SelectedResistencia, SelectedHabilidade,SelectedForca,Painel,Launch,Cancel;
	
	private String name,role,description,cons,dex,str;
	
	private final int BIA_ID=0;
	private final int HELENA_ID=1;
	private final int JOANA_ID=2;
	private final int DUDU_ID=3;
	private int Selected=-1;
	
	
	public CharacterCreation(GameView pGV)
	{
		GV = pGV;
		p.setColor(Color.WHITE);
		backgroundMusic = MediaPlayer.create(GV.getContext(), R.raw.chr_creation_music);
		backgroundMusic.setLooping(true);
		backgroundMusic.start();
		p.setTextSize((int)(GV.getHeight()*0.035f));
		BackgroundImage = BitmapFactory.decodeResource(GV.getResources(), R.drawable.mainmenu_background);
		BackgroundReactagle = new Rect(0,0,GV.getWidth(),GV.getHeight());
		Panel = BitmapFactory.decodeResource(GV.getResources(), R.drawable.panel);
		PanelRectangle = new Rect((int)(GV.getWidth()*0.06f),(int)(GV.getHeight()*0.06f),(int)(GV.getWidth()*0.33f),(int)(GV.getHeight()*0.74f));
		DetailedPanelRectangle = new Rect((int)(GV.getWidth()*0.35f),(int)(GV.getHeight()*0.06f),(int)(GV.getWidth()*0.94f),(int)(GV.getHeight()*0.74f));
		Painel = BitmapFactory.decodeResource(GV.getResources(), R.drawable.painel);
		PainelRectangle= new Rect(0,(int)(GV.getHeight()*0.74f),GV.getWidth(),GV.getHeight());
		Launch = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chr_launch_button);
		LaunchRectangle= new Rect((int)(GV.getWidth()*0.6f),(int)(GV.getHeight()*0.78f),(int)(GV.getWidth()*0.85f),(int)(GV.getHeight()*0.92));
		Cancel = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chr_cancel_button);
		CancelRectangle= new Rect((int)(GV.getWidth()*0.1f),(int)(GV.getHeight()*0.78f),(int)(GV.getWidth()*0.35f),(int)(GV.getHeight()*0.92));
		
		Bia = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrbia);
		Helena = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrhelena);
		Joana = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrjoana);
		Dudu = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrdudu);
		BiaRectable = new Rect((int)(GV.getWidth()*0.08f),(int)(GV.getHeight()*0.12f),(int)(GV.getWidth()*0.315f),(int)(GV.getHeight()*0.23f));
		HelenaRectangle = new Rect((int)(GV.getWidth()*0.08f),(int)(GV.getHeight()*0.27f),(int)(GV.getWidth()*0.315f),(int)(GV.getHeight()*0.38f));
		JoanaRectangle = new Rect((int)(GV.getWidth()*0.08f),(int)(GV.getHeight()*0.42f),(int)(GV.getWidth()*0.315f),(int)(GV.getHeight()*0.53f));
		DuduRectangle = new Rect((int)(GV.getWidth()*0.08f),(int)(GV.getHeight()*0.57f),(int)(GV.getWidth()*0.315f),(int)(GV.getHeight()*0.68f));
		
		SelectedPhotoRectangle = new Rect((int)(GV.getWidth()*0.37f),(int)(GV.getHeight()*0.1f),(int)(GV.getWidth()*0.5f),(int)(GV.getHeight()*0.3f));
		SelectedNamePerfil = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chr_name_perfil);
		SelectedNamePerfilRectangle = new Rect((int)(GV.getWidth()*0.50f),(int)(GV.getHeight()*0.12f),(int)(GV.getWidth()*0.91f),(int)(GV.getHeight()*0.27f));
		SelectedDescr = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chr_descr);
		SelectedDescrRectangle = new Rect((int)(GV.getWidth()*0.37f),(int)(GV.getHeight()*0.29f),(int)(GV.getWidth()*0.91f),(int)(GV.getHeight()*0.54f));
		SelectedResistencia = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chr_resistencia);
		SelectedHabilidade = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chr_habilidade);
		SelectedForca = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chr_forca);
		SelectedResistenciaRectangle = new Rect((int)(GV.getWidth()*0.37f),(int)(GV.getHeight()*0.56f),(int)(GV.getWidth()*0.53f),(int)(GV.getHeight()*0.68f));
		SelectedHabilidadeRectangle= new Rect((int)(GV.getWidth()*0.56f),(int)(GV.getHeight()*0.56f),(int)(GV.getWidth()*0.72f),(int)(GV.getHeight()*0.68f));
		SelectedForcaRectangle= new Rect((int)(GV.getWidth()*0.75f),(int)(GV.getHeight()*0.56f),(int)(GV.getWidth()*0.91f),(int)(GV.getHeight()*0.68f));
		
		
	}
	
	public void Draw(Canvas c)
	{
		c.drawBitmap(BackgroundImage, null, BackgroundReactagle, p);
		c.drawBitmap(Panel,null,PanelRectangle,p);
		c.drawBitmap(Panel,null,DetailedPanelRectangle,p);
		c.drawBitmap(Painel,null,PainelRectangle,p);
		c.drawBitmap(Launch,null,LaunchRectangle,p);
		c.drawBitmap(Cancel,null,CancelRectangle,p);
		c.drawBitmap(Bia,null,BiaRectable,p);
		c.drawBitmap(Helena,null,HelenaRectangle,p);
		c.drawBitmap(Joana,null,JoanaRectangle,p);
		c.drawBitmap(Dudu,null,DuduRectangle,p);
		if(Selected!=-1)
		{
			c.drawBitmap(SelectedPhoto,null,SelectedPhotoRectangle,p);
			c.drawBitmap(SelectedNamePerfil,null,SelectedNamePerfilRectangle,p);
			c.drawBitmap(SelectedDescr,null,SelectedDescrRectangle,p);
			c.drawBitmap(SelectedResistencia,null,SelectedResistenciaRectangle,p);
			c.drawBitmap(SelectedHabilidade,null,SelectedHabilidadeRectangle,p);
			c.drawBitmap(SelectedForca,null,SelectedForcaRectangle,p);
			c.drawText(name, GV.getWidth()*0.62f, GV.getHeight()*.18f, p);
			c.drawText(role, GV.getWidth()*0.62f, GV.getHeight()*.23f, p);
			c.drawText(cons, GV.getWidth()*0.45f, GV.getHeight()*.65f, p);
			c.drawText(dex, GV.getWidth()*0.62f, GV.getHeight()*.65f, p);
			c.drawText(str, GV.getWidth()*0.82f, GV.getHeight()*.65f, p);
			int start, end, max_perline;
			start=0;
			max_perline = (int)(GV.getWidth()*0.44f/(p.getTextSize()/2));
			for(int i =0;i<3;i++)
			{
				end = start+max_perline;
				if (end>description.length())
					end = description.length();
				c.drawText(description,start, end, GV.getWidth()*0.49f, GV.getHeight()*.37f+12*i, p);
				start = end;
				if(start>description.length())
					break;
				
			}
			
		}
	
	}
	
	
	public void Touched(MotionEvent event)
	{
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			if(BiaRectable.contains((int)event.getX(),(int)event.getY()))
			{
				Bia = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrbia_over);
				Helena = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrhelena);
				Joana = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrjoana);
				Dudu = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrdudu);
				SelectedPhoto = BitmapFactory.decodeResource(GV.getResources(), R.drawable.biaface);
				name="Bia";
				role="Navegadora";
				description="Bia é geniosa e valente. Tem uma ótima habilidade de seus percausos pra lá e pra cá. É uma ótima navegadora";
				cons="9";
				dex="14";
				str= "7";
				Selected = this.BIA_ID;
			}
			if(HelenaRectangle.contains((int)event.getX(),(int)event.getY()))
			{
				Bia = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrbia);
				Helena = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrhelena_over);
				Joana = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrjoana);
				Dudu = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrdudu);
				SelectedPhoto = BitmapFactory.decodeResource(GV.getResources(), R.drawable.helena_face);
				name="Helena";
				role="Capitã";
				description="Helena é a capitã e lider da missão. Sua liderança já a levou aos confins do universo. Por isso adquiriu uma resitência alta.";
				cons="15";
				dex="6";
				str= "9";
				Selected = this.HELENA_ID;
			}
			if(JoanaRectangle.contains((int)event.getX(),(int)event.getY()))
			{
				Bia = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrbia);
				Helena = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrhelena);
				Joana = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrjoana_over);
				Dudu = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrdudu);
				SelectedPhoto = BitmapFactory.decodeResource(GV.getResources(), R.drawable.joana_face);
				name="Joana";
				role="Exploradora";
				description="Joana veio a missão para explorar o novo planeta. Sua carreira de exploradora a levou a ter um balanço entre seus talentos.";
				cons="10";
				dex="10";
				str= "10";
				Selected = this.JOANA_ID;
			}
			if(DuduRectangle.contains((int)event.getX(),(int)event.getY()))
			{
				Bia = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrbia);
				Helena = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrhelena);
				Joana = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrjoana);
				Dudu = BitmapFactory.decodeResource(GV.getResources(), R.drawable.chrdudu_over);
				SelectedPhoto = BitmapFactory.decodeResource(GV.getResources(), R.drawable.dudu_face);
				name="Dudu";
				role="Piloto";
				description="Eduardo é um habil piloto capaz de navegar qualquer coisa que se mova. Tem seu destaque na força.";
				cons="8";
				dex="8";
				str= "14";
				Selected = this.DUDU_ID;
			}

			if(CancelRectangle.contains((int)event.getX(),(int)event.getY()))
			{
				GV.GameObjects.remove(this);
				GV.GameObjects.add(new MainMenu(GV));
				this.Destroy();
				
			}

            if(LaunchRectangle.contains((int)event.getX(),(int)event.getY()))
            {
                if(Selected==-1)
                    return;
                GV.GameObjects.remove(this);
                Map m = MapFactory.LoadMap("hangar",GV);
                GV.GameObjects.add(m);
                GV.currentMap = m;
                Character c;
                switch(Selected)
                {
                    case HELENA_ID:
                        c = new Character(9,15,6,new Position(16,10),BitmapFactory.decodeResource(GV.getResources(),R.drawable.helena),GV,"Helena");
                        GV.GameObjects.add(c);
                        GV.player = c;
                        break;
                    case BIA_ID:
                        c = new Character(7,9,14,new Position(16,10),BitmapFactory.decodeResource(GV.getResources(),R.drawable.bia),GV,"Bianca");
                        GV.GameObjects.add(c);
                        GV.player = c;
                        break;
                    case DUDU_ID:
                        c = new Character(14,8,8,new Position(16,10),BitmapFactory.decodeResource(GV.getResources(),R.drawable.dudu),GV,"Eduardo");
                        GV.GameObjects.add(c);
                        GV.player = c;
                        break;
                    case JOANA_ID:
                        c = new Character(10,10,10,new Position(16,10),BitmapFactory.decodeResource(GV.getResources(),R.drawable.joana),GV,"Joana");
                        GV.GameObjects.add(c);
                        GV.player = c;
                        break;

                }

                this.Destroy();
            }
		}
	}
}
