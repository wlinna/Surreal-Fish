package surrealfish.entity.controls;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.LinkedList;
import java.util.Queue;
import surrealfish.entity.action.EntityAction;

public class CActionQueue extends AbstractControl {

    private Queue<EntityAction> actions = new LinkedList<>();

    public void enqueue(EntityAction action) {
        actions.add(action);
        action.setSpatial(spatial);
    }

    @Override
    protected void controlUpdate(float tpf) {
        EntityAction current = actions.peek();
        if (current == null) {
            return;
        }

        boolean active = current.update(tpf);

        if (!active) {
            current.end();
            actions.poll();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
}
