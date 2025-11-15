package org.gumrockets.entity;

import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class EntitySettings {
    public String path;
    public SpawnGroup spawnGroup;
    public TagKey<Biome> selectorTag;
    public float x;
    public float y;
    public int spawnWeight;
    public int minGroupSize;
    public int maxGroupSize;
    public boolean spawnsNaturally = false;
    public boolean tracksVelocity;

    public EntitySettings(String path, SpawnGroup spawnGroup, TagKey<Biome> selectorTag, float x, float y, int spawnWeight, int minGroupSize, int maxGroupSize) {
        this.path = path;
        this.spawnGroup = spawnGroup;
        this.selectorTag = selectorTag;
        this.x = x;
        this.y = y;
        this.spawnWeight = spawnWeight;
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
        this.spawnsNaturally = true;
    }

    public EntitySettings(String path, SpawnGroup spawnGroup, float x, float y) {
        this.path = path;
        this.spawnGroup = spawnGroup;
        this.x = x;
        this.y = y;
    }

    public EntitySettings(String path, SpawnGroup spawnGroup, float x, float y, boolean tracksVelocity) {
        this.path = path;
        this.spawnGroup = spawnGroup;
        this.x = x;
        this.y = y;
        this.tracksVelocity = tracksVelocity;
    }
}
