package com.movoto.scripts.consumer.Library.TestModels;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.movoto.scripts.BaseTest;

public class TestPage extends BaseTest {


		public <T> List<T> BuildModels(Class<T> clazz, String pageName,List<WebElement> webElement_clazzs)  throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException,SecurityException, InvocationTargetException, NoSuchMethodException
		{
			List<T> models=new ArrayList<T>();
			//Class<?> clazz=Class.forName(modelName);
			if (webElement_clazzs==null)
			{
				String clazzXPath = this.GetAnnotationValue(BuildInfo.class,clazz,"XPATH","PageName",pageName);
				webElement_clazzs = this.library.getDriver().findElements(By.xpath(clazzXPath));
				
			}
			webElement_clazzs.forEach(webElement_clazz -> 
			{
				try {
				models.add(this.BuildModel(clazz,pageName,webElement_clazz));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			return models;
		}
		
		public <T> List<T> BuildModels(Class<T> clazz,String pageName) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException{
			return this.BuildModels(clazz,pageName,null);
		}
		

		
		public <T> T BuildModel(Class<T> clazz,String pageName,WebElement webElement_clazz) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException 
		{
			T model=clazz.newInstance();
			
			if (webElement_clazz==null)
			{
				String clazzXPath = this.GetAnnotationValue(BuildInfo.class,clazz,"XPATH","PageName",pageName);
				webElement_clazz = this.library.getDriver().findElement(By.xpath(clazzXPath));
				
			}
			
			Field[] fields = clazz.getFields();
			
			for (Field field:fields)
			{
				String fieldType=field.getGenericType().getTypeName();
				
				if (fieldType.contains("WebElement"))
				{
					Field selfField = clazz.getSuperclass().getField("Self");
					selfField.set(model, webElement_clazz);
				}
				else 
				{

					String fieldXPath = this.GetAnnotationValue(BuildInfo.class,field,"XPATH","PageName",pageName);
					WebElement webElement_field =this.GetChildWebElement(webElement_clazz, fieldXPath);
					if (fieldType.contains("movoto")) 
					{
						if (fieldType.contains("java.util.List"))
						{
							List<WebElement> webElement_fields =this.GetChildWebElements(webElement_clazz, fieldXPath);
							
						    field.set(model, this.BuildModels(Class.forName(field.getGenericType().toString().substring(15, field.getGenericType().toString().length()-1)),pageName,webElement_fields));
						}
						else
						{
						    field.set(model, this.BuildModel(field.getType(),pageName,webElement_field));
						}
					}
					else
					{
						if (fieldType=="java.lang.String")
						{
							try {
								//field.setAccessible(true);
								String valueFrom = this.GetAnnotationValue(BuildInfo.class,field,"ValueFrom","PageName",pageName);
								if (!valueFrom.equals(""))
								{
									field.set(model, webElement_field.getAttribute(valueFrom));
								}
								else
								{
									field.set(model, webElement_field.getText());
								}
								
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}


			return (T)model;
			
		}
		 
		public <T> T BuildModel(Class<T> clazz,String pageName) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, NoSuchFieldException, SecurityException, InvocationTargetException, NoSuchMethodException{
			return this.BuildModel(clazz, pageName,null);
		}
		
    private WebElement GetChildWebElement(WebElement parentWebElement,String xPath)
    {
    	
    	return parentWebElement.findElement(By.xpath(xPath));
    }

    private List<WebElement> GetChildWebElements(WebElement parentWebElement,String xPath)
    {
    	
    	return parentWebElement.findElements(By.xpath(xPath));
    }
    
    
    private <T extends Annotation> String GetAnnotationValue(Annotation annotation, String annotationFieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
    	
    	
    	Method m = annotation.getClass().getMethod(annotationFieldName, null);
    	String annotationValue = (String)m.invoke(annotation,null);
    	return annotationValue;
    }
    
    private <T extends Annotation> String GetAnnotationValue(Class<T> annotationClass,AnnotatedElement baseClass, String annotationFieldName,String indexName,String indexValue) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
    {
    	
    	List<Annotation> annotations = Arrays.asList(baseClass.getAnnotationsByType(annotationClass));
    	Annotation targetAnnotation = 
    		annotations.stream().filter(annotation -> {
			try {
				return this.GetAnnotationValue(annotation,indexName).equals(indexValue);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}).findFirst().get();
    	return this.GetAnnotationValue(targetAnnotation, annotationFieldName);

    }
	private Annotation FindAnnotation(Annotation[] annotations, String annotationName)
	{
		for (Annotation tempannotation: annotations)
		{
			if (tempannotation.getClass().getName()==annotationName)
			{
				return tempannotation;
			}
		}
		return null;
	}
}
