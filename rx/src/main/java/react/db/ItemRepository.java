package react.db;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import react.model.Item;

@Repository
public interface ItemRepository extends ReactiveMongoRepository<Item, Integer> {
}
