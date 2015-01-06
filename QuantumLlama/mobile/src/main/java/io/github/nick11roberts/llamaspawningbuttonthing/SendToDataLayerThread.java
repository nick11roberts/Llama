package io.github.nick11roberts.llamaspawningbuttonthing;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

class SendToDataLayerThread extends Thread {
    private String path;
    private DataMap dataMap;
    private GoogleApiClient googleClient;

    // Constructor for sending data objects to the data layer
    SendToDataLayerThread(String p, DataMap data, GoogleApiClient client) {
        this.path = p;
        this.dataMap = data;
        this.googleClient = client;
    }

    public void run() {
        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(this.googleClient).await();
        for (Node node : nodes.getNodes()) {

            // Construct a DataRequest and send over the data layer
            PutDataMapRequest putDMR = PutDataMapRequest.create(this.path);
            putDMR.getDataMap().putAll(this.dataMap);
            PutDataRequest request = putDMR.asPutDataRequest();
            DataApi.DataItemResult result = Wearable.DataApi.putDataItem(this.googleClient,request).await();
            if (result.getStatus().isSuccess()) {
                Log.v("myTag", "DataMap: " + this.dataMap + " sent to: " + node.getDisplayName());
            } else {
                // Log an error
                Log.v("myTag", "ERROR: failed to send DataMap");
            }
        }
    }
}