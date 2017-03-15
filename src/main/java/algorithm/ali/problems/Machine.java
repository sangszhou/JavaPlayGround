package algorithm.ali.problems;

/**
 * Created by xinszhou on 23/02/2017.
 */
public class Machine {

    int roomId;
    int groupId;

    public Machine(int roomId, int groupId) {
        this.roomId = roomId;
        this.groupId = groupId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getGroupId() {
        return groupId;
    }
}
