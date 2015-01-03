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
        Double[][] randMultiplier = new Double[n/2][2];
        String[][] deconstructedString = new String[n/2][2];
        AnuRandom random = new AnuRandom(n);
        String hexFromServer = new String(random.getBytes());
        int strSplitCounter = 0;
        int strSplitEnd = 2;

        int iteratorDimensionOne = 0;
        int iteratorDimensionTwo = 0;

        //Gets bytes from server, throws catchable exception if server inaccessible
        try {
            hexFromServer = new String(random.getBytesSafe());
        } catch (IOException e) {
            //Handle inaccessible server
        }

        // algorithm for adding and converting string hex bytes to 0-1 range random double array
        for(int i=0; i<=(n/2)-1; i++){
            for(int j=0; j<=1; j++){
                String ss = hexFromServer.substring(strSplitCounter , strSplitEnd);
                deconstructedString[i][j] = ss;

                //Convert from base 16 string to int
                randHexNum = Integer.parseInt(deconstructedString[i][j], 16);
                randMultiplier[i][j] = randHexNum/255.0; // divide by the max hex value to return a value between 0-1.

                strSplitCounter  += 2;
                strSplitEnd += 2;
            }
        }

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
