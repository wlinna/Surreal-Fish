package surrealfish;

import arkhados.net.DefaultReceiver;
import arkhados.net.OneTrueMessage;
import arkhados.net.Receiver;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.input.ChaseCamera;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.NetworkClient;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import surrealfish.net.DataRegistration;

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
        player = (Node) assetManager.loadModel("Models/testiUkko.j3o");        
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
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }


    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Walk")) {
            channel.setAnim("Walk");
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(1f);
        }
    }

 
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // unused
    }
}
