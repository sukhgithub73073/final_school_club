package com.op.eschool.models.attendance;

public class SubjectAttendanceModel {
    public String subject ;
    public double classesHeld ,classesAttended , projectedAtt  , totalAtt ;

    public SubjectAttendanceModel(String subject, double classesHeld, double classesAttended, double projectedAtt, double totalAtt) {
        this.subject = subject;
        this.classesHeld = classesHeld;
        this.classesAttended = classesAttended;
        this.projectedAtt = projectedAtt;
        this.totalAtt = totalAtt;
    }
}
