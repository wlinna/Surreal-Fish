package surrealfish;

import arkhados.net.Command;
import arkhados.net.CommandHandler;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import java.util.concurrent.Callable;
import surrealfish.net.commands.CmdSetPlayersCharacter;

public class OwnPlayer extends AbstractAppState implements CommandHandler {

    private Spatial spatial;
    private int playerId = -1;
    private int entityId = -1;
    private Application app;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
    }

    @Override
    public void readGuaranteed(Object o, Command aCmd) {
        if (aCmd instanceof CmdSetPlayersCharacter) {
            CmdSetPlayersCharacter cmd = (CmdSetPlayersCharacter) aCmd;
            if (cmd.getPlayerId() == playerId) {
                entityId = cmd.getEntityId(); 
                app.getStateManager().attach(new ClientInputListener());
            }
        }
    }

    private void ownCharacter() {
        Node entity = (Node) spatial;

        CameraNode camNode = new CameraNode("Camera Node", app.getCamera());
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);

        entity.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 5, 10));
        camNode.lookAt(entity.getLocalTranslation(), Vector3f.UNIT_Y);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        if (spatial != null && playerId != -1 && entityId != -1) {
            return;
        }
        
        Area area = app.getStateManager().getState(Area.class);
        
        if (area == null) {
            return;
        }
        
        spatial = area.getEntity(entityId);
        
        if (spatial == null) {
            return;
        }
        
        ownCharacter();
    }

    @Override
    public void readUnreliable(Object o, Command cmnd) {
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public Spatial getSpatial() {
        return spatial;
    }        
}
