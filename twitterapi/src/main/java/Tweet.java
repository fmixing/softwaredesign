import org.joda.time.DateTime;

public class Tweet {

    private final DateTime creationDate;


    public Tweet(DateTime creationDate) {
        this.creationDate = creationDate;
    }


    public DateTime getCreationDate() {
        return creationDate;
    }
}
