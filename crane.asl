/* Initial goal is to clear all boxes */
!clearAll.

/* Signin (else fail) and clear boxes */
+!clearAll: true <- signIn; !doWork.
+!clearAll: true <- .print("Signin Rejected").

/* Not committed yet and box is too heavy therefore reqest help */
+!doWork:
	not committedTo(_) &
	onTop(Box) & 
	weight(Box, WeightA) & 
	capacity(self, WeightB) &
	WeightA > WeightB <-
		+committedTo(Box);
		+remaining(WeightA-WeightB);
		.broadcast(tell, help(Box)).

/* Work on what you can */
+!doWork: 
	onTop(Box) & 
	weight(Box, WeightA) & 
	capacity(self, WeightB) &
	WeightA <= WeightB <- 
		lift(Box).

/* Box on top but too heavy and already committed */
+!doWork: 
	onTop(Box) & 
	weight(Box, WeightA) & 
	capacity(self, WeightB) &
	WeightA > WeightB <-
		.print("Waiting on help").

/* no Box's should be cleared */
+!doWork: noTop(Any) <- .print("huh? ...").
+!doWork: true <- .print("bye ...").

/* Someone is willing to help and we have enough capasity */
+!help(Box)[source(Agent)]:
	committedTo(Box) &
	capacity(Agent, Capacity) &
	remaining(Remaining) &
	Capacity >= Remaining <-
		-remaining(Remaining);
		-committedTo(Box);
		lift(Box).

/* Someone is willing to help and we do not have enough capasity */
+!help(Box)[source(Agent)]:
	committedTo(Box) &
	capacity(Agent, Capacity) &
	remaining(Remaining) &
	Capacity < Remaining <-
		-remaining(Remaining);
		+remaining(Remaining-Capacity).

