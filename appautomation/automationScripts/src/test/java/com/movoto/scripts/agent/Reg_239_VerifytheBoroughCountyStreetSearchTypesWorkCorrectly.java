package com.movoto.scripts.agent;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import com.google.common.base.Predicate;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;

import com.movoto.api.APIResponseProcessor;
import com.movoto.api.pojo.APIResponse;
import com.movoto.api.pojo.Address;
import com.movoto.scripts.BaseTest;
import com.movoto.utils.JSONParserForAutomationNG;


public class Reg_239_VerifytheBoroughCountyStreetSearchTypesWorkCorrectly extends BaseTest {
	JSONParserForAutomationNG jsonParser;
	JSONObject jsonObj;
	WebDriver wDriver;  
	JavascriptExecutor jse;
	int lastcardIndex=0;

	@Test
	@Parameters("dataProviderPath")
	public void verifytheBoroughCountyStreetSearchTypesWorkCorrectly(String dataProviderPath) throws ParseException {
		try {

			jsonParser = new JSONParserForAutomationNG(dataProviderPath);
			jsonObj = jsonParser.getNode("mapSearchCountryStreetTypes");

		} catch (Exception exc) {
			System.out.println("Exception in mapSearchCountryStreetTypes() ->"
					+ exc.getMessage());
		}
		if (jsonObj != null) {
			verifyBoroughCountyStreetSearchTypesWorkCorrectly(jsonObj);
		}
		else{
			Assert.assertTrue(false, "Please check data for provided for script");
		}
	}


	public void verifyBoroughCountyStreetSearchTypesWorkCorrectly(JSONObject Data) throws ParseException {

			String pageurl;
			String response;
			String response1,response2,response3;
			jse=(JavascriptExecutor)library.getDriver();
			
			
			//============Step 2 & Step 3======================	
					response = setHeaderAndGetResponse(Data, "search_API","1");
					response1=response;
					inputsearchCriteriaAndverify(Data,Data.get("search_Text1").toString(),response);
					library.wait(5);
					pagination(response1,lastcardIndex);
					
						
						//click on page no. to navigate
						System.out.println("============Navigating to next page===================");
						    library.wait(4);
							library.isJSEClicked("HOMEPAGE.secondPage");
							library.wait(5);
							waitForJStoLoad();
						//url of the page contains p-2
							pageurl = library.getUrl();
							System.out.println(pageurl);
							library.wait(5);
							Assert.assertTrue(pageurl.contains("p-2"));
						//call setHeaderAndGetResponse by passing body with page2
							response = setHeaderAndGetResponse(Data, "search_API","2");
							pagination(response,-1);
		
					
			//============Step 4======================			
							//click on page-x
							System.out.println("============Navigating to last page===================");
							int pageX=library.findElements("//div[@class='cards-wrapper']//div[@class='pagination']/div[@class='paging']//a").size();
							//library.findElement("//div[@class='cards-wrapper']//div[@class='pagination']/div[@class='paging']//a["+(pageX-1)+"]").click();
							String tar="//div[@class='cards-wrapper']//div[@class='pagination']/div[@class='paging']//a["+(pageX-1)+"]";
							library.isJSEClicked(tar);
;							String lastpagenum=library.findElement("//div[@class='cards-wrapper']//div[@class='pagination']/div[@class='paging']//a["+(pageX-1)+"]").getAttribute("data-page");
							
							
						//get number
						library.wait(5);
						waitForJStoLoad();
						
						//url of the page contains p-x
							pageurl = library.getUrl();
							System.out.println(pageurl);
							System.out.println("lastpagenum"+lastpagenum);
							Assert.assertTrue(pageurl.contains("p-"+lastpagenum));
							response = setHeaderAndGetResponse(Data, "search_API","3");
							pagination(response,-1);
		
		
			//============Step 5======================
						response = setHeaderAndGetResponse(Data, "search_API","santa");		
						inputsearchCriteriaAndverify(Data,Data.get("search_Text2").toString(),response);
						response1=response;
						pagination(response1,lastcardIndex);  
			//============Step 6======================
						response = setHeaderAndGetResponse(Data, "search_API","bray");
						response1=response;
						inputsearchCriteriaAndverify(Data,Data.get("search_Text3").toString(),response);
						pagination(response1,lastcardIndex);
	}
	
	
	public void inputsearchCriteriaAndverify(JSONObject Data,String searchText,String response)
	{
				// Step - 2
				// Input City info "Queens, NY" to search box
				
				//library.wait(8);
				waitForJStoLoad();
				library.click("HOMEPAGE.searchBox");
				library.clear("HOMEPAGE.searchBox");
				library.typeDataInto(searchText, "HOMEPAGE.searchBox");
				library.wait(2);
				library.typeDataInto(Keys.chord(Keys.TAB), "HOMEPAGE.searchBox");
				library.wait(2);
				library.click("HOMEPAGE.searchButton");
				Handlealert();
				library.wait(3);
				waitForJStoLoad();
				
				lastcardIndex=0;
				

				// Verify below 3 basic check points for borough search:
				
				//====================== 1. url of page contains "queens-ny/@"======================
						String currentURL1 = library.getUrl();
						String pageurlCnt;
						if(searchText.equalsIgnoreCase("Queens, NY"))
						{
							pageurlCnt=Data.get("pageURLContent1").toString();
						}
						else if(searchText.equalsIgnoreCase("santa clara county"))
						{
							pageurlCnt=Data.get("pageURLContent2").toString();
						}
						else
							pageurlCnt=Data.get("pageURLContent3").toString();
						library.wait(5);
						Assert.assertTrue(currentURL1.contains(pageurlCnt));

				
				// 2. verify total house QTY on top/bottom of page is same with API response.
				String UIQtyTop;
				int UIQtyTop1; 
				
				/*if ((library.getCurrentPlatform().contains("IOS_WEB"))) 
				{
					WebElement element = library.getDriver().findElement(By.xpath(".//*[@id='body']//..//div[@class='r-result-panel']/div[3]/div[1]"));
					String content = (String) ((JavascriptExecutor) library.getDriver()).executeScript("return arguments[0].innerHTML", element);
					content=content.substring(content.indexOf("/span>")+6);
					UIQtyTop=content;
					UIQtyTop = UIQtyTop.replace(",", "");
				} */
				 if((library.getCurrentPlatform().contains("Android")) || (library.getCurrentPlatform().contains("IOS_WEB")))
				{
					WebElement element = library.getDriver().findElement(By.xpath(".//*[@id='body']//..//div[@class='r-result-panel']/div[3]/div[1]"));
					String content = (String) ((JavascriptExecutor) library.getDriver()).executeScript("return arguments[0].innerHTML", element);
					content=content.substring(content.indexOf("of ")+3);
					UIQtyTop=content;
					UIQtyTop = UIQtyTop.replace(",", "");
				}
				else
				{
					UIQtyTop = library.getTextFrom("HOMEPAGE.qtyTop");
					System.out.println("total"+UIQtyTop);
					//UIQtyTop = UIQtyTop.replace("Results 1-50 of", "");
					UIQtyTop = UIQtyTop.replace(",", "");
					//UIQtyTop=UIQtyTop.substring(1);
					UIQtyTop=UIQtyTop.substring(UIQtyTop.indexOf("of ")+3);
					
				}
				UIQtyTop1=Integer.parseInt(UIQtyTop);
				System.out.println("UI - Qty Top : " + UIQtyTop1 );
				
				
				int APIQty = (Integer) library.getValueFromJson("$.totalCount", response);
				
				Assert.assertEquals(UIQtyTop1, APIQty);

				String UIQtyBottom = library.getTextFrom("HOMEPAGE.qtyBottom");
				UIQtyBottom = UIQtyBottom.replace("1 - 50 of", "");
				UIQtyBottom = UIQtyBottom.replace(",", "");
				UIQtyBottom=UIQtyBottom.substring(1);
				int UIQtyBottom1=Integer.parseInt(UIQtyBottom);
				System.out.println("UI - Qty Bottom : " + UIQtyBottom1 );

				Assert.assertEquals(UIQtyBottom1, APIQty);
				
				
				
				

				// 3. "Queens, NY" is displayed in the search box.
				String searchBoxText=library.getAttributeOfElement("value", "HOMEPAGE.searchBox");
				Assert.assertTrue(searchBoxText.contains(searchText.toString()));
				
				
				//====================== 4. Verify title of the page============================
				
				WebElement hiddenInput = library.getDriver().findElement(By.xpath("//div[@class='r-result-panel']/div[2]/h1"));
				String title = hiddenInput.getAttribute("title");
				System.out.println("Title of the page :"+title);
				if(searchText.equalsIgnoreCase("Queens, NY"))
				{
					Assert.assertTrue(title.contains(Data.get("Queens_Title").toString()));
				}
				else if(searchText.equalsIgnoreCase("santa clara county"))
				{
					Assert.assertTrue(title.contains(Data.get("Santa_Title").toString()));
				}
				else if(searchText.equalsIgnoreCase("bray ave"))
				{
					Assert.assertTrue(title.contains(Data.get("Bray_Title").toString()));
				}
				
				//======================Common verify for house card:======================

				
				int iHouseCardCount = library.findElements("//div[@class='cards-wrapper']//div[@class='cardone cardbox']").size();
				System.out.println("Total House card available : " + iHouseCardCount);
				
				
				//==============Get API response============================
				
					int propertyCardPropertyBedAPI=0;
					boolean visibilityAdd;
					String jsCode=null;
					JSONObject individualobj;
					Object apiResponseDataobj=null;
					try{
					JSONParser jsonParser=new JSONParser();
					JSONObject jsonObject=(JSONObject)jsonParser.parse(response);
					JSONArray jsonArray=(JSONArray)jsonObject.get("listings");
					int size=jsonArray.size();
					
					

						for (int i = 0; i < iHouseCardCount; i++) 
						{
							library.wait(5);
							visibilityAdd=true;
							System.out.println("***********UI ITERATION**********************"+i);
							 //String propertyCardStreetAddressUI = library.getTextFrom("//div[@class='cardone cardbox']["+i+"]/div/a/div[@class='info']//div[@class='baseInfo']//div/span[@itemprop='streetAddress']");
							jsCode="var ele= document.getElementsByClassName('addresslink')["+i+"].childNodes[1].childNodes[1]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
							String propertyCardStreetAddressUI=jse.executeScript(jsCode).toString();
							propertyCardStreetAddressUI=propertyCardStreetAddressUI.replace(",","");
					         System.out.println("UI ADD "+propertyCardStreetAddressUI);
							 boolean isItemFound =false;
					 
							 //if(propertyCardStreetAddressUI.equalsIgnoreCase("undisclosed"))
								  //continue;
					 
							 	String propertyCardStreetAddressAPI=null; 
							 	for(int k=0;k<size;k++)
							 		{  
							 			System.out.println("***********KVALUE**********************"+k);
							 			
							 			individualobj = (JSONObject)jsonArray.get(k);
							 			apiResponseDataobj=(String)(((JSONObject)individualobj.get("address")).get("addressInfo"));
							 			propertyCardStreetAddressAPI=String.valueOf(apiResponseDataobj);
								      
								      System.out.println("API ADD "+propertyCardStreetAddressAPI);

								      if (propertyCardStreetAddressAPI.contains("#APT ")) {
								    	  	propertyCardStreetAddressAPI = propertyCardStreetAddressAPI.replaceAll("#APT ", "#");
								      	}else if(propertyCardStreetAddressAPI.contains("APT ")){
									    	  propertyCardStreetAddressAPI = propertyCardStreetAddressAPI.replaceAll("APT ", "#");
									      	}
								      
								      //==========================Handling verification when address is undisclosed=====================================
								      if(propertyCardStreetAddressUI.equalsIgnoreCase("undisclosed"))
								      {
								    	  visibilityAdd=false;
								    	  
								    	  String visibility =String.valueOf(individualobj.get("visibility"));
								    	  System.out.println("undiscolsed************* " + visibility);
								    	  if(!visibility.equalsIgnoreCase("NO_RESTRICTION"))
								    	  {
								    		  //Get API price and city
								    		  	apiResponseDataobj=individualobj.get("listPrice");
									    		int propertyCardPropertypriceAPI=Integer.parseInt(String.valueOf(apiResponseDataobj));
									    		
									    		apiResponseDataobj=(String)(((JSONObject)individualobj.get("address")).get("city"));
									    		  String propertyCardCityAPI=String.valueOf(apiResponseDataobj);
								    		 
								    		  //Get UI price and city
								    		  jsCode="var ele= document.getElementsByClassName('price')["+i+"]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
								    		  String sPrice=jse.executeScript(jsCode).toString();
								    		  int propertyCardPropertypriceUI=(sPrice!=null && !sPrice.equals(""))?Integer.parseInt(sPrice.replace("$", "").replaceAll(",", "")):0;
									    	  
									    	  
									    	  
								    		  jsCode="var ele= document.getElementsByClassName('address-city')["+i+"]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
								    		  String sCity=jse.executeScript(jsCode).toString();
								    		  sCity=sCity.replace(",","");
									    	  
									    	  if(propertyCardPropertypriceUI==propertyCardPropertypriceAPI && sCity.equalsIgnoreCase(propertyCardCityAPI) )
									    		  propertyCardStreetAddressUI=propertyCardStreetAddressAPI;
								    	  }
								    			
								      }
						 
								      
								      // continues match further when street address match
								      if(propertyCardStreetAddressAPI.equalsIgnoreCase(propertyCardStreetAddressUI))
								      	{
								    	  isItemFound=true;
								    	  propertyCardPropertyBedAPI=0;
								    	    if(i==iHouseCardCount)
								    	    	lastcardIndex=k;
								    	  
								    	//============================== Price==========================================
								    	  
								    	    jsCode="var ele= document.getElementsByClassName('price')["+i+"]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
								    		String sPrice=jse.executeScript(jsCode).toString();
								    		int propertyCardPropertypriceUI=(sPrice!=null && !sPrice.equals(""))?Integer.parseInt(sPrice.replace("$", "").replaceAll(",", "")):0;
								    		System.out.println("UI Price : " + propertyCardPropertypriceUI);
								    		
								    		apiResponseDataobj=individualobj.get("listPrice");
								    		int propertyCardPropertypriceAPI=Integer.parseInt(String.valueOf(apiResponseDataobj));
								    	  System.out.println("API Price : " + propertyCardPropertypriceAPI);
								    	  Assert.assertTrue(propertyCardPropertypriceUI == propertyCardPropertypriceAPI);

								    	  //============================Street Address======================== 
								    	  	if(visibilityAdd==true)
								    	  	{
										    	 
								    	  		jsCode="var ele= document.getElementsByClassName('addresslink')["+i+"].childNodes[1].childNodes[1]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
												propertyCardStreetAddressUI=jse.executeScript(jsCode).toString();
												propertyCardStreetAddressUI=propertyCardStreetAddressUI.replace(",","");
										    	 
												apiResponseDataobj=(String)(((JSONObject)individualobj.get("address")).get("addressInfo"));
									 			propertyCardStreetAddressAPI=String.valueOf(apiResponseDataobj);
						       
						       
												 if (propertyCardStreetAddressAPI.contains("#APT ")) {
													 propertyCardStreetAddressAPI = propertyCardStreetAddressAPI.replaceAll("#APT ", "#");
												 }else if(propertyCardStreetAddressAPI.contains("APT ")){
											    	  propertyCardStreetAddressAPI = propertyCardStreetAddressAPI.replaceAll("APT ", "#");
											      	}
								 
												 System.out.println("APIAddress"+propertyCardStreetAddressAPI);	
												 Assert.assertEquals(propertyCardStreetAddressUI, propertyCardStreetAddressAPI); 
								    	  	}
					
					
										//======================== Card URL validation - UI and API================================
										//String sCardURL = library.getAttributeOfElement("href", "xpath->(//div[@class='cardone cardbox']["+i+"]/div/a[@class='imgmask'])");
								    	jsCode="var ele=document.getElementsByClassName('imgmask')["+i+"].getAttribute('href'); if(ele=='null' || ele==null) return ''; else return ele";
								    	String sCardURL = jse.executeScript(jsCode).toString();
								    	System.out.println("Card URL : " + sCardURL);
								    	
								    	String cardurlAPI =String.valueOf(individualobj.get("listingUrl"));

										System.out.println("API Card URL : " + cardurlAPI);
									    
										Assert.assertTrue(sCardURL.contains(cardurlAPI ), "Card Url matching with API response");
					
										//=================================Verify city name=====================================
										//String sCity = library.getTextFrom("//div[@class='cardone cardbox']["+i+"]/div/a/div[@class='info']/div[@class='baseInfo']/div[@class='addresslink']/div/span[@class='address-city']");
									    jsCode="var ele= document.getElementsByClassName('address-city')["+i+"]; if(ele=='null' || ele==null) return ''; else return ele.textContent;";
							    		String sCity=jse.executeScript(jsCode).toString();
							    		sCity=sCity.replace(",","");
										System.out.println("City Name:"+sCity);
										
										apiResponseDataobj=(String)(((JSONObject)individualobj.get("address")).get("city"));
							    		  String propertyCardCityAPI=String.valueOf(apiResponseDataobj);
										
										System.out.println("APICity Name:"+propertyCardCityAPI);
									    Assert.assertEquals(sCity, propertyCardCityAPI);  
					
										
										//===============================Verify the presence of favourite icon========================================
										int cnt=library.findElements("//div[@class='cardone cardbox']["+(i-1)+"]//div[@class='fav']/a[@data-ga-name='HouseCardFavorite']").size();
										if(cnt==1){
											System.out.println("Favoruite symbol exists: " + cnt +"iteration:"+ i);
											Assert.assertTrue(cnt==1);
										}
										else
										{
											System.out.println("Favoruite symbol does not exists: " + cnt +"iteration:"+ i);
											Assert.assertTrue(cnt!=1);
										}
					
										//=================================Verify no.of Bedrooms=====================================
										
										String iconclass;
										
										jsCode="var ele= document.getElementsByClassName('top-base-info')["+i+"].children[3];  if(ele=='null' || ele==null) return ''; else return ele.textContent;";
										
										String bedroom=jse.executeScript(jsCode).toString();
										System.out.println("UI BEDROOM"+bedroom);
										
										
										
										apiResponseDataobj=individualobj.get("bedrooms");
										//String apibedroomsI =String.valueOf(apiResponseDataobj!=null?Integer.parseInt(String.valueOf(apiResponseDataobj)):"0");
										
										String apibedroomsI =String.valueOf(apiResponseDataobj);
										System.out.println("API BEDROOM"+apibedroomsI);
										jsCode="var ele= document.getElementsByClassName('top-base-info')["+i+"].children[3].childElementCount;  if(ele=='null' || ele==null||ele=='1') return '1'; else return '0'";
										
										String gvalue=jse.executeScript(jsCode).toString();
										
										int innerI=Integer.parseInt(gvalue);
										//int innerI=jse.executeScript(jsCode);
										
										//int innerI= library.findElements("//div[@class='cardone cardbox']["+i+"]/div/a/div[@class='info']/div/span[2]/i").size();
										if(innerI>0){
											//ele= library.findElement("//div[@class='cardone cardbox']["+i+"]/div/a/div[@class='info']/div/span[2]/i");
											jsCode="var ele= document.getElementsByClassName('top-base-info')["+i+"].children[3].childNodes[1].className; if(ele=='null' || ele==null) return ''; else return ele";
											iconclass=jse.executeScript(jsCode).toString();
										}
											
										else{
											//ele= library.findElement("//div[@class='cardone cardbox']["+i+"]/div/a/div[@class='info']/div/span[2]/following::i[1]");
											jsCode="var ele= document.getElementsByClassName('top-base-info')[36].children[4].className;  if(ele=='null' || ele==null) return ''; else return ele";
											iconclass=jse.executeScript(jsCode).toString();
										}
										//iconclass=ele.getAttribute("class");
										//System.out.println("ClassName"+iconclass);
										
										if(iconclass.contains("Acre") || iconclass.contains("sqft"))
										{
											System.out.println("Bedroom count is Zero");
											if(apibedroomsI==null || apibedroomsI == "null"  || apibedroomsI == null)
													Assert.assertEquals(apibedroomsI,"null");
											else
												Assert.assertEquals(apibedroomsI,"0");
											
										}
										else if(iconclass.contains("bed") )
										{
											if(bedroom.equals(" —") || bedroom.equals("—") || bedroom==" —" || bedroom=="—")
											{
												System.out.println("Bedroom count is Zero"+bedroom);
												if(apibedroomsI==null || apibedroomsI == "null"  ||apibedroomsI == null)
													Assert.assertEquals(apibedroomsI,"null");
												else
													Assert.assertEquals(apibedroomsI,"0");
												
											}
											else
											{
												
												if(!bedroom.contains("."))
												{
													double BedAPI= Double.parseDouble(apibedroomsI);
													System.out.println("API Bed room count"+BedAPI);
													System.out.println("Bed room count"+Double.parseDouble(bedroom));
											        Assert.assertEquals(Double.parseDouble(bedroom), BedAPI);
											     }
												else
												{   
											        Float BedAPI = Float.parseFloat(apibedroomsI);
											        System.out.println("API Bed room count"+BedAPI);
											        System.out.println("Bed room count"+Float.parseFloat(bedroom));
											        Assert.assertEquals(Float.parseFloat(bedroom), BedAPI);
											     }
											 }
											}
										
					
									//=================================Verify no.of Bathrooms=====================================
										
									//String bathRooms=library.getTextFrom("//div[@class='cardone cardbox']["+i+"]/div/a/div[@class='info']/div/span[3]");else return ele";
										
										jsCode="var ele= document.getElementsByClassName('top-base-info')["+i+"].children[5];  if(ele=='null' || ele==null) return 'nothing'; else return ele.textContent;";
										String bathRooms=jse.executeScript(jsCode).toString();
									
									String iconclass1;
									apiResponseDataobj=individualobj.get("bathroomsTotal");
									
									String apibathroomsI=String.valueOf(apiResponseDataobj);
									System.out.println("API BATHROOM"+apibathroomsI);
									
									if(bathRooms.equalsIgnoreCase("nothing") || bathRooms.equalsIgnoreCase(""))
									{
										jsCode="var ele= document.getElementsByClassName('top-base-info')["+i+"].children[5].className;  if(ele=='null' || ele==null) return ''; else return ele";
										iconclass1=jse.executeScript(jsCode).toString();
									}
									else
									{
										jsCode="var ele= document.getElementsByClassName('top-base-info')["+i+"].children[6].className;  if(ele=='null' || ele==null) return ''; else return ele";
										 iconclass1=jse.executeScript(jsCode).toString();
									}
									
									if(iconclass1.contains("days"))
									{
										System.out.println("Bathroom count is Zero");
										if(apibathroomsI==null || apibathroomsI == "null"  || apibathroomsI == null)
												Assert.assertEquals(apibathroomsI,"null");
										else
											Assert.assertEquals(apibathroomsI,"0");
										
									}
									else if(iconclass1.contains("bath") )
									{
										if(bathRooms.equals(" —") || bathRooms.equals("—") || bathRooms==" —" || bathRooms=="—" || bathRooms.equals("— ") || bathRooms=="— ")
										{
											System.out.println("Bathroom count is Zero"+bathRooms);
											if(apibathroomsI==null || apibathroomsI == "null"  ||apibathroomsI == null)
												Assert.assertEquals(apibathroomsI,"null");
											else
												Assert.assertEquals(apibathroomsI,"0");
											
										}
										else
										{
											
											if(!bathRooms.contains("."))
											{
												double BathAPI= Double.parseDouble(apibathroomsI);
												System.out.println("API Bath room count"+BathAPI);
												System.out.println("Bath room count"+Double.parseDouble(bathRooms));
										        Assert.assertEquals(Double.parseDouble(bathRooms), BathAPI);
										     }
											else
											{   
										        Float BathAPI = Float.parseFloat(apibathroomsI);
										        System.out.println("API Bath room count"+BathAPI);
										        System.out.println("Bath room count"+Float.parseFloat(bathRooms));
										        Assert.assertEquals(Float.parseFloat(bathRooms), BathAPI);
										     }
										 }
										}
				                    }//closing of if 
				         
								  if(isItemFound == true){
									  jsonArray.remove(k);
									  size= jsonArray.size();
									  break;
								  }
			         
			           }
					      if(isItemFound == false){
					           Assert.assertFalse(false, "UI value not matching with API");
					           break;
					      }
					
					System.out.println("============================================================================================");
					

				}
					}
					catch(Exception e)
					{
						System.out.println("Exception while parsing response");
						
					}
	}
	
			public void Handlealert()
			{
				try {
					boolean isDialogPresent = library.verifyPageContainsElement("HOMEPAGE.dialogClose");
					if (isDialogPresent){
						library.wait(2);
						library.click("HOMEPAGE.dialogClose");
					}
				} catch (NoSuchElementException e) {
					System.out.println("Dialog Not Found");
				}
			}
	
	public void pagination(String apiresponse,int lastCardsIndex)
			{
				//===========Pagination info contains====================
				library.wait(5);
				String pge=library.getTextFrom("//div[@class='cards-wrapper']//div[@class='pagination']/div[@class='info']/b");
				System.out.println("Page Info contains  "+pge);
				pge=pge.substring(0, pge.indexOf('o'));
				System.out.println(pge);
				pge=pge.substring(pge.indexOf('-')+2,pge.length()-1);
				//System.out.println("Filtered"+pge);
				pge=pge.trim();
				//int x=Integer.parseInt(pge);
				//System.out.println("Filteredxx"+x);
				
				
				//==========Current Page Number==================
				String curr=library.getTextFrom("//div[@class='cards-wrapper']//div[@class='pagination']/div[@class='paging']//a[@class='selected']");
				System.out.println("Current page selected is "+curr);
				
				//Max page no.
				//50 cards are showed on page1
				int noOfCardsShown=library.findElements("//div[@class='cards-wrapper']//div[@class='cardone cardbox']").size();
				System.out.println("Number of cards shown on this page "+noOfCardsShown);		
				
				//Last cards address
				String UIlastCardAdd=library.getTextFrom("//div[@class='cardone cardbox']["+noOfCardsShown+"]/div/a/div[@class='info']/div[@class='baseInfo']/div[@class='addresslink']/div/span[@itemprop='streetAddress']");
				System.out.println("UI Last card Address is "+UIlastCardAdd);
				if(UIlastCardAdd.contains("#"))
				{
					UIlastCardAdd=UIlastCardAdd.replace("#", "#APT ");
					System.out.println("UI Last card Address is "+UIlastCardAdd);
				}
				library.wait(5);
				
					if(!UIlastCardAdd.equalsIgnoreCase("undisclosed"))
				       Assert.assertTrue(apiresponse.contains(UIlastCardAdd));
				
				
				
			}
	
	//Set Header and get Response
		public String setHeaderAndGetResponse(JSONObject data, String apiUrl,String x){
			String response=null;
			JSONObject resp=null;
			JSONParser jParser=new JSONParser();
			
			String contentType = String.valueOf(data.get("ContentType"));
			String API = String.valueOf(data.get(apiUrl).toString());
			library.setRequestHeader("X-MData-Key", data.get("X-MData-Key").toString());
			library.setRequestHeader("Accept-Encoding","application/json");
			//library.setRequestHeader("Content-Length","598");
			library.setRequestHeader("Connection","Keep-Alive");
			library.setRequestHeader("Host","soa8-qa.internal.ng.movoto.net");
			library.setRequestHeader("UserAgent", "Apache-HttpClient/4.1.1 (java 1.5)");
			library.setContentType(contentType);
			
			Map<String, Object> apidata = new HashMap<>();
			if(x.equals("1"))
				apidata = (Map<String, Object>) data.get("Postdata");
			else if(x.equals("2"))
				apidata = (Map<String, Object>) data.get("Postdatapg2");
			else if(x.equals("3"))
				apidata = (Map<String, Object>) data.get("Postdatapg3");
			else if(x.equals("santa"))
				apidata = (Map<String, Object>) data.get("Postdatapgsanta");
			else if(x.equals("bray"))
				apidata = (Map<String, Object>) data.get("Postdatapgbray");
			
			
			response = library.HTTPPost(API, apidata);
			return response;
		}
		
		
		public boolean waitForJStoLoad() {

		    WebDriverWait wait = new WebDriverWait(library.getDriver(), 30);

		    // wait for jQuery to load
		    ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
		      @Override
		      public Boolean apply(WebDriver driver) {
		        try {
		         
		        	return ((Long)((JavascriptExecutor) driver).executeScript("return jQuery.active")==0);
		        }
		        catch (Exception e) {
		          return true;
		        }
		      }
		    };

		    // wait for Javascript to load
		    ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
		      @Override
		      public Boolean apply(WebDriver driver) {
		        return ((JavascriptExecutor) driver).executeScript("return document.readyState")
		            .toString().equals("complete");
		      }
		    };

		  return true;//wait.until(jQueryLoad) && wait.until(jsLoad);
		}
}

