- 01_initialize
  - initialize main controller, scene etc. 
  - Main: handles sceen switching and starging the game, gives ability to play multiple times without closing the program.
  - TIS_CONTROLLER: the high level game logic, will handle moving results from smaller computers to the larger one, deciding which smaller TIS to provide input on etc. based off screen selection 
  - TIS: one of the smaller computers, will be like 9 total. Each will have an interpreter, get passed in input when approproate, then handle that input and return as needed. 
  - UI: all the ui will take place here. Need to figure out how we want to represent everything logically in the other two models so the UI class will be simple

- 02_scene_switching
  - adding logic to move between the start scene, game scene, and end scene. making buttons work, basic scaffolding of the game initialization.

- 03_controller_logic

- 04_name_changes 
  - change 'TIS' to 'silo'
  - change 'TIS_Controller' to 'controller'

- 05_silo_logic 
  - all the logic for interpreting instructions given to a silo
  - 
- 07_fixing_labels 
  - fixed silo methods to actually go to the correct locations after the label methods. 

- 08_game_loop
  - animation timer and main game loop logic in controller
  - added method to find a label based off of the string in silo 
  - main gameplay loop logic 
  - added remaining cases to the play method in silo.
  - added method in silo to update index, which loops around back to the beginning infinitely