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

@EventBusSubscriber
public class martensiteChunk {

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("martensite-chunk")

                .then(Commands.literal("add").then(Commands.argument("name", StringArgumentType.word()).then(Commands.argument("Pos", BlockPosArgument.blockPos()).then(Commands.argument("Pos2", BlockPosArgument.blockPos()).executes(arguments -> {
                    System.out.println("WoW Done!");

                    Level world = arguments.getSource().getUnsidedLevel();
                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);
                    String name = arguments.getArgument("name",String.class);
                    BlockPos initial = arguments.getArgument("Pos", WorldCoordinates.class).getBlockPos(arguments.getSource());
                    BlockPos end = arguments.getArgument("Pos2", WorldCoordinates.class).getBlockPos(arguments.getSource());
                    int chunkX1 = world.getChunkAt(initial).getPos().x;
                    int chunkZ1 = world.getChunkAt(initial).getPos().z;
                    int chunkX2 = world.getChunkAt(end).getPos().x;
                    int chunkZ2 = world.getChunkAt(end).getPos().z;
                    System.out.println("Variable Done!");
                    for(int i=chunkX1;i<=chunkX2;i++){
                        for(int j=chunkZ1;j<=chunkZ2;j++){
                            int[] value = new int[]{i,j};
                            System.out.println("Value Done!");
                            ChunkRegister.addChunk(value, name + "_" + i + "_" + j, entity);
                            System.out.println("Chunk Done!");
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
                    ChunkRegister.addChunk(chunk,name,entity);
                    System.out.println("Chunk Done!");

                    return 0;
                })).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    String name = arguments.getArgument("name",String.class);

                    if (entity != null) {
                        ChunkRegister.addChunk(entity, name);
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

                    ChunkRegister.rename(chunk, name, entity);
                    return 0;
                })))).then(Commands.literal("remove").then(Commands.argument("Pos", BlockPosArgument.blockPos()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    LevelChunk chunk = world.getChunkAt(arguments.getArgument("Pos",WorldCoordinates.class).getBlockPos(arguments.getSource()));

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    ChunkRegister.remove(chunk,entity);
                    return 0;
                }))).then(Commands.literal("clear").executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    ChunkRegister.clear(entity);
                    return 0;
                })).then(Commands.literal("search").then(Commands.argument("name", StringArgumentType.word()).executes(arguments -> {
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    String name = arguments.getArgument("name",String.class);

                    ChunkRegister.search(entity, name);
                    return 0;
                }))).then(Commands.literal("get").then(Commands.argument("Pos", BlockPosArgument.blockPos()).executes(arguments ->{
                    Level world = arguments.getSource().getUnsidedLevel();

                    Entity entity = arguments.getSource().getEntity();
                    if (entity == null && world instanceof ServerLevel _servLevel)
                        entity = FakePlayerFactory.getMinecraft(_servLevel);

                    LevelChunk chunk = world.getChunkAt(arguments.getArgument("Pos", WorldCoordinates.class).getBlockPos(arguments.getSource()));
                    ChunkRegister.get(chunk, entity);
                    return 0;
                }))));
    }

}