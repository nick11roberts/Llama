package io.github.nick11roberts.llamaspawningbuttonthing;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class LlamaWearActivity extends Activity {

    private static int LLAMA_SIZE_MULTIPLIER = 70;
    private static int BUTTON_SIZE_MULTIPLIER = 180;
    private static int MIN_ROTATION = -30;
    private static int MAX_ROTATION = 30;
    private RelativeLayout mainLayout;
    private List<ImageView> llamaList = new ArrayList<>();
    private int indexOfLlamaList = 0;
    private Context c = this;
    private RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama_wear);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mainLayout = (RelativeLayout)findViewById(R.id.wearRelLayoutMain);

                int buttonSize = relativeImageScale(BUTTON_SIZE_MULTIPLIER);

                RelativeLayout.LayoutParams parameters= new RelativeLayout.LayoutParams(buttonSize, buttonSize);

                parameters.addRule(RelativeLayout.CENTER_IN_PARENT);

                ImageButton b = new ImageButton(c);
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
                        (newestLlama).setImageAlpha(200); //Note:Android alpha range is 0-255


                        ///// ROTATION FOR SPECIFIC LLAMA
                        (newestLlama).setPivotX((newestLlama).getX()+(llamaSize/2));
                        (newestLlama).setPivotY((newestLlama).getY()+(llamaSize/2));
                        (newestLlama).setRotation(MIN_ROTATION + (int) (Math.random() * ((MAX_ROTATION - MIN_ROTATION) + 1)));


                        ///// POSITION SPECIFIC LLAMA
                        layoutParams.leftMargin = (int) Math.round(Math.random()*(metrics.widthPixels-llamaSize));
                        layoutParams.topMargin = (int) Math.round(Math.random()*(metrics.heightPixels-llamaSize));


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
        });
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