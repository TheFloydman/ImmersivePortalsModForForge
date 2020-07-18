package com.qouteall.immersive_portals.alternate_dimension;

import com.mojang.serialization.Codec;
import net.minecraft.util.FastRandom;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.FuzzedBiomeMagnifier;
import net.minecraft.world.biome.provider.BiomeProvider;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class ChaosBiomeSource extends BiomeProvider {
    private Biome[] biomeArray;
    private long worldSeed;
    
    public ChaosBiomeSource(long seed) {
        super(Registry.BIOME.stream().collect(Collectors.toList()));
        
        worldSeed = seed;
        
        biomeArray = Registry.BIOME.stream()
            .toArray(Biome[]::new);
    }
    
    private Biome getRandomBiome(int x, int z) {
        int biomeNum = biomeArray.length;
        
        int index = (Math.abs((int) FastRandom.mix(x, z))) % biomeNum;
        return biomeArray[(int) index];
    }
    
    @Override
    public Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        return FuzzedBiomeMagnifier.INSTANCE.getBiome(
            worldSeed, biomeX / 2, 0, biomeZ / 2,
            (x, y, z) -> getRandomBiome(x, z)
        );
    }
    
    @Override
    protected Codec<? extends BiomeProvider> func_230319_a_() {
        return null;
    }
    
    @Override
    public BiomeProvider func_230320_a_(long seed) {
        worldSeed = seed;
        return this;
    }
}
