package ruben.garcalia.guessthenumber.main;

import ruben.garcalia.guessthenumber.realgame.GamePreparatorRuben;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class GuessTheNumberRuben {

  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    List<Integer> numbersList = new ArrayList<>();

    int randomNumber = (int) (Math.random() * 100);
    randomNumber = randomNumber == 0 ? 1 : randomNumber;

    int guessedNumber;
    int guesses = 0;

    System.out.println("Input a number between 1 and 100");

    do {
      guesses++;
      guessedNumber = input.nextInt();

      numbersList.add(guessedNumber);

      if (guessedNumber == randomNumber) break;

      if (guessedNumber < randomNumber) {
        System.out.format("The number is \033[32mbigger\033[0m [\033[33m%d\033[0m tries]%n", guesses);
      }
      else {
        System.out.format("The number is \033[32msmaller\033[0m [\033[33m%d\033[0m tries]%n", guesses);
      }
    } while(guessedNumber != randomNumber);
    
    String plural = guesses > 1 ? "tries" : "try";
    System.out.format("You got it in \033[33m%d\033[0m %s%n", guesses, plural);

    input.close();

    GamePreparatorRuben.printErrors(numbersList);
  }

}
