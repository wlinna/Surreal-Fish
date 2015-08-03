/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surrealfish.entity.controls;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.ArrayList;

/**
 *
 * @author Ilkka
 */
public class CCharaterAnim extends AbstractControl implements AnimEventListener {

    private ArrayList<AnimChannel> channelList = new ArrayList<AnimChannel>();
    private Node node;

    public CCharaterAnim() {
    }

    @Override
    public void setSpatial(Spatial spatial) {
        if (!(spatial instanceof Node)) {
            return;
        }
        node = (Node) spatial;

        super.setSpatial(spatial);



        Node child = (Node) node.getChild(0);
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
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (spatial == null) {
            return;
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        //if (animName.equals("Walk")) {
        //channel.setAnim("Walk");
        //}
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
    }
}
