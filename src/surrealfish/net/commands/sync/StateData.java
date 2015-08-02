package surrealfish.net.commands.sync;

import arkhados.net.Command;
import com.jme3.network.serializing.Serializable;


@Serializable
public abstract class StateData implements Command {
    private int syncId = -1;
    
    public StateData() {
    }

    public StateData(int syncId) {
        this.syncId = syncId;
    }
            
    public abstract void applyData(Object target);

    public int getSyncId() {
        return syncId;
    }

    @Override
    public boolean isGuaranteed() {
        return true; // This is default that some classes need to override
    }
    
    public void setSyncId(int syncId) {
        this.syncId =  syncId;
    }
}