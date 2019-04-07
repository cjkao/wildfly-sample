package sample.domain;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@ApplicationScoped
public class BookManager {

    private ConcurrentMap<String, Book> inMemoryStore
            = new ConcurrentHashMap<>();

    public String add(Book book) {
        inMemoryStore.put(book.getName(), book);
        return "OK add:" + book.getId();
    }

    public Book get(String name) {
        var book = new Book(name);
        // ...
        var ret = inMemoryStore.getOrDefault(name, book);
        return ret;

    }

    public List<Book> getAll() {
        var arr = new ArrayList<Book>();
        arr.addAll(inMemoryStore.values());
        return arr;
    }
}