package react.db;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import react.model.Client;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, Integer> {
}
