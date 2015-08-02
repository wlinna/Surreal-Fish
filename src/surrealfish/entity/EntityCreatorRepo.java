package surrealfish.entity;

import java.util.ArrayList;
import java.util.List;

public class EntityCreatorRepo {
    private List<EntityCreator> creators = new ArrayList<>();
    
    public void addCreator(EntityCreator creator) {
        creators.add(creator);
    }
    
    public EntityCreator creator(int creatorId)  {
        return creators.get(creatorId);
    }
}