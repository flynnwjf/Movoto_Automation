package com.movoto.data;

import com.movoto.data.impl.DataManagerImpl;

public abstract class DataManager {
	
	protected static final String FILE_TYPE_PROPERTIES = "properties";
	protected static final String FILE_TYPE_JSON = "json";
	protected static final String FILE_TYPE_EXCEL = "xlsx";
	protected static final String FILE_TYPE_EXCEL_OLD = "xls";
	
	private static ThreadLocal<TestDTO> localData = new InheritableThreadLocal<>();
	
	public static DataManager getDataManager(String type){
		DataManager manager = null;
		manager = new DataManagerImpl();
		return manager;
	}
	
	public abstract TestDTO populateTestDataForTest(String configurationFilePath);
	
	public synchronized static void setData(TestDTO dto){
		localData.set(dto);
	}
	
	public synchronized static TestDTO getData(){
		return localData.get();
	}
	
}
