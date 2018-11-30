package com.movoto.tests;

import java.util.Date;
import org.testng.annotations.Test;

public class PathTest {

	@Test
	public void testPath() {
		Date date = new Date();
		String dateStr = date.toString();
		System.out.println(dateStr);
		String dates[] = dateStr.split(" ");
		System.out.println(dates[1]);		
	}
	
}
