package io.github.nick11roberts.llamaspawningbuttonthing;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;

public class RetrieveQRandTask extends AsyncTask<AsyncTaskParams, Integer, double[][]> {


    public RetrieveQRandResponse delegate = null;
    private AsyncTaskParams taskParameters;
    private Activity c;
    private ProgressDialog progressDialog;
    protected Boolean networkFail = false;
    private final Integer NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA = 3;
    private Integer n;


    public RetrieveQRandTask(RetrieveQRandResponse delegate, Activity context, Integer inN){
        this.delegate=delegate;
        this.c = context;
        this.n = inN;
    }

    private double[][] getRandomMultiplier(Integer n){

        //Gets bytes from server, will exit if server inaccessible
        Integer randHexNum;

        double[][] randMultiplier = new double[n/ NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA][NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA];
        String[][] deconstructedString = new String[n/ NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA][NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA];
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
            for (int i = 0; i <= (n / NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA) - 1; i++) {
                for (int j = 0; j <= NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA - 1; j++) {
                    randMultiplier[i][j] = 0;
                }
            }
        }
        else {
            // algorithm for adding and converting string hex bytes to 0-1 range random double array
            for (int i = 0; i <= (n / NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA) - 1; i++) {
                for (int j = 0; j <= NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA - 1; j++) {
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
        if(n == NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA) {
            progressDialog = ProgressDialog.show(
                    this.c,
                    c.getResources().getString(R.string.downloading_head),
                    c.getResources().getString(R.string.downloading_body_si),
                    false
            );
        }else if(n==0){
            progressDialog = ProgressDialog.show(
                    this.c,
                    c.getResources().getString(R.string.downloading_head),
                    c.getResources().getString(R.string.downloading_body_pl_start)
                            +" "+c.getResources().getString(R.string.no_lower)+" "
                            +c.getResources().getString(R.string.downloading_body_pl_end),
                    false
            );
        }else{
            progressDialog = ProgressDialog.show(
                    this.c,
                    c.getResources().getString(R.string.downloading_head),
                    c.getResources().getString(R.string.downloading_body_pl_start)
                            +" "+Integer.toString(n/NUMBER_OF_REQUIRED_Q_RANDS_PER_LLAMA)
                            +" "+c.getResources().getString(R.string.downloading_body_pl_end),
                    false
            );
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    protected void onPostExecute(double[][] result) {

        if(networkFail){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View toastLayout = inflater.inflate(R.layout.toast_layout,
                    (ViewGroup) c.findViewById(R.id.toast_layout_root));

            Toast failToast = new Toast(c);

            CustomTypefaceTextView text = (CustomTypefaceTextView) toastLayout.findViewById(R.id.toast_text);
            text.setText(c.getResources().getString(R.string.toast_quantum_llama_network_error));

            failToast.setView(toastLayout);
            failToast.show();

        }

        if(!networkFail) {
            if (this.taskParameters.getIsConversionTask())
                delegate.processFinishConversionTask(result);
            else
                delegate.processFinish(result);
        }

        super.onPostExecute(result);
        progressDialog.dismiss();



    }


}