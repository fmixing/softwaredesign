package hw1;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.Objects;
import java.util.Optional;

public class CacheDAO<T> {

    private int cacheMaxSize;

    private BiMap<Integer, CacheNode<T>> cache = HashBiMap.create();

    private CacheNode<T> head = null;
    private CacheNode<T> tail = null;

    public CacheDAO(int cacheMaxSize) {
        this.cacheMaxSize = cacheMaxSize;
    }

    public Optional<T> put(int key, T value) {
        assert value != null;

        if (cache.size() == 0) {
            createHead(key, value);

            assert size() == sizeOfList() && head.getValue() == value;
            return Optional.empty();
        }

        if (cache.containsKey(key)) {
            CacheNode<T> cacheNode = cache.get(key);

            T valueToReturn = cacheNode.getValue();

            deleteElemFromList(cacheNode);

            createHead(key, value);

            assert size() == sizeOfList();
            return Optional.of(valueToReturn);
        }

        if (cache.size() == cacheMaxSize) {

            deleteElemFromList(tail);

            createHead(key, value);

            assert size() == sizeOfList() && head.getValue() == value;
            return Optional.empty();
        }

        createHead(key, value);

        assert size() == sizeOfList();
        return Optional.empty();
    }

    public Optional<T> get(int key) {
        CacheNode<T> value = cache.get(key);

        if (value == null) {
            assert size() == sizeOfList();
            return Optional.empty();
        }

        if (value == head) {
            assert size() == sizeOfList();
            return Optional.of(value.getValue());
        }

        if (value == tail) {
            deleteElemFromList(tail);
            createHead(key, value.getValue());

            assert size() == sizeOfList() && head.getValue() == value.getValue();
            return Optional.of(value.getValue());
        }

        deleteElemFromList(value);
        createHead(key, value.getValue());

        assert size() == sizeOfList();
        return Optional.of(value.getValue());
    }

    public Optional<T> delete(int key) {

        if (!cache.containsKey(key)) {
            return Optional.empty();
        }

        CacheNode<T> nodeForRemove = cache.get(key);

        if (cache.size() == 1) {
            clear();
            return Optional.of(nodeForRemove.getValue());
        }

        if (head == nodeForRemove) {
            deleteElemFromList(head);

            assert sizeOfList() == size();
            return Optional.of(nodeForRemove.getValue());
        }
        if (tail == nodeForRemove) {
            deleteElemFromList(tail);

            assert sizeOfList() == size();
            return Optional.of(nodeForRemove.getValue());
        }

        deleteElemFromList(nodeForRemove);

        assert sizeOfList() == size();
        return Optional.of(nodeForRemove.getValue());
    }

    public void clear() {
        head = null;
        tail = null;
        cache.clear();
    }


    public int size() {
        int size = cache.size();

        assert size <= cacheMaxSize && sizeOfList() == size || (size == 0 && head == null && tail == null);
        return size;
    }

    private void createHead(int key, T value) {
        CacheNode<T> newHead;
        if (head == null) {
            assert tail == null;

            newHead = new CacheNode<>(value);
            head = newHead;
            tail = newHead;
        } else {
            newHead = new CacheNode<>(value, head);
            head.setLeft(newHead);
        }
        cache.put(key, newHead);
        head = newHead;
    }

    private void deleteElemFromList(CacheNode<T> cacheNode) {
        if (cacheNode.getLeft().isPresent()) {
            cacheNode.getLeft().get().setRight(cacheNode.getRight().orElse(null));
            if (cacheNode == tail)
                tail = cacheNode.getLeft().get();
        }
        if (cacheNode.getRight().isPresent()) {
            cacheNode.getRight().get().setLeft(cacheNode.getLeft().orElse(null));
            if (cacheNode == head)
                head = cacheNode.getRight().get();
        }
        cache.inverse().remove(cacheNode);

    }

    private int sizeOfList() {
        if (head == null) {
            assert tail == null;
            return 0;
        }

        CacheNode<T> currNode = head;
        int size = 1;

        while (currNode.getRight().isPresent()) {
            currNode = currNode.getRight().get();
            size++;
        }
        return size;
    }
}
