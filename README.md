# Individual-Programming-Project
Network Applications Individual Programming P2P Chat System

Project work to build peer to peer chat application, using Java programming language, 
this is purely work of me alone, since it is a Individual-Programming-Project.

In this project there are Two main classes that are required to run on any node to 
become part of the Peer to Peer chat program on any given network. 

1) Service.java
   ------------ 

This is the class that any node need to connect, and node need to know the IP address of the machine
on which the service is running. 

2) Node.java
   ---------

This is class that need to run for any node/user to connect to the Service.java.


Then there are few other classes whose description we need to know, in order to understand the communication flow:

3) ListenNode.java
   ---------------

Node.java uses this class to read the incoming messages from network and route them to other nodes if not required 
by current node, else it consumes it.

4)  NodeThread.java
    ---------------

This class is used by Service class to read the messages coming from the nodes, which are present in its routing table
and measure the hop_count, if it exceeds the value (threshold) then destroys it. Else, forwards to all other nodes except 
to the node from where the message arrived. 

5) ServiceRead.java
   ----------------

It is an extension to above NodeThread class to broadcast the message to all the nodes in the routing table.

7) UpdateRouting.java
   ------------------
  
Maintains the routingTable, which contains the information of the nodes connected to the service. 