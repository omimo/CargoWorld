//TODO: handle overweight situation




// haven't come to the site yet
left.

// total capacity
truckCapacityWeight(100).

// capacity left (can be negative if overweighted)
truckAvailableWeight(100).

// Signing in - added by Omid
!init.
+!init 
	<-	?truckCapacityWeight(Weight);
		signIn(Weight).

//-------------------------------
//	Incoming messages
//-------------------------------

+tellCapacity[source(Sender)]
	<-	.send(Sender,tell,truckCapacityWeight(_self, A)).
	
+tellAvailable[source(Sender)]
	<-	.send(Sender,tell,truckAvailableWeight(_self, A)).
	
// tell me to come to site
+come[source(Sender)]
	<-	+free;
		-left;
		.send(Sender,tell,came).

// tell me to leave the site
+!leave[source(Sender)]
	<-	!leave(source(Sender)).

//-------------------------------
//	Communication with Env
//-------------------------------

// listen to the environment and move box to this truck
//+!move(Crane, BoxName, _self) : .my_name(Name)
//	<-	weight(BoxName, Weight);
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