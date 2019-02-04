package com.example.quartzdemo.scheduler.services;

import com.example.quartzdemo.scheduler.services.vo.JobDetails;
import com.example.quartzdemo.scheduler.services.vo.JobExecutionStatus;
import com.example.quartzdemo.scheduler.services.vo.JobStructure;
import org.quartz.SchedulerException;

import java.util.List;

public interface SchedulerService {

  List<JobDetails> listExistingJobs();

  List<String> listCandidateJobs();

  JobExecutionStatus pauseJobById(String jobId, String jobGroup);

  JobExecutionStatus resumeJobById(String jobId, String jobGroup);

  JobExecutionStatus stopRunningJobById(String jobId, String jobGroup);

  JobExecutionStatus deleteJobById(String jobId, String jobGroup);

  JobExecutionStatus unscheduleJobById(String jobId, String jobGroup);

  JobExecutionStatus executeJobById(String jobId, String jobGroup);

  JobDetails findJobById(String jobId, String jobGroup);

  JobExecutionStatus updateJob(String jobId, String jobGroup, String cronExpression);

  JobExecutionStatus persistJob(JobStructure job);
}
