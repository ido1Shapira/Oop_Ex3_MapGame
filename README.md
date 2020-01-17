# the maze of waze ex #3

Our project represents a game.

In our game there are 24 stages (scenarios) each of them has a different starting point.

Every stage has its own conditions including the duration of the stage, number of robots, number of fruits and a Draph representing the arena the game is at.

The game itself has two wayes of playing

* Manual managment means the client can choose where to put his robots and how to move them every step of the way using the mouse.

* Automatic managment means that our algorithm is responsible of locating the robots and
move them toward the fruits in order to acheve as many fruits as possible

## Our classes

* ### data stuctures- in this package you can find all data stuctures we are using which are the Dgraph and all of its components- nodes, edges etc.

Dgraph- a class represents a directed graph who is made of nodes and edges every edge has a source node and 
a destination node, which means this edge creates a path from source to destination (the opposite path may not exist) 
every edge has a weight filed that represents the "cost" of using this edge during a path,
we would like to think about this cost as the time it takes to get from source node to destination node. 
that is why nonpositive weight is not possible.
more information about Dgraph can be found here- https://github.com/idoshapira051/OPP_ex2-4

* ### game client - in this package there are all of the classes managing the game

* AutoManager - this class is responsible of managing the game in an automatic way according to the moving algorithm.

* ManualManager - this class is responsible of managing the game in a manual way according to the client decisions.

* Logger_KML - this class represents a thread recording the game in a kml format. The thread is activated every 100 milliseconds
and screenshots the game in a kml format using the data from the server 

* MyGameGui - this class represents the screen displaying the game. Every 100 milliseconds 
the window updates which give the client the feeling of watching the game live.
we did the updating using a thread that its only job is to take the current state of game from the server and repaint it on the screen
this class uses stdDraw class writen by Robert Sedgewick and Kevin Wayne. more information about stdDraw can be found here 



