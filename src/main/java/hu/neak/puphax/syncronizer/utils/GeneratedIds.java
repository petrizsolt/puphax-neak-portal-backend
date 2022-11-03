package hu.neak.puphax.syncronizer.utils;

public class GeneratedIds {
	private static long euhozzarId;
	private static long euindikaciokId;
	private static long bnoHozzarId;
	private static long eujoghozzarId;
	private static long orvosokEesztAzon;
	private static boolean successAllPupha;
	
	private GeneratedIds() {
		
	}
	
	public static void initUpdate() {
		euhozzarId = 1;
		euindikaciokId = 1;
		bnoHozzarId = 1;
		eujoghozzarId = 1;
		orvosokEesztAzon = 1;
		successAllPupha = true;
	}
	
	public static void unsuccess() {
		successAllPupha = false;
	}
	
	public static boolean isSuccessUpdate() {
		return successAllPupha;
	}
	
	public static Long getEuhozzarId() {
		return euhozzarId;
	}
	

	public static void incEuhozzarId() {
		euhozzarId++;
	}
	
	public static Long getEuindikaciokId() {
		return euindikaciokId;
	}
	
	public static void incEuindikaciokId() {
		euindikaciokId++;
	}
	
	public static Long getBnohozzarId() {
		return bnoHozzarId;
	}
	
	public static void incBnohozzarId() {
		bnoHozzarId++;
	}
	
	public static Long getEujoghozzarId() {
		return eujoghozzarId;
	}
	
	public static void incEujoghozzarId() {
		eujoghozzarId++;
	}
	
	public static Long getOrvosokEesztAzon() {
		return orvosokEesztAzon;
	}
	
	public static void incOrvosokEesztAzon() {
		orvosokEesztAzon++;
	}
}
