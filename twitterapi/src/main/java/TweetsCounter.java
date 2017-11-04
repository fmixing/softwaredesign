import java.util.List;

public interface TweetsCounter {

    List<Long> getTweetsCount(int countOfHours, String hashtag);
}
