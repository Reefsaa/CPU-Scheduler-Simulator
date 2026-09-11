package osprojct;


	import java.util.ArrayList;
	import java.util.LinkedList;
	import java.util.Queue;

	class Scheduler {
	    private LinkedList<PCB> readyQueue;
	    private ArrayList<PCB> completed;
	    private SystemCalls systemCalls;
	    private int totalProcesses;
	    private int time = 0;

	    public Scheduler(LinkedList<PCB> readyQueue, ArrayList<PCB> completed,
	                     SystemCalls systemCalls, int totalProcesses) {
	        this.readyQueue = readyQueue;
	        this.completed = completed;
	        this.systemCalls = systemCalls;
	        this.totalProcesses = totalProcesses;
	    }

	    // SJF is non-preemptive. If CPU bursts are equal, choose by arrival order.
	    public void sjf() {
	        time = 0;
	        ArrayList<PCB> local = new ArrayList<>();
	        System.out.println("\n[Time " + time + "ms] === SJF Scheduling Started ===");

	        while (completed.size() < totalProcesses) {
	            moveProcesses(local);

	            if (local.isEmpty()) {
	                sleepALittle();
	                continue;
	            }

	            local.sort((a, b) -> {
	                if (a.burstTime != b.burstTime) {
	                    return a.burstTime - b.burstTime;
	                }
	                return a.arrivalOrder - b.arrivalOrder;
	            });

	            PCB current = local.remove(0);
	            executeProcess(current);
	        }

	        System.out.println("[Time " + time + "ms] === SJF Scheduling Completed ===");
	    }

	    // RR uses quantum = 5 ms in the new project.
	    public void roundRobin() {
	        time = 0;
	        int quantum = 5;
	        Queue<PCB> localRQ = new LinkedList<>();
	        System.out.println("\n[Time " + time + "ms] === Round-Robin Scheduling Started (Quantum: " + quantum + "ms) ===");

	        while (completed.size() < totalProcesses) {
	            synchronized (readyQueue) {
	                while (!readyQueue.isEmpty()) {
	                    localRQ.add(readyQueue.remove());
	                }
	            }

	            if (localRQ.isEmpty()) {
	                sleepALittle();
	                continue;
	            }

	            PCB current = localRQ.poll();

	            if (current.startTime == -1) {
	                current.startTime = time;
	                systemCalls.setTime(time);
	                systemCalls.createProcess(current.processId);
	            }

	            current.state = PCB.ProcessState.RUNNING;

	            int startRemaining = current.remainingTime;
	            int execTime = Math.min(quantum, current.remainingTime);
	            int stopRemaining = current.remainingTime - execTime;

	            System.out.println("[Time " + time + "ms - " + (time + execTime) + "ms] P" + current.processId +
	                    " executed, burst remaining: " + startRemaining + " -> " + stopRemaining);

	            time += execTime;
	            current.remainingTime -= execTime;

	            if (current.remainingTime == 0) {
	                finishProcess(current);
	            } else {
	                current.state = PCB.ProcessState.READY;
	                localRQ.add(current);
	            }
	        }

	        System.out.println("[Time " + time + "ms] === Round-Robin Scheduling Completed ===");
	    }

	    // Priority is non-preemptive. Smaller priority number means higher priority.
	    public void priority() {
	        time = 0;
	        ArrayList<PCB> local = new ArrayList<>();
	        System.out.println("\n[Time " + time + "ms] === Priority Scheduling Started ===");

	        while (completed.size() < totalProcesses) {
	            moveProcesses(local);

	            if (local.isEmpty()) {
	                sleepALittle();
	                continue;
	            }

	            applyAgingAndDetectStarvation(local);

	            local.sort((a, b) -> {
	                if (a.priority != b.priority) {
	                    return a.priority - b.priority;
	                }
	                return a.arrivalOrder - b.arrivalOrder;
	            });

	            PCB current = local.remove(0);
	            executeProcessWithPriorityAging(current, local);
	        }

	        System.out.println("[Time " + time + "ms] === Priority Scheduling Completed ===");
	    }

	    private void applyAgingAndDetectStarvation(ArrayList<PCB> waitingProcesses) {
	        int n = waitingProcesses.size();

	        for (PCB p : waitingProcesses) {
	            int waitTime = time - p.arrivalTime;

	            // New requirement: starved if it waits in ready queue more than N * 5 ms.
	            if (waitTime > n * 5 && !p.starved) {
	                p.starved = true;
	                System.out.println("[Time " + time + "ms] STARVATION DETECTED: Process " + p.processId);
	            }

	            // New requirement: aging every 4 ms by decreasing the priority number by 1.
	            if (p.starved &&  time > 0 && time % 4 == 0 && p.priority > 1) {
	                int oldPriority = p.priority;
	                p.priority--;
	                System.out.println("[Time " + time + "ms] AGING: Process " + p.processId +
	                        " priority changed from " + oldPriority + " to " + p.priority);
	            }
	        }
	    }

	    private void executeProcess(PCB pcb) {
	        if (pcb.startTime == -1) {
	            pcb.startTime = time;
	        }

	        pcb.state = PCB.ProcessState.RUNNING;

	        systemCalls.setTime(time);
	        systemCalls.createProcess(pcb.processId);

	        int startRemaining = pcb.remainingTime;
	        int endTime = time + pcb.remainingTime;

	        System.out.println("[Time " + time + "ms - " + endTime + "ms] P" + pcb.processId +
	                " executed, burst remaining: " + startRemaining + " -> 0");

	        time = endTime;
	        pcb.remainingTime = 0;
	        finishProcess(pcb);
	    }

	    // Keeps checking starvation/aging every 1 ms while another process is executing.
	    private void executeProcessWithPriorityAging(PCB pcb, ArrayList<PCB> waitingProcesses) {
	        if (pcb.startTime == -1) {
	            pcb.startTime = time;
	        }

	        pcb.state = PCB.ProcessState.RUNNING;

	        systemCalls.setTime(time);
	        systemCalls.createProcess(pcb.processId);

	        int startTime = time;
	        int startRemaining = pcb.remainingTime;

	        while (pcb.remainingTime > 0) {
	            time++;
	            pcb.remainingTime--;
	            applyAgingAndDetectStarvation(waitingProcesses);
	        }

	        System.out.println("[Time " + startTime + "ms - " + time + "ms] P" + pcb.processId +
	                " executed, burst remaining: " + startRemaining + " -> 0");

	        finishProcess(pcb);
	    }

	    private void finishProcess(PCB pcb) {
	        pcb.completionTime = time;
	        pcb.turnaroundTime = pcb.completionTime - pcb.arrivalTime;
	        pcb.waitingTime = pcb.turnaroundTime - pcb.burstTime;
	        pcb.state = PCB.ProcessState.TERMINATED;

	        systemCalls.setTime(time);
	        systemCalls.terminateProcess(pcb.processId);
	        systemCalls.deallocateMemory(pcb.processId, pcb.memoryRequired);

	        completed.add(pcb);
	    }

	    private void moveProcesses(ArrayList<PCB> local) {
	        synchronized (readyQueue) {
	            while (!readyQueue.isEmpty()) {
	                local.add(readyQueue.remove());
	            }
	        }
	    }

	    private void sleepALittle() {
	        try {
	            Thread.sleep(50);
	        } catch (InterruptedException ignored) {
	        }
	    }
	}

