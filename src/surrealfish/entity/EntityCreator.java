package surrealfish.entity;

import com.jme3.scene.Spatial;

public interface EntityCreator {
    public Spatial create(CreationParams params);
}
