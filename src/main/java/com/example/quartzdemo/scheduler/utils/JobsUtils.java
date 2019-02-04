package com.example.quartzdemo.scheduler.utils;

import com.google.common.collect.Lists;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.util.List;
import java.util.Set;

public class JobsUtils {

  private static List<String> existingJobs = Lists.newArrayList();

  private JobsUtils() {}

  public static List<String> getExistingJobs() {
    if (existingJobs.size() > 0) {
      return existingJobs;
    } else {
      List<String> jobs = Lists.newArrayList();

      List<ClassLoader> classLoadersList = Lists.newLinkedList();
      classLoadersList.add(ClasspathHelper.contextClassLoader());
      classLoadersList.add(ClasspathHelper.staticClassLoader());

      Reflections reflections = new Reflections(new ConfigurationBuilder()
          .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
          .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
          .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("com.example.quartzdemo.scheduler.jobs"))));

      Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
      classes.forEach(c -> jobs.add(c.getSimpleName()));

      return jobs;
    }
  }

}
