
package surrealfish.entity.controls;

import com.jme3.scene.control.Control;
import surrealfish.net.commands.sync.StateData;


public interface CSync extends Control {
    public StateData getSyncableData(StateData stateData);
}
