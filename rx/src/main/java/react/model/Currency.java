package react.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "currency")
public class Currency {

    @Id
    private int id;

    private String name;

    private Double currencyRate;

    public int getId() {
        return id;
    }

    public Currency setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Currency setName(String name) {
        this.name = name;
        return this;
    }

    public Double getCurrencyRate() {
        return currencyRate;
    }

    public Currency setCurrencyRate(Double currencyRate) {
        this.currencyRate = currencyRate;
        return this;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", currencyRate=" + currencyRate +
                '}';
    }
}
