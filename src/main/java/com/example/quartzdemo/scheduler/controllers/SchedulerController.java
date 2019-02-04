package com.example.quartzdemo.scheduler.controllers;

import com.example.quartzdemo.scheduler.jobs.SayHelloJob;
import com.example.quartzdemo.scheduler.services.SchedulerService;
import com.example.quartzdemo.scheduler.services.vo.JobDetails;
import com.example.quartzdemo.scheduler.services.vo.JobExecutionStatus;
import com.example.quartzdemo.scheduler.services.vo.JobStructure;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/scheduler")
@Api(value = "Scheduler", description = "Scheduler job operations")
public class SchedulerController {

  @Autowired
  private Scheduler scheduler; //FIXME to remove

  @Autowired
  private SchedulerService schedulerService;

  @ApiOperation(value = "List existing jobs in execution plan or not, but already registered.")
  @GetMapping(path = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<JobDetails> listExistingJobs() {
    return this.schedulerService.listExistingJobs();
  }

  @Cacheable("jobCandidates")
  @ApiOperation(value = "List available jobs to be registered / scheduled.")
  @GetMapping(path = "/jobs/candidate", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<String> listCandidateJobs() {
    return this.schedulerService.listCandidateJobs();
  }

  @ApiOperation(value = "Pause a job during execution plan.")
  @PutMapping(path = "/job/{id}/group/{group}/pause", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> pauseJobById(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.pauseJobById(jobId, jobGroup);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  @ApiOperation(value = "Resume a paused job.")
  @PutMapping(path = "/job/{id}/group/{group}/resume", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> resumeJobById(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.resumeJobById(jobId, jobGroup);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  @ApiOperation(value = "Stop a scheduled job.")
  @PutMapping(path = "/job/{id}/group/{group}/stop", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> stopRunningJobById(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.stopRunningJobById(jobId, jobGroup);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  @ApiOperation(value = "Unschedule an existing job.")
  @PutMapping(path = "/job/{id}/group/{group}/unschedule", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> unscheduleJobById(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.unscheduleJobById(jobId, jobGroup);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  @ApiOperation(value = "Start a job trigger to be executed right now.")
  @PutMapping(path = "/job/{id}/group/{group}/execute", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> executeJobById(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.executeJobById(jobId, jobGroup);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  @ApiOperation(value = "Delete an existing job.")
  @DeleteMapping(path = "/job/{id}/group/{group}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> deleteJobById(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.deleteJobById(jobId, jobGroup);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  @ApiOperation(value = "Retrieve a job by id.")
  @GetMapping(path = "/job/{id}/group/{group}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobDetails> findJobById(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup) {
    JobDetails jobDetails = this.schedulerService.findJobById(jobId, jobGroup);
    if (jobDetails != null) {
      return ResponseEntity.status(HttpStatus.OK).body(jobDetails);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  @ApiOperation(value = "Update an existing job.")
  @PutMapping(path = "/job/{id}/group/{group}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> updateJob(
      @ApiParam("Job identification") @PathVariable("id") String jobId,
      @ApiParam("Job group") @PathVariable("group") String jobGroup,
      @ApiParam("Job Structure") @RequestBody String cronExpression) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.updateJob(jobId, jobGroup, cronExpression);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  @ApiOperation(value = "Add new job to Quartz manager.")
  @PostMapping(path = "/job", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<JobExecutionStatus> persistJob(
      @ApiParam("Job Structure")
      @RequestBody JobStructure job) {
    JobExecutionStatus jobExecutionStatus = this.schedulerService.persistJob(job);
    return ResponseEntity.status(this.getResponseStatus(jobExecutionStatus.isSuccess())).body(jobExecutionStatus);
  }

  private HttpStatus getResponseStatus(boolean isSuccess) {
    return isSuccess ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
  }

}
