package com.github.HotPocketAdventure.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import com.github.HotPocketAdventure.util.exceptions.InvalidMemberException;

/**
 * A basic text menu utility that uses the standard input/output for displaying and retrieving the user's choices.
 * <br>- The menu can keep track of which menu options have and have not yet been chosen.  
 * <br>- All data members must have values that follow the rules specified by the {@link #validateMembers() validateMembers} method.
 * <br>- All constructors validate given values. If creating a degenerate Menu is necessary, build it using mutators.
 * @author Michael Bradley
 *
 */
public class Menu {

	//Common Menu Choices
	public static final String[] ON_OFF = {"On", "Off"};
	public static final String[] YES_NO = {"Yes", "No"};

	protected static Scanner input = new Scanner(System.in);

	protected int choice;
	protected int lastChoice;

	protected int exitOn;
	protected boolean exitAllowed;

	protected int numOptions;
	protected String[] options;

	protected String title;
	protected String exitText;

	protected boolean[] chosen;
	protected String chosenText;
	protected String unchosenText;

	/**
	 * Default Constructor that demonstrates a menu.
	 * <br>Useful for creating a menu that will have its properties changes individually. 
	 */
	public Menu() {
		choice = -1;
		lastChoice = -1;
		numOptions = 3;

		exitOn = 0;
		exitAllowed = true;

		title = "Demo Menu";
		options = new String[] {"This menu", "Has three options", "And exits on 0"};
		exitText = "Return";

		initChosen();
		chosenText = "Chosen";
		unchosenText = "Not chosen";

		validateMembers();
	}

	/**
	 * Sets all properties necessary to create a complete {@link Menu}.
	 * @param title
	 * @param numOptions
	 * @param options
	 * @param exitAllowed
	 * @param exitOn
	 * @param exitText
	 * @param chosenText
	 * @param unchosenText
	 */
	public Menu(String title, int numOptions, String[] options, boolean exitAllowed, int exitOn, String exitText, String chosenText, String unchosenText) {
		choice = -1;
		lastChoice = -1;

		this.exitAllowed = exitAllowed;
		this.exitOn = exitOn;

		this.numOptions = numOptions;
		setOptions(options);

		setTitle(title);
		setExitText(exitText);

		initChosen();
		this.chosenText = chosenText;
		this.unchosenText = unchosenText;

		validateMembers();
	}

	/**
	 * Sets all properties necessary to create a complete {@link Menu}.
	 * @param title
	 * @param options
	 * @param exitAllowed
	 * @param exitOn
	 * @param exitText
	 * @param chosenText
	 * @param unchosenText
	 */
	public Menu(String title, String[] options, boolean exitAllowed, int exitOn, String exitText, String chosenText, String unchosenText) {
		choice = -1;
		lastChoice = -1;

		this.exitAllowed = exitAllowed;
		this.exitOn = exitOn;

		setNumOptions(options);
		setOptions(options);

		setTitle(title);
		this.exitText = exitText;

		initChosen();
		this.chosenText = chosenText;
		this.unchosenText = unchosenText;

		validateMembers();
	}

	/**
	 * Sets all properties necessary to create an exitable, non-choice-tracking {@link Menu}.
	 * @param title
	 * @param numOptions
	 * @param options
	 * @param exitAllowed
	 * @param exitOn
	 * @param exitText
	 */
	public Menu(String title, int numOptions, String[] options, boolean exitAllowed, int exitOn, String exitText) {
		this(title, numOptions, options, exitAllowed, exitOn, exitText, null, null);
	}

	/**
	 * Sets all properties necessary to create an exitable, non-choice-tracking {@link Menu}.
	 * @param title
	 * @param options
	 * @param exitAllowed
	 * @param exitOn
	 * @param exitText
	 */
	public Menu(String title, String[] options, boolean exitAllowed, int exitOn, String exitText) {
		this(title, options.length, options, exitAllowed, exitOn, exitText);
	}

	/**
	 * Sets all properties necessary to create an unexitable, choice-tracking {@link Menu}.
	 * @param title
	 * @param numOptions
	 * @param options
	 * @param chosenText
	 * @param unchosenText
	 */
	public Menu(String title, int numOptions, String[] options, String chosenText, String unchosenText) {
		this(title, numOptions, options, false, 0, null, chosenText, unchosenText);
	}

	/**
	 * Sets all properties necessary to create an unexitable, choice-tracking {@link Menu}.
	 * @param title
	 * @param options
	 * @param chosenText
	 * @param unchosenText
	 */
	public Menu(String title, String[] options, String chosenText, String unchosenText) {
		this(title, options.length, options, chosenText, unchosenText);
	}

	/**
	 * Sets all properties necessary to create an unexitable, non-choice-tracking {@link Menu}.
	 * @param title
	 * @param numOptions
	 * @param options
	 */
	public Menu(String title, int numOptions, String[] options) {
		this(title, numOptions, options, false, 0, null, null, null);
	}

	/**
	 * Sets the title, number of options and allocates options to an empty String array of size numOptions.
	 * <br>The {@link Menu} is unexitable and does not make use of choice-tracking. 
	 * @param title
	 * @param numOptions
	 */
	public Menu(String title, int numOptions) {
		this(title, numOptions, null);
	}

	/**
	 * Sets all the properties necessary to create an unexitable, non-choice-tracking {@link Menu}.
	 * @param title
	 * @param options
	 */
	public Menu(String title, String[] options) {
		this(title, options.length, options);
	}

	/**
	 * Uses all of the values from another Menu object to create a new one. Validates after copying.
	 * @param other
	 */
	public Menu(Menu other) {
		copy(other);
		validateMembers();
	}


	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		input.close();
	}

	/**
	 * Outputs the menu options to the standard output with no (un)chosen texts
	 * @return a reference to this {@link Menu} object
	 */
	public Menu display() {
		return display(false);
	}

	/**
	 * Outputs the menu options to the standard output
	 * <br>If showChosen is true, adds the (un)chosen texts to each choice.
	 * @return a reference to this {@link Menu} object
	 */
	public Menu display(boolean showChosen) {
		System.out.println(toString(showChosen));
		return this;
	}

	/**
	 * Gets the menu choice from the standard input.
	 * <br>Prompts for input as long as the value is either out of range or invalid.
	 * <br>Does not change the chosen booleans.
	 * @return a reference to this Menu object
	 */
	public Menu getInput() {
		return getInput(false);
	}

	/**
	 * Gets the menu choice from the standard input.
	 * <br>Prompts for input as long as the value is either out of range or invalid.
	 * <br>If changeChosen is true, the chosen boolean for that choice will be set to true. 
	 * @param changeChosen
	 * @return a reference to this {@link Menu} object
	 */
	public Menu getInput(boolean changeChosen) {
		int c = -1;

		do {
			System.out.print("Enter: ");
			try {
				c = input.nextInt();
			} catch (InputMismatchException e) {
				input.next();
			}
		} while (!isValidOption(c));

		lastChoice = choice;
		choice = c;

		if (changeChosen && choice != exitOn) {
			chosen[choice - 1] = true;
		}

		return this;
	}

	/**
	 * @param showChosen if true outputs choice tracking text and leaves them out otherwise.
	 * @return The menu as a String, including all title, all options, choice tracking and exit option if applicable.
	 */
	public String toString(boolean showChosen) {
		StringBuilder ret = new StringBuilder(title);

		//Add a line the length of the title underneath the title 
		if (!title.equals("")) {
			ret.append("\n" + new String(new char[title.length()]).replace('\0', '-') + "\n");
		}

		for (int i = 0; i < numOptions; i++) {
			ret.append((i + 1) + ". " + options[i]);
			if (showChosen) {
				ret.append(" - " + (chosen[i] ? chosenText : unchosenText));
			}
			ret.append('\n');
		}

		if (exitAllowed) {
			ret.append(exitOn + ". " + exitText + "\n");
		}



		return ret.toString();
	}

	/**
	 * This method does the same thing as {@link #toString(boolean) toString(false)}
	 * @return The menu as a String, including all title, all options and exit option if applicable. 
	 */
	@Override
	public String toString() {
		return toString(false);
	}

	/**
	 * Copies all of another {@link Menu}'s values into this.
	 * Does not {@link #validateMembers() validate} the values.
	 * @param other
	 */
	public void copy(Menu other) {
		setChoice(other.getChoice());
		setLastChoice(other.getChoice());
		
		setExitOn(other.getExitOn());
		setExitAllowed(other.isExitAllowed());
		
		setNumOptions(other.getNumOptions());
		setOptions(other.getOptions());
		
		setTitle(other.getTitle());
		setExitText(other.getExitText());
		
		setChosen(other.getChosen());
		setChosenText(other.getChosenText());
		setUnchosenText(other.getUnchosenText());
	}


	//-- Convenience --\\
	/**
	 * @return whether the most recent choice was the exit option.
	 */
	public boolean didChooseExit() {
		return choice == exitOn;
	}

	/**
	 * @return whether the most recent choice was the same as the previous choice.
	 */
	public boolean didChooseSame() {
		return choice == lastChoice;
	}


	/**
	 * Validates data members according to these rules:
	 * <br>- numOptions cannot be less than 0
	 * <br>- exitOn cannot be the same number as one of the options
	 * <br>- length of options array must be equal to numChoices
	 * <br>- length of chosen array must be equal to numChoices
	 * <br>- numOptions cannot be 0 if exiting is not allowed
	 * @throws InvalidMemberException
	 */
	public void validateMembers() {
		if (numOptions < 0) {
			throw new InvalidMemberException("The number of options is less than zero.");
		}

		if (exitOn >= 1 && exitOn <= numOptions) {
			throw new InvalidMemberException("The exit option's number (" + exitOn + ") is within the range [1, " + numOptions + "].");
		}

		if (options.length != numOptions) {
			throw new InvalidMemberException("The number of options (" + numOptions + ") does not equal the length of the options array (" + options.length + ").");
		}

		if (chosen.length != numOptions) {
			throw new InvalidMemberException("The number of options (" + numOptions + ") does not equal the length of the chosen array (" + options.length + ").");
		}

		if (numOptions == 0 && !exitAllowed) {
			throw new InvalidMemberException("The number of options is zero and exiting is not allowed.");
		}
	}
	
	/**
	 * Checks whether the argument is a valid menu option. 
	 * @param choice
	 * @return true if choice is equal to one of the options or if exiting is allowed and choice is equal to the exit option, otherwise false.
	 */
	public boolean isValidOption(int choice) {
		return ((!exitAllowed || choice != exitOn) && (choice < 1 || choice > numOptions)) ? false : true;
	}


	//-- Accessors and Mutators --\\
	/**
	 * @return the most recent choice.
	 */
	public int getChoice() {
		return choice;
	}

	/**
	 * Sets the most recent choice.
	 * @param option
	 * @return a reference to this Menu object.
	 */
	public Menu setChoice(int option) {
		choice = option;
		return this;
	}

	/**
	 * @return the previous choice.
	 */
	public int getLastChoice() {
		return lastChoice;
	}

	/**
	 * Sets the previous choice.
	 * @param option
	 * @return a reference to this Menu object.
	 */
	public Menu setLastChoice(int option) {
		lastChoice = option;
		return this;
	}

	/**
	 * @return the number of options excluding the exit option.
	 */
	public int getNumOptions() {
		return numOptions;
	}

	/**
	 * Sets the number of options excluding the exit option.
	 * Must be greater than or equal to zero
	 * @param choices
	 * @return a reference to this {@link Menu} object.
	 */
	public Menu setNumOptions(int choices) {
		numOptions = choices;
		return this;
	}
	
	/**
	 * Sets the number of options equal to the length of the array or zero if a null argument is received.
	 * @param options
	 * @return a reference to this Menu object.
	 */
	public Menu setNumOptions(String[] options) {
		if (options == null) {
			numOptions = 0;
		} else {
			numOptions = options.length;
		}
		
		return this;
	}

	/**
	 * @return whether exiting this Menu with the exit option is possible.
	 */
	public boolean isExitAllowed() {
		return exitAllowed;
	}

	/**
	 * Sets whether exiting this Menu with the exit option is possible.
	 * @param allowed
	 * @return a reference to this Menu object.
	 */
	public Menu setExitAllowed(boolean allowed) {
		exitAllowed = allowed;
		return this;
	}

	/**
	 * @return the exit option's number.
	 */
	public int getExitOn() {
		return exitOn;
	}

	/**
	 * Sets the option number to exit on.
	 * <br>Must not be in the range [1, numOptions]
	 * @param option
	 * @return a reference to this {@link Menu} object.
	 */
	public Menu setExitOn(int option) {
		exitOn = option;
		return this;
	}

	/**
	 * @return the title of the Menu or an empty string if no title was set.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the Menu. Passing a null reference to this method will set the title to an empty String.
	 * @param text
	 * @return a reference to this Menu object.
	 */
	public Menu setTitle(String text) {
		title = text == null ? "" : text;
		return this;
	}

	/**
	 * @return the exit option's text.
	 */
	public String getExitText() {
		return exitText;
	}

	/**
	 * Sets the Menu's exit option's text. Passing a null reference to this method will set the exit text to an empty String.
	 * @param text
	 * @return a reference to this Menu object.
	 */
	public Menu setExitText(String text) {
		exitText = text == null ? "" : text;
		return this;
	}

	/**
	 * @return the options as an array of Strings 
	 */
	public String[] getOptions() {
		return options;
	}

	/**
	 * Copies the passed String array into a new one of the same size.
	 * <br>A null argument will create an empty String array of size numOptions.
	 * <br>Does not change numOptions.
	 * @param choices
	 * @return a reference to this Menu object.
	 */
	public Menu setOptions(String[] choices) {
		if (choices == null) {
			options = new String[numOptions];
			return this;
		}

		options = new String[choices.length];

		for (int i = 0; i < choices.length; i++) {
			setOptionAt(i, choices[i]);
		}
		return this;
	}

	/**
	 * Note: index will be one less than the displayed option number.
	 * @param index
	 * @return the option at the specified index.
	 */
	public String getOptionAt(int index) {
		return options[index];
	}

	/**
	 * Sets the option at the specified index with a copy of the passed String.
	 * <br>Note: index will be one less than the displayed option number.
	 * @param index
	 * @param option
	 * @return a reference to this Menu object.
	 */
	public Menu setOptionAt(int index, String option) {
		options[index] = new String(option);
		return this;
	}

	/**
	 * @return the choice tracking array of booleans.
	 */
	public boolean[] getChosen() {
		return chosen;
	}

	/**
	 * Copies the passed boolean array into a new one of the same size.
	 * <br>A null argument will create an empty boolean array of size numOptions.
	 * @param chosen
	 * @return a reference to this Menu object.
	 */
	public Menu setChosen(boolean[] chosen) {
		if (chosen == null) {
			this.chosen = new boolean[numOptions];
			return this;
		}

		this.chosen = new boolean[chosen.length];

		for (int i = 0; i < chosen.length; i++) {
			setChosenAt(i, chosen[i]);
		}
		return this;
	}

	/**
	 * Allocates a new boolean array of size numOptions and sets all elements to false.
	 * @return a reference to this Menu object.
	 */
	public Menu initChosen() {
		chosen = new boolean[numOptions];
		setComplete(false);

		return this;
	}

	/**
	 * Note: index will be one less than the displayed option number.
	 * @param index
	 * @return whether the specified option has been chosen.
	 */
	public boolean isChosenAt(int index) {
		return chosen[index];
	}

	/**
	 * Sets whether the options has been chosen at the specified index.
	 * <br>Note: index will be one less than the displayed option number.
	 * @param index
	 * @param picked
	 * @return a reference to this Menu object.
	 */
	public Menu setChosenAt(int index, boolean picked) {
		chosen[index] = picked;
		return this;
	}

	/**
	 * Convenience method
	 * @return whether all options have been chosen at least once.
	 */
	public boolean isComplete() {
		for (int i = 0; i < numOptions; i++) {
			if (!chosen[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Sets all of the elements in the choice tracking array to the passed value.
	 * @param complete
	 * @return a reference to this Menu object.
	 */
	public Menu setComplete(boolean complete) {
		for (int i = 0; i < numOptions; i++) {
			chosen[i] = complete;
		}
		return this;
	}

	/**
	 * @return the number of options that have been chosen.
	 */
	public int getNumChosen() {
		int ret = 0;

		for (int i = 0; i < numOptions; i++) {
			if (chosen[i]) {
				ret++;
			}
		}

		return ret;
	}

	/**
	 * @return the number of options that have not been chosen.
	 */
	public int getNumUnchosen() {
		return numOptions - getNumChosen();
	}

	/**
	 * @return the text displayed next to options that have been chosen.
	 */
	public String getChosenText() {
		return chosenText;
	}

	/**
	 * Sets the text displayed next to options that have been chosen. Passing a null reference to this method will set the chosen text to an empty String.
	 * @param text
	 * @return a reference to this Menu object.
	 */
	public Menu setChosenText(String text) {
		chosenText = text == null ? "" : text;
		return this;
	}

	/**
	 * @return the text displayed next to options that have not been chosen.
	 */
	public String getUnchosenText() {
		return unchosenText;
	}

	/**
	 * Sets the text displayed next to options that have not been chosen. Passing a null reference to this method will set the unchosen text to an empty String.
	 * @param text
	 * @return a reference to this Menu object.
	 */
	public Menu setUnchosenText(String text) {
		unchosenText = text == null ? "" : text;
		return this;
	}



	//-- Building and Saving Menus --\\
	/**
	 * Builds a {@link Menu} object from a JSON file. 
	 * @param path
	 * @return the {@link Menu} object or null if the Menu could not be built
	 * @throws FileNotFoundException
	 */
	public static Menu loadFromFile(String path) throws FileNotFoundException  {
		Menu ret = null;

		try (JsonReader jr = Json.createReader(new FileInputStream(path))) {
			JsonObject jo = jr.readObject();

			JsonArray ja = jo.getJsonArray("options");
			int numOptions = jo.getInt("numOptions", ja != null ? ja.size() : 0);
			String[] options = new String[numOptions];
			
			for (int i = 0; i < numOptions; i++) {
				options[i] = ja.getString(i);
			}

			ret = new Menu(jo.getString("title", ""), 
					numOptions, 
					options, 
					jo.getBoolean("exitAllowed", false), 
					jo.getInt("exitOn", 0), 
					jo.getString("exitText", ""), 
					jo.getString("chosenText", ""), 
					jo.getString("unchosenText", ""));
		}

		ret.validateMembers();
		return ret;
	}

	/**
	 * Writes a {@link Menu} to a JSON file.
	 * @param path
	 * @param menu
	 * @throws FileNotFoundException
	 */
	public static void saveToFile(String path, Menu menu) throws FileNotFoundException {
		menu.validateMembers();
		
		try (JsonWriter jw = Json.createWriter(new FileOutputStream(path))) {
			JsonObjectBuilder jo = Json.createObjectBuilder();
			JsonArrayBuilder ja = Json.createArrayBuilder();
			addString("title", menu.getTitle(), jo);
			jo.add("numOptions", menu.getNumOptions());

			for (int i = 0; i < menu.getNumOptions(); i++) {
				ja.add(menu.getOptionAt(i));
			}
			jo.add("options", ja);
			jo.add("exitAllowed", menu.isExitAllowed());
			jo.add("exitOn", menu.getExitOn());
			addString("exitText", menu.getExitText(), jo);
			addString("chosenText", menu.getChosenText(), jo);
			addString("unchosenText", menu.getUnchosenText(), jo);
			
			jw.write(jo.build());
		}
	}

	/**
	 * Convenience method that handles adding a null String to the JsonObjectBuilder. 
	 * @param key the String object in the Menu
	 * @param value the text to be displayed
	 * @param obj
	 * @return a reference to the passed {@link JsonObjectBuilder}
	 */
	private static JsonObjectBuilder addString(String key, String value, JsonObjectBuilder obj) {
		if (value == null) {
			obj.add(key, JsonValue.NULL);
		} else {
			obj.add(key, value);
		}
		
		return obj;
	}




}