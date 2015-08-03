package surrealfish;

import arkhados.net.Sender;
import arkhados.net.ServerSender;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.HostedConnection;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;
import surrealfish.entity.CreationParams;
import surrealfish.entity.EntityCreator;
import surrealfish.entity.EntityCreatorRepo;
import surrealfish.entity.ProjectileCreator;
import surrealfish.entity.TestCharacterCreator;
import surrealfish.net.Syncer;
import surrealfish.net.commands.sync.CmdAddEntity;
import surrealfish.net.commands.sync.CmdRemoveEntity;

public class Area extends AbstractAppState {

    private EntityCreatorRepo entityCreatorRepo = new EntityCreatorRepo();
    private Map<Integer, Spatial> entities = new HashMap<>();
    private Node worldRoot;
    private int idCounter = 0;
    private SimpleApplication app;
    private Syncer syncer;
    
    private long projectileSpawnTimer = 0;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        syncer = stateManager.getState(Syncer.class);
        syncer.addObject(-1, this);
        load();
    }

    public void load() {
        worldRoot = (Node) app.getAssetManager()
                .loadModel("Scenes/newScene.j3o");
        app.getRootNode().attachChild(worldRoot);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -1f, -1));
        worldRoot.addLight(sun);

        app.getCamera().setLocation(new Vector3f(0, 5, 10));
        app.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        entityCreatorRepo.addCreator(new TestCharacterCreator());
        entityCreatorRepo.addCreator(new ProjectileCreator());
    }

    public Spatial newEntity(int creatorId, Vector3f loc, Quaternion rot,
            int playerId) {
        return addEntity(creatorId, idCounter++, loc, rot, playerId);
    }

    public Spatial addEntity(int creatorId, int entityId, Vector3f loc,
            Quaternion rot, int playerId) {
        EntityCreator creator = entityCreatorRepo.creator(creatorId);
        Spatial entity = creator.create(new CreationParams(loc));

        entity.setUserData(UserData.CREATOR_ID, creatorId);
        entity.setUserData(UserData.ENTITY_ID, entityId);
        entity.setUserData(UserData.PLAYER_ID, playerId);

        worldRoot.attachChild(entity);

        entities.put(entityId, entity);
        syncer.addObject(entityId, entity);

        Sender sender = app.getStateManager().getState(Sender.class);
        if (!sender.isClient()) {
            sender.addCommand(new CmdAddEntity(creatorId, entityId, loc, rot,
                    playerId));
        }

        return entity;
    }
    
    public void removeEntity(int entityId) {
        Spatial entity = entities.remove(entityId);
        
        if (entity == null) {
            return;
        }
        syncer.removeEntity(entityId);
        
        Sender sender = app.getStateManager().getState(Sender.class);
        if (!sender.isClient()) {
            sender.addCommand(new CmdRemoveEntity(entityId));
        }
    }

    public void informAboutEntities(HostedConnection conn) {
        ServerSender sender = (ServerSender) app
                .getStateManager().getState(Sender.class);
        for (Map.Entry<Integer, Spatial> entry : entities.entrySet()) {
            Spatial spatial = entry.getValue();

            int creatorId = spatial.getUserData(UserData.CREATOR_ID);
            int entityId = entry.getKey();
            int playerId = spatial.getUserData(UserData.PLAYER_ID);
            // TODO: This won't work with physics-enabled entities in general
            Vector3f loc = spatial.getLocalTranslation();
            Quaternion rot = spatial.getWorldRotation();
            sender.addCommandForSingle(new CmdAddEntity(creatorId, entityId,
                    loc, rot, playerId), conn);
        }
    }

    public Spatial getEntity(int id) {
        return entities.get(id);
    }
    
    @Override
    public void update(float tpf) {
        if (projectileSpawnTimer <= 0) {
            projectileSpawnTimer = 200;
            newEntity(1, new Vector3f(0,4,0), Quaternion.ZERO, -1);
        }
        projectileSpawnTimer -= tpf;
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
