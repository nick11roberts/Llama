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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class LlamaActivity extends Activity {

    private RelativeLayout mainLayout;
    //private List<ImageView> llamaList = new ArrayList<>();
    private List<Integer> llamaIdList = new ArrayList<>();
    private UniqueViewIdCreator idCreator = new UniqueViewIdCreator();
    private List<RandomLlamaAttributes> randomLlamaAttributesList = new ArrayList<>();
    private Context c = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama);


        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Fipps-Regular.otf");
        SpannableString s = new SpannableString("Llama Button");
        ActionBar actionBar = getActionBar();


        mainLayout = (RelativeLayout)findViewById(R.id.relLayoutMain);

        s.setSpan(new CustomTypefaceSpan("Fipps-Regular.otf", font), 0, s.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(s);



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


        addButtonToScreen().setOnClickListener(new View.OnClickListener() {
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

    private ImageButton addButtonToScreen(){
        int buttonSizeMultiplier = 400;
        int buttonSize = relativeImageScale(buttonSizeMultiplier);

        RelativeLayout.LayoutParams parameters= new RelativeLayout.LayoutParams(buttonSize, buttonSize);
        ImageButton llamaGeneratingButton = new ImageButton(this);

        parameters.addRule(RelativeLayout.CENTER_IN_PARENT);

        llamaGeneratingButton.setLayoutParams(parameters);
        llamaGeneratingButton.getBackground().setAlpha(0);
        llamaGeneratingButton.setImageResource(R.drawable.button);
        llamaGeneratingButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        mainLayout.addView(llamaGeneratingButton);

        return llamaGeneratingButton;
    }

    private void addLlamaToScreen(RandomLlamaAttributes newestLlamaAttributes){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics ();
        int maxRotation = 90;
        int minRotation = -90;
        int llamaSizeMultiplier = 150;
        int llamaSize = relativeImageScale(llamaSizeMultiplier);
        ImageView newestLlama = new ImageView(c);
        TypedValue tv = new TypedValue();
        int actionBarHeight;
        int newId=idCreator.createViewId();

        display.getMetrics(metrics);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(llamaSize, llamaSize);
        (newestLlama).setImageResource(R.drawable.llama);
        (newestLlama).setPivotX((newestLlama).getX()+(llamaSize/2));
        (newestLlama).setPivotY((newestLlama).getY()+(llamaSize/2));

        (newestLlama).setRotation(minRotation + (int) (newestLlamaAttributes.getRotation() * ((maxRotation - minRotation) + 1)));
        c.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);
        layoutParams.leftMargin = (int) Math.round(newestLlamaAttributes.getX()*(metrics.widthPixels-llamaSize));
        layoutParams.topMargin = (int) Math.round(newestLlamaAttributes.getY()*(metrics.heightPixels-(llamaSize*Math.sqrt(2))-actionBarHeight));
        newestLlama.setLayoutParams(layoutParams);

        llamaIdList.add(newId);
        newestLlama.setId(newId);

        //llamaList.add(newestLlama);
        mainLayout.addView(newestLlama);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_llama, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        Handle action bar item clicks here. The action bar will
        automatically handle clicks on the Home/Up button, so long
        as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();

        if(id == R.id.action_clear_llamas) {
            for(int i=0; i<=llamaIdList.size()-1; i++){
                mainLayout.removeView(findViewById(llamaIdList.get(i)));
            }
            randomLlamaAttributesList.clear();
            return true;
        }
        else if(id == R.id.action_export_llamas){
            /* Do something. */
            return true;
        }
        else if(id == R.id.action_settings){
            /* Do something. */
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int relativeImageScale(int multiplier){
        int imageSize;
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics ();
        display.getMetrics(metrics);

        if(metrics.heightPixels >= metrics.widthPixels)
            imageSize = (metrics.heightPixels/metrics.widthPixels)*multiplier;
        else
            imageSize = (metrics.widthPixels/metrics.heightPixels)*multiplier;

        return imageSize;
    }

}
