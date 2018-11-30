package com.movoto.aspect;

import com.google.gson.Gson;

public class InstructionManager {
	private static ThreadLocal<Instruction> currentInstruction = new InheritableThreadLocal<>();
	
	public static synchronized Instruction getInstruction(){
		Instruction instruction = currentInstruction.get();
		if(instruction==null){
			instruction = new Instruction();
			currentInstruction.set(instruction);
		}
		return instruction;
	}
	
	
	public static synchronized String getInstructionData(){
		Instruction instruction = currentInstruction.get();
		if(instruction!=null){
			Gson gson = new Gson();
			String json = gson.toJson(instruction);
			return json;
		}
		return null;
	}
	
	public static synchronized void reset() {
		currentInstruction.set(null);
	}
	
}
