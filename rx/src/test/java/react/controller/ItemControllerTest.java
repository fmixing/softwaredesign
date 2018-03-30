package react.controller;


import org.junit.After;
import org.junit.Assert;
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
import react.db.ItemRepository;
import react.model.Client;
import react.model.Currency;
import react.model.Item;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Before
    public void setUp() throws Exception {
        currencyRepository.deleteAll();
        clientRepository.deleteAll();
        itemRepository.deleteAll();

        currencyRepository.saveAll(Arrays.asList(new Currency().setId(1).setName("euro").setCurrencyRate(1d),
                new Currency().setId(2).setName("dollar").setCurrencyRate(1.6d),
                new Currency().setId(3).setName("rubble").setCurrencyRate(0.0167d))).blockLast();

        clientRepository.save(new Client().setId(1).setCurrency("euro")).block();
        clientRepository.save(new Client().setId(2).setCurrency("dollar")).block();
    }


    @After
    public void clearing() {
        itemRepository.deleteAll().block();
    }

    @Test
    public void testAddingItems() throws Exception {
        Item book = new Item().setId(1).setPrice(100d).setName("Book");

        this.webTestClient.post().uri("/items/add?clientId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Item.class)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Book")
                .jsonPath("$.price").isEqualTo(100d);
    }

    @Test
    public void testAddingSameItemTwice() throws Exception {
        Item book = new Item().setId(1).setPrice(100d).setName("Book");

        this.webTestClient.post().uri("/items/add?clientId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Item.class)
                .exchange()
                .returnResult(Item.class)
                .getResponseBody()
                .blockFirst();

        this.webTestClient.post().uri("/items/add?clientId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book.setPrice(134d)), Item.class)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("message").isEqualTo("Item with id 1 already exists");
    }

    @Test
    public void testAddingDifferentCurrency() throws Exception {
        Item book = new Item().setId(1).setPrice(100d).setName("Book");

        this.webTestClient.post().uri("/items/add?clientId=2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Item.class)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Book")
                .jsonPath("$.price").isEqualTo(100d);

        this.webTestClient.post().uri("/items/findById?itemId=1&clientId=2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Item.class)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("Book")
                .jsonPath("$.price").isEqualTo(100d);

        Item block = itemRepository.findById(1).block();

        Assert.assertNotNull(block);
        Assert.assertEquals(160d, block.getPrice(), 1e5);
    }

    @Test
    public void testFindAll() throws Exception {
        Item book = new Item().setId(1).setPrice(100d).setName("Book");
        Item pen = new Item().setId(2).setPrice(10d).setName("Pen");

        this.webTestClient.post().uri("/items/add?clientId=2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Item.class)
                .exchange()
                .returnResult(Item.class)
                .getResponseBody()
                .blockFirst();
        this.webTestClient.post().uri("/items/add?clientId=2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(pen), Item.class)
                .exchange()
                .returnResult(Item.class)
                .getResponseBody()
                .blockFirst();

        this.webTestClient.post().uri("/items/findAll?clientId=1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(book), Item.class)
                .exchange()
                .expectBodyList(Item.class)
                .contains(new Item().setId(1).setPrice(160d).setName("Book"), new Item().setId(2).setPrice(16d).setName("Pen"));

    }
}