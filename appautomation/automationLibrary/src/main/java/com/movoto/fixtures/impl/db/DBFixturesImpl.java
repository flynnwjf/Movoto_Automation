package com.movoto.fixtures.impl.db;

import java.io.IOException;
import java.util.Map;
import com.movoto.data.TestDTO;
import com.movoto.fixtures.DBFixtures;
import com.movoto.fixtures.impl.util.FileUtil;

public class DBFixturesImpl implements DBFixtures {

	private TestDTO dto;
	private DBContext context;
	private ThreadLocal<Database> currentDb = new ThreadLocal<>();

	public DBFixturesImpl(TestDTO dto) {
		this.dto = dto;
		this.context = DBContext.getDBContext();
	}

	@Override
	public boolean connectToDatabase(String db) {
		String dbURL = dto.getTestProperties().getDataBaseURL();
		if (dbURL != null){
			new Database(db, dbURL, "", "");
			return true;
		}
		else{
			Map<String, String> map = loadDBDetails(db);
			if (map != null) {
				String dataBase = db;
				String connectionURL = map.get("URL");
				String userName = map.get("USERNAME");
				String password = map.get("PASSWORD");

				Database database = new Database(dataBase, connectionURL, userName, password);
				if (database != null) {
					currentDb.set(database);
				}
				return true;
			}
			return false;
		}	
	}

	@Override
	public Object executeQuery(String query) {
		Database database = currentDb.get();
		if (database != null) {
			return database.executeQuery(query);
		}
		else{
			return this.context.getDatabase().executeQuery(query);
		}
	}

	@Override
	public boolean closeDatabaseConnection() {
		Database database = currentDb.get();
		if (this.context.getDatabase() != null){
			this.context.getDatabase().close();
			return true;
		}
		if (database != null) {
			database.close();
			return true;
		}
		return false;
	}

	private Map<String, String> loadDBDetails(String db) {
		try {
			String path = dto.getTestProperties().getDatabaseConfigPath();
			if (path != null) {
				path = path + db;
			}
			Map<String, String> map = FileUtil.readFileAsMap(path);
			return map;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
