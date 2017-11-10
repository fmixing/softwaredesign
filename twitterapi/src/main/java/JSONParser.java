import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class JSONParser {

    private static Logger logger = LoggerFactory.getLogger(JSONParser.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d hh:mm:ss ZZZZZ yyyy", Locale.ENGLISH);


    public List<Tweet> getTweets(HttpResponse<JsonNode> jsonNodeHttpResponse) {
        List<Tweet> result = new ArrayList<>();
        JSONArray jsonTweets = jsonNodeHttpResponse.getBody().getObject().getJSONArray("statuses");

        for (int i = 0; i < jsonTweets.length(); i++) {
            JSONObject jsonObject = jsonTweets.getJSONObject(i);

            JSONObject user = jsonObject.getJSONObject("user");
            System.out.println(user.getString("screen_name"));

            try {
                Date date = dateFormat.parse(jsonObject.getString("created_at"));

                result.add(new Tweet(new DateTime(date)));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }

        return result;
    }

    public String getNextMaxId(HttpResponse<JsonNode> jsonNodeHttpResponse) {
        JSONObject search_metadata;
        try {
            search_metadata = jsonNodeHttpResponse.getBody().getObject().getJSONObject("search_metadata");
        } catch (JSONException e) {
            logger.error("Got a request {}", jsonNodeHttpResponse.getBody().toString());
            throw new RuntimeException(e);
        }

        String next_results = search_metadata.getString("next_results");

        List<String> requestList = Arrays.stream(next_results.split("\\?")).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        assert requestList.size() == 1;

        String request = requestList.get(0);

        Optional<String> max_id = Optional.empty();
        try {
            max_id = Arrays.stream(request.split("&"))
                    .filter(s -> s.startsWith("max_id"))
                    .map(s -> s.split("=")[1]).findAny();
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("Smth went really bad while parsing max_id from {}", request);
        }

        if (!max_id.isPresent()) {
            logger.error("Smth went really bad while parsing max_id from {}", request);
            throw new RuntimeException();
        }

        return max_id.get();
    }
}
