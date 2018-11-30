package com.movoto.scripts.consumer.Library.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.google.common.io.Files;
import com.google.common.reflect.TypeToken;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.movoto.common.JSONManager;

import net.minidev.json.JSONArray;


public final class utilities{

		

	public static class JsonManager{

		public static Gson Gson;
		public JsonManager()
		{
			MyExclusionStrategy myExclusionStrategy = new MyExclusionStrategy(WebElement.class);
			Gson = new GsonBuilder().setExclusionStrategies(myExclusionStrategy).create();
		}
		public static String JsonSerializer(Object jsonObject)
		{
			return Gson.toJson(jsonObject); 
		
		}
		public static void JasonSerializerIntoFile(String outputFielPath,String json) throws IOException
		{
			FileWriter fileWriter = new FileWriter(outputFielPath); 
			Object jsonObject = JsonPath.read(json, "$");
			String jsonObjectClassType = jsonObject.getClass().getTypeName();
			if (jsonObjectClassType.contains("JSONArray"))
			{
				JSONArray jObjectArray = (JSONArray)jsonObject;
				fileWriter.write(jObjectArray.toJSONString());  
			}
			else
			{
				JSONObject jObject = new JSONObject((LinkedHashMap)jsonObject);
				fileWriter.write(jObject.toJSONString());  
			}
	        fileWriter.flush();  
	        fileWriter.close(); 
		}
		
		
		public static Object JsonDeserializerByFilePath(String jsonFilePath, Class<?> tClazz) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{
			Object tClassObject = Gson.fromJson(new BufferedReader(new FileReader(jsonFilePath)), tClazz); 
			return tClassObject;
		
		}
		
		public static Object JsonDeserializer(String json, Class<?> tClazz) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{
			Object tClassObject = Gson.fromJson(json, tClazz); 
			return tClassObject;
		
		}
		public static Object JsonDeserializer(String jsonFilePath, String jsonPath,Class<?> tClazz) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{
			Object tClassObject = Gson.fromJson(new BufferedReader(new FileReader(jsonFilePath)), tClazz); 
			return tClassObject;
		
		}
	
		public static Object JsonDeserializerArrayByFilePath(String jsonFilePath,Type tClazz) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{
			//T tInstance = tClazz.newInstance(); 
			Object returnObjects= Gson.fromJson(new BufferedReader(new FileReader(jsonFilePath)), tClazz); 
			return returnObjects;
		
		}
		public static Object JsonDeserializerArray(String jsonString,Type clazzType) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{
			//T tInstance = tClazz.newInstance(); 
			Object returnObjects= Gson.fromJson(jsonString, clazzType); 
			return returnObjects;
		
		}
		
	
		public static Object JsonDeserializerArray(String jsonFilePath, String jsonPath,Type tClazz) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{
			String parsejsonPath = GetJsonObjectByJPath(jsonFilePath,jsonPath).toString();
			return Gson.fromJson(parsejsonPath, tClazz);
		}
		
		public static Object GetJsonObjectByJPath(String jsonFilePath, String jsonPath) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{	
			return JsonPath.parse(new File(jsonFilePath)).read(jsonPath);
		}
		public static Object GetJsonObjectByJPath(String jsonFilePath) throws JsonSyntaxException, JsonIOException, InstantiationException, IllegalAccessException, IOException
		{	
			return JsonPath.parse(new File(jsonFilePath));
		}
	}
	public static class FileManager{
		public static List<String> ReadFileContent(String filePath) throws FileNotFoundException
		{
			List<String> strings=new ArrayList<String>();
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			    strings = reader.lines().collect(Collectors.toList());
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}
			return strings;
		}

		public static String ReadFilesAllContent(String filePath) throws FileNotFoundException
		{
			String strings="";
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			    strings = reader.lines().toString();
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}
			return strings;
		}
		
		public static void WriteFileContent(String filePath,List<String> strings) throws FileNotFoundException
		{
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedWriter writer = Files.newWriter(new File(filePath), charset)) {
				strings.forEach(s->{
					try {
						writer.write(s+"\r\n", 0, s.length());
						writer.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				writer.close();
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}
		}
		
		public static void WriteFileContent(String filePath,String strings) throws FileNotFoundException
		{
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedWriter writer = Files.newWriter(new File(filePath), charset)) {
					try {
						writer.write(strings+"\r\n", 0, strings.length());
						writer.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				writer.close();
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}
		}
		
		public static void AppendFileContent(String filePath,List<String> strings) throws FileNotFoundException
		{
			Charset charset = Charset.forName("US-ASCII");
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
				strings.forEach(s->{
					try {
						writer.append(s+"\r\n", 0, s.length());
						writer.newLine();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});
				writer.close();
			} catch (IOException x) {
			    System.err.format("IOException: %s%n", x);
			}
		}
		public static Boolean FileExist(String filePath)
		{
			File f = new File(filePath);
			return f.exists();
		}
	}
	public static class MyExclusionStrategy implements ExclusionStrategy {
		   
		private final Class<?> typeToSkip;

		public MyExclusionStrategy(Class<?> typeToSkip) 
		{
	    this.typeToSkip = typeToSkip;
		}

	    public boolean shouldSkipClass(Class<?> clazz)
	    {
	      return (clazz == typeToSkip);
	    }

	    public boolean shouldSkipField(FieldAttributes f) 
	    {
	        return f.getAnnotation(Test.class) != null;
	    }
	}


}
