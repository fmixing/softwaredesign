package clock;

import java.time.Instant;

public interface Clock {

    Instant now();

    void setNow(Instant now);
}
