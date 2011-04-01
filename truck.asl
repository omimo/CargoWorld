// haven't come to the site yet
left.

// total capacity
capacity(100).

// capacity left (can be negative if overweighted)
available(100).

//-------------------------------
//	Incoming messages
//-------------------------------

+tellCapacity[source(Sender)]
	<-	.send(Sender,tell,capacity(A)).
	
+tellAvailable[source(Sender)]
	<-	.send(Sender,tell,available(A)).
	
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
+!move(Crane, BoxName, Truck) : .my_name(Name)
	<-	weight(BoxName, Weight);
		!load(BoxName, Weight, Crane).
	
//-------------------------------
//	Plans
//-------------------------------

+!leave(Sender) : available(N) & N < 0
	<-	.send(Sender,tell,overweight).
	
+!leave(Sender) : true
	<-	+left;
		-free;
		.send(Sender,tell,leaveAccepted).

// is this ever happen?
+!load(Box, Weight, Sender) : not free
	<-	true.
//	<-	.send(Sender, tell, busy).

+!load(Box, Weight, Sender) : true
	<-	-available(N);
		+available(N - Weight);
		+loaded(Box, Weight).
//		.send(Sender, tell, loadSuccessful(Box)).
		
		
+!unload(Box, Sender) : not free
	<-	.send(Sender, tell, busy).
		
+!unload(Box, Sender) : true
	<-	loaded(Box,Weight);
		-available(N);
		+available(N + Weight).