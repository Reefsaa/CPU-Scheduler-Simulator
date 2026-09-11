package osprojct;

	import java.util.ArrayList;

	class ResultsTable {
	    public static void display(ArrayList<PCB> completed, String algorithmName) {
	        System.out.println("\n============================================================================================");
	        System.out.println("GANTT CHART / EXECUTION ORDER - " + algorithmName);
	        System.out.println("============================================================================================");

	        for (PCB p : completed) {
	            System.out.print("| P" + p.processId + " ");
	        }
	        System.out.println("|");

	        for (PCB p : completed) {
	            System.out.print(p.startTime + "ms   ");
	        }
	        if (!completed.isEmpty()) {
	            System.out.println(completed.get(completed.size() - 1).completionTime + "ms");
	        }

	        System.out.println("\n============================================================================================");
	        System.out.println("PROCESS STATISTICS - " + algorithmName);
	        System.out.println("============================================================================================");
	        System.out.println("PID | Burst | Priority | Memory | Start | Termination | Turnaround | Waiting | Starved");
	        System.out.println("--------------------------------------------------------------------------------------------");

	        double avgWait = 0;
	        double avgTurnaround = 0;

	        for (PCB p : completed) {
	            System.out.printf("%-3d | %-5d | %-8d | %-6d | %-5d | %-11d | %-10d | %-7d | %s\n",
	                    p.processId,
	                    p.burstTime,
	                    p.originalPriority,
	                    p.memoryRequired,
	                    p.startTime,
	                    p.completionTime,
	                    p.turnaroundTime,
	                    p.waitingTime,
	                    p.starved ? "YES" : "NO");

	            avgWait += p.waitingTime;
	            avgTurnaround += p.turnaroundTime;
	        }

	        System.out.println("============================================================================================");

	        if (!completed.isEmpty()) {
	            System.out.printf("Average Waiting Time: %.2f ms\n", avgWait / completed.size());
	            System.out.printf("Average Turnaround Time: %.2f ms\n", avgTurnaround / completed.size());
	        }

	        System.out.println("============================================================================================\n");
	    }
	}

