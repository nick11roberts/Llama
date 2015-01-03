package io.github.nick11roberts.llamaspawningbuttonthing;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

public class RetrieveQRandTask extends AsyncTask<Integer, Void, Double[][]> {

    public RetrieveQRandResponse delegate = null;

    public RetrieveQRandTask(RetrieveQRandResponse delegate){
        this.delegate=delegate;
    }

    private Double[][] getRandomMultiplier(Integer n){

        //Gets bytes from server, will exit if server inaccessible
        Integer randHexNum = 0;
        Double[][] randMultiplier = new Double[n][2];
        AnuRandom random = new AnuRandom(n);
        String temp = new String(random.getBytes());
        int strSplitCounter = 0;
        int strSplitEnd = 2;
        //Gets bytes from server, throws catchable exception if server inaccessible
        try {
            temp = new String(random.getBytesSafe());
        } catch (IOException e) {
            //Handle inaccessible server
        }

        Log.d("qLlamaThread", temp);

        while (strSplitEnd <= temp.length()) {
            String ss = temp.substring(strSplitCounter , strSplitEnd);
            Log.d("qLlamaThread", ss);

            //Do stuff here.

            strSplitCounter  += 2;
            strSplitEnd += 2;
        }


        // algorithm for adding and converting string hex bytes to 0-1 range random double array
        //for(int i=0; i<=n-1; i++){
            for(int j=0; j<=1; j++){
                //Convert from base 16 string to int
                randHexNum = Integer.parseInt(temp, 16);
                randMultiplier[0][j] = randHexNum/255.0; // divide by the max hex value to return a value between 0-1.
                randMultiplier[0][j] = 1.0;
                Log.d("qLlamaThread", "In j loop temp = "+temp);
            }
        //}

        return randMultiplier;
    }

    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
    protected Double[][] doInBackground(Integer... n) {
        return getRandomMultiplier(n[0]);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    protected void onPostExecute(Double[][] result) {
        delegate.processFinish(result);
    }


}
