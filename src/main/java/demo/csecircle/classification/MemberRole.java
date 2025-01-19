package demo.csecircle.classification;

public enum MemberRole {

    WEBSITE_ADMIN("Website Administrator"),
    CLUB_PRESIDENT("Club President"),
    STUDENT("Student");

    private final String displayName;

    MemberRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
