package surrealfish.net.commands.sync.client;

import arkhados.net.Command;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CmdButtons implements Command{
    private int flags;

    public CmdButtons() {
    }
        
    public CmdButtons(int flags) {
        this.flags = flags;
    }       

    @Override
    public boolean isGuaranteed() {
        return true;
    }

    public int getFlags() {
        return flags;
    }
}
