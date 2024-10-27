package adivinaelnumero.ruben.garciacalvillo.arroyo;

import java.util.Scanner;
public class AdivinaElNumeroRuben {

  public static void main(String[] args) {    
    Scanner input = new Scanner(System.in);
    
    int randomNumber = (int) (Math.random() * 100);
    randomNumber = randomNumber == 0 ? 1 : randomNumber;

    int guessedNumber;
    int guesses = 0;

    System.out.println("Elige un numero entre 1 y 100");

    do {
      guesses++;
      guessedNumber = input.nextInt();

      if (guessedNumber == randomNumber) break;

      if (guessedNumber < randomNumber) {
        System.out.format("El numero es \033[32mmayor\033[0m [Llevas \033[33m%d\033[0m intentos]%n", guesses);
      }
      else {
        System.out.format("El numero es \033[32mmenor\033[0m [Llevas \033[33m%d\033[0m intentos]%n", guesses);
      }
    } while(guessedNumber != randomNumber);
    
    // Tiene que ser String porque char no admite caracteres vacios
    String plural = guesses > 1 ? "s" : "";
    System.out.format("Adivnaste el numero en \033[33m%d\033[0m intento%s%n", guesses, plural);

    input.close();
  }

}
