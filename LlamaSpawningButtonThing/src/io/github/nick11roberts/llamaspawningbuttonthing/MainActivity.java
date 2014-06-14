package io.github.nick11roberts.llamaspawningbuttonthing;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	
	private static boolean DEBUG = true;
	private static int IMAGE_HEIGHT = 120;
	private static int MIN_ROTATION = -60;
	private static int MAX_ROTATION = 60;
	private int min;
	private int maxWidth; 
	private int maxHeight; 
	private double maxWidthDisplacement;
	private double maxHeightDisplacement;
	private RelativeLayout mainLayout;
	private ImageButton myNiceButton;
	private List<ImageView> llamaProscriptionList = new ArrayList<ImageView>();
	private int indexOfLlamaProscriptionList = 0;
	private Context c = this;
	private RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(IMAGE_HEIGHT, IMAGE_HEIGHT);
	//private Display display = getWindowManager().getDefaultDisplay();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        maxWidthDisplacement = findScreenWidth() * .1;
        maxHeightDisplacement = findScreenHeight() * .1;
        
        min = /*(int) maxWidthDisplacement*/240;
        maxWidth = (int) (findScreenWidth()-maxWidthDisplacement);
        maxHeight = /*(int) (findScreenHeight()-maxHeightDisplacement)*/1200;
        
        
        mainLayout = (RelativeLayout)findViewById(R.id.relLayoutMain);
        
        myNiceButton = (ImageButton)findViewById(R.id.theButtonThing); 
        
        
        addListenerOnButton();
    }

    
    public void addListenerOnButton(){
    	        
    	myNiceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View myNiceButton) {
				
				
				ImageView newestLlama = new ImageView(c);
				newestLlama.setImageResource(R.drawable.actual_llama);
				newestLlama.setLayoutParams(layoutParams);
				newestLlama.setX(min 
						+ (int)(Math.random() * ((maxWidth - min) + 1)));
				newestLlama.setY(min 
						+ (int)(Math.random() * ((maxHeight - min) + 1)));
				newestLlama.setPivotX(newestLlama.getX());
				newestLlama.setPivotY(newestLlama.getY());
				newestLlama.setRotation(MIN_ROTATION 
						+ (int)(Math.random() * ((MAX_ROTATION - MIN_ROTATION) + 1)));
				
				if(DEBUG){
					System.out.println(" -- ");
					System.out.println("Screen dimensions: " + findScreenWidth() 
							+ " X " + findScreenHeight());
					System.out.println("maxWidth = " + maxWidth);
					System.out.println("maxHeight = " + maxHeight);
					System.out.println("getX = " + newestLlama.getX());
					System.out.println("getY = " + newestLlama.getY());
					System.out.println(" -- ");
				}
				
				llamaProscriptionList.add(newestLlama);
				mainLayout.addView(llamaProscriptionList.get(indexOfLlamaProscriptionList));
				indexOfLlamaProscriptionList++;
				
 
			}
 
		});
    	
    	
        	
    }
    
    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	int findScreenWidth(){
    	int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	int width;
    	
    	if (currentapiVersion >= 13){
	    	display.getSize(size);
	    	width = size.x;
    	}
    	else{
    		width = display.getWidth();
    	}
    	return width;
    }
    
    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	int findScreenHeight(){
    	int currentapiVersion = android.os.Build.VERSION.SDK_INT;
    	Display display = getWindowManager().getDefaultDisplay();
    	Point size = new Point();
    	int height;
    	
    	if (currentapiVersion >= 13){
	    	display.getSize(size);
	    	height = size.y;
    	}
    	else{
    		height = display.getHeight();
    	}
    	return height;
    }
    
    

}
