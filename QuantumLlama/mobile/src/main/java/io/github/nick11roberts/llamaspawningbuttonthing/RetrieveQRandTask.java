package io.github.nick11roberts.llamaspawningbuttonthing;

import android.accounts.NetworkErrorException;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.ConnectException;

public class RetrieveQRandTask extends AsyncTask<AsyncTaskParams, Integer, double[][]> {


    public RetrieveQRandResponse delegate = null;
    private AsyncTaskParams taskParameters;
    private Context c;
    private ProgressDialog progressDialog;
    protected Boolean networkFail = false;


    public RetrieveQRandTask(RetrieveQRandResponse delegate, Context context){
        this.delegate=delegate;
        this.c = context;
    }

    private double[][] getRandomMultiplier(Integer n){

        //Gets bytes from server, will exit if server inaccessible
        Integer randHexNum;
        Integer numberOfRequiredRands = 3;
        double[][] randMultiplier = new double[n/numberOfRequiredRands][numberOfRequiredRands];
        String[][] deconstructedString = new String[n/numberOfRequiredRands][numberOfRequiredRands];
        String hexFromServer = "";
        int strSplitCounter = 0;
        int strSplitEnd = 2;

        //Gets bytes from server, throws catchable exception if server inaccessible
        ConnectivityManager cm =
                (ConnectivityManager)this.c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            try {
                AnuRandom random = new AnuRandom(n);
                hexFromServer = new String(random.getBytes());
                hexFromServer = new String(random.getBytesSafe());
            } catch (IOException ioE) {
                networkFail = true;
            }
        }
        else{
            networkFail=true;
        }

        if(networkFail){
            for (int i = 0; i <= (n / numberOfRequiredRands) - 1; i++) {
                for (int j = 0; j <= numberOfRequiredRands - 1; j++) {
                    randMultiplier[i][j] = 0;
                }
            }
        }
        else {
            // algorithm for adding and converting string hex bytes to 0-1 range random double array
            for (int i = 0; i <= (n / numberOfRequiredRands) - 1; i++) {
                for (int j = 0; j <= numberOfRequiredRands - 1; j++) {
                    String ss = hexFromServer.substring(strSplitCounter, strSplitEnd);
                    deconstructedString[i][j] = ss;

                    //Convert from base 16 string to int
                    randHexNum = Integer.parseInt(deconstructedString[i][j], 16);
                    randMultiplier[i][j] = randHexNum / 255.0; // divide by the max hex value to return a value between 0-1.

                    strSplitCounter += 2;
                    strSplitEnd += 2;
                }
            }
        }

        return randMultiplier;
    }

    /** The system calls this to perform work in a worker thread and
     * delivers it the parameters given to AsyncTask.execute() */
    protected double[][] doInBackground(AsyncTaskParams... parameters) {
        this.taskParameters = parameters[0];
        return getRandomMultiplier(parameters[0].getN());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(this.c, "Downloading...","Retrieving quantum llamas", false);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    protected void onPostExecute(double[][] result) {

        if(networkFail){
            Toast.makeText(this.c,"Network error", Toast.LENGTH_SHORT).show();
        }

        if(this.taskParameters.getIsConversionTask())
            delegate.processFinishConversionTask(result);
        else
            delegate.processFinish(result);

        super.onPostExecute(result);
        progressDialog.dismiss();



    }


}