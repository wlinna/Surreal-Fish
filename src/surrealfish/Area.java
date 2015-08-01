/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surrealfish;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author william
 */
public class Area extends AbstractAppState {

    private Map<Integer, Spatial> entities = new HashMap<>();
    private Node worldRoot;
    private int idCounter = 0;
    private SimpleApplication app;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        load();
    }

    public void load() {
        worldRoot = (Node) app.getAssetManager()
                .loadModel("Scenes/newScene.j3o");
        app.getRootNode().attachChild(worldRoot);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -1f, -1));
        worldRoot.addLight(sun);

        app.getCamera().setLocation(new Vector3f(0, 160, 20));
        app.getCamera().lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
        super.cleanup();
    }
}
