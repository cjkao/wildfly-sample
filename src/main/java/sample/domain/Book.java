package sample.domain;

//import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Data
//@Builder
public class Book {
    public Book(String name) {
        UUID uuid = UUID.randomUUID();
        id = uuid.toString();
        this.name = name;
        log.info("wow...this is amazing book");
    }

    public Book() {
        UUID uuid = UUID.randomUUID();
        id = uuid.toString();
        log.info("wow...this is amazing");
        name = "na";
    }

    private String id;
    private String name;
}