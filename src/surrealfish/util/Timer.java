package surrealfish.util;

public class Timer {

    private float original;
    private float timeLeft;
    private boolean active = false;
    private boolean timeJustEnded = false;

    public Timer(float seconds) {
        original = seconds;
        timeLeft = seconds;
    }

    public void update(float tpf) {
        timeJustEnded = false;

        if (!active) {
            return;
        }

        if (timeLeft > 0) {
            timeLeft -= tpf;
            if (timeLeft <= 0) {
                timeJustEnded = true;
            }
        }
    }

    public float getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
        original = timeLeft;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean timeJustEnded() {
        return timeJustEnded;
    }

    public float getOriginal() {
        return original;
    }
}
