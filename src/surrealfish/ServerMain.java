package surrealfish;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.renderer.RenderManager;

public class ServerMain extends SimpleApplication {        
    
    public static void main(String[] args) {
        ServerMain app = new ServerMain();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        BulletAppState physics = new BulletAppState();
        physics.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        physics.getPhysicsSpace().setAccuracy(1 / 60f);        
        stateManager.attach(physics);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
