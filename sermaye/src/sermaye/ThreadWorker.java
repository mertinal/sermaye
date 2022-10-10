package sermaye;


import java.util.LinkedList;
import java.util.Queue;

public class ThreadWorker implements Runnable {

    private final int id;
    private volatile Queue workQueue;   //Does this have to volatile??
    public  volatile boolean shutdown = false;
    public int workQuantity = 0;
    public boolean isFinished = false;

    /**
     * Class constructor.
     *
     * @param threadName Name of the thread for identifying.
     *
     */
    public ThreadWorker(int threadId) {
        this.id = threadId;
        this.workQueue = new LinkedList();
    }

    /**
     * Adds items to the queue.
     *
     * @param object Object to be added to the queue.
     */
    public synchronized void addToQueue(String object) {
    	this.workQuantity++;
        workQueue.add(object); //Does it have to be syncronized void
    }
    
    public void processSentences(String sentence) {
    	sentence = sentence.replaceAll("[\\t\\n\\r]+"," ");
    	sentence = sentence.trim().replaceAll(" +", " ");
    	String[] arr = sentence.split(" ");
    	for (String string : arr) {
    		Multithreading.wordCount++;
			if(Multithreading.map.get(string) != null){
				Multithreading.map.put(string,Multithreading.map.get(string)+1);
			}else {
				Multithreading.map.put(string,1);
			}
		}
    }

    @Override
    public void run() {
    	System.out.println("   ThreadId=" + this.id + ", Count=" + workQueue.size());
        while (!shutdown) {
            if (!workQueue.isEmpty()) {
                String item = (String) workQueue.peek();
                processSentences(item);
                //Process item
               // System.out.println(" just processed " + item);
                workQueue.remove();
            }else {
            	shutdown = true;
            }
        }
        isFinished = true;
    }
    
    public Queue getQueue() {
    	return this.workQueue;
    }
}