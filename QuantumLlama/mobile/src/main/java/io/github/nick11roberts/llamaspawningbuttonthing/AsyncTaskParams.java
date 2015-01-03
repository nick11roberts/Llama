package io.github.nick11roberts.llamaspawningbuttonthing;


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

    public void setN(Integer nInput){
        this.n = nInput;
    }

    public Integer getN(){
        return this.n;
    }

    public void setIsConversionTask(Boolean isConversionTaskInput){
        this.isConversionTask = isConversionTaskInput;
    }

    public Boolean getIsConversionTask(){
        return this.isConversionTask;
    }
}
