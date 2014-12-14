package nick11roberts.github.io.llamaspawningbuttonthing;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;


public class LlamaActivity extends ActionBarActivity {

    @SuppressLint("SetJavaScriptEnabled")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama);

        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);


        NoZoomControlWebView web = new NoZoomControlWebView(this);
        //web.setId(1);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDisplayZoomControls(false);
        web.getSettings().setBuiltInZoomControls(false);
        web.getSettings().setSupportZoom(false);
        web.getSettings().setUseWideViewPort(false);
        web.setBackgroundColor(0x00000000);
        web.setLayerType(NoZoomControlWebView.LAYER_TYPE_SOFTWARE, null);
        web.loadUrl("file:///android_asset/web/Llama-Web/index.html");
        mainLayout.addView(web, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_llama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
