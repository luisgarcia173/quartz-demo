package com.example.quartzdemo.scheduler.services.vo;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Map;

public class JobStructure implements Serializable {

  private String name;
  private String group;
  private String description;
  private Map<String, ?> parameters;
  private boolean singleRun;
  private String cronExpression;
  private ZonedDateTime startAt;

  public JobStructure() {}
  public JobStructure(String name, String group, String description, Map<String, ?> parameters, boolean singleRun, String cronExpression, ZonedDateTime startAt) {
    this.name = name;
    this.group = group;
    this.description = description;
    this.parameters = parameters;
    this.singleRun = singleRun;
    this.cronExpression = cronExpression;
    this.startAt = startAt;
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

  public Map<String, ?> getParameters() {
    return parameters;
  }

  public void setParameters(Map<String, ?> parameters) {
    this.parameters = parameters;
  }

  public boolean isSingleRun() {
    return singleRun;
  }

  public void setSingleRun(boolean singleRun) {
    this.singleRun = singleRun;
  }

  public String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public ZonedDateTime getStartAt() {
    return startAt;
  }

  public void setStartAt(ZonedDateTime startAt) {
    this.startAt = startAt;
  }

  @Override
  public String toString() {
    return "JobStructure{" +
        "name='" + name + '\'' +
        ", group='" + group + '\'' +
        ", description='" + description + '\'' +
        ", parameters=" + parameters +
        ", singleRun=" + singleRun +
        ", cronExpression='" + cronExpression + '\'' +
        ", startAt=" + startAt +
        '}';
  }
}
