package com.chill.feedback.models;

import com.chill.feedback.models.Feedback;
import com.chill.feedback.models.Tag;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("feedbacks")
public class Complaint extends Feedback {
    private Tag tag;

    public Complaint() {}

    public Complaint(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() { return tag; }
    public void setTag(Tag tag) { this.tag = tag; }


}
