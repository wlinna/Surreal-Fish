/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surrealfish.net.commands;

import arkhados.net.Command;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CmdServerLogin implements Command {

    private String name;
    private int playerId;
    private boolean accepted;

    public CmdServerLogin() {
    }

    public CmdServerLogin(String nick, int playerId, boolean accepted) {
        this.name = nick;
        this.playerId = playerId;
        this.accepted = accepted;
    }

    public String getName() {
        return name;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public boolean isGuaranteed() {
        return true;
    }
}
