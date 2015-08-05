package surrealfish.net.commands;

import arkhados.net.Command;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CmdSetPlayersCharacter implements Command{
    private int entityId;
    private int playerId;

    public CmdSetPlayersCharacter() {
    }

    public CmdSetPlayersCharacter(int entityId, int playerId) {
        this.entityId = entityId;
        this.playerId = playerId;
    }

    public int getEntityId() {
        return entityId;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public boolean isGuaranteed() {
        return true;
    }
}