package react.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import react.db.ClientRepository;
import react.db.CurrencyRepository;
import react.model.Client;
import reactor.core.publisher.Mono;

@RestController
public class ClientController {

    private final ClientRepository clientRepository;

    private final CurrencyRepository currencyRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository, CurrencyRepository currencyRepository) {
        this.clientRepository = clientRepository;
        this.currencyRepository = currencyRepository;
    }

    @PostMapping("/client")
    public Mono<Client> createClient(@RequestBody Client client) {
        return currencyRepository.findOneByName(client.getCurrency())
                .switchIfEmpty(Mono.error(new RuntimeException("Currency " + client.getCurrency() + " doesn't exist")))
                .flatMap(currency -> clientRepository.existsById(client.getId()))
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new RuntimeException("Client with id " + client.getId() + " already exists"));
                    }
                    return clientRepository.save(client);
                });
    }
}
