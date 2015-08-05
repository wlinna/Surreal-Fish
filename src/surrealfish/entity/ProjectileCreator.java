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
        projectile.addControl(new CProjectile());
        projectile.addControl(new GhostControl(new SphereCollisionShape(1f)));
        return projectile;
    }
    
    
        @Serializable
        public static class ProjectileStateData extends StateData {
            
            private Vector3f loc;
            private Quaternion rot;
            private Vector3f dir;
            private Vector3f tar;
            
            public ProjectileStateData() {
            }

            public ProjectileStateData(int syncId, Spatial spatial) {
                super(syncId);
                this.loc = spatial.getLocalTranslation();
                this.rot = spatial.getLocalRotation();
                this.dir = spatial.getControl(CProjectile.class).getDirection();
                this.tar = spatial.getControl(CProjectile.class).getTarget();
            }

            @Override
            public void applyData(Object target) {
                Spatial spatial = (Spatial) target;
                spatial.setLocalTranslation(loc);
                spatial.setLocalRotation(rot);
                spatial.getControl(CProjectile.class).setDirection(dir);
                spatial.getControl(CProjectile.class).setTarget(tar);
            }

            @Override
            public boolean isGuaranteed() {
                return false;
            }
            
        }

}