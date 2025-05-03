# Martensite Entity Skipper - Neoforge version
A performace mod aims at Minecraft entity optimizations
## Features
1. Enhanced version of nearestPlayer to get the nearest player to the certain entity
2. Cancel entity tick event according to the distance form entity to player
3. Cramp experience orb entity together to reduce entity count
4. An additional experience orb optimization to aborb them as fast as possible
5. Reduce item layers to avoid unnecessary item rendering
6. Increasing expected value of despawn rate for a group of closing entity, also known as Group Unload
## Partial Reason
1. getting the distance from nearest player is an important function used to calculate the cancel rate of entity tick. If the calculations cost too much resources, the mod will be useless
2. Player won't be able to see most entities far away unless they stand on the high ground with a high view distance
3. Experience orbs separated once their value didn't match any of the prescribed value which only enhance visual effect
4. Just an additional feature helps a little
5. Player barely care about the layer count of item, and they can't actually tell how many items mean by the layer count
6. Simple approach to avoid a stack of entity that can't be despawn
## Cautions
1. Skip ticks will also slow down aging and moving
2. fast experience absorb will cause the value of orbs to varying at a small error

And due to the problem above, there some solution other than waiting for update. 
First, there is a chunk register system you can access through command **martensite-chunk** which allows you to mark certain chunks with a name.
The chuncks that have been marked will be excluded to the skip system and group unload mechanism, and thus won't be afffected.

**Be careful! Marking Chunks will NOT Forceload them**

As for the fast absorb feature, currently don't have a solution, is close on default.
Also this feature might soon become unnecessary due to the cramping advancement
## Future Feature
2. AI optimizations as a less aggressive alternative to skipping ticks
3. More specific entity optimizations
