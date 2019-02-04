package com.example.quartzdemo.scheduler.services.vo;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class JobDetails implements Serializable {

  private String name;
  private String group;
  private String description;
  private String cronExpression;
  private String state;
  private Date startTime;
  private Date endTime;
  private Date nextFireTime;
  private Date previousFireTime;

  public JobDetails() {}
  public JobDetails(String name, String group, String description, String cronExpression, String state, Date startTime, Date endTime, Date nextFireTime, Date previousFireTime) {
    this.name = name;
    this.group = group;
    this.description = description;
    this.cronExpression = cronExpression;
    this.state = state;
    this.startTime = startTime;
    this.endTime = endTime;
    this.nextFireTime = nextFireTime;
    this.previousFireTime = previousFireTime;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public Date getNextFireTime() {
    return nextFireTime;
  }

  public void setNextFireTime(Date nextFireTime) {
    this.nextFireTime = nextFireTime;
  }

  public Date getPreviousFireTime() {
    return previousFireTime;
  }

  public void setPreviousFireTime(Date previousFireTime) {
    this.previousFireTime = previousFireTime;
  }

  @Override
  public String toString() {
    return "JobDetails{" +
        "name='" + name + '\'' +
        ", group='" + group + '\'' +
        ", description='" + description + '\'' +
        ", cronExpression='" + cronExpression + '\'' +
        ", state='" + state + '\'' +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", nextFireTime=" + nextFireTime +
        ", previousFireTime=" + previousFireTime +
        '}';
  }
}
