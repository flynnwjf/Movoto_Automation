package com.movoto.fixtures.impl.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import com.movoto.common.ExcelFileManager;
import com.movoto.common.JSONManager;
import com.movoto.data.TestDTO;
import com.movoto.fixtures.UtilityFixtures;
import com.movoto.reporter.ReportManager;

public class UtilityFixturesImpl implements UtilityFixtures {

	private ThreadLocal<ExcelFileManager> localExcel = new ThreadLocal<>();
	protected TestDTO dto;

	public UtilityFixturesImpl(TestDTO dto) {
		this.dto = dto;
	}

	public Map<String, String> loadObjectRepository(String filePath) {
		return null;
	}

	@Override
	public void openExcelSheet(String path, String sheet, String mode) {
		ExcelFileManager excel = new ExcelFileManager();
		try {
			excel.loadExcelSheet(sheet, path, mode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		localExcel.set(excel);
	}

	@Override
	public String getFromExcelRowAndColumn(int row, String header) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			return excel.retrieveRowColumnDataByName(row, header);
		}
		return null;
	}

	@Override
	public void closeExcelSheet(String path, String sheet, String mode) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			excel.unloadExcelSheet();
		}

	}

	@Override
	public void writeToExcel(String data, int row, int column) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			excel.writeIntoExcelSheet(data, String.valueOf(row), String.valueOf(column));
		}
	}

	@Override
	public int getExcelRowNumberForKey(String key) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			try {
				return excel.findRowNumberForKey(key);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
	}

	@Override
	public int getExcelRowCount() {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			return excel.getRowCounts();
		}
		return 0;
	}

	@Override
	public boolean deleteRowForKey(String key) {
		ExcelFileManager excel = localExcel.get();
		if (excel != null) {
			excel.deleteRowForKey(key);
		}
		return true;
	}

	@Override
	public Object getValueFromJson(String jsonPath, String json) {
		try {
			return JSONManager.getValueFromJson(jsonPath, json);
		} catch (Exception e) {
			return null;
		}
		
	}

	@Override
	public boolean connectToDatabase(String db) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object executeQuery(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean closeDatabaseConnection() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean wait(int seconds) {
		try {
			long millis = seconds * 1000;
			Thread.sleep(millis);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public void setCurrentTestMethod(String name) {
		ReportManager.getCurrentTestResult().setMethod(name);
	}

	@Override
	public String getCurrentPlatform() {
		return dto.getTestProperties().getPlatformName();
	}
	
	@Override
	public String getCurrentBrowser() {
		return dto.getTestProperties().getBrowserName();
	}
	
	
	@Override
	public String getCurrentDate() {
		return new Date().toString();
	}
	
	@Override
	 public String getBrowserName() {
	  // TODO Auto-generated method stub
	  return dto.getTestProperties().getBrowserName();
	 }

	@Override
	public String getCurrentOS() {
		// TODO Auto-generated method stub
		return dto.getTestProperties().getCurrentOS();

	}
	
	@Override
	 public String getCurrentPlatformType() {
	  // TODO Auto-generated method stub
	  return dto.getTestProperties().getPlatformType();
	 }

  @Override
  public boolean checkFileInFileSystem(String filepath) {
    
	  try {
	      if (filepath == null)
	        throw new FileNotFoundException();
	      File file = Paths.get(filepath).toFile();
	      return (file.exists() && file.isFile() && !file.isDirectory()) ? true : false;
	    } catch (FileNotFoundException | InvalidPathException | NullPointerException e) {
	      e.printStackTrace();
	      return false;
	    }
  }
	
	

}
