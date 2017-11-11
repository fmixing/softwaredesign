import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        JSONArray jsonTweets = jsonNodeHttpResponse.getBody().getObject().getJSONArray("statuses");

        long min = Long.MAX_VALUE;

        for (int i = 0; i < jsonTweets.length(); i++) {
            JSONObject jsonObject = jsonTweets.getJSONObject(i);

            String id_str = jsonObject.getString("id_str");
            long l = Long.parseLong(id_str);
            if (l < min) {
                min = l;
            }
        }

        return String.valueOf(min);
    }
}
