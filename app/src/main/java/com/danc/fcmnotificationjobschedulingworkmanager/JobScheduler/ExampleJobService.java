package com.danc.fcmnotificationjobschedulingworkmanager.JobScheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    private boolean jobCanceled = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: Job Started");
        doBackGroundWork(jobParameters);
        return true;
    }

    private void doBackGroundWork(JobParameters jobParameters){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 10; i ++){
                    Log.d(TAG, "run: " + i);

                    if (jobCanceled){
                        return;
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e){
                        Log.d(TAG, "run: " + e.getMessage());
                    }
                }

                Log.d(TAG, "run: Job Finished");
                jobFinished(jobParameters, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: Job Canceled Before Completion");
        jobCanceled = true;
        return true;
    }
}
