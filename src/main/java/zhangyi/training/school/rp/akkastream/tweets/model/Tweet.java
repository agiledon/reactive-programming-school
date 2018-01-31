package zhangyi.training.school.rp.akkastream.tweets.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Tweet {
    private final Author author;
    private final long timestamp;
    private final String body;

    public Tweet(Author author, long timestamp, String body) {
        this.author = author;
        this.timestamp = timestamp;
        this.body = body;
    }

    public Author getAuthor() {
        return author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getBody() {
        return body;
    }

    public Set<Hashtag> hashtags() {
        return Arrays.asList(body.split(" ")).stream()
                .filter(a -> a.startsWith("#"))
                .map(a -> new Hashtag(a))
                .collect(Collectors.toSet());
    }
}
