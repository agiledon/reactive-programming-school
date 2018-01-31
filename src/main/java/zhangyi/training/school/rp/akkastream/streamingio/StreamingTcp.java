package zhangyi.training.school.rp.akkastream.streamingio;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.util.concurrent.CompletionStage;

public class StreamingTcp {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("streaming-tcp");
        final Materializer mat = ActorMaterializer.create(system);

        final Source<Tcp.IncomingConnection, CompletionStage<Tcp.ServerBinding>> connections = Tcp.get(system).bind("127.0.0.1", 8889);
        connections.runForeach(conn -> {
            System.out.println("New connection from: " + conn.remoteAddress());
            final Flow<ByteString, ByteString, NotUsed> echo = Flow.of(ByteString.class)
                    .via(Framing.delimiter(ByteString.fromString("\n"), 256, FramingTruncation.DISALLOW))
                    .map(ByteString::utf8String)
                    .map(s -> s + "!!!\n")
                    .map(ByteString::fromString);
            conn.handleWith(echo, mat);
        }, mat);
    }
}
