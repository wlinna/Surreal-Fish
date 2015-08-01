package surrealfish.net;

import arkhados.net.Command;
import arkhados.net.CommandHandler;
import arkhados.net.Receiver;
import arkhados.net.ServerSender;
import com.jme3.app.state.AppStateManager;
import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import surrealfish.PlayerData;
import surrealfish.ServerMain;
import surrealfish.net.commands.CmdClientLogin;
import surrealfish.net.commands.CmdServerLogin;
import surrealfish.net.commands.CmdTopicOnly;
import surrealfish.net.commands.Topic;


/**
 *
 * @author william
 */
public class ServerNetListener implements ConnectionListener, CommandHandler {

    private ServerMain app;
    private AppStateManager stateManager;
    private boolean someoneJoined = false;

    public ServerNetListener(ServerMain app, Server server) {
        this.app = app;
        stateManager = app.getStateManager();
    }

    @Override
    public void connectionAdded(Server server, HostedConnection conn) {
        final int clientId = conn.getId();
        if (!ServerClientData.exists(clientId)) {
            ServerClientData.add(clientId);
            ServerSender sender = stateManager.getState(ServerSender.class);
            sender.addConnection(conn);
            stateManager.getState(Receiver.class).addConnection(conn);

            CmdTopicOnly connectionEstablishendCommand =
                    new CmdTopicOnly(Topic.CONNECTION_ESTABLISHED);
            sender.addCommand(connectionEstablishendCommand);
        } else {
            Logger.getLogger(ServerNetListener.class.getName())
                    .log(Level.SEVERE, "Client ID exists!");
            conn.close("ID exists already");
        }
    }

    @Override
    public void connectionRemoved(final Server server,
            final HostedConnection conn) {
        app.enqueue(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Integer playerId = conn.getAttribute("player-id");
                ServerSender sender = stateManager.getState(ServerSender.class);
                sender.removeConnection(conn);

                ServerClientData.remove(conn.getId());

                if (playerId == null) {
                    return null;
                }

                ServerClientData.removeConnection(playerId);

                PlayerData.remove(playerId);

                if (!server.hasConnections() && someoneJoined) {
                    app.stop();
                }

                return null;
            }
        });

    }

    @Override
    public void readGuaranteed(Object source, Command command) {
        if (command instanceof CmdTopicOnly) {
            handleTopicOnlyCommand((HostedConnection) source,
                    (CmdTopicOnly) command);
        } else if (command instanceof CmdClientLogin) {
            handleClientLoginCommand((HostedConnection) source,
                    (CmdClientLogin) command);
        }

    }

    private void handleTopicOnlyCommand(HostedConnection source,
            CmdTopicOnly topicCommand) {
        ServerSender sender = stateManager.getState(ServerSender.class);

        switch (topicCommand.getTopicId()) {
            case Topic.UDP_HANDSHAKE_REQUEST:
                sender.addCommandForSingle(
                        new CmdTopicOnly(Topic.UDP_HANDSHAKE_ACK, false),
                        source);
                break;
        }
    }

    private void handleClientLoginCommand(final HostedConnection source,
            final CmdClientLogin commmand) {
        final int clientId = source.getId();

        if (!ServerClientData.exists(clientId)) {
            Logger.getLogger(ServerNetListener.class.getName()).log(
                    Level.WARNING,
                    "Receiving join message from unknown client (id: {0})",
                    clientId);
            return;
        }

        app.enqueue(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                final int playerId = PlayerData.getNew(commmand.getName());
                source.setAttribute("player-id", playerId);

                ServerClientData.setConnected(clientId, true);
                ServerClientData.setPlayerId(clientId, playerId);
                ServerClientData.addConnection(playerId, source);

                CmdServerLogin serverLoginMessage =
                        new CmdServerLogin(commmand.getName(), playerId, true);
                someoneJoined = true;
                ServerSender sender = stateManager.getState(ServerSender.class);
                sender.addCommandForSingle(serverLoginMessage, source);
                return null;
            }
        });
    }

    @Override
    public void readUnreliable(Object source, Command command) {
        if (command instanceof CmdTopicOnly) {
            handleTopicOnlyCommand((HostedConnection) source,
                    (CmdTopicOnly) command);
        }
    }
}