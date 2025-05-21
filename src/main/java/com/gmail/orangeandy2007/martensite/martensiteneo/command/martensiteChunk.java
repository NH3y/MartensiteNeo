package com.gmail.orangeandy2007.martensite.martensiteneo.command;

/* imports omitted */

import com.gmail.orangeandy2007.martensite.martensiteneo.management.ChunkRegister;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@EventBusSubscriber
public class martensiteChunk {

    @SubscribeEvent
    public static void registerCommand(@NotNull RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("martensite-chunk")

                .then(Commands.literal("add").then(Commands.argument("name", StringArgumentType.word()).then(Commands.argument("Pos", BlockPosArgument.blockPos()).then(Commands.argument("Pos2", BlockPosArgument.blockPos()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    String name = arguments.getArgument("name",String.class);
                    BlockPos initial = arguments.getArgument("Pos", WorldCoordinates.class).getBlockPos(arguments.getSource());
                    BlockPos end = arguments.getArgument("Pos2", WorldCoordinates.class).getBlockPos(arguments.getSource());
                    LevelChunk chunkA = world.getChunkAt(initial);
                    LevelChunk chunkB = world.getChunkAt(end);

                    int[] X = Arrays.stream(new int[]{chunkB.getPos().x,chunkA.getPos().x}).sorted().toArray();
                    int[] Z = Arrays.stream(new int[]{chunkB.getPos().z,chunkA.getPos().z}).sorted().toArray();
                    int i;
                    int j;
                    for(i = X[0]; i <= X[1]; i++){
                        for(j = Z[0]; j <= Z[1]; j++){
                            int[] value = new int[]{i,j};
                            ChunkRegister.addChunk(world,value, name + "_" + i + "_" + j, entity);
                        }
                    }

                    return 0;
                })).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    LevelChunk chunk = world.getChunkAt(arguments.getArgument("Pos",WorldCoordinates.class).getBlockPos(arguments.getSource()));
                    String name = arguments.getArgument("name",String.class);
                    ChunkRegister.addChunk(world,chunk,name,entity);
                    System.out.println("Chunk Done!");

                    return 0;
                })).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    String name = arguments.getArgument("name",String.class);

                    if (entity != null) {
                        ChunkRegister.addChunk(world,entity, name);
                    }
                    System.out.println("Chunk Done!");

                    return 0;
                }))).then(Commands.literal("rename").then(Commands.argument("Pos", BlockPosArgument.blockPos()).then(Commands.argument("name", StringArgumentType.word()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    LevelChunk chunk = world.getChunkAt(arguments.getArgument("Pos",WorldCoordinates.class).getBlockPos(arguments.getSource()));
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    String name = arguments.getArgument("name", String.class);

                    ChunkRegister.rename(world, chunk, name, entity);
                    return 0;
                })))).then(Commands.literal("remove").then(Commands.argument("Pos", BlockPosArgument.blockPos()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    LevelChunk chunk = world.getChunkAt(arguments.getArgument("Pos",WorldCoordinates.class).getBlockPos(arguments.getSource()));

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    ChunkRegister.remove(world, chunk,entity);
                    return 0;
                }))).then(Commands.literal("clear").executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    ChunkRegister.clear(world, entity);
                    return 0;
                })).then(Commands.literal("search").then(Commands.argument("name", StringArgumentType.word()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    String name = arguments.getArgument("name",String.class);

                    ChunkRegister.search(world, entity, name);
                    return 0;
                }))).then(Commands.literal("get").then(Commands.argument("Pos", BlockPosArgument.blockPos()).executes(arguments ->{
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    if(world instanceof ServerLevel level ) {
                        LevelChunk chunk = level.getChunkAt(arguments.getArgument("Pos", WorldCoordinates.class).getBlockPos(arguments.getSource()));
                        ChunkRegister.get(chunk, entity);
                    }
                    return 0;
                }))));
    }

}