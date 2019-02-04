package com.example.quartzdemo.scheduler.utils;

import org.quartz.JobKey;
import org.quartz.TriggerKey;

public class JobKeyUtils {

  private JobKeyUtils() { }

  public static JobKey get(String id, String group) {
    return new JobKey(id, group);
  }

  public static TriggerKey getTriggerKey(String id, String group) {
    return new TriggerKey(id, group);
  }

}
