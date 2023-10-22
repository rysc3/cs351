# Frogger

Frogger is an arcade game that saw it's popularity peak in the early 80's. My implementation has multiple parameters to note, some key differences, & some key similarities to the original arcade game.

Control the frog using the the `w` `a` `s` `d` keys, and try to make it to the end without being hit.

## The Game Logic 
The majority of the game logic takes place in Frogger.java. This is where we actually handle the game loop, spawning and removing of enemies, and logic to catch game endings. We pass main into Frogger.java so that I can change the current scene to the win, lose, or start screen based off of the conclusion of the game. 

There are a number of neat stats I have provided on the conclusion page for both winners and losers. These are just a number of things that I thought might be interesting to track. 

The board is broken up into "Rows" on which the frog can move, and the enemies move. each enemy has a Direction, which determines both his direction, speed, and direction. The Row, direction, sprite, and velocity are all chosen completely at random each time one is generated. There are also a number of "safe" rows that no enemies can spawn on, which are green. I have hard coded these rows at random to get a board I liked. These could have also been chosen at random, but at some point I'm literally just leaving everything up to random. 

While rows are what divides the scene vertically, there is also a sort of invisisble grid that is going on which is what determines the frog's movement. The enemies move completely free of this frog grid. If the frog and the enemies were on the same grid we would either require more complicated logic for the frog to move in larger steps than the enemies, or the enemies would be moving in a much more choppy fashion.

Frogger.java has an array list of all of the enemies currently on the screen. To avoid a concurrent modification exception by removing an enemy from the enemies array while still looping over it, we create an enemies to remove array list at the beginning of each clock cycle in the animation timer, we then loop through each enemy in our enemies array, add the ones that are no longer valid to the removable array list, then remove and create new ones right away. 

I have chosen the current number of players based on how javafx handles the image handling. My game is not necessarily the most efficient, and while it does use a canvas graphics context to help with buffering issues, I have not put in extra work to make it run as easily as possible. Because of this you will start to see the program lag as expected when updating the number of eemies. I suspect this is partially due to the fact that for every enemy I am rendering a square javafx type, and then rendering the image ontop of that, which is how I'm handling the movement and collisison logic. 


## Parameters 
- NUM_ENEMIES 
  - integer in Frogger.java which determines the number of enemies on the screen at any given time, divided randomly amongst all rows.
- GAME_WIDTH
  - the width of the screen that is open while the game is actively running 
- GAME_HEIGHT 
  - the height of the screen that is open while the game is actively running 
- NUM_ROWS & ROW_SIZE 
  - determines the number of rows and the size (in pixels) of each row on the screen. These are not optimized to be easily changed, but can be if needed. 
- FROG_MOVE 
  - the size (on the x axis) of each jump the frog can make in pixels.

## things to note 
- Images 
  - The frog (a crab in my game) and the enemies are an assortment of Sprites [which are free to download.](https://www.kenney.nl/assets/tiny-dungeon) My enemy sprites are located at /com/Sprites01/* and my frog sprite is in /com/Sprites02/crab01.png
  - any image could be added to this directory to change the frog or the enemies if you wanted, so long as they are `png` format and follow the naming convention. Adding one without removing another would confuse the logic revolving around picking the images, so it would be advisable to remove one image to place a new one in, and update the file name accordingly.