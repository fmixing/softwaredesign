package messages;

public class Request {

    private final int timeoutMs;

    private final String request;

    public Request(int timeoutMs, String request) {
        this.timeoutMs = timeoutMs;
        this.request = request;
    }

    public int getTimeoutMs() {
        return timeoutMs;
    }

    public String getRequest() {
        return request;
    }
}
