# illumiotakehome

# Implementation Details
1) Used Spring Boot to get the skeleton architecture with Maven build using https://start.spring.io/
2) FirewallApplication class is the entry point which creates a Firewall class object and passes the path to the network rules as constructor.
3) The csv file is then processed and each rule is created using a Rule class bean and the all the parameters are hashed.
4) A 64-bit hash function has been implemented. All these hashes are stored in a hashset which is a static variable.
5) accept_packet function is implemented which accepts all the 4 parameters and the hash is verified in the hash table. If the hash is found then the packet is accepted or else rejected.
6) All business rules are implemented which takes care of ranges for ports and ip addresses passed in the csv file. The ip address range is solved by converting the ip address to a decimal using a helper function.


# Test Startegy
1) Firewall class has a main method which tests all the functions individually. The main method calls each function with a valid or invalid input and gauges the output returned. This serves as unit test cases.
2) I have taken two seperate bins namely positive test cases and negative cases and called accept_packet in the Firewall application.
3) I tested various scenarios by giving incorrect port number, incorrect ip address which is not present in the rules csv file.
4) Edges cases such as giving port number out of the range, invalid ip address, ip address which is not in the range of given csv file have been given.

# Trade Offs:
1) Used the hash of all the valid parameters and stored in the hash table. This makes the search fast but the memory is going to take a hit as we are maintaining an in memory hash table.
2) Eventhough I am using a 64 bit hash there is still a good chance of having collisions. Having a Map with a list to the of valid ip's next to them could solve this problem.
3) If given additional time I would implment the above map with hash as key and a list of rules in values of the map.
4) I would also write JUnit test cases instead of using the main method to test individual functions.

# Packaging and Running:
1) The project uses Maven to build the jar file. Maven is a packaging utiliy. 
2) Clone the repository and go into the illumiotakehome folder and execute the following command mvn clean install
3) The above command packages the application and generates a jar file in the target folder.
4) Now go into the target folder and run the following command java -jar firewall-0.0.1-SNAPSHOT.jar path_to_the_rules_csv_file
5) My console output: https://github.com/BharathaAravind/illumiotakehome/blob/master/src/main/resources/Screenshot.PNG

# Team preference:
1) My order of preference would be platform team and policy team.
2) I look forward for your feedback regarding my implementation and design

# Citations:
1) https://www.mkyong.com/java/java-convert-ip-address-to-decimal-number/
2) https://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings
