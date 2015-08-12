package surrealfish;

import com.jme3.math.Vector3f;


public class Input {
    private int flags;
    
    private Vector3f relativeDirection = new Vector3f();
    private Vector3f targetDirection = new Vector3f();
    
    void setFlags(int newFlags) {
        flags = newFlags;
        
        int forward = 0;
        int right = 0;
        
        int iForward = MappingNames.toIndex(MappingNames.FORWARD);
        int iBackward = MappingNames.toIndex(MappingNames.BACKWARD);
        int iLeft = MappingNames.toIndex(MappingNames.LEFT);
        int iRight = MappingNames.toIndex(MappingNames.RIGHT);
        
        if ((flags & (1 << iForward)) == (1 << iForward)) {
            forward += 1;
        }
        
        if ((flags & (1 << iBackward)) == (1 << iBackward)) {
            forward -= 1;
        }
        
        if ((flags & (1 << iLeft)) == (1 << iLeft)) {
            right -= 1;
        }
        
        if ((flags & (1 << iRight)) == (1 << iRight)) {
            right += 1;
        }
        
        relativeDirection.x = right;
        relativeDirection.y = 0;
        relativeDirection.z = -forward;
        
        relativeDirection.normalizeLocal();
    }

    public Vector3f getRelativeDirection() {
        return relativeDirection;
    }

    public Vector3f getTargetDirection() {
        return targetDirection;
    }
}
