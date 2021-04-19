package me.befell.hypixelindicators.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
    final ExecutorService executor = Executors.newFixedThreadPool(5);
    public void start(Runnable task){
        executor.execute(task);
    }
}
