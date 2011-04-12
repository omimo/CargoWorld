// tell truck leave if availble weight is below this
truckMinWeight(5).

!init.
+!init <- 
	signIn;
	!clearAll;
	.print("bye...").
	
+!clearAll: onTop(Box) <- !clear; !clearAll.
+!clearAll.
-!clearAll <- .print("clear failed... continuing"); !clearAll.

//-------------------------------------
// update box info so that I can continue to work on other boxes
+!clear:
	workingOn(Box) &
	onTruck(Box)
	<-	
	.print("clear-update");
		reset(Box).

// see if I can help someone
+!clear:
	not workingOn(_) &
	helpWith(Box)
	<-
	.print("clear-help");
		!helpWith(Box).

// try lifting
+!clear: 
	not workingOn(_) &
	onTop(Box)
	<-	
	.print("clear-lift");
		!lifting.

// after lifted (will continue to work after box loaded on truck)
+!clear: 
	workingOn(BoxA) &
	lifted(BoxA)
	<-	
	.print("clear-post-lift");
		!boxLifted(BoxA).

// nothing to do in this cycle. wait
+!clear: onTop(_).
	

		
//-------------------------------------
// lift heavy box that is under my's capacity
+!lifting:
	capacity(_self,Capacity) &
	onTop(BoxA) &
	onTop(BoxB) &
	weight(BoxA,WeightA) &
	weight(BoxB,WeightB) &
	WeightA >= WeightB &
	WeightA <= Capacity
	<-	
	.print("lifting-2box");
		+workingOn(BoxA);
		+waitToMove(BoxA);
		lift(BoxA).

// lift any box that is under my capacity
+!lifting:
	capacity(_self,Capacity) &
	onTop(BoxA) &
	weight(BoxA,WeightA) &
	WeightA <= Capacity
	<-	
	.print("lifting-1box");
		+workingOn(BoxA);
		+waitToMove(BoxA);
		lift(BoxA).

// ask other's help
+!lifting: onTop(Box) 
	<-	
	.print("lifting-help");
		+workingOn(BoxA);
		lift(Box);
		.broadcast(tell, helpWith(Box)).



//-------------------------------------
// box I was working on got lifted

// I'm the first lifter / select truck
+!boxLifted(BoxA) : waitToMove(BoxA) &
	onSite(Truck) &
	askTruckCapacity(Truck) &
	truckAvailable(Truck, AvailableWeight) &
	weight(Box, BoxWeight) &
	BoxWeight <= AvailableWeight
	<-	
	.print("postLift-mover");
		!moving(Box, Truck).

// Other lifters waits
+!boxLifted(BoxA) <-
	.print("postLift-helper").

+!askTruckCapacity(Truck)
	<-	
	.print("postLift-capacity?");
		?truckAvailable(Truck, AvailableWeight).
		

//-------------------------------------
// moving box to truck
+!moving(Box, Truck) : not onSite(Truck)
	<-	
	.print("move-!onSite");
		.send(Truck, tell, move(_self, Box, Truck));
		.send(Truck, tell, come);
		moveAndDrop(Box, Truck).
		
+!moving(Box, Truck)
	<-	
	.print("move-onSite");
		.send(Truck, tell, move(_self, Box, Truck));
		moveAndDrop(Box, Truck).
	


//-------------------------------------
// others need help

// I'm available now
+!helpWith(Box) :
	not workingOn(_) &
	onTop(Box)
	<-	
	.print("help-availble");
		-helpWith(Box);
		+workingOn(Box);
		lift(Box).
		
// I'm busy. Offer help later
+!helpWith(Box)[source(Agent)]
	<-
	.print("help-busy").


//-------------------------------------
// reset the crane and move on

//tell truck to leave
+!reset(Box) : 
	waitToMove(Box) &
	onSite(Truck) &
	askTruckCapacity(Truck) &
	truckAvailableWeight(Truck, Weight) &
	truckMinWeight(MinWeight) &
	MinWeight >= Weight
	<-	.send(Truck, tell, leave);
		-waitToMove(Box);
		-workingOn(Box).

+!reset(Box) : waitToMove(Box)	
	<-	-waitToMove(Box);
		-workingOn(Box).
+!reset
	<-	-workingOn(Box).