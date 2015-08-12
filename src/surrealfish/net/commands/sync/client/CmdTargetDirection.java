package surrealfish.net.commands.sync.client;

import arkhados.net.Command;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CmdTargetDirection implements Command{
    private Vector3f targetDirection;

    public CmdTargetDirection() {
    }
        
    public CmdTargetDirection(Vector3f targetDirection) {
        this.targetDirection = new Vector3f(targetDirection) ;
    }       

    @Override
    public boolean isGuaranteed() {
        return false;
    }

    public Vector3f getTargetDirection() {
        return targetDirection;
    }
}
