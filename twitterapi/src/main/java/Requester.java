import org.joda.time.DateTime;

import java.util.List;

public interface Requester {

    List<Tweet> searchWithLimit(DateTime limit, String hashtag);
}
