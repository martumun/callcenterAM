package model;

import java.util.Random;

public class Call {
    private final String id;
    private final Integer duration;
    private CallStatus status;

    private final Random randomDuration = new Random();
    private static final Integer MIN_DURATION = 5000;
    private static final Integer MAX_DURATION = 10000;

    // Set random duration to new call
    public Call(String id) {
        this.id = id;
        this.duration = randomDuration.nextInt(MAX_DURATION - MIN_DURATION + 1) + MIN_DURATION;
        this.status = CallStatus.ON_HOLD;
    }

    public Integer getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public CallStatus getStatus() {
        return status;
    }

    public void setStatus(CallStatus status) {
        this.status = status;
    }
}
