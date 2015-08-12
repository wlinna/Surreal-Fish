package surrealfish.spell;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.HashMap;
import java.util.Map;
import surrealfish.entity.action.EntityAction;
import surrealfish.spell.spells.FireStuff;

public class Spell {

    public static abstract class CastSpellActionCreator {

        public abstract EntityAction create(Vector3f target);
    }
    private static int idCounter;
    /**
     * Spells has all spells mapped by their name so that spell data can be
     * retrieved from anywhere
     */
    private static Map<String, Spell> spellsByName = new HashMap<>();
    private static Map<Integer, Spell> spellsById = new HashMap<>();

    static {
        createSpells();
    }

    public static void createSpells() {
        addSpell(FireStuff.create());
    }

    public static Spell byName(String name) {
        return spellsByName.get(name);
    }

    private static void addSpell(Spell spell) {
        spell.id = idCounter++;
        spellsByName.put(spell.name, spell);
        spellsById.put(spell.id, spell);
    }
    protected CastSpellActionCreator actionCreator;
    private final String name;
    private int id;
    private final float cooldown;

    public Spell(String name, float cooldown) {
        this.name = name;
        this.cooldown = cooldown;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public float getCooldown() {
        return cooldown;
    }

    public CastSpellActionCreator getActionCreator() {
        return actionCreator;
    }
}
