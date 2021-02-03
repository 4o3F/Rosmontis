package moe.exusiai.timer;

import moe.exusiai.watcher.DynamicWatcher;
import moe.exusiai.watcher.LiveWatcher;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer implements Runnable{
    public static void StartTimer() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.scheduleAtFixedRate(new Timer(), 1, 10, TimeUnit.SECONDS);
    }
    public void run() {
        DynamicWatcher.DynamicWatcher();
        LiveWatcher.LiveWatcher();
        //System.out.println(System.currentTimeMillis());
    }
}
