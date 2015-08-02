package surrealfish.entity;

import com.jme3.scene.Spatial;

/**
 *
 * @author william
 */
public interface EntityCreator {
    public Spatial create(CreationParams params);
}
