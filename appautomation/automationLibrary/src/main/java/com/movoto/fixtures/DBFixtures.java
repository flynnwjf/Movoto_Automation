package com.movoto.fixtures;

public interface DBFixtures {
	
	public boolean connectToDatabase(String db);

	public Object executeQuery(String query);

	public boolean closeDatabaseConnection();
	
}
