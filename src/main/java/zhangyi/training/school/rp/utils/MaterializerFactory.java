package zhangyi.training.school.rp.utils;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;

public final class MaterializerFactory {
    private MaterializerFactory() {

    }

    public static Materializer create() {
        final ActorSystem system = ActorSystem.create("akka-streams demo");
        return ActorMaterializer.create(system);
    }
}
