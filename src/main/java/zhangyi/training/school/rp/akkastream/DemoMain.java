package zhangyi.training.school.rp.akkastream;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.Source;
import scala.concurrent.duration.Duration;

import java.math.BigInteger;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class DemoMain {

    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("demo");
        final Materializer materializer = ActorMaterializer.create(system);

        final Source<Integer, NotUsed> source = Source.range(1, 100);
        final Source<BigInteger, NotUsed> factorials = source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));


        final CompletionStage<Done> done = factorials
                .zipWith(Source.range(0, 99), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(1, Duration.create(1, TimeUnit.SECONDS), 1, ThrottleMode.shaping())
                .runForeach(s -> System.out.println(s), materializer);
    }
}
