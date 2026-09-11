package osprojct;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class FileReaderThread extends Thread{
	


	    private LinkedList<PCB> jobQueue;

	    public FileReaderThread(LinkedList<PCB> jobQueue) {
	        this.jobQueue = jobQueue;
	    }

	    @Override
	    public void run() {
	        try {
	            System.out.println("File Reader Thread started");

	            // Put job.txt in the project folder, not inside src with an absolute path.
	          BufferedReader reader = new BufferedReader(
    new FileReader("/Users/reef/Desktop/os/osprojct/job.txt"));
    	            String line;
	            int count = 0;

	            while ((line = reader.readLine()) != null) {
	                line = line.trim();
	                if (!line.isEmpty()) {
	                    String[] parts = line.split(";");
	                    String[] info = parts[0].split(":");

	                    PCB pcb = new PCB(
	                            Integer.parseInt(info[0].trim()),
	                            Integer.parseInt(info[1].trim()),
	                            Integer.parseInt(info[2].trim()),
	                            Integer.parseInt(parts[1].trim())
	                    );

	                    pcb.arrivalOrder = count;

	                    synchronized (jobQueue) {
	                        jobQueue.add(pcb);
	                        jobQueue.notifyAll();
	                    }

	                    count++;
	                }
	            }

	            reader.close();
	            System.out.println("File reading completed. Total jobs: " + count);

	        } catch (IOException e) {
	            System.err.println("Error reading file: " + e.getMessage());
	        }
	    }
	}
