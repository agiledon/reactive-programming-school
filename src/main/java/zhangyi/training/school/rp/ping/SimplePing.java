package zhangyi.training.school.rp.ping;


import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SimplePing {
    public static CompletableFuture<HostInfo> ping(String ipAddress) {
        CompletableFuture<Exception> exception = new CompletableFuture<>();
        return CompletableFuture.supplyAsync(() -> {
            try {
                validate(ipAddress);
                byte[] bytes = parseIpAddress(ipAddress);
                return fetchHostInfo(bytes);
            } catch (InvalidIpAddressException | IOException e) {
                exception.complete(e);
                throw new CompletionException(e);
            }
        });
    }

    private static HostInfo fetchHostInfo(byte[] bytes) throws IOException {
        InetAddress inet = InetAddress.getByAddress(bytes);
        if (inet.isReachable(1000)) {
            return new HostInfo(inet.getHostName(), inet.getHostAddress());
        } else {
            return new HostInfo("unknown host name", "unknown ip address");
        }
    }

    private static void validate(String ipAddress) throws InvalidIpAddressException {
        String regEx = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(ipAddress);
        if (!matcher.matches()) {
            throw new InvalidIpAddressException(String.format("Given ip address %s is invalid", ipAddress));
        }
    }

    static byte[] parseIpAddress(String ipAddress) {
        String[] parts = ipAddress.split("\\.");
        byte[] ipBuf = new byte[4];
        for(int i = 0; i < 4; i++){
            ipBuf[i] = (byte)(Integer.parseInt(parts[i])&0xff);
        }

        return ipBuf;
    }

    private static class InvalidIpAddressException extends Exception {
        public InvalidIpAddressException(String message) {
            super(message);
        }
    }
}
