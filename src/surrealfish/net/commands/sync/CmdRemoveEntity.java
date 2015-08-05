package surrealfish.net.commands.sync;

import com.jme3.network.serializing.Serializable;
import surrealfish.Area;

@Serializable
public class CmdRemoveEntity extends StateData {

    private int entityId;

    public CmdRemoveEntity() {
    }

    public CmdRemoveEntity(int entityId) {
        super(-1);
        this.entityId = entityId;
    }

    @Override
    public void applyData(Object target) {
        Area area = (Area) target;
        area.removeEntity(entityId);
    }
}