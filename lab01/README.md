# cs351 Lab 01
## About
  file runs until `DONE` is entered
## Data formatting
  Ran into an interesting issue with the format of the data. The given input data is in the format "2-4,6-8". This 
  introduces the issue of getting our data into a format in which we can actually read it how we want. Taking in the 
  filepath as an argument, we use File to open it, and then a scanner object to read the data from the file. 
  - At first thought, you would think simply calling Scanner.nextInt() would work, but since our data is not 
    formatted to have spaces, Scanner.next() returns the entire line. 
  - I ran into another issue when trying to do `String[] lineArr = line.split(",");` followed by `int test = lineArr
    [0].charAt(1);` and implicit conversion between chars and integers in java. This causes the issue where `char 
    "2"` does not actually equate to `int 2`, but rather to `int 20`. This is becuase we are converting from the 
    ascii table value to the decimal value associated with that string. Technically, it would still be fine to leave 
    it this way since "2" will always be less than "3" etc. but I'm just realizing that when writing this readme and 
    already corrected it lol.
  - You'll also run into the issue of using `Integer.parseInt();` when trying to pass a character with `.charAt();` 
    unless you get a little crazy and do `Integer.parseInt(lineArr[0].charAt(0).toString());` or something along 
    those lines which is getting a little crazy. 
  - Overall, the correct solution I arrived at was to assign `lineArr` in the same way as above, and then assign 
    each of my values, Lower & Upper for both sets in the following format: `l1 = Character.getNumericvalue(lineArr
    [0].charAt(0));`.
  - I am also checking the format of my data ahead of time. I validate that the lower value of a given set is always 
    less than or equal to the upper of that set. This saves some headaches in the below `within` & `overlapping` 
    calculations, and I've been told I can assume perfect input data.
## Checking weather within or overlapping
  After the above mess, the operation is relatively simple; we only have two cases we need to check for the within & 
  overlapping cases.
  ### Within is true when:
    `( Upper 1 >= Upper 2 ) && ( Lower 1 <= Lower 2 )` or `( Upper 2 >= Upper 1 ) && ( Lower 2 <= Lower 1 )`
  - this field takes the original function or the inverse of it, since according to the lab info page at /info/lab1.
    pdf it shows we should actually allow set 1 to be within set 2, or set 2 to be within set 1.
  ### Overlapping is true when
    `( Lower 1 < Lower 2 ) && ( Upper 1 < Upper 2)` or `( Lower 1 > Lower 2 ) && ( Upper 1 > Upper 2 )`
  - covers all situations in which one field would be overlapping the other.
## Checking weather within or overlapping
   
- Input file must be the exact format of the file at /info/test_case1.txt
  #### Run from the command line:
  - clone: `git clone https://github.com/rysc3/cs351Lab01.git`
  - enter dir: `cd cs351Lab01`
  - compile: `javac cs351Lab01.java`
  - run: `java cs351Lab01.java <type> <absolute file path>`, ex: `java cs351Lab01.java within 
    C:\ryansAwesomeFiles\input.txt`
  #### Output from test cases:
        PS \cs351Lab01> java .\cs351Lab01.java
        Enter 'within' or 'overlap': within
        Enter absolute filepath: C:\ryansAwesomeFilePath\input.txt
        within: 2                    
        Enter 'within' or 'overlap': overlap
        Enter absolute filepath: C:\ryansAwesomeFilePath\input.txt
        overlap: 4                   
        Enter 'within' or 'overlap': done
        PS \cs351Lab01>
      