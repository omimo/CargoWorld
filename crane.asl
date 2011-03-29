// Agent Crane in project CargoWorld.mas2j

/* Initial beliefs and rules */

/* Initial goals */
!signIn.
!clearAll.

/* Plans */
+!signIn: true <- signIn.

+!clearAll: onTop(Box) <- !clear; !clearAll.
+!clearAll: true <- .print("Done").

+!clear: lifting(Agent,Box) <- lift(Box,_).
+!clear: onTop(BoxA) & onTop(BoxB) & weight(BoxA, WeightA) & weight(BoxB, WeightB) & WeightA > WeightB <- lift(BoxA,_).
+!clear: onTop(Box) <- lift(Box,_).

