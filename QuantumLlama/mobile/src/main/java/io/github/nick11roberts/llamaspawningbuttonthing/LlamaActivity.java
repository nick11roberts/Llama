package io.github.nick11roberts.llamaspawningbuttonthing;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.Log;
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

    private RelativeLayout mainLayout;
    private List<ImageView> llamaList = new ArrayList<>();
    private List<RandomLlamaAttributes> randomLlamaAttributesList = new ArrayList<>();
    //private int indexOfLlamaList = 0;
    private Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama);

        int buttonSizeMultiplier = 400;
        int buttonSize = relativeImageScale(buttonSizeMultiplier);
        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Fipps-Regular.otf");
        SpannableString s = new SpannableString("Llama Button");
        ActionBar actionBar = getActionBar();
        RelativeLayout.LayoutParams parameters= new RelativeLayout.LayoutParams(buttonSize, buttonSize);
        ImageButton llamaGeneratingButton = new ImageButton(this);

        mainLayout = (RelativeLayout)findViewById(R.id.relLayoutMain);

        s.setSpan(new CustomTypefaceSpan("Fipps-Regular.otf", font), 0, s.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(s);

        parameters.addRule(RelativeLayout.CENTER_IN_PARENT);

        llamaGeneratingButton.setLayoutParams(parameters);
        llamaGeneratingButton.getBackground().setAlpha(0);
        llamaGeneratingButton.setImageResource(R.drawable.button);
        llamaGeneratingButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mainLayout.addView(llamaGeneratingButton);

        if(savedInstanceState!=null){
            int indexOfLlamaList = savedInstanceState.getInt("indexOfLlamaList");
            double[] xCoordList = savedInstanceState.getDoubleArray("xCoordinates");
            double[] yCoordList = savedInstanceState.getDoubleArray("yCoordinates");
            double[] rCoordList = savedInstanceState.getDoubleArray("rCoordinates");

            for (int i=0; i<=indexOfLlamaList-1; i++){
                RandomLlamaAttributes llamaAttributes = new RandomLlamaAttributes();
                llamaAttributes.setX(xCoordList[i]);
                llamaAttributes.setY(yCoordList[i]);
                llamaAttributes.setRotation(rCoordList[i]);
                randomLlamaAttributesList.add(llamaAttributes);
            }
                for(int i = 0; i<=randomLlamaAttributesList.size()-1; i++)
                    addLlamaToScreen(randomLlamaAttributesList.get(i));
        }

        llamaGeneratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myNiceButton) {

                RandomLlamaAttributes llamaAttributes = new RandomLlamaAttributes();
                /*
                Give users the option to add multiple llamas per button press? Perhaps an unlock-able
                feature after pressing the button a lot of times?
                */

                /* THIS IS WHERE THE RANDOM STUFF MATTERS */
                llamaAttributes.setRotation(Math.random());
                llamaAttributes.setX(Math.random());
                llamaAttributes.setY(Math.random());
                randomLlamaAttributesList.add(llamaAttributes);

                addLlamaToScreen(llamaAttributes);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        int indexOfLlamaList = randomLlamaAttributesList.size();
        double[] xCoordList = new double[randomLlamaAttributesList.size()];
        double[] yCoordList = new double[randomLlamaAttributesList.size()];
        double[] rCoordList = new double[randomLlamaAttributesList.size()];

        for (int i=0; i<=randomLlamaAttributesList.size()-1; i++){
            xCoordList[i]=randomLlamaAttributesList.get(i).getX();
            yCoordList[i]=randomLlamaAttributesList.get(i).getY();
            rCoordList[i]=randomLlamaAttributesList.get(i).getRotation();
        }

        savedInstanceState.putInt("indexOfLlamaList", indexOfLlamaList);
        savedInstanceState.putDoubleArray("xCoordinates",xCoordList);
        savedInstanceState.putDoubleArray("yCoordinates",yCoordList);
        savedInstanceState.putDoubleArray("rCoordinates",rCoordList);

    }

    private void addLlamaToScreen(RandomLlamaAttributes newestLlamaAttributes){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics ();
        int llamaSizeMultiplier = 150;
        int llamaSize = relativeImageScale(llamaSizeMultiplier);
        ImageView newestLlama = new ImageView(c);
        TypedValue tv = new TypedValue();
        int actionBarHeight;

        display.getMetrics(metrics);

        /* SET HEIGHT LAYOUT PARAMETER */
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(llamaSize, llamaSize);

        /* SET SPECIFIC LLAMA IMAGE */
        (newestLlama).setImageResource(R.drawable.llama);

        /* ROTATIONAL AXIS FOR SPECIFIC LLAMA */
        (newestLlama).setPivotX((newestLlama).getX()+(llamaSize/2));
        (newestLlama).setPivotY((newestLlama).getY()+(llamaSize/2));

        /* RANDOMIZE LLAMA ROTATION */
        int maxRotation = 90;
        int minRotation = -90;
        (newestLlama).setRotation(minRotation + (int) (newestLlamaAttributes.getRotation() * ((maxRotation - minRotation) + 1)));

        c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        /* POSITION SPECIFIC LLAMA */
        layoutParams.leftMargin = (int) Math.round(newestLlamaAttributes.getX()*(metrics.widthPixels-llamaSize));
        layoutParams.topMargin = (int) Math.round(newestLlamaAttributes.getY()*(metrics.heightPixels-(llamaSize*Math.sqrt(2))-actionBarHeight));

        /* SET PARAMETERS */
        newestLlama.setLayoutParams(layoutParams);

        /* ADD TO LLAMA LIST */
        llamaList.add(newestLlama);

        /* ADD VIEW */
        mainLayout.addView(newestLlama);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_llama, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();

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
