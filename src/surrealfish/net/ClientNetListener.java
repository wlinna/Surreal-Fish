package surrealfish.net;

import com.jme3.network.Client;
import com.jme3.network.ClientStateListener;

import arkhados.net.Command;
import arkhados.net.CommandHandler;
import arkhados.net.Sender;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import surrealfish.ClientMain;
import surrealfish.Area;
import surrealfish.OwnPlayer;
import surrealfish.net.commands.CmdClientLogin;
import surrealfish.net.commands.CmdServerLogin;
import surrealfish.net.commands.CmdSetPlayersCharacter;
import surrealfish.net.commands.CmdTopicOnly;
import surrealfish.net.commands.Topic;
import surrealfish.util.Timer;

public class ClientNetListener extends AbstractAppState
        implements ClientStateListener, CommandHandler {

    private ClientMain app;
    private String name;
    private Timer udpHandshakeAckTimer = new Timer(1f);
    private boolean handshakeComplete = false;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (ClientMain) app;
    }

    public void reset() {
        udpHandshakeAckTimer.setTimeLeft(1f);
        udpHandshakeAckTimer.setActive(false);
        handshakeComplete = false;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        udpHandshakeAckTimer.update(tpf);
        if (udpHandshakeAckTimer.timeJustEnded()) {
            Sender sender = app.getStateManager().getState(Sender.class);
            sender.addCommand(
                    new CmdTopicOnly(Topic.UDP_HANDSHAKE_REQUEST, false));
            udpHandshakeAckTimer
                    .setTimeLeft(udpHandshakeAckTimer.getOriginal());
        }
    }

    @Override
    public void clientConnected(Client c) {
    }

    @Override
    public void clientDisconnected(Client c,
            ClientStateListener.DisconnectInfo info) {
        System.out.println("Disconnected from server");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void readGuaranteed(Object source, Command command) {
        if (command instanceof CmdTopicOnly) {
            handleTopicCommand((CmdTopicOnly) command);
        } else if (command instanceof CmdServerLogin) {
            handleLoginCommand((CmdServerLogin) command);
        } else if (command instanceof CmdSetPlayersCharacter) {
            handleSetPlayersCharacter((CmdSetPlayersCharacter) command);
        }
    }

    @Override
    public void readUnreliable(Object source, Command command) {
        if (command instanceof CmdTopicOnly) {
            handleTopicCommand((CmdTopicOnly) command);
        }
    }

    private void handleTopicCommand(CmdTopicOnly topicOnlyCommand) {
        switch (topicOnlyCommand.getTopicId()) {
            case Topic.UDP_HANDSHAKE_ACK:
                handleUdpHandshakeAck();
                break;
            case Topic.CONNECTION_ESTABLISHED:
                Sender sender = app.getStateManager().getState(Sender.class);
                sender.addCommand(new CmdTopicOnly(
                        Topic.UDP_HANDSHAKE_REQUEST, false));
                udpHandshakeAckTimer.setActive(true);
        }
    }

    private void handleLoginCommand(CmdServerLogin loginCommand) {
        if (!loginCommand.isAccepted()) {
            return;
        }

        app.getStateManager().getState(OwnPlayer.class)
                .setPlayerId(loginCommand.getPlayerId());
        app.getStateManager().attach(new Area());
    }

    private void handleSetPlayersCharacter(CmdSetPlayersCharacter message) {
        udpHandshakeAckTimer.setActive(false);
    }

    private void handleUdpHandshakeAck() {
        if (!handshakeComplete) {
            Sender sender = app.getStateManager().getState(Sender.class);
            CmdClientLogin command = new CmdClientLogin(name);
            sender.addCommand(command);
            handshakeComplete = true;
        }
    }
}