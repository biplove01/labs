// Abstract class
abstract class Calculation {
    // Normal method
    int add(int x, int y) {
        return x + y;
    }

    // Abstract method
    abstract int mul(int x, int y);
}

// Demo class inheriting Calculation
class Demo extends Calculation {
    // Implementing abstract method
    int mul(int x, int y) {
        return x * y;
    }

    // Demo's own method
    int calcDiv(int x, int y) {
        return x / y;
    }
}

// Main class to test
public class Main {
    public static void main(String[] args) {
        // Creating object of Demo
        Demo demo = new Demo();

        // Using Demo object
        System.out.println("Using Demo object:");
        System.out.println("Add: " + demo.add(10, 5));
        System.out.println("Mul: " + demo.mul(10, 5));
        System.out.println("Div: " + demo.calcDiv(10, 5));

        // Accessing methods using abstract class reference
        Calculation calc = new Demo();  // Reference of abstract class
        System.out.println("\nUsing abstract class reference:");
        System.out.println("Add: " + calc.add(10, 5));
        System.out.println("Mul: " + calc.mul(10, 5));
        // calc.calcDiv(10,5); // ERROR: calcDiv not in Calculation class
    }
}
