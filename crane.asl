// Agent Crane in project CargoWorld.mas2j

/* Initial beliefs and rules */

/* Initial goals */

!start.

/* Plans */

+!start : true <- .print("Hi").

+onTop(B) : true <- move(B,truck1).  // Just to test
