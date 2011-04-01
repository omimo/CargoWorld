!init.
+!init <- signIn; !clearAll; .print("bye...").

+!clearAll: onTop(Box) <- !clear; !clearAll.
+!clearAll.
-!clearAll <- .print("clear failed... continuing"); !clearAll.

+!clear:
	capacity(_self,Capacity)&
	onTop(BoxA)&
	onTop(BoxB)&
	weight(BoxA,WeightA)&
	weight(BoxB,WeightB)&
	WeightA >= WeightB&
	WeightA <= Capacity <-
		lift(BoxA,_).

+!clear:
	onTop(Box)&
	capacity(_self,Capacity)&
	weight(Box,Weight)&
	Weight <= Capacity <-
		lift(Box,_).

+!clear: onTop(Box)<-lift(Box,_).
-!clear: .print("lift failed ... but how?").
