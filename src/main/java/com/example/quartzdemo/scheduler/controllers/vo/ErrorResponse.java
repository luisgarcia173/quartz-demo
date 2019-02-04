package com.example.quartzdemo.scheduler.controllers.vo;

import org.springframework.http.HttpStatus;

public class ErrorResponse {

  private HttpStatus status;
  private String errorCode;
  private String message;
  private String detail;

  private ErrorResponse() {}

  public static final class ErrorResponseBuilder {

    private HttpStatus status;
    private String errorCode;
    private String message;
    private String detail;

    public ErrorResponseBuilder(HttpStatus status) {
      this.status = status;
    }

    public ErrorResponseBuilder withErrorCode(String errorCode) {
      this.errorCode = errorCode;
      return this;
    }

    public ErrorResponseBuilder withMessage(String message) {
      this.message = message;
      return this;
    }

    public ErrorResponseBuilder withDetail(String detail) {
      this.detail = detail;
      return this;
    }

    public ErrorResponse build(){
      ErrorResponse error = new ErrorResponse();
      error.status = this.status;
      error.errorCode = this.errorCode;
      error.message = this.message;
      error.detail = this.detail;
      return error;
    }

  }

  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDetail() {
    return detail;
  }

  public void setDetail(String detail) {
    this.detail = detail;
  }
}