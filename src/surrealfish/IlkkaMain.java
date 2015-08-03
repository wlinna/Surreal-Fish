package surrealfish;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import java.util.ArrayList;
import java.util.List;
import surrealfish.entity.controls.CCharaterAnim;
/**
 * test
 *
 * @author normenhansen
 */
public class IlkkaMain extends SimpleApplication
         {

    private ArrayList<AnimChannel> channelList = new ArrayList<AnimChannel>();
    //private AnimControl control;
    private CameraNode camNode;
    private Vector2f oldMousePos;
    private BitmapText hudText;
    Node player;
    float walkTime = 0;

    public static void main(String[] args) {
        IlkkaMain app = new IlkkaMain();
        app.start();
    }

    @Override
    public void simpleInitApp() {


        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(dl);

        rootNode.attachChild(assetManager.loadModel("Scenes/newScene.j3o"));

        player = (Node) assetManager.loadModel("Models/testiUkko.j3o");


        rootNode.attachChild(player);
        CCharaterAnim c = new CCharaterAnim();
        player.addControl(c);
        /*Node child = (Node) player.getChild(0);
         AnimControl control1 = child.getControl(AnimControl.class);
        control1.addListener(this);

        for (Spatial object : child.getChildren()) {
            AnimControl control = object.getControl(AnimControl.class);
            if (control == null) {
                continue;
            }

            control.addListener(this);
            AnimChannel channel = control.createChannel();
            channel.setAnim("Walk");
            channel.setSpeed(0f);
            channelList.add(channel);
        }

         */
        flyCam.setEnabled(false);

        camNode = new CameraNode("Camera Node", cam);
        camNode.setControlDir(ControlDirection.SpatialToCamera);

        player.attachChild(camNode);
        camNode.setLocalTranslation(new Vector3f(0, 5, 10));
        camNode.lookAt(player.getLocalTranslation(), Vector3f.UNIT_Y);

        ChaseCamera chaseCam = new ChaseCamera(cam, player, inputManager);

        // Test multiple inputs per mapping
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));

        // Test multiple listeners per mapping
        //inputManager.addListener(actionListener, "My Action");
        inputManager.addListener(analogListener, "Forward");
        inputManager.addListener(analogListener, "Backward");
        inputManager.addListener(analogListener, "Left");
        inputManager.addListener(analogListener, "Right");
        oldMousePos = new Vector2f(inputManager.getCursorPosition());
        IntalizeText();
    }

    private void IntalizeText() {
        hudText = new BitmapText(guiFont, false);
        hudText.setSize(guiFont.getCharSet().getRenderedSize());      // font size
        hudText.setColor(ColorRGBA.Blue);                             // font color
        hudText.setText("You can write any string here");             // the text
        hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
        guiNode.attachChild(hudText);

    }

    @Override
    public void simpleUpdate(float tpf) {

        
        if (inputManager.getCursorPosition().x != oldMousePos.x) {
            float dx = inputManager.getCursorPosition().x - oldMousePos.x;
            player.rotate(0, -dx * 0.01f, 0);
            oldMousePos.set(inputManager.getCursorPosition());
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }

    private void updateWalkTime() {
        for (AnimChannel object : channelList) {
            object.setTime(walkTime);
        }
    }

    public final Vector3f getForward(Node node) {
        Vector3f forward = new Vector3f(0, 0, -1);
        forward = node.getLocalRotation().mult(forward);
        return forward;
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean pressed, float tpf) {
            System.out.println(name + " = " + pressed);
        }
    };
    public AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            if (name.equals("Forward")) {
                Vector3f dir = getForward(player);
                dir.multLocal(tpf);
                player.move(dir);
                walkTime += tpf;
                updateWalkTime();
            }

            if (name.equals("Backward")) {
                Vector3f dir = getForward(player);
                dir.multLocal(-tpf);
                player.move(dir);
                walkTime -= tpf;
                updateWalkTime();
            }

            if (name.equals("Right")) {


                Vector3f dir = getForward(player).cross(Vector3f.UNIT_Y);
                dir.multLocal(tpf);
                player.move(dir);
            }

            if (name.equals("Left")) {
                Vector3f dir = getForward(player).cross(Vector3f.UNIT_Y).mult(-1f);
                dir.multLocal(tpf);
                player.move(dir);
            }
        }
    };
}
