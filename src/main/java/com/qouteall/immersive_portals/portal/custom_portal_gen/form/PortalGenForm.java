package com.qouteall.immersive_portals.portal.custom_portal_gen.form;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import com.qouteall.immersive_portals.portal.custom_portal_gen.CustomPortalGeneration;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.server.ServerWorld;
import java.util.function.Function;

public abstract class PortalGenForm {
    public static final Registry<Codec<? extends PortalGenForm>> codecRegistry = Util.make(() -> {
        SimpleRegistry<Codec<? extends PortalGenForm>> registry = new SimpleRegistry<>(
            RegistryKey.func_240904_a_(new ResourceLocation("imm_ptl:custom_portal_gen_form")),
            Lifecycle.stable()
        );
        
        Registry.register(
            registry, new ResourceLocation("imm_ptl:classical"), ClassicalForm.codec
        );
        Registry.register(
            registry, new ResourceLocation("imm_ptl:heterogeneous"), HeterogeneousForm.codec
        );
        Registry.register(
            registry, new ResourceLocation("imm_ptl:flipping_floor_square"), FlippingFloorSquareForm.codec
        );
        
        return registry;
    });
    
    public static final Codec<PortalGenForm> codec = codecRegistry.dispatchStable(
        PortalGenForm::getCodec, Function.identity()
    );
    
    public abstract Codec<? extends PortalGenForm> getCodec();
    
    public abstract PortalGenForm getReverse();
    
    // Return true for succeeded
    public abstract boolean perform(
        CustomPortalGeneration cpg,
        ServerWorld fromWorld, BlockPos startingPos,
        ServerWorld toWorld
    );
    
    public boolean initAndCheck() {
        return true;
    }
}