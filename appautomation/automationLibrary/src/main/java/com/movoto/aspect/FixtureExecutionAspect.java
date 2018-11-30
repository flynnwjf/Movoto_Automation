package com.movoto.aspect;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.movoto.data.DataManager;
import com.movoto.reporter.ReportManager;

@Aspect
public class FixtureExecutionAspect {

	ThreadLocal<Boolean> stopTest = new ThreadLocal<>();

	@Around("execution(public * com.movoto.fixtures.impl.FixtureLibraryImpl.*(..))")
	public Object generic(ProceedingJoinPoint point) {
		Instruction instruction = InstructionManager.getInstruction();
		Object result;
		String methodName = MethodSignature.class.cast(point.getSignature()).getMethod().getName();
		instruction.setScreenshotEnabled(false);
		instruction.setScreenshot("");
		instruction.setMethodName(methodName);
		instruction.setInstructionType(getInstructionType(methodName));
		Object params[] = point.getArgs();
		long start = System.currentTimeMillis();
		instruction.setStartTime(start);
		boolean assertFlag = getAssertFlag(instruction, params);
		result = null;
		try {
			Boolean stopFlag = stopTest.get();
			if (stopFlag == null) {
				stopFlag = Boolean.FALSE;
				stopTest.set(stopFlag);
			}
			if (!stopFlag || methodName.equalsIgnoreCase("quit")) {
				result = point.proceed();
			} else {
				result = Boolean.FALSE;
				instruction.setStatus("SKIPPED");
			}
			instruction.setErrorMessage("");
		} catch (Throwable e) {
			result = Boolean.FALSE;
			stopTest.set(Boolean.TRUE);
			e.printStackTrace();
			instruction.setStatus("EXCEPTION");
			instruction.setErrorMessage(e.getMessage());
		}
		long end = System.currentTimeMillis();
		long duration = end - start;
		double durationSec = duration / 1000.00;
		instruction.setEndTime(end);
		instruction.setReturnValue(String.valueOf(result));
		// System.out.println("Step: " + methodName + " Duration: " +
		// durationSec);
		String str = "";
		for (Object obj : params) {
			String s = String.valueOf(obj);
			str += s + " , ";
		}
		// System.out.println("Step: " + methodName + " Params: " + str + "
		// Duration: " + durationSec);
		try {
			int index = str.lastIndexOf(",");
			str = str.substring(0, index);
		} catch (Exception e) {
			// TODO: handle exception
		}
		instruction.setParams(str);
		Boolean reportStatus = Boolean.TRUE;
		if (assertFlag) {
			if (!(Boolean) result) {
				reportStatus = Boolean.FALSE;
			}
		}
		if (instruction.getStatus() == null || instruction.getStatus().length() <= 0) {
			if (reportStatus) {
				instruction.setStatus("PASSED");
			} else {
				instruction.setStatus("FAILED");
			}
		}
		
		if(!reportStatus && instruction.isScreenshotEnabled()){
			try {
				WebDriver driver = DataManager.getData().getDriver();
				if (driver != null) {
					instruction.setScreenshotEnabled(true);
					String screenShot = getScreenshot(driver, "test-output/screenshots");
					instruction.setScreenshot(screenShot);
					ReportManager.log("Screenshot", screenShot);
				} else {
					ReportManager.log("Screenshot", null);
					instruction.setScreenshotEnabled(false);
				}
			} catch (Exception e) {
				ReportManager.log("Screenshot", null);
			}
		}
		
		String instructionData = InstructionManager.getInstructionData();
		ReportManager.log(instructionData, true);
		InstructionManager.reset();
		return result;
	}

	private String getScreenshot(WebDriver driver, String outDir) {
		String path = null;

		try {
			File dir = new File(outDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			int num = new Random().nextInt(10000);
			File dest = new File(outDir + File.separator + "Screenshot" + num + ".jpg");
			FileUtils.copyFile(file, dest);
			path = outDir + "/" + dest.getName();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;

	}

	@After("execution(public * com.movoto.fixtures.DriverFixtures.*(..))")
	public void afterDriver(JoinPoint point) {
		WebDriver driver = DataManager.getData().getDriver();
		Instruction instruction = InstructionManager.getInstruction();
		boolean isStepScreenshotEnabled = DataManager.getData().getTestProperties().isStepScreenshotEnabled();
		boolean isAssertScreenshotEnabled = DataManager.getData().getTestProperties().isAssertScreenshotEnabled();
		String type = instruction.getInstructionType();
		
		if (isStepScreenshotEnabled || (isAssertScreenshotEnabled && type!=null && type.equalsIgnoreCase("assert"))) {
			try {
				if (driver != null) {
					instruction.setScreenshotEnabled(true);
//					String screenShot = getScreenshot(driver, "test-output/screenshots");
//					instruction.setScreenshot(screenShot);
//					ReportManager.log("Screenshot", screenShot);
				} else {
//					ReportManager.log("Screenshot", null);
					instruction.setScreenshotEnabled(false);
				}
			} catch (Exception e) {
//				ReportManager.log("Screenshot", null);
			}
		}
	}

	private boolean getAssertFlag(Instruction instruction, Object params[]) {
		if (instruction.getInstructionType().equalsIgnoreCase("assert")) {
			if (params.length == 2) {
				Boolean isAssert = Boolean.valueOf(String.valueOf(params[1]));
				return isAssert != null ? isAssert : false;
			}
		}
		return false;
	}

	private String getInstructionType(String methodName) {
		String type = "action";
		if (methodName.startsWith("verify") || methodName.startsWith("is")) {
			type = "assert";
		} else if (methodName.startsWith("get")) {
			type = "get";
		} else {
			type = "action";
		}

		return type;
	}

}
