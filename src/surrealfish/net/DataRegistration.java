/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package surrealfish.net;

import arkhados.net.Ack;
import arkhados.net.OneTrueMessage;
import arkhados.net.OtmIdCommandListPair;
import com.jme3.network.serializing.Serializer;
import surrealfish.entity.ProjectileCreator;
import surrealfish.entity.TestCharacterCreator;
import surrealfish.net.commands.CmdClientLogin;
import surrealfish.net.commands.CmdServerLogin;
import surrealfish.net.commands.CmdSetPlayersCharacter;
import surrealfish.net.commands.CmdTopicOnly;
import surrealfish.net.commands.sync.CmdAddEntity;
import surrealfish.net.commands.sync.StateData;

public class DataRegistration {

    public static void register() {
        Serializer.registerClass(OneTrueMessage.class);
        Serializer.registerClass(OtmIdCommandListPair.class);
        Serializer.registerClass(Ack.class);

        Serializer.registerClass(CmdTopicOnly.class);
        Serializer.registerClass(CmdServerLogin.class);
        Serializer.registerClass(CmdSetPlayersCharacter.class);
        Serializer.registerClass(CmdClientLogin.class);

        Serializer.registerClass(CmdAddEntity.class);
        Serializer.registerClass(StateData.class);

        Serializer.registerClass(
                TestCharacterCreator.TestCharacterStateData.class);
        Serializer.registerClass(ProjectileCreator.ProjectileStateData.class);
    }
}
