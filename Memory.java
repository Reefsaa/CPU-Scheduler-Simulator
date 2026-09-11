package osprojct;

	class Memory {
	    public final int TOTAL_MEMORY = 2048;
	    private int availableMemory;

	    public Memory() {
	        this.availableMemory = TOTAL_MEMORY;
	    }

	    public synchronized boolean allocate(int size) {
	        if (availableMemory >= size) {
	            availableMemory -= size;
	            return true;
	        }
	        return false;
	    }

	    public synchronized void deallocate(int size) {
	        availableMemory += size;
	        if (availableMemory > TOTAL_MEMORY) {
	            availableMemory = TOTAL_MEMORY;
	        }
	    }

	    public synchronized int getAvailable() {
	        return availableMemory;
	    }
	}

