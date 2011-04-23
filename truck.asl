//TODO: handle overweight situation
//truckArrive
//truckLeave

// total capacity (not being used)
truckCapacityWeight(100).

// capacity left (can be negative if overweighted)
truckAvailableWeight(100).

// Signing in - added by Omid
!init.
+!init 
	<-	?truckCapacityWeight(Weight);
		 signIn(Weight);
		?truckAvailableWeight(A);
		+free;
		 truckArrive;
		.broadcast(tell, truckAvailableCapacity(A)).
		
//-------------------------------
//	Incoming Messages
//-------------------------------		
		 
+!kqml_received(Crane,tell,move(_,Box,Me),_)
	<-	?weight(BoxName, Weight);
		!load(BoxName, Weight, Crane).
		
//-------------------------------
//	Plans
//-------------------------------

+!leave : truckAvailableWeight(A) & A <= 0
	<-	.broadcast(tell,overloaded(A * (-1))).

+!leave : 
	onTop(Box) &
	weight(Box,Weight) &
	truckAvailableWeight(A) &
	Weight <= A
	<-	-free;
		 truckLeave;
		 truckArrive.
		
+!leave : 
	lifted(Box) &
	weight(Box,Weight) &
	truckAvailableWeight(A) &
	Weight <= A
	<-	-free;
		 truckLeave;
		 truckArrive.
		
+!leave : 
	onTop(Box) &
	weight(Box,Weight) &
	truckAvailableWeight(A) &
	Weight <= A
	<-	-free;
		 truckLeave;
		 truckArrive.

+!load(Box, Weight, Sender) : free
	<-	-truckAvailableWeight(N);
		+truckAvailableWeight(N - Weight);
		.broadcast(tell, truckAvailableCapacity(N - Weight));
		+loaded(Box, Weight)
		!leave.
		
+!unload(Box) : free
	<-	?loaded(Box,Weight);
		-truckAvailableWeight(N);
		+truckAvailableWeight(N + Weight);
		.broadcast(tell, truckAvailableCapacity(N + Weight)).
		
