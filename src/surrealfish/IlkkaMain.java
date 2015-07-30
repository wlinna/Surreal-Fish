package surrealfish;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.input.ChaseCamera;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * test
 *
 * @author normenhansen
 */
public class IlkkaMain extends SimpleApplication
        implements AnimEventListener {

    //private AnimChannel channel;
    //private AnimControl control;
    Node player;

    public static void main(String[] args) {
        IlkkaMain app = new IlkkaMain();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(dl);
        
        rootNode.attachChild(assetManager.loadModel("Scenes/newScene.j3o"));
        
        player = (Node) assetManager.loadModel("Models/testiUkko.j3o");
        // Use BetterCharacterControl instead of using move-method of player
//        player.addControl(new BetterCharacterControl(5f, 5f, 1f));
//        player.getControl(BetterCharacterControl.class).setWalkDirection(new Vector3f(Vector3f.UNIT_Z));


        
        rootNode.attachChild(player);
        for (Spatial object : ((Node) player.getChild(0)).getChildren()) {
            AnimControl control = object.getControl(AnimControl.class);
            if (control == null) {
                continue;
            }
            control.addListener(this);
            AnimChannel channel = control.createChannel();
            channel.setAnim("Walk");
        }

        flyCam.setEnabled(false);
        ChaseCamera chaseCam = new ChaseCamera(cam, player, inputManager);
        chaseCam.setSmoothMotion(true);
        //control = player.getChild("TestiUkko:Body").getControl(AnimControl.class);
        //control.addListener(this);
        //channel = control.createChannel();
        //channel.setAnim("Walk");
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.move(0f, 0f, 1f * tpf);
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }


    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Walk")) {
            channel.setAnim("Walk");
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(1f);
        }
    }

 
    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
}
