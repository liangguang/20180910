package com.lgp.utils.system;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MachineNameUtils {

	
	public static String CreateMachineName(){
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addr.getHostName().toString();
	}
	
	
}
