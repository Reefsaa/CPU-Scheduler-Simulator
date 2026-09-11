package osprojct;

	class PCB {
	    enum ProcessState { NEW, READY, RUNNING, TERMINATED }

	    int processId;
	    ProcessState state = ProcessState.NEW;

	    int burstTime;
	    int remainingTime;
	    int priority;
	    int originalPriority;
	    int memoryRequired;

	    int arrivalTime = 0;       // All processes arrive at time 0 according to the new project
	    int arrivalOrder;          // Used for tie-breaking by order in the input file

	    int startTime = -1;
	    int completionTime;
	    int waitingTime;
	    int turnaroundTime;

	    boolean starved = false;

	    public PCB(int id, int burst, int prio, int memory) {
	        this.processId = id;
	        this.burstTime = burst;
	        this.remainingTime = burst;
	        this.priority = prio;
	        this.originalPriority = prio;
	        this.memoryRequired = memory;
	        this.arrivalOrder = id;
	    }
	}

