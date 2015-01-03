package io.github.nick11roberts.llamaspawningbuttonthing;


import android.content.Context;

public class AsyncTaskParams {
    private Integer n;
    private Boolean isConversionTask;

    public AsyncTaskParams(){
        n = 0;
        isConversionTask = true;

    }

    public AsyncTaskParams(Integer nInput, Boolean isConversionTaskInput){
        this.n = nInput;
        this.isConversionTask = isConversionTaskInput;
    }

    public Integer getN(){
        return this.n;
    }

    public Boolean getIsConversionTask(){
        return this.isConversionTask;
    }
}
