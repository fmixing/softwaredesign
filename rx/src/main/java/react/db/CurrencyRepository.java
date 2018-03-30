package react.db;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import react.model.Currency;
import reactor.core.publisher.Mono;

@Repository
public interface CurrencyRepository extends ReactiveMongoRepository<Currency, Integer> {
    Mono<Currency> findOneByName(final String name);
}
