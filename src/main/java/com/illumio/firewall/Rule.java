package com.illumio.firewall;

//Bean used to store rule efficiently used to create hash
public class Rule {

	// Rule parameters
	private String direction;
	private String protocol;
	private Integer port;
	private long ipAddress;

	// Constructor
	public Rule(String direction, String protocol, String port, long ipAddress) {
		this.direction = direction;
		this.protocol = protocol;
		this.port = Integer.parseInt(port);
		this.ipAddress = ipAddress;
	}

	//Getters and Setters
	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public long getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(long ipAddress) {
		this.ipAddress = ipAddress;
	}

	// Overriding equals method
	@Override
	public boolean equals(Object o) {
		Rule networkRule = (Rule) o;
		return direction == networkRule.direction && protocol == networkRule.protocol && port == networkRule.port
				&& ipAddress == networkRule.ipAddress;
	}

	// Overriding tostring method - Used to create hash
	@Override
	public String toString() {
		return this.direction + "-" + this.protocol + "-" + Integer.toString(this.port) + "-"
				+ Long.toString(this.ipAddress);
	}

	// Creating a 64 bit long hash as the default hashcode is only of int size to
	// reduce the collisions. Could have used Guava ibrary to optimize it further
	// Taken from:
	// https://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings
	public long hash() {

		long h = 1125899906842597L; // prime
		String string = this.toString();
		int len = string.length();
		for (int i = 0; i < len; i++) {
			h = 31 * h + string.charAt(i);
		}
		return h;
	}

}
