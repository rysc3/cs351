/*
 * @author Ryan Scherbarth
 * @author Manny Metcalfe
 * @author Ambrose Hwang
 * 
 * Last Edited: 11/16/2023
 */
package com.prog04;


import javafx.animation.AnimationTimer;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.util.*;

import java.util.concurrent.*;


/**
 * Reads file and sets up UI and Silos.
 */
public class Controller {
    /*
     * Layout of games arraylist, as specified by the rubric.
     * | - - - - - - |
     * | 1 2 3 4 |
     * | 5 6 7 8 |
     * | 9 10 11 12 |
     * | - - - - - - |
     */

    private ScheduledExecutorService service;
    private CountDownLatch latch;
    private ArrayList<ArrayList<Silo>> games = new ArrayList<>();

    private final List<Object> waiting = Collections.synchronizedList(new ArrayList<>());

    private final ArrayList<String> instructions = new ArrayList<>();
    private final com.prog04.UI UI;

    private int gridSize;
    private int maxRows;
    private int maxColumns;
    private final ArrayList<ArrayList<String>> fullCompInstructions = new ArrayList<>();

    private final ArrayList<SiloStream> streams = new ArrayList<>();

    private long intervalTime = 50;

    private boolean step = false;


    private ArrayList<ArrayList<TransferRegion>> vertical = new ArrayList<>();
    private ArrayList<ArrayList<TransferRegion>> horizontal = new ArrayList<>();
    private String title;


    private final Main mainGame;

    private ArrayList<ArrayList<Thread>> threads = new ArrayList<>();


    private AnimationTimer timer;


    //Constructor that sets up controller
    public Controller(int[] size, Main main) {
        this.mainGame = main;
        readFile();
        this.UI = new UI(size); // initialize ui
    }


    //Getter function that gets the rows
    public int getRows(){
        return this.maxRows;
    }

    //Getter function that gets columns
    public int getColumns(){
        return this.maxColumns;
    }
    //Function that adds waiting delay to silos
    public void addWait(Silo silo){
        synchronized (waiting) {
            waiting.add(silo);
        }
    }

    //Gets value of the waiting value
    public List<Object> getWaiting(){
        synchronized (waiting) {
            return waiting;
        }
    }

    //Function that removes the wait from the silo
    public void removeWait(Silo silo){
        synchronized (waiting) {
            waiting.remove(silo);
        }
    }
    // initialize all the individual games to be played at once
    private void initializeGames() {
        intervalTime = (long) mainGame.getArtificialDelayValue();
        threads = new ArrayList<>();
        games = new ArrayList<>();
        vertical = new ArrayList<>();
        horizontal = new ArrayList<>();
        int rows = 0;
        for (int i = 0; i < maxRows; i++) {
            games.add(new ArrayList<>());
            threads.add(new ArrayList<>());
            for (int x = 0; x < maxColumns; x++) {
                if (fullCompInstructions.size() == maxColumns * maxRows) {
                    games.get(i).add(new Silo(fullCompInstructions.get(rows), this)); // Create 9 new games
//                    threads.get(i).add(new Thread(games.get(i).get(x)));
//                    for(int b = 0; b< fullCompInstructions.get(rows).size(); b++){
//                        System.out.println(i + " " + x);
//                        System.out.println(fullCompInstructions.get(rows).get(b));
//                    }
                    rows++;
                }
            }
        }

        //Creating vertical regions and ports..
        //One extra row versus silos.
        for (int i = 0; i < maxRows + 1; i++) {
            vertical.add(new ArrayList<>());
            for (int x = 0; x < maxColumns; x++) {
                Port port1 = new Port();
                Port port2 = new Port();
                if (i == 0) {
                    port1 = null;
                }
                if (i == maxRows) {
                    port2 = null;
                }
                TransferRegion region = new TransferRegion(port1, port2, i, x, TransferType.VERTICAL);
                vertical.get(i).add(region);

            }
        }
        //Horizontal regions and ports.
        for (int i = 0; i < maxRows; i++) {
            horizontal.add(new ArrayList<>());
            for (int x = 0; x < maxColumns + 1; x++) {
                Port port1 = new Port();
                Port port2 = new Port();
                if (x == 0) {
                    port1 = null;
                }
                if (x == maxColumns) {
                    port2 = null;
                }
                TransferRegion region = new TransferRegion(port1, port2, i, x, TransferType.HORIZONTAL);
                horizontal.get(i).add(region);

            }
        }

        //Changing ports for silo streams.
        for (int s = 0; s < streams.size(); s++) {
            SiloStream stream = streams.get(s);
            int row = stream.getRow();
            int column = stream.getColumn();
            StreamType type = stream.getType();
            ArrayList<Integer> values = new ArrayList<>();
            if (type == StreamType.IN) {
                values = stream.getCurrentValues();
            } else {
                values = stream.getUpdateList();
            }
            Port newPort = new Port(type, values);
            //Find region and change port.
            int portNum = 1;
            if (row < 0) {
                if (type == StreamType.IN) portNum = 2;
                TransferRegion region = vertical.get(0).get(column);
                region.setPort(portNum, newPort);
            } else if (row == maxRows) {
                if (type == StreamType.OUT) portNum = 2;
                TransferRegion region = vertical.get(vertical.size() - 1).get(column);
                region.setPort(portNum, newPort);
            } else if (column < 0) {
                if (type == StreamType.IN) portNum = 2;
                TransferRegion region = horizontal.get(row).get(0);
                region.setPort(portNum, newPort);
            } else if (column == maxColumns) {
                if (type == StreamType.OUT) portNum = 2;
                TransferRegion region = horizontal.get(row).get(horizontal.size() - 1);
                region.setPort(portNum, newPort);
            }
        }

    }

    private String findText(int silo) {
        int realBox;
        int row = silo/maxColumns;
        int column = silo % maxColumns;
        realBox = (row*4) + column;
        return switch (realBox) {
            case 0 -> mainGame.getTextFromArea1();
            case 1 -> mainGame.getTextFromArea2();
            case 2 -> mainGame.getTextFromArea3();
            case 3 -> mainGame.getTextFromArea4();
            case 4 -> mainGame.getTextFromArea5();
            case 5 -> mainGame.getTextFromArea6();
            case 6 -> mainGame.getTextFromArea7();
            case 7 -> mainGame.getTextFromArea8();
            case 8 -> mainGame.getTextFromArea9();
            case 9 -> mainGame.getTextFromArea10();
            case 10 -> mainGame.getTextFromArea11();
            case 11 -> mainGame.getTextFromArea12();
            default -> "error";
        };
    }

    private void setInstruction(String addInstruction, int silo) {
        int realBox;
        int row = silo/maxColumns;
        int column = silo % maxColumns;
        realBox = (row*4) + column;
        switch (realBox) {
            case 0 -> mainGame.setTextToArea1(mainGame.getTextFromArea1() + addInstruction);
            case 1 -> mainGame.setTextToArea2(mainGame.getTextFromArea2() + addInstruction);
            case 2 -> mainGame.setTextToArea3(mainGame.getTextFromArea3() + addInstruction);
            case 3 -> mainGame.setTextToArea4(mainGame.getTextFromArea4() + addInstruction);
            case 4 -> mainGame.setTextToArea5(mainGame.getTextFromArea5() + addInstruction);
            case 5 -> mainGame.setTextToArea6(mainGame.getTextFromArea6() + addInstruction);
            case 6 -> mainGame.setTextToArea7(mainGame.getTextFromArea7() + addInstruction);
            case 7 -> mainGame.setTextToArea8(mainGame.getTextFromArea8() + addInstruction);
            case 8 -> mainGame.setTextToArea9(mainGame.getTextFromArea9() + addInstruction);
            case 9 -> mainGame.setTextToArea10(mainGame.getTextFromArea10() + addInstruction);
            case 10 -> mainGame.setTextToArea11(mainGame.getTextFromArea11() + addInstruction);
            case 11 -> mainGame.setTextToArea12(mainGame.getTextFromArea12() + addInstruction);

        }
    }

    //Update output streams of the right side of the output boxes
    public void updateOutPut() {
        int out = 1;
        for (int i = 0; i < streams.size(); i++) {
            SiloStream stream = streams.get(i);
            if (stream.type == StreamType.OUT) {
                ArrayList<Integer> nums = new ArrayList<>(stream.getUpdateList());
                String newString = "";
                for (int r = 0; r < nums.size(); r++) {
                    newString += nums.get(r);
                    newString += '\n';
                }
                if (out == 1) {
                    out++;
                    mainGame.setTextToOutERight(newString);
                } else {
                    mainGame.setTextToOutLRight(newString);
                }
            }
        }
    }

    //Gets the title
    public String getTitle() {
        return title;
    }

    //Fills the text with the instructions
    public void fillText() {
        for (int r = 0; r < fullCompInstructions.size(); r++) {
            String newString = "";
            for (int c = 0; c < fullCompInstructions.get(r).size(); c++) {
                newString += fullCompInstructions.get(r).get(c);
                if (r != fullCompInstructions.size() - 1 && c != fullCompInstructions.get(r).size() - 1) {

                }
                newString += '\n';
            }
            if (!newString.equals("")) {
                setInstruction(newString, r);
            }
        }
    }


    //Updates the instruction within the text
    public void updateInstruction() {
        for (int r = 0; r < fullCompInstructions.size(); r++) {
            String text = findText(r);
            if (!text.equals("")) {
                String[] newInstruct = findText(r).split("\n");
                ArrayList<String> newList = new ArrayList<>(List.of(newInstruct));
                fullCompInstructions.set(r, newList);
            }
        }
    }


    // Enum of values for the transfer regions
    public enum Direction {
        RIGHT(),
        LEFT(),
        UP(),
        DOWN();

        Direction() {

        }
    }

    //Pulls value from the specified silo and from the direction
    public int pullValue(Silo silo, Direction direction) {
        int value;
        int[] rowColumn = getRowColumn(silo);
        TransferRegion region = getRegion(rowColumn[0], rowColumn[1], direction);
        value = region.getVal(rowColumn[0], rowColumn[1]);
        return value;
    }

    //Pushes the value to specified silo and direction
    public void pushValue(int value, Silo silo, Direction direction) {
        int[] rowColumn = getRowColumn(silo);
        TransferRegion region = getRegion(rowColumn[0], rowColumn[1], direction);
        region.pushVal(value, rowColumn[0], rowColumn[1]);
    }

    private TransferRegion getRegion(int row, int column, Direction direction) {
        TransferRegion region = null;
        if (direction == Direction.UP) region = vertical.get(row).get(column);
        else if (direction == Direction.DOWN) region = vertical.get(row + 1).get(column);
        else if (direction == Direction.LEFT) region = horizontal.get(row).get(column);
        else if (direction == Direction.RIGHT) region = horizontal.get(row).get(column + 1);
        return region;
    }

    private int[] getRowColumn(Silo silo) {
        int[] vals = new int[2];
        //Find silo.
        for (int r = 0; r < maxRows; r++) {
            for (int c = 0; c < maxColumns; c++) {
                if (games.get(r).get(c) == silo) {
                    vals[0] = r;
                    vals[1] = c;
                    break;
                }
            }
        }
        return vals;
    }

    //Sets the latch countdown
    public void countDown() {
        latch.countDown();
    }



    // Start the game
    public void start() {
        updateInstruction();
        initializeGames();
        service = Executors.newScheduledThreadPool(maxColumns * maxRows);
        latch = new CountDownLatch(maxColumns * maxRows);
//        int index = 0;
//        for (int r = 0; r < games.size(); r++) {
//            for (int c = 0; c < games.get(r).size(); c++) {
//                Silo game = games.get(r).get(c);
//                service.execute(game);
//                mainGame.setAccValue(index, game.getACC());
//                mainGame.setBakValue(index, game.getBAK());
//                mainGame.setLastValue(index, game.getCurrent());
//                updateOutPut();
//                index++;
//            }
//        }
//        try {
//            latch.await();
//            //Thread.sleep(TimeUnit.MILLISECONDS.toMillis(intervalTime));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } finally {
//            latch = new CountDownLatch((maxColumns * maxRows) -waitNum);
//        }

        timer = new AnimationTimer() {
            long lastInterval;

            @Override
            public void handle(long now) {
                long intervalNano = Duration.ofMillis(intervalTime).toNanos();
                if (now - intervalNano > lastInterval) {
                    int index = 0;
                    for (int r = 0; r < games.size(); r++) {
                        for (int c = 0; c < games.get(r).size(); c++) {
                            //System.out.println(waiting.size());
                            Silo game = games.get(r).get(c);
                            synchronized (waiting) {
                                if (!waiting.contains(game)) {
                                    service.execute(game);
                                }
                            }
                            mainGame.setAccValue(index, game.getACC());
                            mainGame.setBakValue(index, game.getBAK());
                            mainGame.setLastValue(index, game.getCurrent());
                            updateOutPut();
                            index++;
                        }
                    }
                    try {
                        latch.await();
                        //Thread.sleep(TimeUnit.MILLISECONDS.toMillis(intervalTime));
                        //service.shutdown();
                        //service = Executors.newScheduledThreadPool(maxColumns * maxColumns);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        synchronized (waiting) {
                            latch = new CountDownLatch((maxColumns * maxRows) - waiting.size());
                        }
                    }
                    lastInterval = now;
                    if(step){
                        this.stop();
                    }
                }
            }
        };
        timer.start();
    }

    //Method that sets the boolean method of step to true
    public void step(){
        step = true;
    }

    //Method that stops the timer
    public void pauseGame() {
        if (timer != null) {
            timer.stop();
        }
        if (service != null && !service.isShutdown()) {
            //service.shutdownNow();
        }
    }

    //Method that resumes the game by starting timer
    public void resumeGame() {
        //service = Executors.newScheduledThreadPool(maxColumns * maxRows);
        step = false;
        if (timer != null) {
            timer.start();
        }

    }

    //Stops and resets the silos
    public void stopAndResetGame() {
        if (timer != null) {
            timer.stop();
        }

        // Shutdown the executor service
        if (service != null) {
            service.shutdownNow();
        }
        mainGame.resetUI();
    }

    //Retrieves UI
    public com.prog04.UI getUI() {
        return UI;
    }

    private boolean readFile() {
        boolean canRead = true;
        Scanner fileScan = new Scanner(System.in);
        String fileName = fileScan.nextLine();
        fileScan.close();
        try (Scanner file = new Scanner(new File(fileName))) {
            if (file.hasNextLine()) {
                title = file.nextLine();
            }
            getInstructions(file);
            readGrid(file);
            readInputOutput(file);

        } catch (FileNotFoundException e) {
            canRead = false;
            System.out.println("File not found. ");
        }
        return canRead;
    }

    /**
     * Gets player instructions from the file and puts it into an arrayList.
     *
     * @param fileScan File scanner, assumed to be at the first instruction.
     */
    private void getInstructions(Scanner fileScan) {
        while (fileScan.hasNextLine()) {
            String instruction = fileScan.nextLine();
            if (instruction.equals("GRID"))
                return;
            else
                instructions.add(instruction);
        }
    }

    private void readGrid(Scanner fileScan) {
        int block = 0;
        gridSize = 0;
        ArrayList<String> compInstructions = new ArrayList<>();
        if (fileScan.hasNextLine()) {
            String[] rowsColumns = fileScan.nextLine().split(" ");
            try {
                maxRows = Integer.parseInt(rowsColumns[0]);
                maxColumns = Integer.parseInt(rowsColumns[1]);
                gridSize = maxRows * maxColumns;
            } catch (Exception e) {
                System.out.println("Invalid rows and columns. ");
                return;
            }
            while (fileScan.hasNextLine()) {
                String instruction = fileScan.nextLine();
                if (instruction.equals("END")) {
                    fullCompInstructions.add(compInstructions);
                    compInstructions = new ArrayList<>();
                    block++;
                    if (block == gridSize)
                        return;
                } else {
                    if (validateInstruction(instruction))
                        compInstructions.add(instruction);
                }
            }
        }
    }

    private void readInputOutput(Scanner fileScan) {
        boolean start = true;
        String location = null;
        String title = null;
        ArrayList<Integer> values = new ArrayList<>();
        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            if (start && line.equals("END")) {
                return;
            } else if (line.equals("END")) {
                StreamType type = null;
                if (title.contains("INPUT"))
                    type = StreamType.IN;
                else if (title.contains("OUTPUT"))
                    type = StreamType.OUT;
                String[] rowColumns = location.split(" ");
                try {
                    streams.add(new SiloStream(type, title, Integer.parseInt(rowColumns[0]),
                            Integer.parseInt(rowColumns[1]), values));
                } catch (Exception e) {
                    System.out.println("Something is wrong with the IO streams. ");
                }
                title = null;
                location = null;
                values = new ArrayList<>();
                start = true;
            } else {
                start = false;
                if (title == null) {
                    title = line;
                } else if (location == null) {
                    location = line;
                } else {
                    try {
                        values.add(Integer.parseInt(line));
                    } catch (Exception ignored) {

                    }
                }
            }
        }
    }

    //Checks if the instructions are valid
    public boolean validateInstruction(String instruction) {
        String[] arguments = instruction.split(" ");
        int size = arguments.length;
        if (size > 0) {
            switch (arguments[0]) {
                case "SLEEP":
                    try {
                        if (size == 2) {
                            Integer.parseInt(arguments[1]);
                            return true;
                        }
                    } catch (Exception ignore) {

                    }
                    return false;
                case "NEGATE":
                case "SWAP":
                case "SAVE":
                case "NOOP":
                    return (size == 1);
                case "MOVE":
                    return (size == 3 && checkSource(arguments[1]) && checkDest(arguments[2]));
                case "SUB":
                case "JRO":
                case "ADD":
                    return (size == 2 && checkSource(arguments[1]));
                case "JUMP":
                case "JEZ":
                case "JNZ":
                case "JGZ":
                case "JLZ":
                    return (size == 2);
                default:
                    return (arguments[0].charAt(0) == ':' && arguments[0].charAt(arguments.length - 1) == ':'
                            && size == 1);
            }
        }
        return false;
    }

    /**
     * Checks if a source argument is valid. Can be any destination or an immediate
     * value.
     *
     * @param srcArg source argument.
     * @return returns true if valid and false if not.
     */
    public boolean checkSource(String srcArg) {
        try {
            Integer.parseInt(srcArg);
            return true;
        } catch (Exception ignored) {
        }
        // If not a number then src can be any destination value.
        return checkDest(srcArg);
    }

    /**
     * Checks a destination argument.
     *
     * @param destArg destination argument
     * @return returns true if destination is valid and false if not.
     */
//    public boolean checkDest(String destArg) {
//        return switch (destArg) {
//            case "ACC", "NIL", "LEFT", "RIGHT", "UP", "DOWN" -> true;
//            default -> false;
//        };
//    }

    /**
     * Checks destination argument in a command.
     *
     * @param destArg destination argument.
     * @return returns true if valid and false if not.
     */
    public boolean checkDest(String destArg) {
        if ("ACC".equals(destArg) || "NIL".equals(destArg) || "LEFT".equals(destArg) || "RIGHT".equals(destArg)
                || "UP".equals(destArg) || "DOWN".equals(destArg)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * This class just represents a stream type of either in or out.
     */
    public enum StreamType {
        OUT("Output"),
        IN("Input"),

        PORT("Port");

        String name;

        StreamType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * This class represents a Silo stream either input or output.
     * Each stream has a set of values, a title, and type.
     *
     * @author Danny Metcalfe
     */
    public class SiloStream {

        private final int row;
        private final int column;
        private final StreamType type;
        private final String title;
        private final ArrayList<Integer> reqValues;

        private final ArrayList<Integer> currentValues;

        @Override
        public String toString() {
            String endString = "";
            endString = endString + "Name: " + title + '\n';
            endString = endString + "Type: " + type.getName() + '\n';
            endString = endString + "Required values: " + '\n';
            for (int i = 0; i < reqValues.size(); i++) {
                endString = endString + reqValues.get(i) + ' ';
            }
            endString += '\n';

            return endString;

        }

        /**
         * Print the current output values.
         */
        public void printCurrentOut() {
            if (type == StreamType.OUT) {
                for (int i = 0; i < currentValues.size(); i++) {
                    System.out.println(currentValues.get(i));
                }
            }
        }

        private void checkOut(int row, int column){
            if(this.currentValues.size() == this.reqValues.size()){
                games.get(row).get(column).end();
            }
        }

        private SiloStream(StreamType type, String title, int row, int column, ArrayList<Integer> reqValues) {
            this.type = type;
            this.title = title;
            this.row = row;
            this.column = column;
            if (type == StreamType.IN) {
                this.currentValues = reqValues;
                this.reqValues = new ArrayList<>();
            } else {
                this.currentValues = new ArrayList<>();
                this.reqValues = reqValues;
            }
        }


        private void handleInvalidOutput() {
            int sizeCurrent = currentValues.size();
            int sizeReq = reqValues.size();
            for (int r = 0; r < sizeCurrent; r++) {
                if (sizeCurrent <= sizeReq) {
                    if (!currentValues.get(r).equals(reqValues.get(r))) {
                        //TODO: do highlight from ui.
                        //getUI().highlight(r);
                    }
                }
            }
        }


        /**
         * Gets the list of needed values for output and an empty list for input.
         *
         * @return a list of integers.
         */
        public ArrayList<Integer> getValues() {
            return new ArrayList<>(reqValues);
        }

        /**
         * Gets a list of values for an input list and an empty lsit for outPut.
         *
         * @return a list of integers.
         */
        public ArrayList<Integer> getCurrentValues() {
            return new ArrayList<>(currentValues);
        }

        /**
         * Gets a list that can get changes, meant for output value updating.
         *
         * @return returns an array list of integers.
         */
        private ArrayList<Integer> getUpdateList() {
            return currentValues;
        }

        /**
         * Get the type of stream.
         *
         * @return returns the stream type. Input or Output.
         */
        public StreamType getType() {
            return type;
        }

        /**
         * Get the stream title.
         *
         * @return returns a string title of the stream.
         */
        public String getTitle() {
            return title;
        }

        /**
         * Gets the row of the silo stream.
         *
         * @return the row of the silo stream.
         */
        public int getRow() {
            return row;
        }

        /**
         * Gets the column of the silo stream.
         *
         * @return return the column of the silo stream.
         */
        public int getColumn() {
            return column;
        }


    }

    /**
     * Two transfer types of horizontal and vertical.
     */
    private enum TransferType {
        HORIZONTAL(),
        VERTICAL();

        TransferType() {

        }
    }

    /**
     * Represents a port within a tranfer region.
     */
    private class Port {
        private int index = 0;
        private int val = 0;
        private ArrayList<Integer> streamValues = null;
        private StreamType type;

        /**
         * Start a port with type port with a value.
         *
         * @param value
         */
        private Port(int value) {
            type = StreamType.PORT;
            val = value;
        }

        /**
         * Creates a new port that has a list and type. For input and output streams.
         *
         * @param type   stream type. Can be a port or input or output.
         * @param values list of values to assign to the port.
         */
        private Port(StreamType type, ArrayList<Integer> values) {
            this.type = type;
            streamValues = values;
        }

        /**
         * Creates a new port.
         */
        private Port() {
            type = StreamType.PORT;
        }

        /**
         * Updates a value in the port.
         *
         * @param value value to update the port with.
         */
        private void updateValue(int value, int row, int column) {
            if (type == StreamType.PORT) val = value;
            else if (type == StreamType.OUT) {
                streamValues.add(value);
                for(int i = 0; i<streams.size(); i++){
                    if(streams.get(i).type == StreamType.OUT){
                        if(streams.get(i).getUpdateList() == streamValues){
                            if(streams.get(i).getUpdateList().size() == streamValues.size()) {
                                streams.get(i).checkOut(row, column);
                            }
                        }
                    }
                }
            }
        }


        /**
         * Gets the value in the port. If an input stream will giva a value from input list.
         *
         * @return Returns an integer value from the port.
         */
        private int getVal() {
            int value = -1;
            if (type == StreamType.IN) {
                if (index < streamValues.size()) {
                    value = streamValues.get(index);
                    index++;
                } else {
                    index = 0;
                    value = streamValues.get(index);
                    index++;
                }
            } else if (type == StreamType.PORT) value = val;
            return value;
        }


    }

    /**
     * Represents the two ports of a tranfer region and holds the values in the ports.
     * Has a push and pull method to update or get values.
     */
    private class TransferRegion {
        //Top or left silo port.
        private Port port1;
        //bottom or right silo port.
        private Port port2;

        //Location of the transfer region..
        private final int row;
        private final int column;
        private final TransferType type;


        private boolean pulled = false;
        private boolean pushed = false;

        private final Object syncronizer = new Object();

        private CountDownLatch latch1;


        /**
         * Creates a new transfer region.
         *
         * @param port1  Port 1
         * @param port2  Port 2
         * @param row    row of transfer region.
         * @param column column of tranfer region.
         * @param type   type of region either vertical or horizontal.
         */
        private TransferRegion(Port port1, Port port2, int row, int column, TransferType type) {
            latch1 = new CountDownLatch(2);
            this.port1 = port1;
            this.port2 = port2;
            this.row = row;
            this.column = column;
            this.type = type;
        }

        


        /**
         * Pushes value into a port.
         *
         * @param value  value to push
         * @param row    Row of silo wanting to push.
         * @param column Column of silo wanting to push.
         */
        private void pushVal(int value, int row, int column) {
            //For transfer regions on the same column.
            //Does not look at up vs left or right vs down here.
            synchronized (this) {
                if (this.column == column && type == TransferType.VERTICAL) {
                    //For silo below this region wanting to push up.
                    if (this.row == row && port1 != null) {
                        port1.updateValue(value, row, column);
                    }
                    //If this region is pushed to from above.
                    else if (this.row - 1 == row && port2 != null) {
                        port2.updateValue(value, row, column);
                    }
                }
                if (this.row == row && type == TransferType.HORIZONTAL) {
                    if (this.column == column && port1 != null) {
                        port1.updateValue(value, row, column);
                    }
                    if (this.column - 1 == column && port2 != null) {
                        port2.updateValue(value, row, column);
                    }
                }
            }

            try {
                if (port1 != null && port2 != null && port1.type == StreamType.PORT && port2.type == StreamType.PORT) {
                    latch.countDown();
                    latch1.countDown();
                    latch1.await();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }

        /**
         * Get the value from the port.
         *
         * @param row    Row of silo wanting to get value.
         * @param column Column of silo wanting to get value.
         * @return Returns the value in the port.
         */
        private int getVal(int row, int column) {
            try {
                if (port1 != null && port2 != null && port1.type == StreamType.PORT && port2.type == StreamType.PORT) {
                    latch.countDown();
                    latch1.countDown();
                    latch1.await();
                }
            } catch (InterruptedException e) {
                return 0;
            }
            latch1 = new CountDownLatch(2);


            synchronized (this) {
                if (this.column == column && type == TransferType.VERTICAL) {
                    if (this.row == row && port2 != null) {
                        return port2.getVal();
                    }
                    if (this.row - 1 == row && port1 != null) {
                        return port1.getVal();
                    }
                }
                if (this.row == row && type == TransferType.HORIZONTAL) {
                    if (this.column == column && port1 != null) {
                        return port2.getVal();
                    }
                    if (this.column - 1 == column && port2 != null) {
                        return port1.getVal();
                    }
                }
                return 0;
            }
        }

        /**
         * Sets a new port to port 1 or port 2.
         *
         * @param portNum 1 or 2 for port number.
         * @param newPort The new port to assign.
         */
        private void setPort(int portNum, Port newPort) {
            if (portNum == 1) {
                this.port1 = newPort;
            }
            if (portNum == 2) {
                this.port2 = newPort;
            }
        }

    }

    /**
     * Gets the list of human instructions to put in the ui. User instructions list.
     *
     * @return Returns the list of user instructions.
     */
    public ArrayList<String> getHumanInstructions() {
        return new ArrayList<>(instructions);
    }

    /**
     * Gets the list of the computer instructions in the silos.
     *
     * @return Returns the list of instructions in the silo.
     */

    public ArrayList<ArrayList<String>> getSiloInstructions() {
        return new ArrayList<>(fullCompInstructions);
    }

    /**
     * Gets the column number for silo grid size.
     *
     * @return Returns how many columns of silos there are.
     */
    public int getMaxColumns() {
        return maxColumns;
    }

    /**
     * Gets the number of rows in the grid of silos.
     *
     * @return Returns how many rows of silos there are.
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * Gets the list of input and output streams. This list is assumed to not be changed and is not a copy.
     *
     * @return Returns a list of input and output streams. List is assumed to be used only for reading.
     */
    public ArrayList<SiloStream> getStreams() {
        return this.streams;
    }

    public Main getMainGame() {
        return this.mainGame;
    }

    //Tests with simple_example1.txt
    public static void main(String[] arg) {
//        Controller test = new Controller(new int[]{400, 600, 600, 600}, main);
//        test.pushValue(10, test.games.get(0).get(0), Direction.DOWN);
//        System.out.println(test.pullValue(test.games.get(1).get(0), Direction.UP));
//        test.pushValue(40, test.games.get(1).get(0), Direction.UP);
//        System.out.println(test.pullValue(test.games.get(0).get(0), Direction.DOWN));
//        System.out.println(test.pullValue(test.games.get(0).get(0), Direction.UP));
//        System.out.println(test.pullValue(test.games.get(0).get(0), Direction.UP));
//        test.pushValue(323, test.games.get(2).get(3), Direction.DOWN);
//        test.streams.get(3).printCurrentOut();
//        test.pushValue(50, test.games.get(1).get(1), Direction.LEFT);
//        System.out.println(test.pullValue(test.games.get(1).get(0), Direction.RIGHT));

    }


}
