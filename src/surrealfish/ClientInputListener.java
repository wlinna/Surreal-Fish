package surrealfish;

import arkhados.net.Sender;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import java.util.HashSet;
import java.util.Set;
import surrealfish.net.commands.sync.client.CmdButtons;

public class ClientInputListener extends AbstractAppState
        implements ActionListener {

    private Application app;
    private Set<String> pressed = new HashSet<>();

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;

        app.getInputManager().addListener(this, MappingNames.ARRAY);
        setKeys();
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
}
