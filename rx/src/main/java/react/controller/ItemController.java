package react.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import react.db.ClientRepository;
import react.db.CurrencyRepository;
import react.db.ItemRepository;
import react.model.Currency;
import react.model.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/items")
public class ItemController {

    private final ClientRepository clientRepository;

    private final ItemRepository itemRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ItemController(ClientRepository clientRepository, ItemRepository itemRepository,
                          CurrencyRepository currencyRepository) {
        this.clientRepository = clientRepository;
        this.itemRepository = itemRepository;
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("/findAll")
    public Flux<Item> findAllForClient(@RequestParam Integer clientId) {
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new RuntimeException("Client with id " + clientId + " doesn't exist")))
                .flatMap(client -> currencyRepository.findOneByName(client.getCurrency()))
                .flatMapMany(currency -> itemRepository.findAll().map(item -> convertFromCurrency(currency, item)));
    }

    @PostMapping("/findById")
    public Mono<Item> findById(@RequestParam int itemId, @RequestParam int clientId) {
        System.err.println(itemId + " " + clientId);
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new RuntimeException("Client with id " + clientId + " doesn't exist")))
                .flatMap(client -> currencyRepository.findOneByName(client.getCurrency()))
                .flatMap(currency -> itemRepository.findById(itemId).map(item -> convertFromCurrency(currency, item)));
    }

    @PostMapping("/add")
    public Mono<Item> addItem(@RequestBody Item item, @RequestParam int clientId) {
        return clientRepository.findById(clientId)
                .switchIfEmpty(Mono.error(new RuntimeException("Client with id " + clientId + " doesn't exist")))
                .flatMap(client -> currencyRepository.findOneByName(client.getCurrency()))
                .flatMap(currency -> itemRepository.existsById(item.getId())
                        .flatMap(exist -> {
                            System.err.println(exist);
                            if (exist) {
                                return Mono.error(new RuntimeException("Item with id " + item.getId() + " already exists"));
                            }
                            return itemRepository.save(convertToCurrency(currency, item)).map(saved -> item);
                        })
                );
    }

    private Item convertToCurrency(Currency currency, Item item) {
        return item.toBuilder().setId(item.getId()).setName(item.getName())
                .setPrice(currency.getCurrencyRate() * item.getPrice());
    }

    private Item convertFromCurrency(Currency currency, Item item) {
        return item.toBuilder().setId(item.getId()).setName(item.getName())
                .setPrice(item.getPrice() / currency.getCurrencyRate());
    }
}
