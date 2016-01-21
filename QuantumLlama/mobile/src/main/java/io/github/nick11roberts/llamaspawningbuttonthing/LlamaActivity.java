package io.github.nick11roberts.llamaspawningbuttonthing;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LlamaActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        RetrieveQRandResponse{

    private RelativeLayout mainLayout;
    private ProgressDialog dialog;
    private List<Integer> llamaIdList = new ArrayList<>();
    private UniqueViewIdCreator idCreator = new UniqueViewIdCreator();
    private List<RandomLlamaAttributes> randomLlamaAttributesList = new ArrayList<>();
    private final Integer NUMBER_OF_REQUIRED_QRANDS_PER_LLAMA = 3;
    private final int DEFAULT_QUANTUM_LLAMA_DOWNLOAD_SIZE = 10; //will make an option to change this later
    private int downloadQLlamasMenuItemID;
    private final int TOTAL_NUMBER_OF_QRANDS = DEFAULT_QUANTUM_LLAMA_DOWNLOAD_SIZE * NUMBER_OF_REQUIRED_QRANDS_PER_LLAMA;
    private double[][] quantumRandomNumList = new double[TOTAL_NUMBER_OF_QRANDS][NUMBER_OF_REQUIRED_QRANDS_PER_LLAMA];
    private int quantumLlamaDepletionCounter = 0;
    private Context c = LlamaActivity.this;
    private GoogleApiClient googleClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/Fipps-Regular.otf");
        SpannableString s = new SpannableString(getResources().getString(R.string.actionbar_title));
        ActionBar actionBar = getActionBar();

        mainLayout = (RelativeLayout)findViewById(R.id.relLayoutMain);

        s.setSpan(new CustomTypefaceSpan("Fipps-Regular.otf", font), 0, s.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        actionBar.setTitle(s);

        ImageButton llamaButton = addButtonToScreen();

        if(savedInstanceState!=null){
            int indexOfLlamaList = savedInstanceState.getInt("indexOfLlamaList");
            double[] xCoordList = savedInstanceState.getDoubleArray("xCoordinates");
            double[] yCoordList = savedInstanceState.getDoubleArray("yCoordinates");
            double[] rCoordList = savedInstanceState.getDoubleArray("rCoordinates");

            updateLlamas(indexOfLlamaList, xCoordList, yCoordList, rCoordList);
        }

        googleClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        llamaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View myNiceButton) {

                RandomLlamaAttributes llamaAttributes = new RandomLlamaAttributes();

                /*
                Give users the option to add multiple llamas per button press? Perhaps an unlock-able
                feature after pressing the button a lot of times?
                */

                if(quantumLlamaDepletionCounter >= 1){
                    llamaAttributes.setX(quantumRandomNumList[quantumLlamaDepletionCounter-1][0]);
                    llamaAttributes.setY(quantumRandomNumList[quantumLlamaDepletionCounter-1][1]);
                    llamaAttributes.setRotation(quantumRandomNumList[quantumLlamaDepletionCounter-1][2]);
                    quantumLlamaDepletionCounter--;

                    if(quantumLlamaDepletionCounter == 3){
                        LayoutInflater inflater = getLayoutInflater();
                        View toastLayout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

                        Toast qRemainingToast = new Toast(c);

                        CustomTypefaceTextView text = (CustomTypefaceTextView) toastLayout.findViewById(R.id.toast_text);
                        text.setText(Integer.toString(quantumLlamaDepletionCounter)+" "+getResources().getString(R.string.toast_quantum_llamas_remaining));

                        qRemainingToast.setView(toastLayout);
                        qRemainingToast.setDuration(Toast.LENGTH_SHORT);
                        qRemainingToast.show();
                    }else if(quantumLlamaDepletionCounter == 0){

                        LayoutInflater inflater = getLayoutInflater();
                        View toastLayout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

                        Toast qRemainingToast = new Toast(c);

                        CustomTypefaceTextView text = (CustomTypefaceTextView) toastLayout.findViewById(R.id.toast_text);
                        text.setText(getResources().getString(R.string.toast_quantum_llamas_depleted));

                        qRemainingToast.setView(toastLayout);
                        qRemainingToast.setDuration(Toast.LENGTH_SHORT);
                        qRemainingToast.show();

                    }

                }else {
                    llamaAttributes.setX(Math.random());
                    llamaAttributes.setY(Math.random());
                    llamaAttributes.setRotation(Math.random());
                }

                randomLlamaAttributesList.add(llamaAttributes);

                addLlamaToScreen(llamaAttributes);
            }
        });
    }

    private void updateLlamas(int indexOfLlamaList, double[] xCoordList, double[] yCoordList, double[]rCoordList){
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

        downloadQLlamasMenuItemID = menu.add(
                0, 0, 10,
                getResources().getString(R.string.menu_download_llamas_start)+" "
                        + Integer.toString(DEFAULT_QUANTUM_LLAMA_DOWNLOAD_SIZE)
                        +" " +getResources().getString(R.string.menu_download_llamas_end)
        ).getItemId();

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

            LayoutInflater inflater = getLayoutInflater();
            View toastLayout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

            Toast clearToast = new Toast(c);

            CustomTypefaceTextView text = (CustomTypefaceTextView) toastLayout.findViewById(R.id.toast_text);

            //This is mostly for grammar
            if(llamaIdList.size()>=2)
                text.setText(Integer.toString(llamaIdList.size())+" "+getResources().getString(R.string.toast_clear_pl));
            else if(llamaIdList.size()==1)
                text.setText(Integer.toString(llamaIdList.size())+" "+getResources().getString(R.string.toast_clear_si));
            else
                text.setText(getResources().getString(R.string.no_upper)+" "+getResources().getString(R.string.toast_clear_pl));

            clearToast.setView(toastLayout);
            clearToast.setDuration(Toast.LENGTH_SHORT);
            clearToast.show();

            this.clearLlamas();
            return true;
        } else if(id == R.id.action_algorithm_randomize_llamas){
            int llamaListSize = llamaIdList.size();
            double[] xCoordinates = new double[llamaListSize];
            double[] yCoordinates = new double[llamaListSize];
            double[] rCoordinates = new double[llamaListSize];

            for(int i=0; i<=llamaListSize-1; i++){
                xCoordinates[i] = Math.random();
                yCoordinates[i] = Math.random();
                rCoordinates[i] = Math.random();
            }

            this.clearLlamas();
            updateLlamas(llamaListSize,xCoordinates,yCoordinates,rCoordinates);

            LayoutInflater inflater = getLayoutInflater();
            View toastLayout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

            Toast randomizeToast = new Toast(c);

            CustomTypefaceTextView text = (CustomTypefaceTextView) toastLayout.findViewById(R.id.toast_text);

            //This is mostly for grammar
            if(llamaIdList.size()>=2)
                text.setText(Integer.toString(llamaIdList.size())+" "+getResources().getString(R.string.toast_alg_randomize_pl));
            else if(llamaIdList.size()==1)
                text.setText(Integer.toString(llamaIdList.size())+" "+getResources().getString(R.string.toast_alg_randomize_si));
            else
                text.setText(getResources().getString(R.string.no_upper)+" "+getResources().getString(R.string.toast_alg_randomize_pl));

            randomizeToast.setView(toastLayout);
            randomizeToast.setDuration(Toast.LENGTH_SHORT);
            randomizeToast.show();

            return true;
        } else if(id == R.id.action_quantum_randomize_llamas){

            new RetrieveQRandTask(this, this, llamaIdList.size()* NUMBER_OF_REQUIRED_QRANDS_PER_LLAMA).execute(
                    new AsyncTaskParams(llamaIdList.size()* NUMBER_OF_REQUIRED_QRANDS_PER_LLAMA,true)
            );

            return true;
        } else if(id == downloadQLlamasMenuItemID){ // special case because it must be created dynamically.

            new RetrieveQRandTask(this, this, TOTAL_NUMBER_OF_QRANDS).execute(
                    new AsyncTaskParams(TOTAL_NUMBER_OF_QRANDS,false)
            );

            return true;
        }

        /*else if(id == R.id.action_wear_llamas){
            *//* Do something. *//*
            return true;
        }
        else if(id == R.id.action_settings){
            *//* Do something. *//*
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    public void processFinish(double[][] output){

        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

        //provides the number of sets of coordinates (one set per llama, each set contains 3 elements [x,y,r])
        quantumLlamaDepletionCounter = TOTAL_NUMBER_OF_QRANDS / NUMBER_OF_REQUIRED_QRANDS_PER_LLAMA;

        Toast qDownloadedToast = new Toast(c);

        CustomTypefaceTextView text = (CustomTypefaceTextView) toastLayout.findViewById(R.id.toast_text);
        text.setText(Integer.toString(DEFAULT_QUANTUM_LLAMA_DOWNLOAD_SIZE)+" "+getResources().getString(R.string.toast_quantum_llamas_downloaded));

        qDownloadedToast.setView(toastLayout);
        qDownloadedToast.setDuration(Toast.LENGTH_SHORT);
        qDownloadedToast.show();

        quantumRandomNumList = output;
    }

    public void processFinishConversionTask(double[][] output){

        int llamaListSize = llamaIdList.size();
        double[] xCoordinates = new double[llamaListSize];
        double[] yCoordinates = new double[llamaListSize];
        double[] rCoordinates = new double[llamaListSize];

        for(int i=0; i<=llamaListSize-1; i++){
            xCoordinates[i] = output[i][0];
            yCoordinates[i] = output[i][1];
            rCoordinates[i] = output[i][2];
        }

        this.clearLlamas();
        updateLlamas(llamaListSize,xCoordinates,yCoordinates,rCoordinates);

        LayoutInflater inflater = getLayoutInflater();
        View toastLayout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

        Toast qRandomizeToast = new Toast(c);

        CustomTypefaceTextView text = (CustomTypefaceTextView) toastLayout.findViewById(R.id.toast_text);

        //This is mostly for grammar
        if(llamaIdList.size()>=2)
            text.setText(Integer.toString(llamaIdList.size())+" "+getResources().getString(R.string.toast_q_randomize_pl));
        else if(llamaIdList.size()==1)
            text.setText(Integer.toString(llamaIdList.size())+" "+getResources().getString(R.string.toast_q_randomize_si));
        else
            text.setText(getResources().getString(R.string.no_upper)+" "+getResources().getString(R.string.toast_q_randomize_pl));

        qRandomizeToast.setView(toastLayout);
        qRandomizeToast.setDuration(Toast.LENGTH_SHORT);
        qRandomizeToast.show();
    }

    private void clearLlamas(){
        for(int i=0; i<=llamaIdList.size()-1; i++){
            mainLayout.removeView(findViewById(llamaIdList.get(i)));
        }
        randomLlamaAttributesList.clear();
        llamaIdList.clear();
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

    /*
     * FOR WEAR
     */

    // Connect to the data layer when the Activity starts
    @Override
    protected void onStart() {
        super.onStart();
        googleClient.connect();
    }

    // Send a data object when the data layer connection is successful.

    @Override
    public void onConnected(Bundle connectionHint) {

        String WEARABLE_DATA_PATH = "/wearable_data";

        // Create a DataMap object and send it to the data layer
        DataMap dataMap = new DataMap();
        dataMap.putLong("time", new Date().getTime());
        dataMap.putString("hole", "1");
        dataMap.putString("front", "250");
        dataMap.putString("middle", "260");
        dataMap.putString("back", "270");
        //Requires a new thread to avoid blocking the UI
        new SendToDataLayerThread(WEARABLE_DATA_PATH, dataMap, googleClient).start();

    }

    // Disconnect from the data layer when the Activity stops
    @Override
    protected void onStop() {

        if (null != googleClient && googleClient.isConnected()) {
            googleClient.disconnect();
        }
        super.onStop();

    }

    // Placeholders for required connection callbacks
    @Override
    public void onConnectionSuspended(int cause) { }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) { }

}
