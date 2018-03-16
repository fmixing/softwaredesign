package messages;

public abstract class SearchRequest {
    private final String request;

    SearchRequest(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}
