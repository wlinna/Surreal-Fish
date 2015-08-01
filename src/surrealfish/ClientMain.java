package surrealfish;

import arkhados.net.ClientSender;
import arkhados.net.DefaultReceiver;
import arkhados.net.OneTrueMessage;
import arkhados.net.Receiver;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.NetworkClient;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import surrealfish.net.ClientNetListener;
import surrealfish.net.DataRegistration;

/**
 * test
 *
 * @author normenhansen
 */
public class ClientMain extends SimpleApplication {

    public static void main(String[] args) {
        ClientMain app = new ClientMain();
        app.start();
    }
    // TODO: Remove this. It's temporarily here
    public static int playerId = -1;

    @Override
    public void simpleInitApp() {
        BulletAppState physics = new BulletAppState();
        physics.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(physics);
        physics.getPhysicsSpace().setAccuracy(1 / 30f);

        DataRegistration.register();

        NetworkClient client = Network.createClient();
        Receiver receiver = new DefaultReceiver();
        stateManager.attach(receiver);
        client.addMessageListener(receiver, OneTrueMessage.class);
        client.addClientStateListener(new ClientNetListener());

        ClientSender sender = new ClientSender();
        stateManager.attach(sender);
        receiver.registerCommandHandler(sender);

        try {
            client.connectToServer("127.0.0.1", 12345, 12345);
            client.start();
        } catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName())
                    .log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
