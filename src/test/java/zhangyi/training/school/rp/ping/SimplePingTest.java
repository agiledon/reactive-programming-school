package zhangyi.training.school.rp.ping;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class SimplePingTest {
    @Test
    public void should_fetch_host_info() throws ExecutionException, InterruptedException {
        CompletableFuture<HostInfo> ping = SimplePing.ping("127.0.0.1");
        HostInfo hostInfo = ping.get();
        assertThat(hostInfo.getHostName()).isEqualTo("localhost");
        assertThat(hostInfo.getHostAddress()).isEqualTo("127.0.0.1");
    }

    @Test
    public void should_throw_exception() throws ExecutionException, InterruptedException {
        CompletableFuture<HostInfo> ping = SimplePing.ping("255.123.0.1");
        HostInfo hostInfo = ping.get();
        assertThat(hostInfo.getHostName()).isEqualTo("unknown host name");
        assertThat(hostInfo.getHostAddress()).isEqualTo("unknown ip address");
    }
}