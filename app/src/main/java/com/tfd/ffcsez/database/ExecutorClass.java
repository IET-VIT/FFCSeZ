package com.tfd.ffcsez.database;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorClass {

    private static final Object LOCK = new Object();
    private static ExecutorClass instance;
    private final Executor diskIO;

    public ExecutorClass(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static ExecutorClass getInstance(){
        if (instance == null){
            synchronized (LOCK){
                instance = new ExecutorClass(Executors.newSingleThreadExecutor());
            }
        }
        return instance;
    }

    public Executor diskIO() {
        return diskIO;
    }
}
