package surrealfish.net;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.HostedConnection;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.Callable;
import arkhados.net.Command;
import arkhados.net.CommandHandler;
import arkhados.net.Sender;
import arkhados.net.ServerSender;
import java.util.Map;
import java.util.Set;
import surrealfish.PlayerData;
import surrealfish.entity.controls.CSync;
import surrealfish.net.commands.sync.StateData;

public class Syncer extends AbstractAppState implements CommandHandler {

    private Application app;
    private float syncTimer = 0f;
    private float defaultSyncFrequency;
    private final Map<Integer, Object> syncObjects = new HashMap<>();
    private final Queue<StateData> stateDataQueue = new LinkedList<>();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        Sender sender = stateManager.getState(Sender.class);
        if (sender.isServer()) {
            defaultSyncFrequency = 0.05f;
        }
    }

    @Override
    public void update(float tpf) {
        Sender sender = app.getStateManager().getState(Sender.class);
        if (sender.isClient()) {

            for (Iterator<StateData> it = stateDataQueue.iterator();
                    it.hasNext();) {
                StateData stateData = it.next();
                Object object = syncObjects.get(stateData.getSyncId());
                if (object != null) {
                    stateData.applyData(object);
                }

                it.remove();
            }
        } else {
            syncTimer += tpf;
            if (syncTimer >= defaultSyncFrequency) {
                sendSyncData();
                syncTimer = 0.0f;
            }
        }
    }

    private void sendSyncData() {
        ServerSender sender =
                app.getStateManager().getState(ServerSender.class);

        Set<Entry<Integer, Object>> entrySet = syncObjects.entrySet();

        for (Iterator<Entry<Integer, Object>> it = entrySet.iterator();
                it.hasNext();) {
            Entry<Integer, Object> entry = it.next();
            if (!(entry.getValue() instanceof Spatial)) {
                continue;
            }

            Spatial spatial = (Spatial) entry.getValue();

            CSync syncControl = spatial.getControl(CSync.class);
            if (syncControl != null) {
                StateData data = syncControl.getSyncableData(null);
                if (data != null) {
                    sender.addCommand(data);
                }
            }
        }
    }

    private void doMessage(int syncId, Command command) {
        Object object = syncObjects.get(syncId);

        if (object == null) {
            return;
        }

        if (command instanceof StateData) {
            ((StateData) command).applyData(object);
        }
    }

    public void addObject(int id, Object object) {
        syncObjects.put(id, object);
    }

    public void removeEntity(int id) {
        syncObjects.remove(id);
    }

    public void clear() {
        syncObjects.clear();
        stateDataQueue.clear();
    }

    @Override
    public void readGuaranteed(Object source, Command guaranteed) {
        Sender sender = app.getStateManager().getState(Sender.class);
        if (sender.isClient()) {
            clientHandleCommands(guaranteed);
        } else {
            serverHandleCommands((HostedConnection) source, guaranteed);
        }
    }

    @Override
    public void readUnreliable(Object source, Command unreliable) {
        Sender sender = app.getStateManager().getState(Sender.class);
        if (sender.isClient()) {
            clientHandleCommands(unreliable);
        } else {
            serverHandleCommands((HostedConnection) source, unreliable);
        }
    }

    private void clientHandleCommands(final Command command) {
        app.enqueue(new Callable<Void>() {
            @Override
            public Void call() throws Exception {

                if (command instanceof StateData) {
                    stateDataQueue.add((StateData) command);
                }
                return null;
            }
        });
    }

    private void serverHandleCommands(HostedConnection source,
            final Command command) {
        int playerId = ServerClientData.getPlayerId(source.getId());
        final int id = PlayerData.getIntData(playerId, PlayerData.ENTITY_ID);
        if (id != -1) {
            app.enqueue(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    doMessage(id, command);
                    return null;
                }
            });
        } else {
            System.out.println("Entity id for player " + playerId
                    + " does not exist");
        }
    }
}