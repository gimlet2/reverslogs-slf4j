package com.restmonkeys.reverslogs.slf4j.collections;

public class Pair<T, K> {
    private T p1;
    private K p2;

    public Pair(T p1, K p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public T getP1() {
        return p1;
    }

    public K getP2() {
        return p2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return p1.equals(pair.p1) && p2.equals(pair.p2);
    }

    @Override
    public int hashCode() {
        int result = p1.hashCode();
        result = 31 * result + p2.hashCode();
        return result;
    }
}
