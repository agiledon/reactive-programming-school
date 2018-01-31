package zhangyi.training.school.rp.akkastream.streamingio;

import akka.Done;
import akka.actor.ActorSystem;
import akka.stream.ActorAttributes;
import akka.stream.ActorMaterializer;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.stream.javadsl.Sink;
import akka.util.ByteString;

import java.io.File;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class StreamingFile {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        final ActorSystem system = ActorSystem.create("streaming-tcp");
        final Materializer mat = ActorMaterializer.create(system);

        String resourcePath = StreamingFile.class.getResource("/bank_test_data.csv").getPath();
        final File file = new File(resourcePath);
        Sink<String, CompletionStage<Done>> outputSink = Sink.foreach(System.out::println);
        CompletionStage<IOResult> ioResult = FileIO
                .fromFile(file)
                .via(Framing.delimiter(ByteString.fromString("\n"), 256, FramingTruncation.DISALLOW))
                .zipWithIndex()
                .map(l -> l.second() + ": " + l.first().utf8String())
                .to(outputSink).run(mat);

        ioResult.thenAcceptAsync(l -> System.out.println(l), system.dispatcher());
    }
}
