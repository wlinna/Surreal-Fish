package surrealfish.entity;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Spatial;
import surrealfish.Globals;
import surrealfish.entity.controls.CTestCharacter;
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
