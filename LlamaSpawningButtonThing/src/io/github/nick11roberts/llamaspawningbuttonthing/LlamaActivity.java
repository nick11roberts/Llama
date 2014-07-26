package io.github.nick11roberts.llamaspawningbuttonthing;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class LlamaActivity extends ActionBarActivity {
	
	private static int IMAGE_HEIGHT = 120;
	private static int MIN_ROTATION = -60;
	private static int MAX_ROTATION = 60;
	
	private RelativeLayout mainLayout;
	private ImageButton myNiceButton;
	private List<ImageView> llamaProscriptionList = new ArrayList<ImageView>();
	private int indexOfLlamaProscriptionList = 0;
	private Context c = this;
	private RelativeLayout.LayoutParams layoutParams;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llama);

        mainLayout = (RelativeLayout)findViewById(R.id.relLayoutMain);
        
        myNiceButton = (ImageButton)findViewById(R.id.theButtonThing); 
        
        
        addListenerOnButton();
    }

    
    public void addListenerOnButton(){
    	        
    	myNiceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View myNiceButton) {
				
				DisplayMetrics metrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(metrics);
				
				
				///// SET HEIGHT LAYOUT PARAMETER
				layoutParams = new RelativeLayout.LayoutParams(IMAGE_HEIGHT, IMAGE_HEIGHT);
				
				///// INSTANTIATE LLAMA
				ImageView newestLlama = new ImageView(c);
				
				
				//// SET SPECIFIC LLAMA IMAGE
				(newestLlama).setImageResource(R.drawable.actual_llama);
				
				
				///// ROTATION FOR SPECIFIC LLAMA ---- SEEMS INDEPENDANT
				(newestLlama)
					.setPivotX((newestLlama).getX());
				(newestLlama)
					.setPivotY((newestLlama).getY());
				(newestLlama)
					.setRotation(MIN_ROTATION + (int)(Math.random() * ((MAX_ROTATION - MIN_ROTATION) + 1)));
				
				///// POSITION SPECIFIC LLAMA ---- SEEMS DEPENDANT 
				layoutParams.leftMargin = new Random().nextInt(metrics.widthPixels 
					- (newestLlama).getWidth());
				layoutParams.topMargin = new Random().nextInt(metrics.heightPixels 
					- 2*(newestLlama).getHeight());
				
				
				//// ADD TO LLAMA LIST
				llamaProscriptionList.add(newestLlama);
				
				//// SET PARAMETERS
				(llamaProscriptionList.get(indexOfLlamaProscriptionList)).setLayoutParams(layoutParams);
				
				///// ADD VIEW
				mainLayout.addView(llamaProscriptionList.get(indexOfLlamaProscriptionList));
				
				// INCREMENT COUNTER
				indexOfLlamaProscriptionList+=1;
				
 
			}
 
		});
    	
    	
        	
    }
    
    
    

}
