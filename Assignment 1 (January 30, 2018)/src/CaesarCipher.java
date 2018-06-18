import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author Matt Bowen
 * @version Feb 4, 2018
 * This is the Caesar Cipher program, which is a multi-purpose program intended to encrypt, decrypt, and break shifted ciphers.
 *
 */
public class CaesarCipher 
{
	/*
	 * Variable declarations, including all of the files, writers, scanners, and ArrayLists that will be used in this program.
	 */
	String option = "0";
	Scanner input;
	File message;
	File cipher;
	File plain;
	FileWriter fileWriter;
	BufferedWriter toFile;
	File dictionary;
	Scanner messageScanner;
	ArrayList<String> words;
	ArrayList<String> dictWords;
	ArrayList<Character> wordLetters;
	String letters = "abcdefghijklmnopqrstuvwxyz";
	
	/*
	 * The constructor for this program, initializes our scanner the user will use, and runs the menu.
	 */
	public CaesarCipher()
	{
		input = new Scanner(System.in);
		menu();
	}
	
	/**
	 * This method represents the menu that will run. It will run until the user inputs "4" as their option, and it will only accept
	 * options 1,2,3 or 4, as defined by the switch statement inside. For option 1, the encrypt function will be run, for option 2,
	 * the decrypt option will run, for option 3, the dictionary attack will be run, and will exit at option 4.
	 */
	public void menu()
	{
		System.out.println("Welcome to the Caesar cipher program.\n");
		while (!option.equals("4"))
		{
			System.out.println("Please select an option...");
			System.out.println("1- Encrypt the file message.txt");
			System.out.println("2- Decrypt the file cipher.txt");
			System.out.println("3- Break the cipher");
			System.out.println("4- Quit\n");
			
			option = input.next();
			
			switch (option)
			{
			case "1":
				encrypt();
				break;
			case "2":
				decrypt();
				break;
			case "3":
				breakCipher();
				break;
			case "4":
				break;
			default:
				System.out.println("Please select a valid option.\n");
				break;
			}
		}
		
		System.out.println("Thanks for using this program!");
	}
	
	/*
	 * This is the function that will be used to encrypt the words from the "message.txt" file, and write them to the "cipher.txt" file, 
	 * encrypted by the shift indicated by the user.
	 */
	public void encrypt()
	{
		/*
		 * This initializes the input file, and the ArrayLists used to hold the words from the file, and the ArrayList to hold the
		 * characters in each of the words.
		 */
		message = new File("message.txt");
		words = new ArrayList<String>();
		wordLetters = new ArrayList<Character>();
		
		//This gets the shift amount from the user, and stores it as a variable, and the program will only accept values from 1 to 26 for shift.
		System.out.println("Please enter a shift amount.\n");
		int shift = input.nextInt();
		while (shift > 26 || shift < 1)
		{
			System.out.println("Please enter a number between 1 and 26 for a shift value.");
			shift = input.nextInt();
		}
		
		//This line initializes the string that will be used to represent the shifted words.
		String shiftedWords = "";
		
		//This line calls the read and setup function for our file "message.txt". It will read the words and chars from the file into our ArrayLists
		readAndSetup(message);
		
		/*
		 * This is where the shifting of the letters occurs. For each letter of each word read from "message.txt", if the character is a letter
		 * (can be illustrated by if the character is in our alphabet string from above), then we will shift the letter by the shift amount.
		 */
		for (int x = 0; x < wordLetters.size(); x++)
		{
			if (letters.indexOf(wordLetters.get(x)) != -1)
			{
				//If the ASCII value of the letter with the shift added is between 97 and 122 (inclusive), then the shift amount is added, and new character is made.
				if ((wordLetters.get(x) + shift) <= 122 && (wordLetters.get(x) + shift) >= 97)
				{
					shiftedWords += (char) (wordLetters.get(x) + shift);
				}
				
				//If the ASCII value of the sum is greater than 122, then we must subtract 26 to get the correct amount, since there are 26 letters, and the range must be from 97 to 122.
				else if ((wordLetters.get(x) + shift) > 122)
				{
					shiftedWords += (char) (wordLetters.get(x) + shift - 26);
				}
				
			}
			
			//If the character is not in the alphabet, then we do no shifting before adding it, as it's most likely a punctuation or other special character.
			else
			{
				shiftedWords += wordLetters.get(x);
			}
		}
		
		/*
		 * This prints out the shifted words onto the screen, as well as creates the cipher file, and writes the shiftedWords to the "cipher.txt" file.
		 */
		System.out.println("\nYour encrypted message is: " + shiftedWords);
		cipher = new File("cipher.txt");
		writeToFile(cipher, shiftedWords);		
		System.out.println("This has been written to the file cipher.txt." + " \n");
		
	}
	
	/*
	 * This is the function that will be used to decrypt the contents of "cipher.txt" and write the plaintext to "plain.txt", decrypted using
	 * the shift amount indicated by the user.
	 */
	public void decrypt()
	{
		/*
		 * Much like the encrypt method, this initializes the input file, and the ArrayLists used to hold the words from the file, and the ArrayList 
		 * to hold the characters in each of the words.
		 */
		cipher = new File("cipher.txt");
		words = new ArrayList<String>();
		wordLetters = new ArrayList<Character>();
		
		//This gets the shift amount from the user, and stores it as a variable, and the program will only accept values from 1 to 26 for shift.
		System.out.println("Please enter a shift amount.\n");
		int shift = input.nextInt();
		while (shift > 26 || shift < 1)
		{
			System.out.println("Please enter a number between 1 and 26 for a shift value.");
			shift = input.nextInt();
		}
		
		//This line initializes the string that will be used to represent the plain text words.
		String plainWords = "";
		
		//This line calls the read and setup function for our file "cipher.txt". It will read the words and chars from the file into our ArrayLists
		readAndSetup(cipher);
		
		/*
		 * This is where the shifting of the letters back to plain text occurs. For each letter of each word read from "cipher.txt", if the character is a letter
		 * (can be illustrated by if the character is in our alphabet string from above), then we will shift the letter in reverse by the shift amount.
		 */
		for (int x = 0; x < wordLetters.size(); x++)
		{
			//If the character is a part of the alphabet.
			if (letters.indexOf(wordLetters.get(x)) != -1)
			{
				//If the ASCII value of the letter with the shift subtracted is between 97 and 122 (inclusive), then the shift amount is subtracted, and new character is made.
				if ((wordLetters.get(x) - shift) <= 122 && (wordLetters.get(x) - shift) >= 97)
				{
					plainWords += (char) (wordLetters.get(x) - shift);
				}
				
				//If the ASCII value of the difference is less than 97, then we must add 26 to get the correct amount, since there are 26 letters, and the range must be from 97 to 122.
				else if ((wordLetters.get(x) - shift) < 97)
				{
					plainWords += (char) (wordLetters.get(x) - shift + 26);
				}
				
			}
			//If the character is not in the alphabet, then we do no shifting before adding it, as it's most likely a punctuation or other special character.
			else
			{
				plainWords += wordLetters.get(x);
			}
		}
		
		/*
		 * This prints out the plain text words onto the screen, as well as creates the plain.txt file, and writes the plainWords to the "plain.txt" file.
		 */
		System.out.println("\nYour decrypted message is: " + plainWords);
		plain = new File("plain.txt");
		writeToFile(plain, plainWords);
		System.out.println("This has been written to the file plain.txt." + " \n");
	}
	
	/*
	 * This is the function that will be used to compare each of the words to a dictionary of known English words, and determines which index is the 
	 * most likely to reveal the plain text, based on if 80% or more of the words can be found in the dictionary file. It also will reveal the plain text
	 * as well on the screen.
	 */
	public void breakCipher()
	{
		/*
		 *Much in the same way as the decrypt function, we will read in the cipher file, and initialize each of the ArrayLists to hold the words and characters.
		 *However, we will also read in our "dictionary.txt" file as well, and create another ArrayList to hold the words from that file too.
		 */
		cipher = new File("cipher.txt");
		dictionary = new File("dictionary.txt");
		words = new ArrayList<String>();
		dictWords = new ArrayList<String>();
		wordLetters = new ArrayList<Character>();
		
		//We will create the string that will be used to represent the decrypted words for each index as we go. We also create a counter for words that appear in the dictionary.
		String plainWords = "";
		int counter = 0;
		
		//This will read in the words from the cipher file and store them in their corresponding ArrayLists for words and characters.
		readAndSetup(cipher);
		
		//We will attempt to read the words from the dictionary file, and if for some reason that file doesn't exist, the program will terminate, with an indicative message.
		try
		{
			messageScanner = new Scanner(dictionary);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Please make sure the file \"message.txt\" is in the correct directory, and that the file name is correct.");
			System.exit(0);
		}
		
		//As long as there is a word left in the dictionary file, we will add it to our ArrayList for the dictionary words.
		while (messageScanner.hasNext())
		{
			String nextWord = messageScanner.next();
			dictWords.add(nextWord);
		}
		
		//Close the scanner.
		messageScanner.close();
		
		
		//For each shift up to 26
		for (int shift = 0; shift < 26; shift++)
		{
			//Reset the counter to 0, and the decrypted string to empty for each iteration.
			counter = 0;
			plainWords = "";
			
			/*
			 * This is where the shifting of the letters back to plain text occurs 26 times. For each letter of each word read from "cipher.txt", if the character is a letter
			 * (can be illustrated by if the character is in our alphabet string from above), then we will shift the letter in reverse by the shift amount.
			 */
			for (int x = 0; x < wordLetters.size(); x++)
			{
				//If the character is in the alphabet.
				if (letters.indexOf(wordLetters.get(x)) != -1)
				{
					//If the ASCII value of the letter with the shift subtracted is between 97 and 122 (inclusive), then the shift amount is subtracted, and new character is made.
					if ((wordLetters.get(x) - shift) <= 122 && (wordLetters.get(x) - shift) >= 97)
					{
						plainWords += (char) (wordLetters.get(x) - shift);
					}
					
					//If the ASCII value of the difference is less than 97, then we must add 26 to get the correct amount, since there are 26 letters, and the range must be from 97 to 122.
					else if ((wordLetters.get(x) - shift) < 97)
					{
						plainWords += (char) (wordLetters.get(x) - shift + 26);
					}
					
				}
				//We want to keep spaces in the words, since we want to be able to separate the words by spaces in the next step. We do not want punctuation though, since it's not in the dictionary.
				else if (wordLetters.get(x).toString().equals(" "))
				{
					plainWords += wordLetters.get(x);
				}
			}
			
			//Create an array of words from the decrypted message string, separated by the spaces (why we kept the spaces in the last step.)
			String[] decryptedWords = plainWords.split(" ");
			
			//For each word in the "decrypted" words
			for (int y = 0; y < decryptedWords.length; y++)
			{
				//And for each word in teh dictionary
				for (int z = 0; z < dictWords.size(); z++)
				{
					//If the "decrypted" word matches a word in the dictionary, then we increase the counter by 1, and continue.
					if (decryptedWords[y].equals(dictWords.get(z)))
					{
						counter++;
						continue;
					}
				}
			}
			
			//If the ratio of words found in the dictionary to the "decrypted" words is 80% or more, then we say it is likely that the shift amount is the key. Also prints out the plain text.
			if ((double) counter/decryptedWords.length >= .8)
			{
				System.out.println("\nIt is likely that the key is " + shift);
				System.out.println("It is likely that the unencrypted message is: " + plainWords + "\n");
			}
		}	
	}
	
	/*
	 * This function writes the value held in the String "s" to the File "f"
	 */
	public void writeToFile(File f, String s)
	{
		//We try to create the file writers using the input file, and attempt to write the input string to the file, and close it. If an IOException occurs for some reason, the program terminates.
		try 
		{
			fileWriter = new FileWriter(f);
			toFile = new BufferedWriter(fileWriter);
			toFile.write(s);
			toFile.close();
		} 
		catch (IOException e) 
		{
			System.out.println("Something went wrong with the file I/O.");
			System.exit(0);
		}
		
	}
	
	/*
	 * This function will read all of the data from the input file, and store the words into an appropriate ArrayList, and also store all of the characters from those words
	 * into another appropriate ArrayList for later use.
	 */
	public void readAndSetup(File f)
	{
		
		/*
		 * We will attempt to read the words in from the file. As long as there is a word left in the input file, we will add it to our ArrayList for the words, 
		 * leaving a space at the end of each. We will also attempt to close the file.
		 */
		try
		{
			messageScanner = new Scanner(f);
			while (messageScanner.hasNext())
			{
				String nextWord = messageScanner.next();
				nextWord += " ";
				words.add(nextWord);
			}
			messageScanner.close();
		}
		//If the file isn't found, the program will terminate with appropriate notification.
		catch(FileNotFoundException e)
		{
			System.out.println("Please make sure the file \"" + f.getName() + "\" is in the correct directory, and that the file name is correct.");
			System.exit(0);
		}
		
		
		
		//For each word in the ArrayList for words.
		for (int x = 0; x < words.size(); x++)
		{
			//For each character in the word
			for (int y = 0; y < words.get(x).length(); y++)
			{
				//Add each character to the ArrayList for Characters, that will be used for shifting later.
				wordLetters.add(words.get(x).toLowerCase().charAt(y));
			}
		}
	}
	
	//The main function that calls the constructor for this file, and starts everything up.
	public static void main(String[] args) 
	{
		new CaesarCipher();
	}

}
