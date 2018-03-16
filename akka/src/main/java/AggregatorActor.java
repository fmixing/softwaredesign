import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import messages.BingRequest;
import messages.GoogleRequest;
import messages.Request;
import messages.YandexRequest;
import replies.AggregationReply;
import replies.SearchReply;
import scala.concurrent.duration.Duration;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class AggregatorActor extends AbstractLoggingActor {


    private final Map<ActorRef, String> actorWaitingRefs = new HashMap<>();

    private ActorRef waitingActor;

    private Map<String, List<String>> answer = new HashMap<>();

    private Server server;

    public AggregatorActor(Server server) {
        this.server = server;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Request.class, request -> {
                    log().info("Got a searching request {}", request.getRequest());

                    waitingActor = getSender();

                    ActorRef google = getContext().actorOf(SearchActor.props(server));
                    ActorRef yandex = getContext().actorOf(SearchActor.props(server));
                    ActorRef bing = getContext().actorOf(SearchActor.props(server));

                    actorWaitingRefs.put(google, SearchActor.google);
                    actorWaitingRefs.put(yandex, SearchActor.yandex);
                    actorWaitingRefs.put(bing, SearchActor.bing);

                    google.tell(new GoogleRequest(request.getRequest()), getSelf());
                    yandex.tell(new YandexRequest(request.getRequest()), getSelf());
                    bing.tell(new BingRequest(request.getRequest()), getSelf());

                    getContext().setReceiveTimeout(Duration.create(request.getTimeoutMs(), TimeUnit.MILLISECONDS));
                    getContext().become(waiting());
                })
                .matchAny(o -> log().error("Unknown operation {}", o))
                .build();
    }

    private Receive waiting() {
        return receiveBuilder()
                .match(SearchReply.class, reply -> {
                    String name = actorWaitingRefs.remove(getSender());

                    log().info("Got a {} reply", name);
                    log().info("{}", waitingActor);

                    answer.put(name, reply.getReferences());

                    if (actorWaitingRefs.isEmpty()) {
                        sendAnswer();
                    }
                })
                .match(ReceiveTimeout.class, receiveTimeout -> sendAnswer())
                .build();
    }

    private void sendAnswer() {
        getContext().getChildren().forEach(child -> getContext().stop(child));

        waitingActor.tell(new AggregationReply(answer), getSelf());

        log().info("Shutting down");

        getContext().setReceiveTimeout(Duration.Undefined());

        getContext().stop(getSelf());
    }
}
