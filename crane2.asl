truckMinWeight(5);

!init.
+!init <- 
	signIn;
	!clearAll; 
	.print("bye...").

//-------------------------------------
// update box info so that I can continue to work on other boxes
+!clearAll:
	workingOn(Box) &
	onTruck(Box)
	<-	reset(Box).

// see if I can help someone
+!clearAll:
	not workingOn(_) &
	helpWith(Box)
	<-	!helpWith(Box);
		clearAll.

// try lifting
+!clearAll: 
	not workingOn(_) &
	onTop(Box)
	<-	!lifting;
		!clearAll.

// after lifted (will continue to work after box loaded on truck)
+!clearAll: 
	workingOn(BoxA) &
	lifted(BoxA)
	<-	!boxLifted(BoxA).

+!clearAll.

// ???
// -!clearAll <- .print("clear failed... continuing"); !clearAll.


//-------------------------------------
// box I was working on got lifted

// I'm the first lifter / select truck
+!boxLifted(BoxA) : waitToMove(BoxA)	
	truck(Truck) &
	.send(Truck, tell, tellAvailable) &
	truckAvailable(Truck, AvailableWeight) &
	weight(Box, BoxWeight) &
	BoxWeight <= AvailableWeight
	<-	!moving(Box, Truck).

// Other lifters waits
+!boxLifted(BoxA).
		
		
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
	<-	+workingOn(BoxA);
		+waitToMove(BoxA);
		lift(BoxA).

// lift any box that is under my capacity
+!lifting:
	capacity(_self,Capacity) &
	onTop(BoxA) &
	weight(BoxA,WeightA) &
	WeightA <= Capacity
	<-	+workingOn(BoxA);
		+waitToMove(BoxA);
		lift(BoxA).

// ask other's help
+!lifting: onTop(Box) 
	<-	+workingOn(BoxA);
		lift(Box);
		.broadcast(tell, helpWith(Box)).



//-------------------------------------
// moving box to truck
+!moving(Box, Truck) : not onSite(Truck)
	<-	.send(Truck, tell, come);
		moveAndDrop(Box, Truck).
		
+!moving(Box, Truck)
	<-	moveAndDrop(Box, Truck).
	

//-------------------------------------
// others need help

// I'm available now
+!helpWith(Box) :
	not workingOn(_) &
	onTop(Box)
	<-	-helpWith(Box);
		+workingOn(Box);
		lift(Box).
		
// I'm busy. Offer help later
+!helpWith(Box)[source(Agent)].


//-------------------------------------
// reset the crane and move on

//tell truck to leave
+!reset(Box) : 
	waitToMove(Box) &
	.send(Truck, tell, tellAvailable) &
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