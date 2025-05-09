// Feedback.java
package com.chill.feedback.model;

import java.util.Date;
import java.util.UUID;

public interface Feedback {
    UUID getFeedbackId();
    String getUserId();
    String getVendorId();
    String getComment();
    Date getCreatedAt();

    void setComment(String comment);

}
