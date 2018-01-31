package zhangyi.training.school.rp.akkastream.streamingio;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.*;
import akka.util.ByteString;

import java.util.concurrent.CompletionStage;


public class ReplTcpClient {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("repl-tcp");
        final Materializer mat = ActorMaterializer.create(system);

        final Flow<ByteString, ByteString, CompletionStage<Tcp.OutgoingConnection>> connection = Tcp.get(system).outgoingConnection("127.0.0.1", 8889);
        final Flow<String, ByteString, NotUsed> replParser = Flow.<String>create()
                .takeWhile(elem -> !elem.equals("q"))
                .concat(Source.single("BYE"))
                .map(elem -> ByteString.fromString(elem + "\n"));

        final Flow<ByteString, ByteString, NotUsed> repl = Flow.of(ByteString.class)
                .via(Framing.delimiter(ByteString.fromString("\n"), 256, FramingTruncation.DISALLOW))
                .map(text -> { System.out.println(text); return "next";})
//                .map(elem -> readLine(">"))
                .via(replParser);

        connection.join(repl).run(mat);
    }
}
