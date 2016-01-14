package rpgengineiv.com.neuwaldlabs;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends Activity {

    private GameView GV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        GV = new GameView(this);
        setContentView(GV);
    }



    @Override
    public void onBackPressed(){
        if(!GV.IsPaused())
            if(GV.player!=null)
                GV.backButtonPressed();

        // super.onBackPressed();// do something here and don't write super.onBackPressed()
    }

}
