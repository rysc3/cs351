package lab2;

import java.util.ArrayList;
import java.util.List;

public class Tests {
    public static void main(String[] args) {
        // Let's start by creating our list
        ContiguousList cl = new ContiguousList();
        // Now create a few strings
        String geralt = "Geralt";
        String yen    = "Yen";
        String triss  = "Triss";
        String saskia = "Saskia";
        // Now let's try prepend
        cl.prepend(geralt);
        cl.prepend(yen);
        // Then some appends
        cl.append(triss);
        cl.append(saskia);

        shouldBe("Empty?", cl.isEmpty(), false);
        shouldBe("Has Geralt", cl.has("Geralt"), true);
        shouldBe("Has Dandelion", cl.has("Dandelion"), false);
        shouldBe("Retrieve Index 2", cl.retrieve(2), triss);
        shouldBe("Retrieve Index 10", cl.retrieve(10), null);
        shouldBe("Delete Index 1", cl.delete(1), geralt);
        shouldBe("Delete Saskia", cl.delete(saskia), true);
        shouldBe("Delete Ciri", cl.delete("Ciri"), false);
        shouldBe("Size 2", cl.length(), 2);
        shouldBe("Mutate 0", cl.mutate(0, "Ciri"), yen);
        shouldBe("Has Ciri", cl.has("Ciri"), true);

        List<String> people = List.of("Ciri", geralt, "Vesemir");

        shouldBe("Delete Ciri And Geralt", cl.deleteAll(people), true);
        shouldBe("Delete Nothing", cl.deleteAll(new ArrayList<>()), false);

        cl.empty();

        shouldBe("Empty", cl.isEmpty(), true);

        for(int i = 0; i < 100; i++) {
            cl.insert(0, geralt);
        }

        shouldBe("Size 100", cl.length(), 100);
    }

    private static <T> void shouldBe(String description, T given, T equalTo) {
        System.out.print(description + ": ");
        if (given != null && given.equals(equalTo)) {
            System.out.println("PASSED");
        }
        else if (given == equalTo) {
            System.out.println("PASSED");
        }
        else {
            System.out.println("FAILED: " + given + " != " + equalTo);
        }
    }
}
