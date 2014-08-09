package io.github.nick11roberts.llamaspawningbuttonthing;

import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.annotation.SuppressLint;
import android.os.Bundle;

public class LlamaActivity extends ActionBarActivity {
	
	
	
    @SuppressLint("SetJavaScriptEnabled")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama_web);
        
        
        final WebView localLlamaEmbed = (WebView)findViewById(R.id.llamaBrowser);   
        localLlamaEmbed.getSettings().setJavaScriptEnabled(true);  
          
//        /* WebViewClient must be set BEFORE calling loadUrl! */  
//        webview.setWebViewClient(new WebViewClient() {  
//            @Override  
//            public void onPageFinished(WebView view, String url)  
//            {  
//                webview.loadUrl("javascript:(function() { " +  
//                        "document.getElementsByTagName('body')[0].style.color = 'red'; " +  
//                        "})()");  
//            }  
//        });  
          
        
        localLlamaEmbed.setBackgroundColor(0x00000000);
        localLlamaEmbed.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        
        localLlamaEmbed.loadUrl("file:///android_asset/web/Llama-Web/index.html");  
        
        
        
    }

    
    
    
    
    

}
