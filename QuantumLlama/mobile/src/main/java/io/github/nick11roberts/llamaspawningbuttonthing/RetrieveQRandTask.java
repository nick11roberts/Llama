package io.github.nick11roberts.llamaspawningbuttonthing;

import android.os.AsyncTask;

import java.io.IOException;

public class RetrieveQRandTask extends AsyncTask<String, Void, Double> {

    private Double getRandomMultiplier(){

        //Gets bytes from server, will exit if server inaccessible
        Integer randHexNum = 0;
        Double randMultiplier = 0.0;
        AnuRandom random = new AnuRandom(1);
        String temp = new String(random.getBytes());
        System.out.println(temp);
        //Gets bytes from server, throws catchable exception if server inaccessible
        try {
            temp = new String(random.getBytesSafe());
        } catch (IOException e) {
            //Handle inaccessible server
        }


        //Convert from base 16 string to int
        randHexNum = Integer.parseInt(temp, 16);
        randMultiplier = randHexNum/255.0; // divide by the max hex value to return a value between 0-1.

        return randMultiplier;
    }

    protected Double doInBackground(String... urls) {
        return getRandomMultiplier();
    }

}
