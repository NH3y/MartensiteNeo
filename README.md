# Martensite Entity Skipper - Neoforge version
A performace mod aims at Minecraft entity optimizations
## Features
1. Enhanced version of nearestPlayer to get the nearest player to the certain entity
2. Cancel entity tick event according to the distance form entity to player
3. Cramp experience orb entity together to reduce entity count
4. An additional experience orb optimization to aborb them as fast as possible
## Cautions
1. Skip ticks will also slow down aging and moving
2. fast experience absorb will cause the value of orbs to varying at a small error
And due to the problem above, there some solution other than waiting for update
First, there is a chunk register system you can access through command **martensite-chunk** which allows you to mark certain chunks with a name
The chuncks that have been marked will be excluded to the skip system, and thus won't be afffected

**Be careful! Marking Chunks will NOT Forceload them**

As for the fast absorb feature, currently don't have a solution, is close on default
## Future Feature
1. Group Unload from other Martensite version
2. AI optimizations as a less aggressive alternative to skipping ticks
3. More specific entity optimizations
