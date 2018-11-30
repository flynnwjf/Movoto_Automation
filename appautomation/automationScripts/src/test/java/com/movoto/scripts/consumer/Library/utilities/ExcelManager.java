package com.movoto.scripts.consumer.Library.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.DataStarterIndex;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus.BrowserInfo;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus.PlantFrom;
import com.movoto.scripts.consumer.Library.utilities.ExcelManager.CodeReviewStatus.StatusPhase;

public class ExcelManager{
	public TreeMap<String,RowAndColumnInf> MergedHeaders;
	public Map<Map<String,String>,String> dataMap;
	public int counts = 0;
	private String sheetName;
	public Sheet sheet = null;
	private String readOrWrite;
	private String locationPath;
	public String comments = "";
	private FileInputStream fin;
	private FileOutputStream out;
	private boolean isNew = false;
	private HSSFWorkbook workbook;
	private XSSFWorkbook workbookXssf;
	private ArrayList<String> arrayList;
	private boolean isErrorFound = false;
	private Set<String> lockedFiles = new HashSet<>();
	public int lastRow;
	public int columnWidth;
	
	public ExcelManager(String locationPath, String sheetName, String readOrWrite) throws Exception {
		this.loadExcelSheet(locationPath, sheetName, readOrWrite);
	}

	public TreeMap<String,RowAndColumnInf> GetMergedHeaders(Sheet sheet,DataStarterIndex dataStarterIndex){
		
		return MergedHeaders;
	}
	
	public void loadExcelSheet(String locationPath, String sheetName, String readOrWrite) throws Exception {

		boolean sheetFound = false;
		this.sheetName = sheetName;
		String type = "read";

		if (locationPath.endsWith(".xls") || locationPath.endsWith(".xlsx")) {
			if (readOrWrite.equalsIgnoreCase("read") || readOrWrite.equalsIgnoreCase("write")) {

				if (readOrWrite.equalsIgnoreCase("write"))
					type = "write";
				try {
					openExcelFile(locationPath, type);
					if (locationPath.endsWith(".xls"))
						workbook = new HSSFWorkbook(fin);
					else
						workbookXssf = new XSSFWorkbook(fin);
				} catch (IOException e) {
					e.printStackTrace();
				}
				sheetFound = verifyIfSheetExists();
				if (!sheetFound) {
					if (type.equals("read"))
						;
					else {
						if (workbook != null)
							sheet = workbook.createSheet(sheetName);
						else
							sheet = workbookXssf.createSheet(sheetName);
					}
				} else {
					if (workbook != null)
						sheet = workbook.getSheet(sheetName);
					else
						sheet = workbookXssf.getSheet(sheetName);
					counts = sheet.getPhysicalNumberOfRows();
				}

				this.lastRow = sheet.getLastRowNum();
				this.columnWidth = sheet.getDefaultColumnWidth();
				
			} else {
				if (type.equals("write")) {
					isNew = true;
					if (locationPath.endsWith(".xls")) {
						workbook = new HSSFWorkbook();
						sheet = workbook.createSheet(sheetName);
					} else if (locationPath.endsWith(".xlsx")) {
						workbookXssf = new XSSFWorkbook();
						sheet = workbookXssf.createSheet(sheetName);
					}
				}

			}
		}

	}
	private boolean verifyIfSheetExists() {

		boolean sheetFound = false;
		if (workbook != null) {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				String name = workbook.getSheetName(i);
				if (sheetName.equals(name)) {
					return true;
				}
			}
		} else if (workbookXssf != null) {
			for (int i = 0; i < workbookXssf.getNumberOfSheets(); i++) {
				String name = workbookXssf.getSheetName(i);
				if (sheetName.equals(name)) {
					return true;
				}
			}
		}
		return sheetFound;
	}
	private void openExcelFile(String filePath, String mode) throws Exception {
		File file = new File(filePath);
		int counter = 0;
		if (file.exists()) {
			synchronized (lockedFiles) {
				while (isLocked(filePath)) {
					System.out.println("Locked: " + filePath);
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO: handle exception
					}
					counter++;
					if(counter>=10){
						throw new Exception("Could not open the file: "+filePath);
					}
				}
				 this.fin = new FileInputStream(new File(filePath));
				if (mode.equalsIgnoreCase("write")) {
					lockFile(filePath);
					System.out.println("Locked: " + filePath);
				}
			}
		} else {
			this.fin = null;
		}
	}
	
	private String retrieveRowColumnData(int rowIndex, int columnIndx) {

		String resultString = "";
		Row row = sheet.getRow(rowIndex);
		int lastRow = sheet.getLastRowNum();
		int columnWidth = sheet.getDefaultColumnWidth();
		if (rowIndex <= lastRow) {
			try {
				int columnNo = 0;
				int lastCell = row.getLastCellNum();
				if (columnIndx <= lastCell - 1) {
					for (int i = 0; i < lastCell; i++) {
						Cell cell = row.getCell(i);
						if (columnNo == columnIndx) {
							if (cell != null)
								resultString = returnStringTypeof(cell);
							else
								resultString = "";
							break;
						}
						columnNo = columnNo + 1;
					}
				} else {
					if (columnNo <= columnWidth)
						resultString = "";
					else {
						isErrorFound = true;
						comments = "Column doesn't exist";
					}
				}
			} catch (Exception e) {
				isErrorFound = true;
				comments = "Row : " + (rowIndex + 1) + " is a blank row";
			}
		} else {
			isErrorFound = true;
			comments = "Row doesn't exist";
		}
		return resultString;
	}
	

	public CodeReviewStatus getCodeReviewStatusInfo(Sheet sheet){    
		List<CellRangeAddress> cellRangeAddressList = sheet.getMergedRegions();
		int rowNum_PlantFrom = 0;
		int startColumnNum_PlantFrom=2;
		int endColumnNum_PlantFrom=sheet.getColumnWidth(0);
		CodeReviewStatus codeReviewStatus = new CodeReviewStatus();
		List<CellRangeAddress> targetcellRangeAddressList_PlantForm=cellRangeAddressList.stream().filter(cellRangeAddress -> cellRangeAddress.getFirstRow()==rowNum_PlantFrom && cellRangeAddress.getFirstColumn()>=startColumnNum_PlantFrom&&cellRangeAddress.getLastColumn()<=endColumnNum_PlantFrom).collect(Collectors.toList());;
		int columnNum_MovotoPage=0;
		int startRowNum_MovotoPage=targetcellRangeAddressList_PlantForm.get(0).getLastRow()+1;
		int endRowNum_MovotoPage=sheet.getLastRowNum();
		List<CellRangeAddress> targetcellRangeAddressList_MovotoPages=cellRangeAddressList.stream().filter(cellRangeAddress -> cellRangeAddress.getFirstColumn()==columnNum_MovotoPage && cellRangeAddress.getFirstRow()>=startRowNum_MovotoPage&&cellRangeAddress.getLastRow()<=endRowNum_MovotoPage).collect(Collectors.toList());;
		targetcellRangeAddressList_PlantForm.forEach(targetcellRangeAddress_PlantForm ->{
			int firstColumn_PlantForm = targetcellRangeAddress_PlantForm.getFirstColumn();
			PlantFrom plantFrom = new PlantFrom(getCellValue(sheet.getRow(rowNum_PlantFrom).getCell(firstColumn_PlantForm)));
			codeReviewStatus.PlantFroms.add(plantFrom);
			int lastColumn_PlantForm = targetcellRangeAddress_PlantForm.getLastColumn();
			int rowNum_BrowserInfo = 1;
			int startColumnNum_BrowserInfo=firstColumn_PlantForm;
			int endColumnNum_BrowserInfo=lastColumn_PlantForm;
			List<CellRangeAddress> targetcellRangeAddressList_BrowserInfos=cellRangeAddressList.stream().filter(cellRangeAddress -> cellRangeAddress.getFirstRow()==rowNum_BrowserInfo && cellRangeAddress.getFirstColumn()>=firstColumn_PlantForm&&cellRangeAddress.getLastColumn()<=lastColumn_PlantForm).collect(Collectors.toList());
			targetcellRangeAddressList_BrowserInfos.forEach(targetcellRangeAddressList_BrowserInfo -> {
				int firstColumn_BrowserInfo=targetcellRangeAddressList_BrowserInfo.getFirstColumn();
				BrowserInfo browserInfo = new BrowserInfo(getCellValue(sheet.getRow(rowNum_BrowserInfo).getCell(firstColumn_BrowserInfo)));
				plantFrom.BrowserInfos.add(browserInfo);
				
				int rowNum_statusPhase = 2;
				int lastColumn_BrowserInfo=targetcellRangeAddressList_BrowserInfo.getLastColumn();
				List<Map<String,String>> caseIDs=this.getValues4AColumnWithStarterIndex(targetcellRangeAddressList_MovotoPages,sheet, new DataStarterIndex(3,1), 1, 100);
				for (int i = 3; i<=this.lastRow;i++)
				{
					Map<String,String> caseID = caseIDs.get(i-3);
					Map<StatusPhase,String> statuzz = new HashMap<StatusPhase,String>();
					for (int j=firstColumn_BrowserInfo;j<=lastColumn_BrowserInfo;j++)
					{
						String statusPhaseText = getCellValue(sheet.getRow(rowNum_statusPhase).getCell(j));
						String statusValue = getCellValue(sheet.getRow(i).getCell(j));
						if (statusPhaseText.trim().toLowerCase().equals("ready for review") && (statusValue.trim().equals("")||statusValue.trim().equals("N/A"))) break;
						statuzz.put(StatusPhase.overrideValueOf(statusPhaseText), statusValue);
						
					}
					if (statuzz.size()>0) browserInfo.Records.add(new BrowserInfo.Recorded(caseID,statuzz));
					
				}
				});
			
		});
		return codeReviewStatus;
		
	}   

	public List<CellRangeAddress> GetMergedCells(Sheet sheet,int rownNum,int startColumnNum,int endColumnNum)
	{
		List<CellRangeAddress> cellRangeAddresses=new ArrayList<CellRangeAddress>();
		
		return cellRangeAddresses;
	}
	public List<Map<String,String>> getValues4AColumnWithStarterIndex(List<CellRangeAddress> movotoPages,Sheet sheet,DataStarterIndex dataStarterIndex ,int columnCount ,int rowCount){    
		List<Map<String,String>> values = new ArrayList<Map<String,String>>();
		for (int i=dataStarterIndex.firstDataRowNum;i<=dataStarterIndex.firstDataRowNum+rowCount;i++)
		{
			for (int j=(int)(dataStarterIndex.firstDataColumnNum);j<(int)dataStarterIndex.firstDataColumnNum+columnCount;j++)
			{
				if (i > this.lastRow||j >this.sheet.getRow(0).getPhysicalNumberOfCells()) 
					return values;//||cellValue==null||cellValue==""

				Map<String,String> value = new HashMap<String,String>();
				String cellValue = getCellValue(sheet.getRow(i).getCell(j));
				value.put("caseID", cellValue);
				if (cellValue.contains("91"))
				{
					System.out.println("");
				}
				for (CellRangeAddress movotoPage:movotoPages)
				{
					if (movotoPage.getFirstRow()-1<i&&i<movotoPage.getLastRow()+1) value.put("movotoPage",getCellValue(sheet.getRow(movotoPage.getFirstRow()).getCell(movotoPage.getFirstColumn())));
				}
				values.add(value);
			}
		}
		return values;
		
	}   

	public String getMergedRegionValue(Sheet sheet ,int row , int column){    
	    int sheetMergeCount = sheet.getNumMergedRegions();    
	    
	    for(int i = 0 ; i < sheetMergeCount ; i++){    
	        CellRangeAddress ca = sheet.getMergedRegion(i);    
	        int firstColumn = ca.getFirstColumn();    
	        int lastColumn = ca.getLastColumn();    
	        int firstRow = ca.getFirstRow();    
	        int lastRow = ca.getLastRow();    
	            
	        if(row >= firstRow && row <= lastRow){    
	                
	            if(column >= firstColumn && column <= lastColumn){    
	                Row fRow = sheet.getRow(firstRow);    
	                Cell fCell = fRow.getCell(firstColumn);    
	                return getCellValue(fCell) ;    
	            }    
	        }    
	    }    
	        
	    return null ;    
	}    
	
	/**   
	* 获取单元格的值   
	* @param cell   
	* @return   
	*/    
	public String getCellValue(Cell cell){    
	        
	    if(cell == null) return "";    
	        
	    if(cell.getCellType() == Cell.CELL_TYPE_STRING){    
	            
	        return cell.getStringCellValue();    
	            
	    }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){    
	            
	        return String.valueOf(cell.getBooleanCellValue());    
	            
	    }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){    
	            
	        return cell.getCellFormula() ;    
	            
	    }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){    
	            
	        return String.valueOf(cell.getNumericCellValue());    
	            
	    }    
	    return "";    
	}    
	
	/**
	 * Description : Converts and returns string, formula, boolean and numeric
	 * cells as String
	 * 
	 * @return cellValue
	 * @throws UnsupportedEncodingException
	 */
	private String returnStringTypeof(Cell cellType) throws UnsupportedEncodingException {

		String cellValue = "";
		if (cellType != null) {
			switch (cellType.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				cellValue = cellType.toString().trim();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				cellValue = convertNumericCelltoString(cellType).toString().trim();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				boolean cellVal = cellType.getBooleanCellValue();
				cellValue = new Boolean(cellVal).toString().trim();
				break;
			case Cell.CELL_TYPE_FORMULA:
				FormulaEvaluator evaluator = null;
				if (workbook != null)
					evaluator = workbook.getCreationHelper().createFormulaEvaluator();
				else
					evaluator = workbookXssf.getCreationHelper().createFormulaEvaluator();
				cellValue = evaluator.evaluate(cellType).formatAsString();
				if (cellValue.endsWith(".0") || cellValue.endsWith(".00"))
					cellValue = cellValue.substring(0, cellValue.indexOf("."));
				break;
			default:
			}
		}
		return new String(cellValue.trim().getBytes(), "UTF-8");
	}
	/**
	 * Description : Read a cell value from the excel sheet and if the cell
	 * value is numeric then convert it to String
	 * 
	 * @param columnValue
	 * @return strValue
	 */
	private String convertNumericCelltoString(Cell columnValue) {

		String strValue = "";
		if (columnValue != null) {
			double cellVal = columnValue.getNumericCellValue();
			BigDecimal big = new BigDecimal(cellVal);
			strValue = String.valueOf(big);
			if (strValue.contains(".")) {
				double val = Double.valueOf(strValue).doubleValue();
				strValue = String.valueOf(val);
				if (strValue.endsWith("0"))
					strValue = strValue.substring(0, strValue.length() - 1);
			}
		}
		return strValue;
	}
	
	private synchronized boolean isLocked(String file) {
		return lockedFiles.contains(file);
	}

	private synchronized void lockFile(String file) {
		lockedFiles.add(file);
	}

	private synchronized boolean unLockFile(String file) {
		return lockedFiles.remove(file);
	}
	
	public static class DataStarterIndex
	{

		public int firstDataRowNum;
		public int firstDataColumnNum;
		
		public DataStarterIndex(int firstDataRowNum,int firstDataColumnNum){
			this.firstDataRowNum=firstDataRowNum;
			this.firstDataColumnNum=firstDataColumnNum;
		}
	}
	
	public class RowAndColumnInf{
		public Integer FirstRow;
		public Integer LastRow;
		public Integer FirstColumn;
		public Integer LastColumn;
		
	}
	

	public static class CodeReviewStatus{
		public List<PlantFrom> PlantFroms=new ArrayList<PlantFrom>();
		public static class PlantFrom{
			//public PlantFrom(){}
			public PlantFrom(String plantFormName){
				this.PlantFormName =plantFormName;
			}
			public String PlantFormName;
			public List<BrowserInfo> BrowserInfos = new ArrayList<BrowserInfo>();
			
			}
		public static class BrowserInfo{
			public BrowserInfo(String browserName){
				this.BrowserName=browserName;
			}
			
			public String BrowserName;
			public List<Recorded> Records = new ArrayList<Recorded>();
			public static class Recorded{
				public Map<StatusPhase,String> Statuzz = new HashMap<StatusPhase,String>();
				public Map<String,String> CaseID;
				public Recorded(Map<String,String> caseID,Map<StatusPhase,String> statuzz)
				{
					this.Statuzz = statuzz;
					this.CaseID = caseID;
				}
			}
		}
		public static enum StatusPhase{
			//ReadyForReview="ready for review",Reviewed="reviewed",Updated="updated",Accepted="accepted"
			ReadyForReview,Reviewed,Updated,Accepted,Executed;
			private String value;
			public static StatusPhase overrideValueOf(String value) { 
				switch (value.trim())
				{
					case "ready for review":
					{
						return StatusPhase.ReadyForReview;
					}
					case "reviewed":
					{
						return StatusPhase.Reviewed;
					}
					case "updated":
					{
						return StatusPhase.Updated;
					}
					case "accepted":
					{
						return StatusPhase.Accepted;
					}
					case "executed":
					{
						return StatusPhase.Executed;
					}
				}
				return null;}
			
		}
	}
	

		
}

