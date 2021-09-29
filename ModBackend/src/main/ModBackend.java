package main;

import async.ThreadManager;

public class ModBackend {
	
    private static ThreadManager threadManager;
    
	public static void main(String[] args) {

        threadManager = new ThreadManager();
        
        try { threadManager.start(); }  
        catch (Exception e) { e.printStackTrace(); }
        
	}
}
