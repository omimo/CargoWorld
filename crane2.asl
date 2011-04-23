!init.
+!init <- 
	signIn;
	!clearAll;
	.print("bye...").

+!clearAll: onTop(Box) <- !clear; !clearAll.
//+!clearAll.
-!clearAll <- !clearAll.

//-------------------------------------
// update box info so that I can continue to work on other boxes
+!clear:
	workingOn(Box) &
	onTruck(Box)
	<-	
		.print("clear-update");
		!reset(Box).

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
	onTop(Box) & 
	not lifted(Box)
	<-	
		.print("clear-lift");
		!lifting.

// after lifted (will continue to work after box loaded on truck)
+!clear: 
	workingOn(BoxA) &
	lifted(BoxA)
	<-	
		!boxLifted(BoxA).

// nothing to do in this cycle. wait
+!clear: onTop(_).

+!clear
	<-.print("???????? No more boxes?").


//-------------------------------------
// lift light box that is under my's capacity

+!lifting:
	.my_name(Name) &
	capacity(Name,Capacity) &
	onTop(BoxA) &
	onTop(BoxB) &
	not (BoxA == BoxB) &
	weight(BoxA,WeightA) &
	weight(BoxB,WeightB) &
	WeightA <= WeightB &
	WeightA <= Capacity
	<-	
	.print("lifting-2box");.print(Capacity);.print(BoxA);.print(WeightA);
		+workingOn(BoxA);
		+waitToMove(BoxA);
		 lift(BoxA).

// lift any box that is under my capacity
+!lifting:
	.my_name(Name) &
	capacity(Name,Capacity) &
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
+!boxLifted(Box) :
	waitToMove(Box)  &
	onSite(Truck) &
	truck(Truck, AvailableWeight) &
	weight(Box, BoxWeight) &
	AvailableWeight >= BoxWeight
		<- 
			!moving(Box, Truck).
	
// Other lifters waits
+!boxLifted(BoxA).

//-------------------------------------
//Incoming Messages
//-------------------------------------

+!kqml_received(Truck,tell,truckAvailableWeight(AvailableWeight),_) 
	<-
		-truck(Truck, _);
		+truck(Truck, AvailableWeight).

//-------------------------------------
// moving box to truck
+!moving(Box, Truck) : onSite(Truck)
	<-	
	.my_name(Name);
	.print("move-!onSite");
		.send(Truck, tell, move(Name, Box, Truck));
		 moveAndDrop(Box, Truck);

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
+!helpWith(Box)
	<-
	.print("help-busy").


//-------------------------------------
// reset the crane and move on

+!reset(Box) : 
	<-	.print("reset");
		-waitToMove(Box);
		-workingOn(Box).
