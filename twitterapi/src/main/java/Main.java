import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.body.RequestBodyEntity;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

public class Main {

    private static String path = "twitterapi.properties";

    private static String url = "https://api.twitter.com/1.1/search/tweets.json";

    public static void main(String[] args) throws IOException {
        try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {

            Properties properties = new Properties();
            properties.load(is);
            String bearer = properties.getProperty("bearer");

            TweetsCounter tweetsCounter = new TweetsCounterImpl(bearer, url);

            List<Long> tweetsCount = tweetsCounter.getTweetsCount(3, "#GoGoVP");

            System.out.println(tweetsCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void getBearer() throws IOException, UnirestException{
        try (InputStream is = ClassLoader.getSystemResourceAsStream(path)) {
            Properties properties = new Properties();
            properties.load(is);
            String apikey = properties.getProperty("apikey");
            String apisecret = properties.getProperty("apisecret");

            String s = Base64.getEncoder().encodeToString((apikey + ":" + apisecret).getBytes());

            RequestBodyEntity authorization = Unirest.post("https://api.twitter.com/oauth2/token")
                    .header("Authorization", "Basic " + s)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .body("grant_type=client_credentials");

            HttpResponse<JsonNode> jsonNodeHttpResponse = authorization.asJson();

            JSONObject object = jsonNodeHttpResponse.getBody().getObject();

            Object access_token = object.get("access_token");

            System.out.println(access_token.toString());
        }
    }
}
