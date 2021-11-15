package com.techelevator.view;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.services.TransferService;
import com.techelevator.tenmo.services.UserService;

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
		out.println("Enter ID of user you are sending to (0 to cancel):");
		int userIdChoice = Integer.parseInt(in.nextLine());
		out.flush();
		return userIdChoice;
	}

	public BigDecimal getTransferAmount(){
		out.println("Enter amount: ");
		Double transferAmount = Double.parseDouble(in.nextLine());
		BigDecimal amount = new BigDecimal(transferAmount);
		out.flush();
		return amount;
	}

	public int getTransferDetailId(){
		out.println("Please enter transfer ID to view details (0 to cancel): \"");
		int transferIdChoice = Integer.parseInt(in.nextLine());
		out.flush();
		return transferIdChoice;
	}

	public void insufficientFunds(){
		out.println();
		out.println(" Insufficient Funds. Please try a smaller amount after viewing your balance. ");
		out.flush();
	}

	public void incorrectUserId(){
		out.println();
		out.println("User ID does not exist! Please check the list of users again.");
		out.flush();
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
		out.println("---------");
	}

	public void printBalance(BigDecimal[] balances) {
		System.out.println("--------------------------------------------");
		String printedBalance = "";
		if (balances != null){
			if (balances.length == 1){
				printedBalance = "Your current account balance is: " + showAsDollars(balances[0]);
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

	public void printTransfers(AuthenticatedUser currentUser, Transfer[] transfers, TransferService transferService,
							   UserService userService){


		System.out.println(
				"-------------------------------------------\n" +
				"Transfers\n" +
				"ID          From/To                 Amount\n" +
				"-------------------------------------------\n");
		for (Transfer transfer : transfers) {
			String fromUsername = userService.findUserByAccountId(currentUser, transfer.getAccountFrom()).getUsername();
			String toUsername = userService.findUserByAccountId(currentUser, transfer.getAccountFrom()).getUsername();
			boolean isSend = false;
			if (transfer.getTransferTypeDesc().equals("Send")){
				isSend = true;
			}
			System.out.println(
					transfer.getTransferId() + "        " + consoleFromType(transfer.getTransferTypeDesc()) + ": " +
							(isSend ? userService.findUserByAccountId(currentUser, transfer.getAccountTo()).getUsername() :
									userService.findUserByAccountId(currentUser, transfer.getAccountFrom()).getUsername()) + "          " + showAsDollars(transfer.getAmount())
							+ "\n");
		}
			System.out.println(
							"---------\n");
		int transferDetailId = getTransferDetailId();
		printTransferDetails(currentUser, transferDetailId, transferService, userService);

//does not having a space for the + cause a problem?
			//get user input
	}

public void printTransferDetails (AuthenticatedUser currentUser, int transferDetailId, TransferService transferService, UserService userService)	{
	Transfer chosenTransfer = transferService.getTransferByTransferId(currentUser, transferDetailId);

	String fromUsername = userService.findUserByAccountId(currentUser, chosenTransfer.getAccountFrom()).getUsername();
	String toUsername = userService.findUserByAccountId(currentUser, chosenTransfer.getAccountTo()).getUsername();

	System.out.println("--------------------------------------------\n" +
			"Transfer Details\n" +
			"--------------------------------------------\n" +
			" Id: " + chosenTransfer.getTransferId() + "\n" +
			" From: " + fromUsername + "\n" +
			" To: " + toUsername + "\n" +
			" Type: " + chosenTransfer.getTransferTypeDesc() + "\n" +
			" Status: " + chosenTransfer.getTransferStatusDesc() + "\n" +
			" Amount: " + showAsDollars(chosenTransfer.getAmount()));
}



	public void printNoTransfers(){
		System.out.println("No transfers were found.");
	}

	public String consoleFromType (String type){
		HashMap<String, String> typeToConsole = new HashMap<>();
		typeToConsole.put("Send", "To");
		typeToConsole.put("Received", "From");
		return typeToConsole.get(type);
	}

	public String showAsDollars(BigDecimal money){
		String stringMath = "$" + String.format("%.2f",money);
		return stringMath;
	}
}
