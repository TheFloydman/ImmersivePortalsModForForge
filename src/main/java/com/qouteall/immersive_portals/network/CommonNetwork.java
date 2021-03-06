package com.qouteall.immersive_portals.network;

import com.qouteall.hiding_in_the_bushes.MyNetwork;
import com.qouteall.immersive_portals.McHelper;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;

// common between Fabric and Forge
public class CommonNetwork {
    
    @Nullable
    public static RegistryKey<World> forceRedirect = null;
    
    public static boolean getIsProcessingRedirectedMessage() {
        return CommonNetworkClient.isProcessingRedirectedMessage;
    }
    
    public static void withForceRedirect(RegistryKey<World> dimension, Runnable func) {
        Validate.isTrue(McHelper.getServer().getExecutionThread() == Thread.currentThread());
        
        RegistryKey<World> oldForceRedirect = forceRedirect;
        forceRedirect = dimension;
        try {
            func.run();
        }
        finally {
            forceRedirect = oldForceRedirect;
        }
    }
    
    /**
     * If it's not null, all sent packets will be wrapped into redirected packet
     * {@link com.qouteall.immersive_portals.mixin.common.entity_sync.MixinServerPlayNetworkHandler_E}
     */
    @Nullable
    public static RegistryKey<World> getForceRedirectDimension() {
        return forceRedirect;
    }
    
    // avoid duplicate redirect nesting
    public static void sendRedirectedPacket(
        ServerPlayNetHandler serverPlayNetworkHandler,
        IPacket<?> packet,
        RegistryKey<World> dimension
    ) {
        if (getForceRedirectDimension() == dimension) {
            serverPlayNetworkHandler.sendPacket(packet);
        }
        else {
            serverPlayNetworkHandler.sendPacket(
                MyNetwork.createRedirectedMessage(
                    dimension,
                    packet
                )
            );
        }
    }
}
