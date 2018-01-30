package zhangyi.training.school.rp.rxjava.domain;

public class House {
    private String communityName;
    private String description;

    public House(String communityName, String description) {
        this.communityName = communityName;
        this.description = description;
    }

    public String getCommunityName() {
        return communityName;
    }

    public String getDescription() {
        return description;
    }
}
