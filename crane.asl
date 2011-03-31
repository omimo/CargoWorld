!init.
/*
	.broadcast(tell, hello).
	+hello[source(A)] <- .print("Received hello from ", A).
*/

+!init: true <-	signIn; !clearAllBoxes; .print("bye...").

+!clearAllBoxes: onTop(Box) <- !clearBox; !clearAllBoxes.
+!clearAllBoxes.

+!clearBox:
	onTop(Box)&
	weight(Box,Weight)&
	capacity(_self,Capacity)&
	Weight <= Capacity<-
		.print("lifting box");
		lift(Box,_).

+!clearBox:
	onTop(Box)&
	weight(Box,Weight)&
	capacity(_self,Capacity)<-
		+committedTo(Box);
		+remaining(Weight-Capacity);
		.print("Requesting Help");
		.broadcast(tell, requestHelp(Box)).

+!clearBox: onTop(Box) <- .print("Do nothing").
+!clearBox.

+!requestHelp(Box)[source(Requester)]: 
	not committed(_) <-
		+committedTo(Box);
		.print("Offering Help");
		.send(Requester,tell,offerHelp).

+!offerHelp[source(Friend)]: 
	remaining(Remaining)&
	capacity(Friend,Capacity)&
	Capacity <= Remaining <-
		.print("Ack Help");
		+friend(Friend);
		-remaining(Remaining);
		+remaining(Remaining-Capacity).

+!offerHelp[source(Friend)]:
	committedTo(Box) <-
		.print("Start Processesing Help");
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

+!processhelp[source(Reqester)] <-
	-committedTo(Box);
	lift(Box).
