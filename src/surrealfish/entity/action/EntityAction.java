package surrealfish.entity.action;

import com.jme3.scene.Spatial;

public abstract class EntityAction {

    private String name;
    private int typeId;
    private Spatial spatial;

    public void setSpatial(Spatial spatial) {
        this.spatial = spatial;
    }
    
    protected Spatial getSpatial() {
        return spatial;
    }

    public abstract boolean update(float tpf);

    public void end() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
