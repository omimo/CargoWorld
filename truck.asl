//TODO: handle overweight situation




// haven't come to the site yet
left.

// Signing in - added by Omid
!init.
+!init <-
		+truckAvailableWeight(100);	// capacity left (can be negative if overweighted)
		+truckCapacityWeight(100); // total capacity
		signIn(100);truckArrive.

//-------------------------------
//	Incoming messages
//-------------------------------

+tellCapacity[source(Sender)]
	<-	?truckAvailableWeight(A);
		.send(Sender,tell,truckCapacityWeight(A)).
	
+tellAvailable[source(Sender)]
	<-	.print("msgRecv");
		+beingAsked(Sender);
		?truckAvailableWeight(A);
		.send(Sender,tell,truckAvailableWeight(A)).
	
// tell me to come to site
+come[source(Sender)]
	<-	+free;
		-left;
		.send(Sender,tell,came).

// tell me to leave the site
+!leave[source(Sender)]
	<-	!leave(source(Sender)).
	
+!kqml_received(Crane,tell,move(_,Box,Me),_) : beingAsked(Crane)
	<-	?weight(BoxName, Weight);
		-beingAsked(_);
		!load(BoxName, Weight, Crane).

//-------------------------------
//	Communication with Env
//-------------------------------

// listen to the environment and move box to this truck
//+!move(Crane, BoxName, Name) : .my_name(Name)
//	<-	?weight(BoxName, Weight);
//		!load(BoxName, Weight, Crane).
	
//-------------------------------
//	Plans
//-------------------------------

+!leave(Sender) : truckAvailableWeight(N) & N < 0
	<-	.send(Sender,tell,overweight).
	
+!leave(Sender) : true
	<-	+left;
		-free;
		.send(Sender,tell,leaveAccepted).


+!load(Box, Weight, Sender) : not free
	<-	true.
//	<-	.send(Sender, tell, busy).	// removed because it seems impossible to happen

+!load(Box, Weight, Sender) : true
	<-	-truckAvailableWeight(N);
		+truckAvailableWeight(N - Weight);
		+loaded(Box, Weight).
//		.send(Sender, tell, loadSuccessful(Box)).	// removed because it seems impossible to happen
		
		
 +!unload(Box, Sender) : not free
	<-	.send(Sender, tell, busy).
		
 +!unload(Box, Sender) : true
	<-	loaded(Box,Weight);
		-truckAvailableWeight(N);
		+truckAvailableWeight(N + Weight).