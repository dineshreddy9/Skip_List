package dxt161330;
import dxt161330.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver2 {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc;
		if (args.length > 0) {
			File file = new File(args[0]);
			sc = new Scanner(file);
		} else {
			sc = new Scanner(System.in);
		}
		String operation = "";
		long operand = 0;
		int modValue = 999983;
		long result = 0;
		Long returnValue = null;
		SkipList<Long> skipList = new SkipList<>();
		// Initialize the timer
		Timer timer = new Timer();

		while (!((operation = sc.next()).equals("End"))) {
			switch (operation) {
			case "Add": {
				operand = sc.nextLong();
				System.out.println("Add "+ operand + " " + skipList.add(operand));
				break;
			}
			case "Ceiling": {
				operand = sc.nextLong();
				returnValue = skipList.ceiling(operand);
				System.out.println("Ceiling "+operand+" " + returnValue);
				break;
			}
			case "First": {
				returnValue = skipList.first();
				System.out.println("First " + returnValue);
				break;
			}
			case "Get": {
				int intOperand = sc.nextInt();
				returnValue = skipList.get(intOperand);
				System.out.println("Get "+intOperand+" " + returnValue);
				break;
			}
			case "Last": {
				returnValue = skipList.last();
				System.out.println("Last " + returnValue);
				break;
			}
			case "Floor": {
				operand = sc.nextLong();
				returnValue = skipList.floor(operand);
				System.out.println("Ceiling "+operand+" " + returnValue);
				break;
			}
			case "Remove": {
				operand = sc.nextLong();
				System.out.println("Remove "+operand+" " + skipList.remove(operand));
				break;
			}
			case "Contains":{
				operand = sc.nextLong();
				System.out.println("Contains "+operand+" " + skipList.contains(operand));
				break;
			}

			}
		}

		// End Time
		timer.end();

		System.out.println(result);
		System.out.println(timer);
	}
}