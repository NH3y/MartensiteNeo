package com.gmail.orangeandy2007.martensite.martensiteneo.management.interfaces;

public interface chunkData {
    default boolean martensiteNeo$getSafeChunk(){return false;}

    void martensiteNeo$setSafeChunk(boolean boo);

    int hashCode();

    boolean equals(Object obj);
}
