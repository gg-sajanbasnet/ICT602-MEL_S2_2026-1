
package smartcommunity.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaintenanceTeam {
    private final String teamId;
    private final String teamName;
    private final List<MaintenanceStaff> members = new ArrayList<>();

    public MaintenanceTeam(String teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public void addMember(MaintenanceStaff staff) {
        if (staff == null) {
            throw new IllegalArgumentException("Staff cannot be null.");
        }

        if (!members.contains(staff)) {
            members.add(staff);
        }
    }

    public boolean hasMember(MaintenanceStaff staff) {
        return members.contains(staff);
    }

    public String getTeamId() { return teamId; }
    public String getTeamName() { return teamName; }

    public List<MaintenanceStaff> getMembers() {
        return Collections.unmodifiableList(members);
    }
}
