package osprojct;


	import java.util.LinkedList;

	public class JobLoaderThread extends Thread {
	    private LinkedList<PCB> jobQueue;
	    private LinkedList<PCB> readyQueue;
	    private SystemCalls systemCalls;
	    private volatile boolean running = true;

	    public JobLoaderThread(LinkedList<PCB> jobQueue, LinkedList<PCB> readyQueue, SystemCalls systemCalls) {
	        this.jobQueue = jobQueue;
	        this.readyQueue = readyQueue;
	        this.systemCalls = systemCalls;
	    }

	    @Override
	    public void run() {
	        System.out.println("[Time 0ms] Job Loader Thread started");

	        while (running) {
	            synchronized (jobQueue) {
	                if (jobQueue.isEmpty()) {
	                    waitForJobsOrMemory();
	                } else {
	                    tryLoadNextJob();
	                }
	            }
	        }

	        System.out.println("[Time " + systemCalls.getSystemTime() + "ms] Job Loader Thread terminated");
	    }

	    private void tryLoadNextJob() {
	        PCB job = jobQueue.peek();

	        if (job != null && systemCalls.allocateMemory(job.processId, job.memoryRequired)) {
	            jobQueue.remove();
	            job.state = PCB.ProcessState.READY;

	            synchronized (readyQueue) {
	                readyQueue.add(job);
	                readyQueue.notifyAll();
	            }
	        } else {
	            waitForJobsOrMemory();
	        }
	    }

	    private void waitForJobsOrMemory() {
	        try {
	            jobQueue.wait(100);
	        } catch (InterruptedException e) {
	            running = false;
	        }
	    }

	    public void stopLoading() {
	        running = false;
	        synchronized (jobQueue) {
	            jobQueue.notifyAll();
	        }
	    }
	}

