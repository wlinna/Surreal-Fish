package surrealfish.net.commands;

import arkhados.net.Command;
import com.jme3.network.serializing.Serializable;

@Serializable
public class CmdTopicOnly implements Command {
    
    private transient boolean isGuaranteed = true;
    private byte topicId;
   
    public CmdTopicOnly() {
    }
    
    public CmdTopicOnly(int topicId) {
        this.topicId = (byte) topicId;
    }

    public CmdTopicOnly(int topicId, boolean isGuaranteed) {
        this.topicId = (byte) topicId;
        this.isGuaranteed = isGuaranteed;        
    }     

    @Override
    public boolean isGuaranteed() {
        return isGuaranteed;
    }

    public int getTopicId() {
        return topicId;
    }   
}