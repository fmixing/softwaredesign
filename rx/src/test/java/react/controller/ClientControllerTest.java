package react.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import react.db.ClientRepository;
import react.db.CurrencyRepository;
import react.model.Client;
import react.model.Currency;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Before
    public void setUp() throws Exception {
        currencyRepository.deleteAll();
        clientRepository.deleteAll();

        currencyRepository.saveAll(Arrays.asList(new Currency().setId(1).setName("euro").setCurrencyRate(1d),
                new Currency().setId(2).setName("dollar").setCurrencyRate(1.6d),
                new Currency().setId(3).setName("rubble").setCurrencyRate(0.0167d))).blockLast();
    }

    @After
    public void clearing() {
        clientRepository.deleteAll().block();
    }

    @Test
    public void createClient() throws Exception {
        Client client = new Client().setId(1).setCurrency("euro");

        this.webTestClient.post().uri("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(client), Client.class)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.currency").isEqualTo("euro");
    }

    @Test
    public void createClientWithInvalidCurrency() throws Exception {
        Client client = new Client().setId(1).setCurrency("fund");

        this.webTestClient.post().uri("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(client), Client.class)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("message").isEqualTo("Currency fund doesn't exist");
    }

    @Test
    public void createClientWithRepeatedId() throws Exception {
        Client client = new Client().setId(1).setCurrency("euro");

        this.webTestClient.post().uri("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(client), Client.class)
                .exchange()
                .returnResult(Client.class)
                .getResponseBody()
                .blockFirst();

        this.webTestClient.post().uri("/client")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(client), Client.class)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("message").isEqualTo("Client with id 1 already exists");
    }
}
