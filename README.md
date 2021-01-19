# DNSSinkholeServer
## Table of contents
* [General info](#general-info)
* [RFC documents](#rfc-documents)
* [Description](#description)
* [Setup](#setup)

## General info
This project implement a DNS Sinkhole server that blocks resolution for a given list of domains (hence the name “sinkhole”)and iteratively resolves any request for domains not included in that block-list.

## RFC documents
* [RFC 1035](https://tools.ietf.org/html/rfc1035), Domain Names - Implementation and Specification
	
## Description
Description for each file:
* DomainParser - Get content and a start position and read domain (labale or pointer)
* eType - enum file that contains the resource record type (Answer, Authority)
* Headers - Represents the Headers section contains Getter/Setter for all the information needed to build the Message
* Message - Represents a request or response, contains all the query fields and knows how to access each of them
* Question- Represents the question section, a Question gets content and a start position and knows how to read the domain to know when it started and when it ended
* ResourceRecord - Represents the answer, authority, and additional sections, with the help of eType, ResourceRecord knows whether it should read, how to read and what fields to fill (For example, in authority we would like to read Rdata)
* SinkholeServer - Represents the DNS Server, first if there is blocklist then create hashmap blocklist, Listen on UDP port 5300, get DNS query  and iteratively finds an answer for it, send the final response to the client
* UDPClient - Create UDP client to send and receive UDP packets.
* UDPServer - Create UDP server, after receiving a packet send to the source of the received packet.
* blocklist.txt - list of domains that SinkholeServer need to block 
	
## Setup
To build and run project:

```
$ javac -d out/ -Xlint src/*.java
$ java -cp out SinkholeServer
$ java -cp out SinkholeServer blocklist.txt$ 
```

