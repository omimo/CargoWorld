!init.
/*
	.broadcast(tell, hello).
	+hello[source(A)] <- .print("Received hello from ", A).
*/

+!init: true <-	signIn; !clearAllBoxes; .print("bye...").

+!clearAllBoxes: onTop(_) <- !clearBox; !clearAllBoxes.
+!clearAllBoxes<-.print("...hi").

+!clearBox:
	onTop(Box)&
	weight(Box,Weight)&
	capacity(self,Capacity)&
	Weight <= Capacity<-
		lift(Box,_).

+!clearBox:
	onTop(Box)&
	weight(Box,Weight)&
	capacity(self,Capacity)<-
		+committedTo(Box);
		+remaining(Weight-Capacity);
		.broadcast(tell, requestHelp(Box)).

+!clearBox.

+!requestHelp(Box)[source(Requester)]: 
	not committed(_) <-
		+committedTo(Box);
		.send(Requester,tell,offerHelp).

+!offerHelp[source(Friend)]: 
	remaining(Remaining)&
	capacity(Friend,Capacity)&
	Capacity <= Remaining <-
		+friend(Friend);
		-remaining(Remaining);
		+remaining(Remaining-Capacity).

+!offerHelp[source(Friend)]:
	committedTo(Box) <-
		?processFriends;
		.send(Friend, tell, processHelp);
		lift(Box,_);
		-committedTo(_);
		-remaining(_).

+!processFriends:
	friend(Friend) <-
		.send(Friend, tell, processHelp);
		-friend(Friend);
		?processFriends.

