
package smartcommunity.domain;

import java.time.LocalDateTime;

public class Feedback {
    private final String feedbackId;
    private final int rating;
    private final String comment;
    private final LocalDateTime submittedAt;

    public Feedback(String feedbackId, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        this.feedbackId = feedbackId;
        this.rating = rating;
        this.comment = comment == null ? "" : comment;
        this.submittedAt = LocalDateTime.now();
    }

    public String getFeedbackId() { return feedbackId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
}
