package zhangyi.training.school.rp.utils;

import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class PingTest {
    @Test
    public void should_ping_ip_address() throws IOException {
        InetAddress inet = InetAddress.getByAddress(new byte[]{127, 0, 0, 1});
        assertThat(inet.isReachable(1000)).isTrue();
        System.out.println(inet.getHostName());
        System.out.println(inet.getHostAddress());
    }

}