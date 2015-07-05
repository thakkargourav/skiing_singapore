#Problem Statement

Sometimes it's nice to take a break and code up a solution to a small, fun problem. Here is one some of our engineers enjoyed recently called Skiing In Singapore.

Well you can’t really ski in Singapore. But let’s say you hopped on a flight to the Niseko ski resort in Japan. Being a software engineer you can’t help but value efficiency, so naturally you want to ski as long as possible and as fast as possible without having to ride back up on the ski lift. So you take a look at the map of the mountain and try to find the longest ski run down.

In digital form the map looks like the number grid below.

4 4
4 8 7 3
2 5 9 3
6 3 2 5
4 4 1 6

The first line (4 4) indicates that this is a 4x4 map. Each number represents the elevation of that area of the mountain. From each area (i.e. box) in the grid you can go north, south, east, west - but only if the elevation of the area you are going into is less than the one you are in. I.e. you can only ski downhill. You can start anywhere on the map and you are looking for a starting point with the longest possible path down as measured by the number of boxes you visit. And if there are several paths down of the same length, you want to take the one with the steepest vertical drop, i.e. the largest difference between your starting elevation and your ending elevation.

On this particular map the longest path down is of length=5 and it’s highlighted in bold below: 9-5-3-2-1.

4 4
4 8 7 3
2 5 9 3
6 3 2 5
4 4 1 6

There is another path that is also length five: 8-5-3-2-1. However the tie is broken by the first path being steeper, dropping from 9 to 1, a drop of 8, rather than just 8 to 1, a drop of 7.

Your challenge is to write a program in your favorite programming language to find the longest (and then steepest) path for a given map.

# Minimum Requirements
Java 8 - This program uses Java 8's streams and lamda functions.
# Building
Checkout from github:
```sh
git clone https://github.com/thakkargourav/skiing_singapore.git
```
Build using maven:
```sh
mvn clean package
```
# Running
You can download the latest jar from [Releases](https://github.com/thakkargourav/skiing_singapore/releases) or build yourself using the command provided above.

You can run the jar using following command:
```sh
java -jar <pathToJar> <pathToFile>
```
* pathToJar is the path to jar file. If you have build it using maven it will be ${project_path}/targets/skiing-singapore-1.0-jar-with-dependencies.jar
* pathToFile is a parameter. It is a path to a file that contains the map.

