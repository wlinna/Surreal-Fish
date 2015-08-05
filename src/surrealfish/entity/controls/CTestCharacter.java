/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package surrealfish.entity.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import surrealfish.UserData;
import surrealfish.entity.TestCharacterCreator;
import surrealfish.net.commands.sync.StateData;

public class CTestCharacter extends AbstractControl implements CSync {

    private Vector3f relativeDirection = new Vector3f();
    private float walkSpeed = 3f;

    @Override
    protected void controlUpdate(float tpf) {
        spatial.move(relativeDirection.mult(walkSpeed * tpf));
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public StateData getSyncableData(StateData stateData) {
        int id = spatial.getUserData(UserData.ENTITY_ID);
        return new TestCharacterCreator.TestCharacterStateData(id, spatial);
    }
    
    public void setRelativeDirection(Vector3f direction) {
        relativeDirection = direction;
    }
}
