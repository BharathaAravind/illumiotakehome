package com.illumio.firewall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirewallApplication {

	private static Firewall firewall;

	public static void main(String[] args) {

		SpringApplication.run(FirewallApplication.class, args);

		if (args.length == 1) {
			// passing the path to the constructor
			firewall = new Firewall(args[0]);
		} else {
			System.out.println("Invalid number of inputs");
		}

		// Test Cases - Positive
		// All rules match an entry
		System.out.println(firewall.accept_packet("inbound", "tcp", 80, "192.168.1.2"));
		System.out.println(firewall.accept_packet("inbound", "udp", 53, "192.168.1.2"));

		// Test Cases - Negative
		// All inputs valid but the ip is out of range
		System.out.println(firewall.accept_packet("outbound", "tcp", 80, "192.168.1.2"));
		// All inputs valid but the port is out of range
		System.out.println(firewall.accept_packet("outbound", "tcp", 999, "192.168.10.11"));
		// Invalid ip provided
		System.out.println(firewall.accept_packet("outbound", "udp", 10000, "192.168.256.255"));
		// Invalid port provided
		System.out.println(firewall.accept_packet("inbound", "tcp", 0, "192.168.1.2"));

	}

}
