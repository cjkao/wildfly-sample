package sample.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Builder
@Slf4j
@Data
public class Magazine {
    private String name;
    private Date releaseDate;
    @Singular
    Map<Integer, String> chapters = new HashMap<>();
}
