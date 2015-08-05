package surrealfish.entity.controls;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.PhysicsTickListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.PhysicsControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.List;
import surrealfish.UserData;
import surrealfish.entity.ProjectileCreator;
import surrealfish.net.commands.sync.StateData;

public class CProjectile extends AbstractControl implements CSync,
        PhysicsControl, PhysicsTickListener {

    private Vector3f direction = new Vector3f();
    private Vector3f velocity = new Vector3f();
    private float projectileSpeed = 7f;
    private GhostControl ghostControl;
    private PhysicsSpace space;

    @Override
    protected void controlUpdate(float tpf) {
        direction.mult(projectileSpeed * tpf, velocity);
        spatial.move(velocity);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);

        if (spatial == null) {
            return;
        }

        ghostControl = spatial.getControl(GhostControl.class);
    }

    @Override
    public void prePhysicsTick(PhysicsSpace space, float tpf) {
    }

    @Override
    public void physicsTick(PhysicsSpace space, float tpf) {
        List<PhysicsCollisionObject> overlappingObjects = ghostControl.getOverlappingObjects();
        if (overlappingObjects.size() > 0) {
        }
    }

    @Override
    public StateData getSyncableData(StateData stateData) {
        int id = spatial.getUserData(UserData.ENTITY_ID);
        return new ProjectileCreator.ProjectileStateData(id, spatial);
    }

    public void setTarget(Vector3f tar) {
        direction = tar.subtract(spatial.getLocalTranslation()).normalizeLocal();
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f vel) {
        velocity.set(vel);
    }

    @Override
    public void setPhysicsSpace(PhysicsSpace space) {
        if (space == null) {
            this.space.removeTickListener(this);
            return;
        }

        this.space = space;

        space.addTickListener(this);
    }

    @Override
    public PhysicsSpace getPhysicsSpace() {
        return space;
    }
}