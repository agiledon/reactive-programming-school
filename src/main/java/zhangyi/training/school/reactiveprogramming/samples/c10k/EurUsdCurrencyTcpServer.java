package zhangyi.training.school.reactiveprogramming.samples.c10k;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.reactivex.Observable;
import io.reactivex.netty.protocol.tcp.server.TcpServer;

import static io.netty.util.CharsetUtil.UTF_8;

public class EurUsdCurrencyTcpServer {
    private static final BigDecimal RATE = new BigDecimal("1.06448");

    public static void main(final String[] args) {
//        TcpServer.newServer(8080).pipelineConfigurator(pipeline -> {
//            pipeline.addLast(new LineBasedFrameDecoder(128));
//            pipeline.addLast(new StringDecoder(UTF_8)));
//        }).awaitShutdown();
    }

    static Observable<String> eurToUsd(BigDecimal eur) {
        return Observable
                .just(eur.multiply(RATE))
                .map(amount -> eur + " EUR is " + amount + " USDn")
                .delay(1, TimeUnit.SECONDS);
    }
}
