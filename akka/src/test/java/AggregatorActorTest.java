import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import messages.Request;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import replies.AggregationReply;

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class AggregatorActorTest {

    private static ActorSystem system;

    @Mock
    private Server serverStub;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public void testAllAnswered() throws Exception {
        when(serverStub.createSearch(eq(SearchActor.google), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });
        when(serverStub.createSearch(eq(SearchActor.yandex), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });
        when(serverStub.createSearch(eq(SearchActor.bing), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });

        Map<String, List<String>> answer = new HashMap<>();
        answer.put(SearchActor.google, createAnswer());
        answer.put(SearchActor.yandex, createAnswer());
        answer.put(SearchActor.bing, createAnswer());

        final TestKit testProbe = new TestKit(system);

        ActorRef testAllAnswered = system.actorOf(Props.create(AggregatorActor.class, serverStub),
                "testAllAnswered");

        testAllAnswered.tell(new Request(1000, "testAllAnswered"), testProbe.getRef());
        testProbe.expectMsg(new AggregationReply(answer));
    }

    @Test
    public void testNoneAnswered() throws Exception {
        when(serverStub.createSearch(eq(SearchActor.google), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });
        when(serverStub.createSearch(eq(SearchActor.yandex), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });
        when(serverStub.createSearch(eq(SearchActor.bing), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(1200);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });

        final TestKit testProbe = new TestKit(system);

        ActorRef testNoneAnswered = system.actorOf(Props.create(AggregatorActor.class, serverStub),
                "testNoneAnswered");

        testNoneAnswered.tell(new Request(1000, "testNoneAnswered"), testProbe.getRef());
        testProbe.expectMsg(new AggregationReply(Collections.emptyMap()));
    }

    @Test
    public void twoAnswered() throws Exception {
        when(serverStub.createSearch(eq(SearchActor.google), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });
        when(serverStub.createSearch(eq(SearchActor.yandex), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });
        when(serverStub.createSearch(eq(SearchActor.bing), any()))
                .thenAnswer(invocation -> {
                    List<String> answer = createAnswer();

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {
                    }

                    return answer;
                });
        Map<String, List<String>> answer = new HashMap<>();
        answer.put(SearchActor.yandex, createAnswer());
        answer.put(SearchActor.bing, createAnswer());

        final TestKit testProbe = new TestKit(system);

        ActorRef twoAnswered = system.actorOf(Props.create(AggregatorActor.class, serverStub),
                "twoAnswered");

        twoAnswered.tell(new Request(1000, "twoAnswered"), testProbe.getRef());
        testProbe.expectMsg(new AggregationReply(answer));
    }

    private List<String> createAnswer() {
        List<String> answer = new ArrayList<>();
        Collections.addAll(answer, "ans1", "ans2", "ans3", "ans4", "ans5");
        return answer;
    }
}