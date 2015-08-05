package surrealfish;

import arkhados.net.Command;
import arkhados.net.CommandHandler;
import com.jme3.app.state.AbstractAppState;
import com.jme3.network.HostedConnection;
import java.util.HashMap;
import java.util.Map;
import surrealfish.net.commands.sync.client.CmdButtons;

public class ServerInputListener extends AbstractAppState
        implements CommandHandler {

    private Map<Integer, Input> inputs = new HashMap<>();

    public void addPlayer(int playerId) {
        inputs.put(playerId, new Input());
    }

    @Override
    public void readGuaranteed(Object o, Command cmd) {
        if (!(cmd instanceof CmdButtons)) {
            return;
        }

        HostedConnection conn = (HostedConnection) o;
        int playerId = conn.getAttribute("player-id");
        CmdButtons cmdButtons = (CmdButtons) cmd;

        Input input = inputs.get(playerId);
        
        if (input == null) {
            return;
        }
        
        input.setFlags(cmdButtons.getFlags());
    }

    @Override
    public void readUnreliable(Object o, Command cmd) {
    }
    
    public Input getInput(int playerId) {
        return inputs.get(playerId);
    }
}
