package zhangyi.training.school.rp.akkastream.tweets;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.Cancellable;
import akka.stream.*;
import akka.stream.javadsl.*;
import scala.concurrent.duration.FiniteDuration;
import zhangyi.training.school.rp.akkastream.tweets.model.Author;
import zhangyi.training.school.rp.akkastream.tweets.model.Hashtag;
import zhangyi.training.school.rp.akkastream.tweets.model.Tweet;

import java.util.ArrayList;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class TweetsHandler {
    private final ActorSystem system = ActorSystem.create("repl-tcp");
    private final Materializer mat = ActorMaterializer.create(system);
    private static final Hashtag AKKA = new Hashtag("#AKKA");

    public void handle() {
        final Source<Tweet, Cancellable> tweets = produceTweets();

        Sink<Author, CompletionStage<Done>> writeAuthors = Sink.foreach(a -> System.out.println(a.getHandle()));
        Sink<Hashtag, CompletionStage<Done>> writeHashtags = Sink.foreach(h -> System.out.println(h.getName()));

        RunnableGraph<NotUsed> runnableGraph = buildTweetsRunnableGraph(tweets, writeAuthors, writeHashtags);
        runnableGraph.run(mat);
    }

    private RunnableGraph<NotUsed> buildTweetsRunnableGraph(Source<Tweet, Cancellable> tweets, Sink<Author, CompletionStage<Done>> writeAuthors, Sink<Hashtag, CompletionStage<Done>> writeHashtags) {
        return RunnableGraph.fromGraph(GraphDSL.create(b -> {
            final UniformFanOutShape<Tweet, Tweet> bcast = b.add(Broadcast.create(2));

            final FlowShape<Tweet, Author> toAuthor = b.add(Flow.of(Tweet.class).map(t -> t.getAuthor()));
            final SinkShape<Author> authors = b.add(writeAuthors);
            b.from(b.add(tweets)).viaFanOut(bcast).via(toAuthor).to(authors);

            final FlowShape<Tweet, Hashtag> toHashtags = b.add(Flow.of(Tweet.class).mapConcat(t -> new ArrayList<>(t.hashtags())));
            final SinkShape<Hashtag> hashtags = b.add(writeHashtags);
            b.from(bcast).via(toHashtags).to(hashtags);

            return ClosedShape.getInstance();
        }));
    }


    private Source<Author, Cancellable> getAKKAAuthor(Source<Tweet, Cancellable> tweets) {
        return tweets
                .filter(t -> t.hashtags().contains(AKKA))
                .map(t -> t.getAuthor());
    }

    private Source<Hashtag, Cancellable> getHashtags(Source<Tweet, Cancellable> tweets) {
        return tweets
                .mapConcat(t -> new ArrayList<>(t.hashtags()));
    }

    private Source<Tweet, Cancellable> produceTweets() {
        System.out.println("Start product tweets.");
        return Source.tick(
                    FiniteDuration.create(0, TimeUnit.MILLISECONDS),
                    FiniteDuration.create(1, TimeUnit.SECONDS),
                    new Tweet(new Author("zhangyi"), System.currentTimeMillis(), "#AKKA is framework"));
    }

    public static void main(String[] args) {
        new TweetsHandler().handle();
    }
}
