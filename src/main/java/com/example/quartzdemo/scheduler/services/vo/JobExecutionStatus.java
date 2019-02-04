package com.example.quartzdemo.scheduler.services.vo;

import org.quartz.JobDetail;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

public class JobExecutionStatus implements Serializable {

  private boolean success;
  private String message;

  private JobExecutionStatus(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public static JobExecutionStatus buildAsFailedAttempt(String action) {
    return new JobExecutionStatus(false, "It was not possible to " +action + " requested Job.");
  }

  public static JobExecutionStatus buildSuccess(String jobKey) {
    return buildSuccess(jobKey, null);
  }
  public static JobExecutionStatus buildSuccess(String jobKey, Temporal executionDate) {
    if (jobKey == null) return new JobExecutionStatus(true, "Job performed successfully.");
    if (executionDate == null) return new JobExecutionStatus(true, "Job [" + jobKey + "] performed successfully.");
    else return new JobExecutionStatus(true, "Job [" + jobKey + "] performed successfully, next execution: " + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(executionDate));
  }

  public static JobExecutionStatus buildOk(String jobKey, String message) {
    return new JobExecutionStatus(true, "Job [" + jobKey + "]: " + message + ".");
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "JobExecutionStatus{" +
        "success=" + success +
        ", message='" + message + '\'' +
        '}';
  }

}
