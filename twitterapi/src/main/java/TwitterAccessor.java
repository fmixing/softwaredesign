import java.util.List;

public interface TwitterAccessor {

    List<Tweet> getTweets(String hashtag);
}
