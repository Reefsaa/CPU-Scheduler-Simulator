package osprojct;

	import java.util.ArrayList;
	import java.util.LinkedList;
	import java.util.Scanner;

	public class MainThread {
	    static LinkedList<PCB> jobQueue = new LinkedList<>();
	    static LinkedList<PCB> readyQueue = new LinkedList<>();
	    static ArrayList<PCB> completed = new ArrayList<>();

	    static SystemCalls systemCalls;
	    static Memory memory;
	    static int totalProcesses = 0;

	    public static void main(String[] args) throws Exception {
	        memory = new Memory();
	        systemCalls = new SystemCalls(memory);

	        System.out.println("\n============================================================================================");
	        System.out.println("CPU SCHEDULER SIMULATOR");
	        System.out.println("============================================================================================");
	        System.out.println("System Date: " + systemCalls.getSystemDate());
	        System.out.println("System Start Time: " + systemCalls.getSystemTime() + "ms");
	        System.out.println("Available Memory: " + systemCalls.getAvailableMemory() + "MB");
	        System.out.println("============================================================================================");

	        FileReaderThread fileReader = new FileReaderThread(jobQueue);
	        JobLoaderThread jobLoader = new JobLoaderThread(jobQueue, readyQueue, systemCalls);

	        fileReader.start();
	        jobLoader.start();

	        fileReader.join();
	        Thread.sleep(300);

	        synchronized (jobQueue) {
	            totalProcesses = jobQueue.size();
	        }
	        synchronized (readyQueue) {
	            totalProcesses += readyQueue.size();
	        }

	        Scanner input = new Scanner(System.in);

	        System.out.println("\nSelect Scheduling Algorithm:");
	        System.out.println("1. Shortest Job First (SJF)");
	        System.out.println("2. Round-Robin (Quantum: 5ms)");
	        System.out.println("3. Priority Scheduling (Non-Preemptive)");
	        System.out.print("\nEnter your choice (1-3): ");

	        int choice = input.nextInt();
	        input.close();

	        Scheduler scheduler = new Scheduler(readyQueue, completed, systemCalls, totalProcesses);

	        String algorithmName;

	        if (choice == 1) {
	            algorithmName = "Shortest Job First (SJF)";
	            scheduler.sjf();
	        } else if (choice == 2) {
	            algorithmName = "Round-Robin (Quantum: 5ms)";
	            scheduler.roundRobin();
	        } else if (choice == 3) {
	            algorithmName = "Priority Scheduling (Non-Preemptive)";
	            scheduler.priority();
	        } else {
	            System.out.println("Invalid choice.");
	            jobLoader.stopLoading();
	            return;
	        }

	        jobLoader.stopLoading();

	        System.out.println("\nAvailable Memory After Execution: " + systemCalls.getAvailableMemory() + "MB");
	        ResultsTable.display(completed, algorithmName);
	    }
	}

