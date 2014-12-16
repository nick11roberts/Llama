package io.github.nick11roberts.llamaspawningbuttonthing;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;


public class LlamaActivity extends Activity {

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
}
