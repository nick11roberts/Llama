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

    private static int LLAMA_SIZE_MULTIPLIER = 150;
    private static int BUTTON_SIZE_MULTIPLIER = 400;
    private static int MIN_ROTATION = -90;
    private static int MAX_ROTATION = 90;
    private RelativeLayout mainLayout;
    private List<ImageView> llamaList = new ArrayList<>();
    private List<RandomLlamaAttributes> randomLlamaAttributesList = new ArrayList<>();
    private int indexOfLlamaList = 0;
    private Context c = this;
    private RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama);

        int buttonSize = relativeImageScale(BUTTON_SIZE_MULTIPLIER);
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
                for(int i = 0; i<=indexOfLlamaList-1; i++)
                    addLlamaToScreen(randomLlamaAttributesList.get(i));
        }

        llamaGeneratingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myNiceButton) {

                RandomLlamaAttributes llamaAttributes = new RandomLlamaAttributes();
                // Give users the option to add multiple llamas per button press? Perhaps an unlock-able
                // feature after pressing the button a lot of times?
                //for(int i=0; i<=n-1; i++)

                /////////////////////////////// THIS IS WHERE THE RANDOM STUFF MATTERS.
                llamaAttributes.setRotation(Math.random());
                llamaAttributes.setX(Math.random());
                llamaAttributes.setY(Math.random());
                randomLlamaAttributesList.add(llamaAttributes);

                addLlamaToScreen(llamaAttributes);

                // INCREMENT COUNTER
                indexOfLlamaList++;
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {


        double[] xCoordList = new double[indexOfLlamaList];
        double[] yCoordList = new double[indexOfLlamaList];
        double[] rCoordList = new double[indexOfLlamaList];

        for (int i=0; i<=indexOfLlamaList-1; i++){
            xCoordList[i]=randomLlamaAttributesList.get(i).getX();
            Log.d("saveLlamaBundleInfoX",Double.toString(xCoordList[i]));
            yCoordList[i]=randomLlamaAttributesList.get(i).getY();
            rCoordList[i]=randomLlamaAttributesList.get(i).getRotation();
        }

        savedInstanceState.putInt("indexOfLlamaList", indexOfLlamaList);
        Log.d("saveLlamaBundleInfoIndex",Integer.toString(indexOfLlamaList));
        savedInstanceState.putDoubleArray("xCoordinates",xCoordList);
        savedInstanceState.putDoubleArray("yCoordinates",yCoordList);
        savedInstanceState.putDoubleArray("rCoordinates",rCoordList);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        indexOfLlamaList = savedInstanceState.getInt("indexOfLlamaList");
        Log.d("restoreLlamaBundleInfoIndex",Integer.toString(indexOfLlamaList));
        double[] xCoordList = savedInstanceState.getDoubleArray("xCoordinates");
        double[] yCoordList = savedInstanceState.getDoubleArray("yCoordinates");
        double[] rCoordList = savedInstanceState.getDoubleArray("rCoordinates");
        //RandomLlamaAttributes[] llamaAttributes = new RandomLlamaAttributes[indexOfLlamaList];

        for (int i=0; i<=indexOfLlamaList-1; i++){
            RandomLlamaAttributes llamaAttributes = new RandomLlamaAttributes();
            llamaAttributes.setX(xCoordList[i]);
            Log.d("restoreLlamaBundleInfoX",Double.toString(xCoordList[i]));
            llamaAttributes.setY(yCoordList[i]);
            llamaAttributes.setRotation(rCoordList[i]);
            randomLlamaAttributesList.add(llamaAttributes);
            Log.d("restoreLlamaBundleInfoLlamaAttrListIndex",Integer.toString(randomLlamaAttributesList.size()));
        }

    }

    private void addLlamaToScreen(RandomLlamaAttributes newestLlamaAttributes){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics ();
        int llamaSize = relativeImageScale(LLAMA_SIZE_MULTIPLIER);
        ImageView newestLlama = new ImageView(c);
        TypedValue tv = new TypedValue();
        int actionBarHeight;

        display.getMetrics(metrics);

        ///// SET HEIGHT LAYOUT PARAMETER
        layoutParams = new RelativeLayout.LayoutParams(llamaSize, llamaSize);

        //// SET SPECIFIC LLAMA IMAGE
        (newestLlama).setImageResource(R.drawable.llama);

        ///// ROTATIONAL AXIS FOR SPECIFIC LLAMA
        (newestLlama).setPivotX((newestLlama).getX()+(llamaSize/2));
        (newestLlama).setPivotY((newestLlama).getY()+(llamaSize/2));

        ///// RANDOMIZE LLAMA ROTATION
        (newestLlama).setRotation(MIN_ROTATION + (int) (newestLlamaAttributes.getRotation() * ((MAX_ROTATION - MIN_ROTATION) + 1)));

        c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

        ///// POSITION SPECIFIC LLAMA
        layoutParams.leftMargin = (int) Math.round(newestLlamaAttributes.getX()*(metrics.widthPixels-llamaSize));
        layoutParams.topMargin = (int) Math.round(newestLlamaAttributes.getY()*(metrics.heightPixels-(llamaSize*Math.sqrt(2))-actionBarHeight));

        //// SET PARAMETERS
        newestLlama.setLayoutParams(layoutParams);

        //// ADD TO LLAMA LIST
        llamaList.add(newestLlama);

        ///// ADD VIEW
        mainLayout.addView(newestLlama);


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
