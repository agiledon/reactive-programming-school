package zhangyi.training.school.rp.ping;

public class HostInfo {
    private String hostName;
    private String hostAddress;

    public HostInfo(String hostName, String hostAddress) {
        this.hostName = hostName;
        this.hostAddress = hostAddress;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostAddress() {
        return hostAddress;
    }
}
