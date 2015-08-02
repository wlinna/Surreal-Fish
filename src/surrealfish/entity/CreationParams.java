
package surrealfish.entity;

import com.jme3.math.Vector3f;


public class CreationParams {
    public int creatorId;
    public Vector3f location;

    public CreationParams(int creatorId, Vector3f location) {
        this.creatorId = creatorId;
        this.location = location;
    }
}
