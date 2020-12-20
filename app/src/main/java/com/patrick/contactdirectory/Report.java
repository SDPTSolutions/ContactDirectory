package com.patrick.contactdirectory;

public class Report {

    private String message;
    private String reporterName;

    public Report(){

    }

    public Report(String message, String reporterName) {
        this.message = message;
        this.reporterName = reporterName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }
}
