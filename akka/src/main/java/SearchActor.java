import akka.actor.AbstractLoggingActor;
import akka.actor.Props;
import messages.BingRequest;
import messages.GoogleRequest;
import messages.YandexRequest;
import replies.SearchReply;

import java.util.List;

public class SearchActor extends AbstractLoggingActor {

    static final String google = "google.com";

    static final String yandex = "yandex.ru";

    static final String bing = "bing.com";

    private Server server;

    static Props props(Server server) {
        return Props.create(SearchActor.class, server);
    }

    public SearchActor(Server server) {
        this.server = server;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(GoogleRequest.class, googleRequest -> {
                    log().info("Got a searching google request {}", googleRequest.getRequest());
                    log().info("Start operation {}", System.currentTimeMillis());

                    List<String> search = server.createSearch(google, googleRequest.getRequest());

                    log().info("End operation {}", System.currentTimeMillis());
                    getSender().tell(new SearchReply(search), getSelf());
                })
                .match(YandexRequest.class, yandexRequest -> {
                    log().info("Got a searching yandex request {}", yandexRequest.getRequest());
                    log().info("Start operation {}", System.currentTimeMillis());

                    List<String> search = server.createSearch(yandex, yandexRequest.getRequest());

                    log().info("End operation {}", System.currentTimeMillis());
                    getSender().tell(new SearchReply(search), getSelf());
                })
                .match(BingRequest.class, bingRequest -> {
                    log().info("Got a searching bing request {}", bingRequest.getRequest());
                    log().info("Start operation {}", System.currentTimeMillis());

                    List<String> search = server.createSearch(bing, bingRequest.getRequest());

                    log().info("End operation {}", System.currentTimeMillis());
                    getSender().tell(new SearchReply(search), getSelf());
                })
                .matchAny(o -> log().error("Unknown operation {}", o))
                .build();
    }
}
