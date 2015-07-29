/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surrealfish.net;

import arkhados.net.Ack;
import arkhados.net.OneTrueMessage;
import arkhados.net.OtmIdCommandListPair;
import com.jme3.network.serializing.Serializer;

public class DataRegistration {

    public static void register() {
        Serializer.registerClass(OneTrueMessage.class);
        Serializer.registerClass(OtmIdCommandListPair.class);
        Serializer.registerClass(Ack.class);
    }
}
