package com.framelibrary.widget;

public abstract class HZCallbackListener {

    protected abstract void callback(Object... obj);

    public void call(Object... obj) {
        try {
            callback(obj);
        } catch (Exception e) {
        }
    }

}
