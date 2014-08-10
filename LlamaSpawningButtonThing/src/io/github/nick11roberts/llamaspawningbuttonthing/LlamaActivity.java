package io.github.nick11roberts.llamaspawningbuttonthing;

import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.annotation.SuppressLint;
import android.os.Bundle;

public class LlamaActivity extends ActionBarActivity {

    @SuppressLint("SetJavaScriptEnabled")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama_web);
        
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        
        
        NoZoomControllWebView web = new NoZoomControllWebView(this);
        web.setId(1);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDisplayZoomControls(false);
        web.getSettings().setBuiltInZoomControls(false);
        web.getSettings().setSupportZoom (false);
        web.getSettings().setUseWideViewPort(false);
        web.setBackgroundColor(0x00000000);
        web.setLayerType(NoZoomControllWebView.LAYER_TYPE_SOFTWARE, null);
        web.loadUrl("file:///android_asset/web/Llama-Web/index.html");
        mainLayout.addView(web, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

    }

}
