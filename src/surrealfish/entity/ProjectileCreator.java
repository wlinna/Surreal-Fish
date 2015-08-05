package surrealfish.entity;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import surrealfish.entity.controls.CProjectile;
import surrealfish.net.commands.sync.StateData;

public class ProjectileCreator implements EntityCreator {

    @Override
    public Spatial create(CreationParams params) {
        Node projectile = new Node("Projectile");
        projectile.setLocalTranslation(params.location);
        projectile.addControl(new GhostControl(new SphereCollisionShape(.3f)));
        projectile.addControl(new CProjectile());
        return projectile;
    }

    @Serializable
    public static class ProjectileStateData extends StateData {

        private Vector3f loc = new Vector3f();
        private Quaternion rot = new Quaternion();
        private Vector3f vel = new Vector3f();

        public ProjectileStateData() {
        }

        public ProjectileStateData(int syncId, Spatial spatial) {
            super(syncId);
            loc.set(spatial.getLocalTranslation());
            rot.set(spatial.getLocalRotation());
            vel.set(spatial.getControl(CProjectile.class).getVelocity());
        }

        @Override
        public void applyData(Object target) {
            Spatial spatial = (Spatial) target;
            spatial.setLocalTranslation(loc);
            spatial.setLocalRotation(rot);
            spatial.getControl(CProjectile.class).setVelocity(vel);
        }

        @Override
        public boolean isGuaranteed() {
            return false;
        }
    }
}