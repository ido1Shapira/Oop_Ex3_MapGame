# the maze of waze ex #3+4 
  
Our project represents a game. 
  
In our game there are 24 stages (scenarios) each of them has a different starting point. 
  
Every stage has its own conditions including the duration of the stage, number of robots, number of fruits and a Dgraph representing the arena the game is at. 
  
The game itself has two ways of playing 
  
* Manual management means the client can choose where to put his robots and how to move them every step of the way using the mouse. 
  
* Automatic management means that our algorithm is responsible of locating the robots and 
move them toward the fruits in order to achieve as many fruits as possible 
  
# assignment.4 extends assignment.3

main differences between assignment 3 and assignment 4 

* within assignment 4 every time someone plays the game the program sends the player resoults to a DataBase.

* by using the DataBase we manage to see what is our best grade in each level and what is our position in relation to the class

* in assignment 4 there are mendetory parameter the player needs to pass before playing next stage.
mendetory resoult are spesific for each level and contains number of points we have to get and number of moves we must'nt cross.

* in assignment 4 in order to play a stage the player has to pass all the previos stages according to the menedory resoults


## Our classes 
  
 ### data structures-  
in this package you can find all data structures we are using which are the Dgraph and all of its components- nodes, edges etc. 
  
Dgraph- a class represents a directed graph who is made of nodes and edges every edge has a source node and  
a destination node, which means this edge creates a path from source to destination (the opposite path may not exist)  
every edge has a weight filed that represents the "cost" of using this edge during a path, 
we would like to think about this cost as the time it takes to get from source node to destination node.  
that is why nonpositive weight is not possible. 
more information about Dgraph can be found here- https://github.com/idoshapira051/OPP_ex2-4 

One method we added to Dgraph is init from a json string. This method gets a json Dgraph and convert the Dgraph it was aplied on to the graph described in the string
  
  ### game client- 
in this package there are all of the classes managing the game 
  
* AutoManager - this class is responsible of managing the game in an automatic way according to the moving algorithm. this class is actually a thread activated during the game and moving the robots that need redirection 
  
* ManualManager - this class is responsible of managing the game in a manual way according to the client decisions. this class is actually a thread activated during the game and moving the robots according to the client's mouse 
  
* DBquery- This class is responsible of the communication with the server's DataBase in this class every method is in charge of a DB query or organizing information we got from the DB To shorten the waiting time, the class will keep a copy of the Logs table.
This class helps the client watch database information about his grades in the game and his position in relation to rest of the class
 
* Logger_KML - this class represents a thread recording the game in a kml format. The thread is activated every 100 milliseconds 
and screenshots the game in a kml format using the data from the server  
  
* MyGameGui - this class represents the screen displaying the game. Every 100 milliseconds  
the window updates which give the client the feeling of watching the game live. 
we did the updating using a thread that its only job is to take the current state of game from the server and repaint it on the screen 
this class uses stdDraw class written by Robert Sedgewick and Kevin Wayne. more information about stdDraw can be found here https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html 
  
* SimpleGameClient - this class has the "main" method running the whole project. In order to start playing the game all you need to do is run this class. 
  
###  algorithms - 
in this package there are all the classes responsible of moving algorithms used in automatic mode 
  
* Graph_algo- in this class there are many algorithms can be applied on Dgraphs,  
in this project we use only "shortestPathDist" and "shortestPath". more information about Graph_algo can be found here- https://github.com/idoshapira051/OPP_ex2-4 
  
* Moving_algo- this class has two main methods in charge of moving the robots 
  
logicWalk- this method's goal is to move the robots in a logical way. 
logical way means that after the game ends the robots' value would be the biggest under the given circumstances. 
of course that our algorithm is not optimal, but it is yet a good algorithm. 
the main thinking of moving the robots is a greedy algorithm  
that moves every robot one step toward the nearest fruit from where he stands at the moment. 
we do it by finding all of the fruit src node and calculate which fruit is the nearest to us 
by using "shortestPathDist", after finding the closest fruit we calculate the quickest path 
using "shortestPath" and sending the robot to the first node in that list. 
Our way of preventing robots from uniting is to make sure that every robot has a different destination node. 
 ****this methos is used only in assignment 3

logicNewWalk- this method's goal is to move the robots in a logical way. logical way means that after the game ends the robots' value would be the biggest under the given circumstances. of course that our algorithm is not optimal, but it is yet a good algorithm. the main thinking of moving the robots is a greedy algorithm  that moves every robot one step toward the nearest fruit from where he stands at the moment Our way of preventing robots from uniting is to make sure that every robot has a different destination node.
  ****this methos is used only in assignment 4
  
randomWalk- this method's goal is to move the robots in a random way. 
     
### myUtils- 
in this package there are two important classes used by all the other classes 
  
* MyParser- this class helps us parse a json string to an object's fileds and take the information we now want. 
in this class there are many "getter" methods since we chose to work with the actual json string given from the server instead of turning it to an object and take the information from it every time. 
  
* MyServer- this class has one field of Game_Server object given from the jar.  
all of the other classes use this single instance of server in order to get information about the game. 
   
* myFruit-  this class represents a fruit in the game

*myRobot - this class represents a robot in the game

a game window for example:

![ללא שם](https://user-images.githubusercontent.com/57194044/72616158-f9355900-393e-11ea-89b7-5995426a5860.jpg)

 
 kml window for example!

![kml](https://user-images.githubusercontent.com/57194044/72685501-828d8c80-3af3-11ea-8ee9-ce62e1eb0c47.jpg)

Maximum grades table:

![table](https://user-images.githubusercontent.com/57194044/72685503-86211380-3af3-11ea-98e9-1ee1ffa3e5c6.jpg)
