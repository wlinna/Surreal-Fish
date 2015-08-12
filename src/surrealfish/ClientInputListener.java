package surrealfish;

import arkhados.net.Sender;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.HashSet;
import java.util.Set;
import surrealfish.net.commands.sync.client.CmdButtons;
import surrealfish.net.commands.sync.client.CmdTargetDirection;
import surrealfish.util.Timer;

public class ClientInputListener extends AbstractAppState
        implements ActionListener {

    private Application app;
    private Set<String> pressed = new HashSet<>();
    private Timer mouseUpdateTimer = new Timer(0.075f);
    private Quaternion rotation = new Quaternion();
    private Vector2f prevMousePos = new Vector2f();
    private Vector3f targetDirection = new Vector3f();
    private Vector3f side = new Vector3f();
    private static final Vector3f MINUS_Z = Vector3f.UNIT_Z.negate();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;

        app.getInputManager().addListener(this, MappingNames.ARRAY);
        setKeys();
        mouseUpdateTimer.setActive(true);

        targetDirection.set(MINUS_Z);
    }

    private void setKeys() {
        InputManager inputManager = app.getInputManager();

        inputManager.addMapping(MappingNames.FORWARD,
                new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping(MappingNames.LEFT,
                new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping(MappingNames.RIGHT,
                new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping(MappingNames.BACKWARD,
                new KeyTrigger(KeyInput.KEY_S));
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (isPressed) {
            pressed.add(name);
        } else {
            pressed.remove(name);
        }

        int flags = generateBits();
        CmdButtons keyboardCmd = new CmdButtons(flags);

        Sender sender = app.getStateManager().getState(Sender.class);
        sender.addCommand(keyboardCmd);
    }

    private int generateBits() {
        int flags = 0;

        for (String key : pressed) {
            int index = MappingNames.toIndex(key);
            flags |= (1 << index);
        }

        return flags;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);

        mouseUpdateTimer.update(tpf);

        updateTarget(tpf);

        if (mouseUpdateTimer.timeJustEnded()) {
            app.getStateManager().getState(Sender.class)
                    .addCommand(new CmdTargetDirection(targetDirection));
            mouseUpdateTimer.setTimeLeft(0.075f);
        }
    }

    private void updateTarget(float tpf) {
        Spatial spatial =
                app.getStateManager().getState(OwnPlayer.class).getSpatial();
        if (spatial == null) {
            return;
        }

        Vector3f forward = spatial.getLocalRotation().mult(MINUS_Z);

        Vector2f newCursorPos = app.getInputManager().getCursorPosition();
        float dx = newCursorPos.x - prevMousePos.x;

        if (FastMath.abs(dx) < 0.001f) {
            return;
        }

        rotation.fromAngles(0f, -dx * 0.01f, 0);
        rotation.mult(targetDirection, targetDirection);

        forward.cross(Vector3f.UNIT_Y, side);
        float sideSign = -FastMath.sign(side.dot(targetDirection));

        spatial.rotate(0f, sideSign * forward.angleBetween(targetDirection),
                0f);

        prevMousePos.set(newCursorPos);
    }
}
