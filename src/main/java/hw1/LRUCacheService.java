package hw1;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class LRUCacheService <T> {

    private CacheDAO<T> cacheDAO;

    public LRUCacheService(int cacheMaxSize) {
        if (cacheMaxSize < 2)
            throw new RuntimeException("The size of cache should be more that one");
        this.cacheDAO = new CacheDAO<>(cacheMaxSize);
    }

    public Optional<T> put(int key, @NotNull T value) {
        if (value == null)
            return Optional.empty();
        return cacheDAO.put(key, value);
    }

    public Optional<T> get(int key) {
        return cacheDAO.get(key);
    }

    public Optional<T> delete(int key) {
        return cacheDAO.delete(key);
    }

    public void clearCache() {
        cacheDAO.clear();
    }

    public int size() {
        return cacheDAO.size();
    }
}
