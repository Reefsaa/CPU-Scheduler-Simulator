package osprojct;

	import java.text.SimpleDateFormat;
	import java.util.Date;

	class SystemCalls {
	    private Memory memory;
	    private int currentTime = 0;
	    private Date systemStartDate;

	    public SystemCalls(Memory memory) {
	        this.memory = memory;
	        this.systemStartDate = new Date();
	    }

	    public void setTime(int time) {
	        this.currentTime = time;
	    }

	    public void createProcess(int processId) {
	        System.out.println("[Time " + currentTime + "ms] Process " + processId + " created");
	    }

	    public void terminateProcess(int processId) {
	        System.out.println("[Time " + currentTime + "ms] Process " + processId + " terminated");
	    }

	    public boolean allocateMemory(int processId, int size) {
	        int beforeAllocation = memory.getAvailable();
	        boolean allocated = memory.allocate(size);

	        if (allocated) {
	            int afterAllocation = memory.getAvailable();
	            System.out.println("[Time " + currentTime + "ms] Allocated " + size + "MB to Process " + processId +
	                    " (" + beforeAllocation + "MB -> " + afterAllocation + "MB)");
	        }

	        return allocated;
	    }

	    public void deallocateMemory(int processId, int size) {
	        int beforeDeallocation = memory.getAvailable();
	        memory.deallocate(size);
	        int afterDeallocation = memory.getAvailable();

	        System.out.println("[Time " + currentTime + "ms] Deallocated " + size + "MB from Process " + processId +
	                " (" + beforeDeallocation + "MB -> " + afterDeallocation + "MB)");
	    }

	    public int getSystemTime() {
	        return currentTime;
	    }

	    public String getSystemDate() {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return dateFormat.format(systemStartDate);
	    }

	    public int getAvailableMemory() {
	        return memory.getAvailable();
	    }
	}

