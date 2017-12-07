package clock;

import java.time.Instant;

public class SettableClock implements Clock {

    private Instant now;


    public SettableClock(Instant now) {
        this.now = now;
    }


    @Override
    public Instant now() {
        return now;
    }


    @Override
    public void setNow(Instant now) {
        this.now = now;
    }
}
