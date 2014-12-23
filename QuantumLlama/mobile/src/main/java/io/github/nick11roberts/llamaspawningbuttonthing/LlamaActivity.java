package io.github.nick11roberts.llamaspawningbuttonthing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class LlamaActivity extends Activity {

    private static int LLAMA_SIZE_MULTIPLIER = 150;
    private static int BUTTON_SIZE_MULTIPLIER = 400;
    private static int MIN_ROTATION = -90;
    private static int MAX_ROTATION = 90;
    private RelativeLayout mainLayout;
    private List<ImageView> llamaList = new ArrayList<>();
    private int indexOfLlamaList = 0;
    private Context c = this;
    private RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Fipps-Regular.otf");

        SpannableString s = new SpannableString("Llama Button");
        s.setSpan(new CustomTypefaceSpan("Fipps-Regular.otf", font), 0, s.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        ActionBar actionBar = getActionBar();
        actionBar.setTitle(s);

        mainLayout = (RelativeLayout)findViewById(R.id.relLayoutMain);

        int buttonSize = relativeImageScale(BUTTON_SIZE_MULTIPLIER);

        RelativeLayout.LayoutParams parameters= new RelativeLayout.LayoutParams(buttonSize, buttonSize);

        parameters.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageButton b = new ImageButton(this);
        b.setLayoutParams(parameters);
        b.getBackground().setAlpha(0);
        b.setImageResource(R.drawable.button);
        b.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mainLayout.addView(b);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myNiceButton) {

                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics ();
                display.getMetrics(metrics);

                int llamaSize = relativeImageScale(LLAMA_SIZE_MULTIPLIER);

                ///// SET HEIGHT LAYOUT PARAMETER
                layoutParams = new RelativeLayout.LayoutParams(llamaSize, llamaSize);

                ///// INSTANTIATE LLAMA
                ImageView newestLlama = new ImageView(c);


                //// SET SPECIFIC LLAMA IMAGE
                (newestLlama).setImageResource(R.drawable.llama);


                ///// ROTATION FOR SPECIFIC LLAMA
                (newestLlama).setPivotX((newestLlama).getX()+(llamaSize/2));
                (newestLlama).setPivotY((newestLlama).getY()+(llamaSize/2));
                (newestLlama).setRotation(MIN_ROTATION + (int) (Math.random() * ((MAX_ROTATION - MIN_ROTATION) + 1)));


                TypedValue tv = new TypedValue();
                c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

                ///// POSITION SPECIFIC LLAMA
                layoutParams.leftMargin = (int) Math.round(Math.random()*(metrics.widthPixels-llamaSize));
                layoutParams.topMargin = (int) Math.round(Math.random()*(metrics.heightPixels-(llamaSize*Math.sqrt(2))-actionBarHeight));


                //// ADD TO LLAMA LIST
                llamaList.add(newestLlama);

                //// SET PARAMETERS
                (llamaList.get(indexOfLlamaList)).setLayoutParams(layoutParams);

                ///// ADD VIEW
                mainLayout.addView(llamaList.get(indexOfLlamaList));

                // INCREMENT COUNTER
                indexOfLlamaList += 1;


            }

        });



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

    private int relativeImageScale(int multiplier){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics ();
        display.getMetrics(metrics);

        int imageSize;

        if(metrics.heightPixels >= metrics.widthPixels)
            imageSize = (metrics.heightPixels/metrics.widthPixels)*multiplier;
        else
            imageSize = (metrics.widthPixels/metrics.heightPixels)*multiplier;

        return imageSize;
    }


}
