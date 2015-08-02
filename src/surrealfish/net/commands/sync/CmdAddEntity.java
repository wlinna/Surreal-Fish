package surrealfish.net.commands.sync;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import surrealfish.Area;

@Serializable
public class CmdAddEntity extends StateData {
    private int entityId;
    private short creatorId;
    private Vector3f location = new Vector3f();
    private Quaternion rotation = new Quaternion();
    private byte playerId;

    public CmdAddEntity() {
    }

    public CmdAddEntity(int creatorId, int entityId, Vector3f location,
            Quaternion rotation, int playerId) {
        this.entityId = entityId;
        this.creatorId = (short) creatorId;
        this.location.set(location);
        this.rotation.set(rotation);
        this.playerId = (byte) playerId;
    }

    @Override
    public void applyData(Object target) {
        Area area = (Area) target;
        area.addEntity(creatorId, entityId, location, rotation, playerId);
    }
}
