package surrealfish.entity;

import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import surrealfish.Globals;
import surrealfish.entity.controls.CProjectile;
import surrealfish.net.commands.sync.StateData;

public class ProjectileCreator implements EntityCreator {

    @Override
    public Spatial create(CreationParams params) {
        Node projectile = new Node("Projectile");
        projectile.setLocalTranslation(params.location);
        projectile.addControl(new GhostControl(new SphereCollisionShape(.1f)));
        projectile.addControl(new CProjectile());

        if (Globals.isClient) {

            ParticleEmitter emitter = new ParticleEmitter("My explosion effect",
                    ParticleMesh.Type.Triangle, 30);
            Material mat = new Material(Globals.assetManager,
                    "Common/MatDefs/Misc/Particle.j3md");
            mat.setTexture("Texture", Globals.assetManager.loadTexture(
                    "Textures/flame.png"));
            emitter.setMaterial(mat);
            emitter.setImagesX(2);
            emitter.setImagesY(2);
            emitter.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));
            emitter.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f));
            emitter.setStartSize(.5f);
            emitter.setEndSize(.1f);
            emitter.setGravity(0, 0, 0);
            emitter.setLowLife(.3f);
            emitter.setHighLife(1f);
            emitter.getParticleInfluencer().setVelocityVariation(.3f);

            projectile.attachChild(emitter);
        }

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