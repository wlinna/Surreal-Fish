package surrealfish.spell.spells;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import surrealfish.Area;
import surrealfish.Globals;
import surrealfish.entity.action.EntityAction;
import surrealfish.entity.controls.CProjectile;
import surrealfish.spell.Spell;

public class FireStuff extends Spell {

    public FireStuff(String name, float cooldown) {
        super(name, cooldown);
    }

    public static Spell create() {
        FireStuff fireStuff = new FireStuff("FireStuff", .8f);
        fireStuff.actionCreator = new CastSpellActionCreator() {
            @Override
            public EntityAction create(Vector3f target) {
                return new ACastProjectile(target);
            }
        };

        return fireStuff;
    }
}

class ACastProjectile extends EntityAction {
    private Vector3f target = new Vector3f();

    public ACastProjectile(Vector3f target) {
        this.target.set(target);
    }        
    
    @Override
    public boolean update(float tpf) {
        Area area = Globals.app.getStateManager().getState(Area.class);
        
        Vector3f start = getSpatial().getLocalTranslation().add(0f, 1f, 0f);
        
        Spatial projectile = area.newEntity(1, start, Quaternion.ZERO, -1);
        CProjectile projectileControl =
                projectile.getControl(CProjectile.class);
        projectileControl.setTarget(target);
        return false;
    }
}