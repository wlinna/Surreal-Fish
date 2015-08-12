package surrealfish.entity.controls;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import surrealfish.Globals;
import surrealfish.Input;
import surrealfish.UserData;
import surrealfish.entity.TestCharacterCreator;
import surrealfish.net.commands.sync.StateData;
import surrealfish.util.Timer;

public class CTestCharacter extends AbstractControl implements CSync {

    private static final Vector3f MINUS_Z = Vector3f.UNIT_Z.negate();
    private Input input;
    private float walkSpeed = 3f;
    private Timer castTimer = new Timer(1f);
    private final Vector3f tempRealDirection = new Vector3f();
    private final Vector3f side = new Vector3f();

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);

        castTimer.setActive(spatial != null);
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (Globals.isClient) {
            return;
        }
        Vector3f forward = spatial.getLocalRotation().mult(MINUS_Z);
        forward.cross(Vector3f.UNIT_Y, side);
        float sideSign = -FastMath.sign(side.dot(input.getTargetDirection()));

        spatial.rotate(0f, sideSign * forward.angleBetween(
                input.getTargetDirection()), 0f);

        spatial.getLocalRotation()
                .mult(input.getRelativeDirection(), tempRealDirection);
        tempRealDirection.multLocal(walkSpeed * tpf);

        spatial.move(tempRealDirection);

        castTimer.update(tpf);

        if (castTimer.timeJustEnded()) {
            castTimer.setTimeLeft(1f);
            spatial.getControl(CSpell.class).castSpell("FireStuff");
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public StateData getSyncableData(StateData stateData) {
        int id = spatial.getUserData(UserData.ENTITY_ID);
        return new TestCharacterCreator.TestCharacterStateData(id, spatial);
    }

    public void setInput(Input input) {
        this.input = input;
    }
}
