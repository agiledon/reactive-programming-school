package zhangyi.training.school.rp.akkastreams;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.ThrottleMode;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


public class FactorialsTest {

    private Source<BigInteger, NotUsed> factorials;
    private ActorSystem system;
    private Materializer materializer;
    private Source<Integer, NotUsed> source;

    @Before
    public void setUp() throws Exception {
        system = ActorSystem.create("demo");
        materializer = ActorMaterializer.create(system);
        source = Source.range(1, 100);
        factorials = source.scan(BigInteger.ONE, (acc, next) -> acc.multiply(BigInteger.valueOf(next)));
    }

    @Test
    public void should_output_factorials_result_to_file() throws InterruptedException {
        final CompletionStage<IOResult> result = factorials
                .map(num -> ByteString.fromString(num.toString() + "\n"))
                .runWith(FileIO.toFile(new File("factorizer.txt")), materializer);

//        Thread.sleep(1000);
    }

    @Test
    public void should_filter_factorials_result_and_output_to_console() throws InterruptedException {
        final CompletionStage<Done> done = factorials
                .zipWith(Source.range(0, 99), (num, idx) -> String.format("%d! = %s", idx, num))
                .throttle(1, Duration.create(1, TimeUnit.SECONDS), 1, ThrottleMode.shaping())
                .runForeach(s -> System.out.println(s), materializer);

//        Thread.sleep(10000);
    }
}
