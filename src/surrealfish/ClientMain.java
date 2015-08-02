package surrealfish;

import arkhados.net.ClientSender;
import arkhados.net.DefaultReceiver;
import arkhados.net.OneTrueMessage;
import arkhados.net.Receiver;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.network.Network;
import com.jme3.network.NetworkClient;
import com.jme3.renderer.RenderManager;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import surrealfish.net.ClientNetListener;
import surrealfish.net.DataRegistration;
import surrealfish.net.Syncer;

public class ClientMain extends SimpleApplication {

    public static void main(String[] args) {
        ClientMain app = new ClientMain();
        app.pauseOnFocus = false;
        app.start();
    }
    // TODO: Remove this. It's temporarily here
    public static int playerId = -1;

    @Override
    public void simpleInitApp() {
        Globals.assetManager = assetManager;

        BulletAppState physics = new BulletAppState();
        physics.setThreadingType(BulletAppState.ThreadingType.PARALLEL);
        stateManager.attach(physics);
        physics.getPhysicsSpace().setAccuracy(1 / 30f);

        DataRegistration.register();

        final NetworkClient client = Network.createClient();
        Receiver receiver = new DefaultReceiver();
        stateManager.attach(receiver);

        client.addMessageListener(receiver, OneTrueMessage.class);

        ClientNetListener netListener = new ClientNetListener();
        client.addClientStateListener(netListener);
        receiver.registerCommandHandler(netListener);
        stateManager.attach(netListener);

        netListener.reset();

        ClientSender sender = new ClientSender();
        sender.setClient(client);
        stateManager.attach(sender);
        receiver.registerCommandHandler(sender);

        Syncer syncer = new Syncer();
        receiver.registerCommandHandler(syncer);
        stateManager.attach(syncer);

        // FIXME: We use delay because otherwise getLastReceivedOrderNum
        // would fail
        delayed(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try {
                    client.connectToServer("127.0.0.1", 12345, 12345);
                    client.start();
                } catch (IOException ex) {
                    Logger.getLogger(ClientMain.class.getName())
                            .log(Level.SEVERE, null, ex);
                    System.exit(1);
                }

                return null;
            }
        }, 100);

        flyCam.setEnabled(false);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    public void delayed(final Callable<Void> f, long time) {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                enqueue(f);
            }
        }, time);
    }
}
