package sample.domain;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class MagManager {

    private ConcurrentMap<String, Magazine> inMemoryStore
            = new ConcurrentHashMap<>();

    public String add(Magazine Magazine) {
        inMemoryStore.put(Magazine.getName(), Magazine);
        return "OK add:" + Magazine.getName();
    }

    public Magazine get(String name) {
//        var Magazine = Magazine.builder.;
        // ...
//        var ret = inMemoryStore.getOrDefault(name, Magazine);
        return null;

    }

    public List<Magazine> getAll() {
        var arr = new ArrayList<Magazine>();
        arr.addAll(inMemoryStore.values());
        return arr;
    }
}