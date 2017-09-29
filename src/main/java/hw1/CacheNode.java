package hw1;

import javax.validation.constraints.NotNull;
import java.util.Optional;

public class CacheNode <T> {

    /**
     * Element should not contain null
     */
    @NotNull
    private T value;

    private CacheNode<T> left;

    private CacheNode<T> right;

    public CacheNode(T value, CacheNode<T> right) {
        this.value = value;
        this.left = null;
        this.right = right;
    }

    public CacheNode(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public T getValue() {
        return value;
    }

    public Optional<CacheNode<T>> getLeft() {
        return Optional.ofNullable(left);
    }

    public void setLeft(CacheNode<T> left) {
        this.left = left;

    }

    public Optional<CacheNode<T>> getRight() {
        return Optional.ofNullable(right);
    }

    public void setRight(CacheNode<T> right) {
        this.right = right;
    }

}
