package surrealfish.entity.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import surrealfish.entity.action.EntityAction;
import surrealfish.spell.Spell;

public class CSpell extends AbstractControl {    
    private final List<Spell> spells = new ArrayList<>();
    private final Map<Integer, Float> cooldowns = new HashMap<>();

    @Override
    protected void controlUpdate(float tpf) {
        for (Map.Entry<Integer, Float> entry : cooldowns.entrySet()) {
            float currentCd = entry.getValue();
            entry.setValue(currentCd - tpf);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void castSpell(String name) {
        Spell theSpell = null;
        for (Spell spell : spells) {
            if (spell.getName().equals(name)) {
                theSpell = spell;
            }
        }
        
        if (theSpell == null || cooldowns.get(theSpell.getId()) > 0f) {
            return;
        }
        
        cooldowns.put(theSpell.getId(), theSpell.getCooldown());
        
        Spell.CastSpellActionCreator creator = theSpell.getActionCreator();
        // TODO: Get target from input
        EntityAction action = creator.create(Vector3f.ZERO.add(0f, 1f, 0f));
        spatial.getControl(CActionQueue.class).enqueue(action);
    }
    
    public void addSpell(Spell spell) {
        spells.add(spell);
        cooldowns.put(spell.getId(), 0f);
    }
}
