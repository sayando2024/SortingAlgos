package sorting;
import java.util.*;

public class BubbleSort {
	
	public static void main(String[] args) {
		Random rand = new Random();
		int[] numbers = new int[10];

		//this is sayan changing the code

		//This is Santanu changing

		
		for (int i = 0; i < numbers.length; i++ ) {
			numbers[i] = rand.nextInt(100);
		}
		System.out.println("Before: ");
		printArray(numbers);
		
		boolean checkSwapped;
		
		do  {
			
			checkSwapped = false;
			for (int i = 0; i < numbers.length - 1; i++) {
				
				if (numbers[i] > numbers[i+1]) {
					
					checkSwapped = true;
					int temp = numbers[i];
					numbers[i] = numbers[i+1];
					numbers[i+1] = temp;
				}
			}
		} while ((checkSwapped == true));
		
		System.out.println("\nAfter: ");
		printArray(numbers);
		
	}
	
	public static void printArray(int[] numbers) {
		for (int i = 0; i < numbers.length; i++) {
			System.out.println(numbers[i]);
		}
	}
}
