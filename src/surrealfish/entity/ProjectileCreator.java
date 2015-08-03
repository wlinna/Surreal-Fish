package surrealfish.entity;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh.Type;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.network.serializing.Serializable;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import surrealfish.Globals;
import surrealfish.UserData;
import surrealfish.entity.controls.CSync;
import surrealfish.net.commands.sync.StateData;

public class ProjectileCreator implements EntityCreator {

    @Override
    public Spatial create(CreationParams params) {
        Node projectile = new Node("Projectile");
        projectile.setLocalTranslation(params.location);
        projectile.addControl(new CProjectile());
        return projectile;
    }
    
    
        @Serializable
        public static class ProjectileStateData extends StateData {
            
            private Vector3f loc;
            private Quaternion rot;
            
            ProjectileStateData() {
            }

            public ProjectileStateData(int syncId, Spatial spatial) {
                super(syncId);
                this.loc = spatial.getLocalTranslation();
                this.rot = spatial.getLocalRotation();
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

class CProjectile extends AbstractControl implements CSync {
    
    private ParticleEmitter emitter;
    
    public CProjectile() { 
        emitter = new ParticleEmitter("My explosion effect", Type.Triangle, 30);
        Material mat_red = new Material(Globals.assetManager, 
            "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", Globals.assetManager.loadTexture(
                "Textures/flame.png"));
        emitter.setMaterial(mat_red);
        emitter.setImagesX(2);
        emitter.setImagesY(2);
        emitter.setEndColor(  new ColorRGBA(1f, 0f, 0f, 1f));
        emitter.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f));
        emitter.setStartSize(.5f);
        emitter.setEndSize(.1f);
        emitter.setGravity(0, 0, 0);
        emitter.setLowLife(.3f);
        emitter.setHighLife(1f);
        emitter.getParticleInfluencer().setVelocityVariation(.3f);
    }

    @Override
    protected void controlUpdate(float tpf) {
        spatial.move(0, -3 * tpf, 0);
        emitter.setLocalTranslation(spatial.getLocalTranslation());
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        ((Node) spatial).attachChild(emitter);
    }
    
    

    @Override
    public StateData getSyncableData(StateData stateData) {
        int id = spatial.getUserData(UserData.ENTITY_ID);
        return new ProjectileCreator.ProjectileStateData(id, spatial);
    }
    
}