import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
// C:\Users\ryan\cs351Lab01\info\test_case1.txt

public class cs351Lab01 {
  public static void main(String[] args) throws FileNotFoundException {

    Scanner scanner = new Scanner(System.in);
    boolean done = false;

    // Loop until user enters 'done'
    while(!done) {  // hehe while not done

      System.out.print("Enter 'within' or 'overlap': ");
      String type = scanner.nextLine();

      if (type.equalsIgnoreCase("done")) {
        done = true;
      }

      if (!type.equals("within") && !type.equals("overlap")) {
        if (!type.equalsIgnoreCase("done")) {
          System.out.println("Invalid type. Exiting the program.");
        }
        return;   // skip error message if this is the case
      }

      System.out.print("Enter absolute filepath: ");
      String filePath = scanner.nextLine();

      if (filePath.equalsIgnoreCase("done")) {
        System.out.println("Exiting the program.");
        return;
      }

      File file = new File(filePath);
      Scanner inputScan = new Scanner(file);

      int count = 0; // to keep score

      // Loop until we reach the end of the file
      while (inputScan.hasNext()) {
        String line = inputScan.next();
        String[] lineArr = line.split(","); // gives us: ["2-4", "5-8"]

        // Further explanation in readme
        int l1 = Character.getNumericValue(lineArr[0].charAt(0)); // 2 ^^
        int u1 = Character.getNumericValue(lineArr[0].charAt(2)); // 4
        int l2 = Character.getNumericValue(lineArr[1].charAt(0)); // 5
        int u2 = Character.getNumericValue(lineArr[1].charAt(2)); // 8

        // check format to make it easier later, make sure in any given set, L < U
        if (!(l1 <= u1) || !(l2 <= u2)) {
          System.out.println("Lower of a given set is larger than upper.");
          System.out.println("Lower:" + l1);
          System.out.println("Upper:" + u1);
          System.out.println("Lower:" + l2);
          System.out.println("Upper:" + u2);
          System.exit(2); // A certain range is not in the correct format
        }

        /*
         * Here, we can do a simple if-else since we've already validated that our type
         * will be one of the two valid types.
         */
        if (type.equals("within")) { // check for "within"
          if (((u1 >= u2) && (l1 <= l2)) || ((u2 >= u1) && (l2 <= l1))) {
            count++;
          }
        } else { // check for "overlap"
          if (((l1 < l2) && (u1 < u2)) || ((l1 > l2) && (u1 > u2))) {
            count++;
          }
        }
      }

      System.out.println(type + ": " + count);
    }
  }
}
