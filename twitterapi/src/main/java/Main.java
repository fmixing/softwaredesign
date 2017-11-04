import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class Main {

    private static String path = "twitterapi.properties";

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {
            properties.load(is);
            TwitterProperties twitterProperties =
                    new TwitterProperties(properties.getProperty("apikey"),
                            properties.getProperty("apisecret"),
                            properties.getProperty("token"),
                            properties.getProperty("tokensecret"));
            TweetsCounter tweetsCounter = new TweetsCounterImpl(twitterProperties);
            List<Long> tweetsCount = tweetsCounter.getTweetsCount(20, "#GoGoVP");

            System.out.println(tweetsCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
