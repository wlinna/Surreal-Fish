
package surrealfish.entity.controls;

import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import surrealfish.Globals;
import surrealfish.UserData;
import surrealfish.entity.ProjectileCreator;
import surrealfish.net.commands.sync.StateData;

public class CProjectile extends AbstractControl implements CSync {
    
    private ParticleEmitter emitter;
    private Vector3f target = new Vector3f(0, -1, 0);
    private float projectileSpeed = 3f;
    
    public CProjectile() { 
        emitter = new ParticleEmitter("My explosion effect", ParticleMesh.Type.Triangle, 30);
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
        spatial.move(target.normalize().mult(projectileSpeed*tpf));
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
    
    public void setTarget(Vector3f target) {
        this.target = target;
    }    
    
    
}