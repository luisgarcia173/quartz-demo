package com.example.quartzdemo.scheduler.services.impl;

import com.example.quartzdemo.scheduler.services.SchedulerService;
import com.example.quartzdemo.scheduler.services.vo.JobDetails;
import com.example.quartzdemo.scheduler.services.vo.JobExecutionStatus;
import com.example.quartzdemo.scheduler.services.vo.JobStructure;
import com.example.quartzdemo.scheduler.utils.JobKeyUtils;
import com.example.quartzdemo.scheduler.utils.JobsUtils;
import com.google.common.collect.Lists;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class SchedulerServiceImpl implements SchedulerService {

  @Autowired
  private Scheduler scheduler;

  @Override
  public List<JobDetails> listExistingJobs() {

    List<JobDetails> jobsDetails = Lists.newArrayList();

    List<String> jobGroupNames = this.getJobGroupNames();
    jobGroupNames.forEach(jobGroup -> this.fillJobListWithTriggerDetails(jobGroup, jobsDetails));

    return jobsDetails;

  }

  @Override
  public List<String> listCandidateJobs() {
    return JobsUtils.getExistingJobs();
  }

  @Override
  public JobExecutionStatus pauseJobById(String jobId, String jobGroup) {
    try {
      this.scheduler.pauseJob(JobKeyUtils.get(jobId, jobGroup));
      return JobExecutionStatus.buildOk(JobKeyUtils.get(jobId, jobGroup).toString(), "successfully paused");
    } catch (SchedulerException e) {
      System.err.println("Error while pausing job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("pause");
    }
  }

  @Override
  public JobExecutionStatus resumeJobById(String jobId, String jobGroup) {
    try {
      this.scheduler.resumeJob(JobKeyUtils.get(jobId, jobGroup));
      return JobExecutionStatus.buildOk(JobKeyUtils.get(jobId, jobGroup).toString(), "successfully resumed");
    } catch (SchedulerException e) {
      System.err.println("Error while resuming job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("resume");
    }
  }

  @Override
  public JobExecutionStatus stopRunningJobById(String jobId, String jobGroup) {
    try {
      this.scheduler.interrupt(JobKeyUtils.get(jobId, jobGroup));
      return JobExecutionStatus.buildOk(JobKeyUtils.get(jobId, jobGroup).toString(), "successfully stopped");
    } catch (SchedulerException e) {
      System.err.println("Error while stopping job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("stop");
    }
  }

  @Override
  public JobExecutionStatus deleteJobById(String jobId, String jobGroup) {
    try {
      this.scheduler.deleteJob(JobKeyUtils.get(jobId, jobGroup));
      return JobExecutionStatus.buildOk(JobKeyUtils.get(jobId, jobGroup).toString(), "successfully deleted");
    } catch (SchedulerException e) {
      System.err.println("Error while deleting job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("delete");
    }
  }

  @Override
  public JobExecutionStatus unscheduleJobById(String jobId, String jobGroup) {
    try {
      this.scheduler.unscheduleJob(JobKeyUtils.getTriggerKey(jobId, jobGroup));
      return JobExecutionStatus.buildOk(JobKeyUtils.get(jobId, jobGroup).toString(), "successfully unscheduled");
    } catch (SchedulerException e) {
      System.err.println("Error while unschedulling job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("unschedule");
    }
  }

  @Override
  public JobExecutionStatus executeJobById(String jobId, String jobGroup) {
    try {
      this.scheduler.triggerJob(JobKeyUtils.get(jobId, jobGroup));
      return JobExecutionStatus.buildOk(JobKeyUtils.get(jobId, jobGroup).toString(), "successfully triggered");
    } catch (SchedulerException e) {
      System.err.println("Error while triggering job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("trigger");
    }
  }

  @Override
  public JobDetails findJobById(String jobId, String jobGroup) {
    return this.retrieveJobDetailsByKey(JobKeyUtils.get(jobId, jobGroup));
  }

  @Override
  public JobExecutionStatus updateJob(String jobId, String jobGroup, String cronExpression) {
    try {
      JobDetails existingJob = this.findJobById(jobId, jobGroup);
      Trigger newTrigger = this.buildCronTrigger(existingJob.getName(), existingJob.getGroup(), existingJob.getDescription(), cronExpression);
      this.scheduler.rescheduleJob(JobKeyUtils.getTriggerKey(jobId, jobGroup), newTrigger);
      return JobExecutionStatus.buildOk(JobKeyUtils.get(jobId, jobGroup).toString(), "successfully updated");
    } catch (SchedulerException e) {
      System.err.println("Error while updating job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("update");
    }
  }

  @Override
  public JobExecutionStatus persistJob(JobStructure job) {
    try {
      // Get job data
      JobDetail jobDetail = this.buildJobDetail(this.getJobClassForName(job.getName()), job.getGroup(), job.getDescription(), job.getParameters());

      // Get trigger data
      Trigger trigger;
      if (job.isSingleRun()) {
        trigger = this.buildSimpleTrigger(jobDetail.getKey().getName(), job.getGroup(), job.getDescription());
      } else {
        trigger = this.buildCronTrigger(jobDetail.getKey().getName(), job.getGroup(), job.getDescription(), job.getCronExpression());
      }

      // Setup scheduler
      scheduler.scheduleJob(jobDetail, trigger);

      return JobExecutionStatus.buildSuccess(jobDetail.getKey().toString());
    } catch (Exception e) {
      System.err.println("Error while creating job: " + e);
      return JobExecutionStatus.buildAsFailedAttempt("create");
    }
  }

  private List<String> getJobGroupNames() {
    try {
      return this.scheduler.getJobGroupNames();
    } catch (SchedulerException e) {
      System.err.println("Error while getting job group names: " + e);
    }
    return Lists.newArrayList();
  }

  private void fillJobListWithTriggerDetails(String jobGroup, List<JobDetails> jobs) {
    try {
      this.scheduler.getJobKeys(GroupMatcher.jobGroupEquals(jobGroup)).forEach(jobKey -> {
        // Add to our jobs list
        JobDetails job = retrieveJobDetailsByKey(jobKey);
        if (job != null) {
          jobs.add(job);
        }
      });
    } catch (SchedulerException e) {
      System.err.println("Error while getting job keys from job group name: " + e);
    }
  }

  private JobDetails retrieveJobDetailsByKey(JobKey jobKey) {
    try {
      // Job Simple Details
      final JobDetail jobDetail = scheduler.getJobDetail(jobKey);
      JobDetails job = new JobDetails();
      job.setName(jobKey.getName());
      job.setGroup(jobKey.getGroup());
      job.setDescription(jobDetail.getDescription());

      // Job Trigger Details
      final Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
      job.setStartTime(trigger.getStartTime());
      job.setStartTime(trigger.getEndTime());
      job.setNextFireTime(trigger.getNextFireTime());
      job.setPreviousFireTime(trigger.getPreviousFireTime());

      // Job Trigger Settings
      if (trigger instanceof CronTrigger) {
        job.setCronExpression(((CronTrigger) trigger).getCronExpression());
      }

      // Job Trigger Current State
      final Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
      job.setState(triggerState.toString());

      return job;
    } catch (SchedulerException e) {
      System.err.println("Error while getting trigger details: " + e);
    }
    return null;
  }

  private JobDetail buildJobDetail(Class<? extends Job> jobClass, String group, String description, Map<String, ?> jobParameters) {
    JobDataMap jobDataMap = new JobDataMap();
    if (jobParameters != null) jobDataMap.putAll(jobParameters);

    return JobBuilder.newJob(jobClass)
        .withIdentity(UUID.randomUUID().toString(), group)
        .withDescription(description)
        .usingJobData(jobDataMap)
        .storeDurably()
        .build();
  }

  private Trigger buildCronTrigger(String jobName, String triggerGroup, String triggerDescription, String cronExpression) {
    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(jobName, triggerGroup)
        .withDescription(triggerDescription)
        //.startAt(Date.from(startAt.toInstant()))
        //.endAt()
        .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
        .forJob(JobKeyUtils.get(jobName, triggerGroup))
        .build();
    return trigger;
  }

  private Trigger buildSimpleTrigger(String jobName, String triggerGroup, String triggerDescription) {
    Trigger trigger = TriggerBuilder.newTrigger()
        .withIdentity(jobName, triggerGroup)
        .withDescription(triggerDescription)
        .forJob(JobKeyUtils.get(jobName, triggerGroup))
        .build();
    return trigger;
  }

  private Class<? extends Job> getJobClassForName(String name) throws ClassNotFoundException {
    Class<? extends Job> jobClass = (Class<? extends Job>) Class.forName("com.example.quartzdemo.scheduler.jobs." + name);
    return jobClass;
  }


}
