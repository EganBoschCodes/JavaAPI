package async;

import java.util.HashMap;

import org.apache.http.impl.client.CloseableHttpClient;

import com.google.gson.Gson;

import datatypes.AuctionHouse;
import datatypes.AuctionsResponse;

public class ThreadManager extends Thread implements Runnable {
	
	public static long lastUpdated = 0;
	
    public static HashMap<String, Integer> usedKeys = new HashMap<String, Integer>();
    public static final Gson GSON = new Gson();
    
    public Flag[] dataGathered = {new Flag(), new Flag(), new Flag(), new Flag()};
    public Flag[] dataParsed = {new Flag(), new Flag(), new Flag(), new Flag()};
    public Flag[] threadDone = {new Flag(), new Flag(), new Flag(), new Flag()};
    public int[] failsafe = {-1, 14, 29, 44};

    public Flag[] runThreads = {new Flag(), new Flag(), new Flag(), new Flag()};
    
    private SnipeThread[] SnipeThreads = {new SnipeThread(this, 0, 0, 17), new SnipeThread(this, 1, 17, 34), new SnipeThread(this, 2, 34, 51), new SnipeThread(this, 3, 51, 1000) };
    
    AuctionHouse AuctionData;
    CloseableHttpClient httpClient;
    
    boolean APIFirstAwait = true;
    
    @Override
    public void start() {
    	super.start();
    	
    	for(int i = 0; i < 4; i++) {
    		System.out.println("THREAD "+i+" STARTED");
        	SnipeThreads[i].start();
        }
    	
    }
    
	@Override
	public void run() {
		int loops = 0;
		while(loops < 4) {
			AuctionHouse ah = getAuctionData();
			System.out.println(System.currentTimeMillis() - ah.lastUpdated);
			
			sleepSafe(Math.max(60000 - System.currentTimeMillis() + ah.lastUpdated, 5000));
			loops++;
		}
		
		System.out.println("TERMINATING");
		
	}
	
	public AuctionHouse getAuctionData()
    {
		
        AuctionHouse AuctionData = new AuctionHouse();
        
        for(int i = 0; i < 4; i++) {
        	dataParsed[i].set(true);
        	threadDone[i].set(false);
			dataGathered[i].set(false);
			failsafe[i] = i * 15 - 1;
        }
        
        for(int i = 0; i < 4; i++) {
        	runThreads[i].set(true);
        }
        
        System.out.println("STARTING WHILE LOOP");
    	while(!threadDone[0].get() || !threadDone[1].get() || !threadDone[2].get() || !threadDone[3].get()) {
    		//System.out.println(!threadDone[0].get() +" "+ !threadDone[1].get() +" "+ !threadDone[2].get() +" "+ !threadDone[3].get());
    		//System.out.println(dataGathered[0].get() +" "+ dataGathered[1].get() +" "+ dataGathered[2].get() +" "+ dataGathered[3].get());
    		for(int i = 0; i < 4; i++) {
    			if(dataGathered[i].get()) {
    				dataGathered[i].set(false);
    				//System.out.println("STARTING PARSING FOR THREAD "+i);
    				AuctionsResponse AuctionPage =  GSON.fromJson(SnipeThreads[i].output, AuctionsResponse.class);
    				//System.out.println("Page Read:" + AuctionPage.page +" " + (SnipeThreads[i].index-1));
		            AuctionData.assimilatePage(AuctionPage);
		            SnipeThreads[i].setSoftMax(AuctionPage.totalPages);
		            failsafe[i] = (SnipeThreads[i].index-1);
    				//System.out.println("FINISHED PARSING FOR THREAD "+i);
		            dataParsed[i].set(true);
    			}
    			if(failsafe[i] == SnipeThreads[i].index - 1) {
		            dataParsed[i].set(true);
    			}
    		}
    		
    	}
    	
        System.out.println("Data Refreshed");
        for(int i = 0; i < 4; i++) {
        	SnipeThreads[i].resetThread();
        }
        
        return AuctionData;	
    }
	
	public void sleepSafe(long millis) {
		try { Thread.sleep(millis); } catch (InterruptedException e) { e.printStackTrace(); }
	}

}
