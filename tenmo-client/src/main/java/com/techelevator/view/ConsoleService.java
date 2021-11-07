package com.techelevator.view;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
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

	public int getTransferUserId(){
		out.println("Id of user you want to send to");
		int userIdChoice = Integer.parseInt(in.nextLine());
		out.flush();
		return userIdChoice;
	}

	public BigDecimal getTransferAmount(){
		out.println("Enter Amount: ");
		Double transferAmount = Double.parseDouble(in.nextLine());
		BigDecimal amount = new BigDecimal(transferAmount);
		out.flush();
		return amount;
	}

	public void displayOtherUsers(User[] users, String username){
		out.println("-----------------------------------------");
		out.println("Users");
		out.printf("%-30s", "ID");
		out.printf("%-10s", "Name");
		out.println();
		out.println("-----------------------------------------");

		for (int i = 0; i < users.length; i++){
			if (!users[i].getUsername().equals(username)){
				out.printf("%-30s", users[i].getId());
				out.printf("%-10s ", users[i].getUsername());
				out.println();
			}
		}
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

	public void printTransfers(Transfer[] transfers){
		System.out.println(
				"-------------------------------------------\n" +
				"Transfers\n" +
				"ID          From/To                 Amount\n" +
				"-------------------------------------------\n");
		for (Transfer transfer : transfers) {
			System.out.println(
					transfer.getTransferId() + "          " + consoleFromType(transfer.getTransferTypeDesc()) + ": " +
							transfer.getUsernameOfOther() + "          " + showAsDollars(transfer.getAmount()) + "\n");
		System.out.println(
							"---------\n" +
							"Please enter transfer ID to view details (0 to cancel): \"");
		int transferDetailId = in.nextInt();

			System.out.println();
//does not having a space for the + cause a problem?
			//get user input
	}
	}


	public void printNoTransfers(){
		System.out.println("No transfers were found.");
	}

	public void printSendTransfer(){
		System.out.println("-------------------------------------------\n" +
				"Users\n" +
				"ID          Name\n" +
				"-------------------------------------------\n" +
				"313         Bernice\n" +
				"54          Larry\n" +
				"---------\n" +
				"\n" +
				"Enter ID of user you are sending to (0 to cancel):\n" +
				"Enter amount:");
	}

	public String consoleFromType (String type){
		HashMap<String, String> typeToConsole = new HashMap<>();
		typeToConsole.put("Send", "From");
		typeToConsole.put("Received", "To");
		return typeToConsole.get(type);
	}

	public String showAsDollars(BigDecimal money){
		String stringMath = "$" + String.format("%.2f",money);
		return stringMath;
	}
}
