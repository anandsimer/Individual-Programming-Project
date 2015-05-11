# Individual-Programming-Project
Network Applications Individual Programming P2P Chat System

Project work to build peer to peer chat application, using Java programming language, 
this is purely work of me alone, since it is a Individual-Programming-Project.

In this project there are Two main classes that are required to run on any node to 
become part of the Peer to Peer chat program on any given network. 

1) Service.java
   ------------ 

This is the class that any node need to connect, and node need to know the IP address of the machine
on which the service is running. Whenever a new node connects to the service, the RoutingInfo is passed on from 
service to the node. 

2) Node.java
   ---------

This is class that need to run for any node/user to connect to the Service.java. All nodes are identified by 
integer, which the user need to pass-on to the service class. The connection is being made on port 8767. 
Joining and leaving of new nodes will be performed from the node.java class, using 'join' keyword to join the
network and 'bye' to leave the chat.


Then there are few other classes whose description we need to know, in order to understand the communication flow:

3) ListenNode.java
   ---------------

Node.java uses this class to read the incoming messages from network and route them to other nodes if not required 
by current node, else it consumes it.

4) NodeThread.java
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


WorkFlow of the application:
============================

Every machine on the network, is required to execute the first two classes, Service.java and Node.java, however, the boot node may not require the Node.java. Service becomes the boot of P2P chat program. If user need to send the message to the network to all users, then he/she must connect to the known Service.java (means it could run on any system just he/she needs to know the IP address of the machine that runs the Service.java) Service.java accepts the connections by default on port 8767. 
To connect to the service.java the user need to run the node.java, which ask the user for IP address to which it wants to connect to. Node program ask user for Integer ID, which is passed on to the service program, to store the ID & IP of the node to the routingTable. And then node program ask for interest("TAG", it takes a string that represent the kind of messages the current user is interested in or represent how the user should reached).
When successfully connected the service program pass on the routing information it got to the node.

Broadcast: Message to node C (suppose) is passed from node A, where both node A and node C are not known to each other. When a message is typed from node A, it reaches to two services (one of it own and to one to which it is connected) and each service is having the routing_information of the individual nodes connected to them. This was a three way broadcasting is done, from using node's routing_information and two from two different services program. This adds some load to the network, to lower the network usage, I have added few logic in the code, that prevents the receiver to send the message back to the sender while broadcasting (to avoid the loop of message sending and receiving between two nodes) and have added the hop_count integer to the message, which increases by one whenever, message take hop from one node to another. Once the count reaches the count of four, the receiver does not forward the message.  When the message reaches the user who is interested in such messages, it consumes it and does not forward it to lower the redundancy. 

For Example : When node observes the messages of interest it consumes it by displaying Message to user that states 
"Happy to find the message of interest("TAG"), consuming it" and when message of no interest ("TAG") arrived the 
node publishes message to the log that states, "Message of not interest, forwarding to other nodes".

Routing Table:  Every Service which connects with the nodes, maintains their ID, IP to hashtable and passes this routing information to the all nodes, however, not creating the redundant routing information to each node connected to it. Each node connected to same service, will *NOT* have same routing table information.


Test:  Yes, I have tested the application on the distributed systems of six computers at home, where different connectivity pattern were tested, hybrid (where one service connected to one or more nodes), chain of connected nodes( each having its own service program connection).

Features to be added: Acknowledgement of the messages and using the JSON to forward the message. 

Conclusion : Peer to Peer Chat program is ready to use, although little more enhancement can be done, if time permits will add all new features and will update. Not completely modelled on Pastry, however, could be regarded as sub-pastry P2P implementation. 