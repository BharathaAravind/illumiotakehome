package com.illumio.firewall;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// Firewall class
public class Firewall {

	// Stores all the vaid hashses of Rules. The toString() of Rule is hashed and
	// stored
	private static HashSet<Long> acceptedRules = new HashSet<>();;

	// Used for validations
	private static final int PORT_MIN = 1;
	private static final int PORT_MAX = 65535;
	private static final int IP_MIN_RANGE = 0;
	private static final int IP_MAX_RANGE = 255;

	// Constructor accepting the path to the rules csv file. Included the csv of
	// rules in src/main/resources folder
	public Firewall(String path) {
		processCSV(path);
	}

	// ProcessCSV function takes the path computes the hash and adds the rule to the
	// acceptedRules HashSet
	public static void processCSV(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = null;

			while ((line = br.readLine()) != null) {
				// Splitting the rule at comma
				String rule[] = line.split(",");

				// Validating the csv file parameters
				if (rule.length == 4 && checkDirection(rule[0]) && checkProtocol(rule[1]) && checkPort(rule[2])
						&& checkIPAddressRange(rule[3])) {
					// This portRange list will have all port numbers valid for a given rule
					List<String> portRange = getPortRange(rule[2]);

					// Iterating over the list of port numbers and each ip for a given port is added
					for (String portNumber : portRange) {
						// The the IP has range splitting at - and iterating over all ip's in that range
						if (rule[3].contains("-")) {
							String[] ipRange = rule[3].split("-");
							// Using the ipToLong function to get decimal number from ip address
							long minIP = ipToLong(ipRange[0]);
							long maxIP = ipToLong(ipRange[1]);
							// Iterating over the ip's add adding each ip to the accepted rules for every
							// portnumber
							for (long i = minIP; i <= maxIP; i++) {
								Rule newRule = new Rule(rule[0], rule[1], portNumber, i);
								acceptedRules.add(newRule.hash());
							}
						} else {
							// If it is a single IP. hashing and adding it to the rules
							Rule newRule = new Rule(rule[0], rule[1], portNumber, ipToLong(rule[3]));
							acceptedRules.add(newRule.hash());
						}
					}

				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
			e.printStackTrace();
		}
	}

	// Test Method pass all the required arguments tests whether it is present or
	// not
	// O(1) Time Complexity
	public boolean accept_packet(String direction, String protocol, int portNumber, String ip) {

		if (checkDirection(direction) && checkProtocol(protocol) && checkPort("" + portNumber)
				&& checkIPAddressRange(ip)) {
			Rule rule = new Rule(direction, protocol, "" + portNumber, ipToLong(ip));
			if (acceptedRules.contains(rule.hash())) {
				return true;
			} else {
				return false;
			}
		} else {
//			System.out.print("Invalid Inputs: " + direction + "->" + protocol + "->" + portNumber + "->" + ip);
			return false;
		}
	}

	// Method to validate direction
	public static boolean checkDirection(String s) {
		if (s.equalsIgnoreCase("inbound") || s.equalsIgnoreCase("outbound")) {
			return true;
		}
		return false;
	}

	// Method to validate protocol
	public static boolean checkProtocol(String s) {
		if (s.equalsIgnoreCase("tcp") || s.equalsIgnoreCase("udp")) {
			return true;
		}
		return false;

	}

	// Method to verify port range
	public static boolean checkPort(String s) {

		if (s.contains("-")) {
			// If port range is given
			String[] portRange = s.split("-");
			Integer minPort = Integer.parseInt(portRange[0]);
			Integer maxPort = Integer.parseInt(portRange[1]);
			if (minPort <= maxPort && minPort >= PORT_MIN && maxPort <= PORT_MAX) {
				return true;
			} else {
				return false;
			}
		} else {
			// If a single port number is given
			try {
				Integer port = Integer.parseInt(s);
				if (port >= PORT_MIN && port <= PORT_MAX) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Invalid port number");
				return false;
			}
		}
	}

	public static boolean checkIPAddressRange(String s) {

		// If a ip address range is given
		if (s.contains("-")) {
			String[] ipList = s.split("-");
			// Helper function to check validity
			return checkIPAddress(ipList[0]) && checkIPAddress(ipList[1]);
		} else {
			return checkIPAddress(s);
		}
	}

	// Given an ip address checks whether is valid or not
	public static boolean checkIPAddress(String s) {
		String[] octets = s.split("\\.");
		return checkOctet(octets[0]) && checkOctet(octets[1]) && checkOctet(octets[2]) && checkOctet(octets[3]);
	}

	// Check whether given octet is valid or not
	public static boolean checkOctet(String s) {
		int octet = Integer.parseInt(s);
		if (octet >= IP_MIN_RANGE && octet <= IP_MAX_RANGE) {
			return true;
		} else {
			return false;
		}

	}

	// Returns a list of port numbers given a range or a single number
	private static List<String> getPortRange(String s) {

		List<String> ports = new ArrayList<>();
		if (s.contains("-")) {
			// If range is given returning all possible values
			String[] portRange = s.split("-");
			Integer minPort = Integer.parseInt(portRange[0]);
			Integer maxPort = Integer.parseInt(portRange[1]);
			for (int i = minPort; i <= maxPort; i++) {
				ports.add("" + i);
			}
		} else {
			// If a sinlge port is given adding only that value
			ports.add(s);
		}

		return ports;
	}

	// Used code from:
	// https://www.mkyong.com/java/java-convert-ip-address-to-decimal-number/
	// Making IP to Long to easily iterate over and generate valid ip addresses
	// given range
	public static long ipToLong(String ipAddress) {

		long result = 0;
		String[] ipAddressInArray = ipAddress.split("\\.");
		for (int i = 3; i >= 0; i--) {
			long ip = Long.parseLong(ipAddressInArray[3 - i]);
			result |= ip << (i * 8);
		}
		return result;
	}

	/*
	 * Used this method to test the functions
	 */
	// public static void main(String[] args) {
	// System.out.println(checkDirection("inbound"));
	// System.out.println(checkDirection("outbound"));
	// System.out.println(checkProtocol("udp"));
	// System.out.println(checkProtocol("tcp"));
	// System.out.println(checkPort("65535"));
	// System.out.println(checkPort("65536"));
	// System.out.println(checkPort("1-65535"));
	// System.out.println(checkPort("1"));
	//
	// System.out.println(checkIPAddressRange("192.168.1.1-192.168.2.5"));
	// List<String> portRange = getPortRange("1-65");
	// for (String string : portRange) {
	// System.out.println(string);
	// }
	//
	// }
}
