package surrealfish.entity;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import surrealfish.Globals;
import surrealfish.UserData;
import surrealfish.entity.controls.CSync;
import surrealfish.net.commands.sync.StateData;

public class TestCharacterCreator implements EntityCreator {

    @Override
    public Spatial create(CreationParams params) {
        Spatial model = Globals.assetManager.loadModel("Models/testiUkko.j3o");
        model.setLocalTranslation(params.location);
        model.addControl(new CTestCharacter());
        return model;
    }

    @Serializable
    public static class TestCharacterStateData extends StateData {

        private Vector3f loc = new Vector3f();
        private Quaternion rot = new Quaternion();

        public TestCharacterStateData() {
        }

        public TestCharacterStateData(int syncId, Spatial spatial) {
            super(syncId);
            loc.set(spatial.getLocalTranslation());
            rot.set(spatial.getLocalRotation());
        }

        @Override
        public void applyData(Object target) {
            Spatial spatial = (Spatial) target;
            spatial.setLocalTranslation(loc);
            spatial.setLocalRotation(rot);
        }

        @Override
        public boolean isGuaranteed() {
            return false;
        }
    }
}

class CTestCharacter extends AbstractControl implements CSync {

    private static final Vector3f FORWARD = new Vector3f(0, 0, -1f);

    @Override
    protected void controlUpdate(float tpf) {
        spatial.rotate(0, 1f * tpf, 0f);
        Vector3f dir = spatial.getLocalRotation().mult(FORWARD).multLocal(tpf);
        spatial.move(dir);
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public StateData getSyncableData(StateData stateData) {
        int id = spatial.getUserData(UserData.ENTITY_ID);
        return new TestCharacterCreator.TestCharacterStateData(id, spatial);
    }
}
