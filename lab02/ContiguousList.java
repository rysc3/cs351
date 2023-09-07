package lab2;

import java.util.Collection;

public class ContiguousList {
  // Array that backs the list
  private String[] elements;
  // Current number of elements in the list
  private int count;

  public ContiguousList() {
    elements = new String[10];
    count = 0;
  }

  // Adds the given element to front of the list
  public void prepend(String e) {
    insert(0, e);
  }

  // Adds the given element to the end of the list
  public void append(String e) {
    String[] valid = validate(elements);  // validate
    
    valid[valid.length-1] = e;  // add input at end of array
    elements = valid;   // reassign elements
    count ++;
  }

  // Inserts the given element add the given index
  // Shifts all elements after the given index up by 1 index
  // If the index is larger than any index in the list then
  // the element should be inserted at the end of the list
  public void insert(int index, String e) {
    String[] valid = validate(elements);  // validate

    for(int i = elements.length-2; i > -1; i--){   // Loop through backwards
      valid[i+1] = valid[i];  // move each item with a greater index down one space
    }
    valid[index] = e;   // add input at specified index
    elements = valid;   // reassign elements
    count ++;
  }

  // Removes all elements from the list
  public void empty() {
    String[] valid = new String[elements.length];

    elements = valid;
    count = 0;
  }

  // Returns true if the given element exists in the list, false otherwise
  public boolean has(String e) {
    // loop through and check
    for(int i=0; i<elements.length; i++){
      if(elements[i] != null && elements[i].equals(e)){ return true; }
    }
    return false;
  }

  // Retrieves the element at the given index, if the index doesn't exist
  // then return null
  public String retrieve(int index) {
    if(index < elements.length) {
      return elements[index];
    }
    return null;
  }

  // Returns true if the list is empty, false otherwise
  public boolean isEmpty() {
    // wanted to do some fun comparing memory size thing here but idk how to lol
    for(int i=0; i<elements.length; i++){
      if(elements[i] != null){
        return false;
      }
    }
    return true;
  }

  // Deletes the element at the given index and returns it,
  // if the index doesn't exist then return null
  public String delete(int index) {
    if (index < 0 || index >= count) { // Check if the index is valid
      return null;
    }

    String delValue = elements[index];

    for (int i = index; i < count - 1; i++) { // Loop from the index to the second-to-last element
      elements[i] = elements[i + 1]; // Shift elements to the left
    }

    elements[count - 1] = null; // Clear the last element
    count--;

    return delValue;
  }


  // Deletes the first occurrence of an element from the list if it exists,
  // if an element is removed return true, false otherwise
  public boolean delete(String e) {
    for(int i=0; i<elements.length; i++){
      if(elements[i] != null && elements[i].equals(e)){
        elements[i] = null;
        count --;
        return true;
      }
    }
    return false;
  }

  // Deletes all elements within the given collection,
  // this includes duplicates. If it removes an element then return true,
  // otherwise false
  public boolean deleteAll(Collection<String> c) {
    boolean removed = false; // Flag to track if any elements were removed

    for (int i = 0; i < elements.length; i++) {
      if (c.contains(elements[i])) {
        // If the collection 'c' contains the current element in 'elements', remove it
        removeElementAtIndex(i);
        removed = true;
        i--; // Decrement the index to recheck the current position
      }
    }

    return removed;
  }


  // Sets the element at the given index to the given element and
  // returns the old element, return null if the index doesn't exist
  public String mutate(int index, String e) {
    if(index <= elements.length && index >= 0 && e != null){
      String delValue = elements[index];
      elements[index] = e;
      return delValue;
    }
    return null;  // invalid info
  }

  // Returns the current length of the list
  public int length() {
    return count;
  }

  // Override toString from Object
  // Should print out with a square bracket at the front and the back of the list
  // Each element should be seperated by a comma and a space
  // For example if your list had the elements "1" "2" and "3"
  // then the output would be "[1, 2, 3]"
  // nulls should not be printed
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("[");

    for(int i=0; i<elements.length; i++){
      str.append(elements[i]);
      if(i+1 != elements.length){
        str.append(",");
      }
    }

    str.append("]");
    return str.toString();
  }

  // Override equals from Object
  // Should return true if the contents of your array are equal
  // to the contents of o's array, otherwise false.
  // nulls in the array should not be counted
  @Override
  public boolean equals(Object o) {
    for(int i=0; i<elements.length; i++){
      if(elements[i] != null && !elements[i].equals(o)){
        return false;
      }
    }
    return true;
  }

  // Function checks if we have hit the max length, it creates the new array if we need,
  // if not then it does nothing.
  public static String[] validate(String[] input){
    int previousLength = input.length;

    if(previousLength < input.length) {
      String[] newArr = new String[previousLength * 2];

      for (int i = 0; i < previousLength; i++) {
        newArr[i] = input[i];
      }

      input = newArr;
    }
    return input;
  }
}
