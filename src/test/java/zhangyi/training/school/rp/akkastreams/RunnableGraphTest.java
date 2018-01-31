package zhangyi.training.school.rp.akkastreams;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.*;
import akka.stream.javadsl.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class RunnableGraphTest {
    final ActorSystem system = ActorSystem.create("graph-test");
    final Materializer mat = ActorMaterializer.create(system);

    @Test
    public void one_in_broadcast_two_then_merge_to_one() {
        final Source<Integer, NotUsed> in = Source.from(Arrays.asList(1, 2, 3, 4, 5));
        final Sink<List<String>, CompletionStage<List<String>>> sink = Sink.head();
        final Flow<Integer, Integer, NotUsed> f1 = Flow.of(Integer.class).map(elem -> elem + 10);
        final Flow<Integer, Integer, NotUsed> f2 = Flow.of(Integer.class).map(elem -> elem + 20);
        final Flow<Integer, String, NotUsed> f3 = Flow.of(Integer.class).map(elem -> {
            System.out.println(elem); return elem.toString();});
        final Flow<Integer, Integer, NotUsed> f4 = Flow.of(Integer.class).map(elem -> elem + 30);

        final RunnableGraph<CompletionStage<List<String>>> result =
                RunnableGraph.fromGraph(GraphDSL.create(sink, (builder, out) -> {
                    final UniformFanOutShape<Integer, Integer> bcast = builder.add(Broadcast.create(2));
                    final UniformFanInShape<Integer, Integer> merge = builder.add(Merge.create(2));

                    /*
                    in ~> f1 ~> bcast ~> f2 ~> merge ~> f3 ~> out
                    bcast ~> f4 ~> merge
                     */

                    final Outlet<Integer> source = builder.add(in).out();
                    builder.from(source)
                            .via(builder.add(f1))
                            .viaFanOut(bcast)
                            .via(builder.add(f2))
                            .viaFanIn(merge)
                            .via(builder.add(f3.grouped(1000)))
                            .to(out);
                    builder.from(bcast).via(builder.add(f4)).toFanIn(merge);

                    return ClosedShape.getInstance();
        }));

        result.run(mat);
    }

    @Test
    public void should_compose_complex_graph() {
        RunnableGraph.fromGraph(GraphDSL.create(builder -> {
            final Outlet<Integer> A = builder.add(Source.single(0)).out();
            final UniformFanOutShape<Integer, Integer> B = builder.add(Broadcast.create(2));
            final UniformFanInShape<Integer, Integer> C = builder.add(Merge.create(2));
            final FlowShape<Integer, Integer> D = builder.add(Flow.of(Integer.class).map(i -> i + 1));
            final UniformFanOutShape<Integer, Integer> E = builder.add(Balance.create(2));
            final UniformFanInShape<Integer, Integer> F = builder.add(Merge.create(2));
            final Inlet<Integer> G = builder.add(Sink.<Integer>foreach(System.out::println)).in();

            /*
                      C    <~   F
            A ~> B ~> C    ~>   F
                 B ~> D ~> E ~> F
                           E ~> G
             */
            builder.from(F).toFanIn(C); //feedback loop
            builder.from(A).viaFanOut(B).viaFanIn(C).toFanIn(F);
            builder.from(B).via(D).viaFanOut(E).toFanIn(F);
            builder.from(E).toInlet(G);

            return ClosedShape.getInstance();
        })).run(mat);
    }
}
