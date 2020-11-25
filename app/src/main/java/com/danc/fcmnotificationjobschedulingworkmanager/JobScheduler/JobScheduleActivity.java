package com.danc.fcmnotificationjobschedulingworkmanager.JobScheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.danc.fcmnotificationjobschedulingworkmanager.R;

public class JobScheduleActivity extends AppCompatActivity {

    private static final String TAG = "JobScheduleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_schedule);
    }

    public void scheduleJob(View view) {
        ComponentName componentName = new ComponentName(this, ExampleJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = jobScheduler.schedule(info);
        
        if (resultCode == JobScheduler.RESULT_SUCCESS){
            Log.d(TAG, "scheduleJob: Job Scheduled");
        } else {
            Log.d(TAG, "scheduleJob: Job Schedule Failed");
        }
    }

    public void cancelJob(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "cancelJob: Job Cancelled");
    }
}