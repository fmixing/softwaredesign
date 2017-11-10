import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;


@RunWith(PowerMockRunner.class)
@PrepareForTest(TweetsCounterImpl.class)
public class TweetsCounterTest {

    private static final int limitHours = 10;

    private List<Long> answer = new ArrayList<>();

    private DateTime testDateTime = new DateTime();

    private TweetsCounter tweetsCounter;


    @Before
    public void setUp() throws Exception {
        RequesterImpl requester = mock(RequesterImpl.class);
        whenNew(RequesterImpl.class).withArguments(anyString(), anyString()).thenReturn(requester);
        tweetsCounter = new TweetsCounterImpl("bearer", "url");
        when(requester.searchWithLimit(any(DateTime.class), any(String.class)))
                .thenReturn(createTweetsWithLimit());
    }


    @Test
    public void testTweetsCounting() {
        List<Long> hashtag = tweetsCounter.getTweetsCount(limitHours, "hashtag");

        Assert.assertThat(hashtag, is(answer));
    }


    private List<Tweet> createTweetsWithLimit() {
        DateTime dateTime = new DateTime(testDateTime);

        Random random = new Random();

        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < limitHours; i++) {
            DateTime tweetDateTime = dateTime.minusHours(i);

            int randCount = random.nextInt(3600);
            answer.add((long) randCount);

            for (int j = 0; j < randCount; j++) {
                tweets.add(new Tweet(tweetDateTime.minusSeconds(1)));
                tweetDateTime = tweetDateTime.minusSeconds(1);
            }
        }

        return tweets;
    }
}
