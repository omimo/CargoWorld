// Agent Crane in project CargoWorld.mas2j

/* Initial beliefs and rules */

/* Initial goals */
!signIn.


/* Plans */
+!signIn: true <- signIn;!clearAll.

+!clearAll: onTop(Box) <- !clear; !clearAll.
+!clearAll: true <- .print("Clear All Done").

+!clear: lifting(Agent,Box) <- lift(Box,_).
+!clear: onTop(BoxA) & onTop(BoxB) & weight(BoxA, WeightA) & weight(BoxB, WeightB) & WeightA > WeightB <- lift(BoxA,_).
+!clear: onTop(Box) <- lift(Box,_).
+!clear: true <- .print("Clear Done").
