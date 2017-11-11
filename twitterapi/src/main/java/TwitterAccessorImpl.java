import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TwitterAccessorImpl implements TwitterAccessor {

    private static final Logger logger = LoggerFactory.getLogger(TwitterAccessorImpl.class);

    private final static String BEARERPREFIX = "Bearer ";

    private JSONParser jsonParser;

    private String bearer;

    private String requestURL;

    private String nextMaxId;


    public TwitterAccessorImpl(String bearer, String requestURL) {
        this.bearer = BEARERPREFIX + bearer;
        this.requestURL = requestURL;
        jsonParser = new JSONParser();
    }


    @Override
    public List<Tweet> getTweets(String hashtag) {

        HttpRequest httpRequest = Unirest.get(requestURL)
                .header("Authorization", bearer)
                .queryString("q",  hashtag)
                .queryString("result_type", "recent")
                .queryString("count", "100");

        if (nextMaxId != null) {
            httpRequest.queryString("max_id", nextMaxId);
        }

        HttpResponse<JsonNode> jsonNodeHttpResponse;
        try {
            jsonNodeHttpResponse = httpRequest.asJson();
        } catch (UnirestException e) {
            logger.error("Smth went wrong while getting tweets using a query {}", httpRequest.getUrl(), e);
            throw new RuntimeException(e);
        }

        nextMaxId = jsonParser.getNextMaxId(jsonNodeHttpResponse);

        return jsonParser.getTweets(jsonNodeHttpResponse);
    }
}
