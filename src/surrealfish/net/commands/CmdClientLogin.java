package surrealfish.net.commands;

import arkhados.net.Command;
import com.jme3.network.serializing.Serializable;

/**
 *
 * @author william
 */
@Serializable
public class CmdClientLogin implements Command {

    private String name;

    public CmdClientLogin() {
    }

    public CmdClientLogin(String nick) {
        this.name = nick;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean isGuaranteed() {
        return true;
    }
}
