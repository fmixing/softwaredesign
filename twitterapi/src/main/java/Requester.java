import twitter4j.Status;

import java.util.Date;
import java.util.List;

public interface Requester {

    List<Status> searchWithLimit(Date limit, String hashtag);
}
