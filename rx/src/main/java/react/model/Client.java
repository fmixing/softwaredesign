package react.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "client")
public class Client {

    @Id
    private int id;

    private String currency;

    public Client() {
    }

    public int getId() {
        return id;
    }

    public Client setId(int id) {
        this.id = id;
        return this;
    }

    public String getCurrency() {
        return currency;
    }

    public Client setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", currency='" + currency + '\'' +
                '}';
    }
}
