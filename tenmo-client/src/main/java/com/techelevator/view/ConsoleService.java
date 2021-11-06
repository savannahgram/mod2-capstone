package com.techelevator.view;


import com.techelevator.tenmo.model.Transfer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

	private PrintWriter out;
	private Scanner in;

	public ConsoleService(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output, true);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		out.println();
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public String getUserInput(String prompt) {
		out.print(prompt+": ");
		out.flush();
		return in.nextLine();
	}

	public Integer getUserInputInteger(String prompt) {
		Integer result = null;
		do {
			out.print(prompt+": ");
			out.flush();
			String userInput = in.nextLine();
			try {
				result = Integer.parseInt(userInput);
			} catch(NumberFormatException e) {
				out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
			}
		} while(result == null);
		return result;
	}

	public void printBalance(BigDecimal[] balances) {
		System.out.println("--------------------------------------------");
		String printedBalance = "";
		if (balances != null){
			if (balances.length == 1){
				printedBalance = "balance is: " + showAsDollars(balances[0]);
			}
			else {
				for (int i = 0; i < balances.length; i++) {
					if (i == 0) {
						printedBalance += "The balance for ";
					}
					else {
						printedBalance += "/n";
					}
					printedBalance += "Account " + i + " is " + showAsDollars(balances[i]);
				}
			}
		}
		else {
			printedBalance = "balance is null";
		}
		System.out.println(printedBalance);
	}

	public void balanceNotFound() {
		System.out.println("The balance could not be found.");
	}

	public void printTransfersByUsername(Transfer[] transfers){
		System.out.println();
	}

	public void printTransfersById(Transfer[] transfers){

	}

	public void printNoTransfers(){
		System.out.println("No transfers were found.");
	}

	public String showAsDollars(BigDecimal money){
		String stringMath = "$" + String.format("%.2f",money);
		return stringMath;
	}
}
