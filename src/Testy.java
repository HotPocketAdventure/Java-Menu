import com.github.HotPocketAdventure.util.Menu;

public class Testy {

	public static void main(String[] args) {
//		TestDefault();
//		TestAllExitAllowed();
//		TestAllExitDisallowed();
//		TestBuildAfterCtor();
		TestNonChoice();
	}
	
	public static Menu TestDefault() {
		Menu menu = new Menu();

		do {
			menu.display().getInput();
			if (menu.didChooseExit()) {
				System.out.println("Default test finished!\n");
			} else if (menu.didChooseSame()) {
				System.out.println("\nYou chose the same option as last time!\n");
			} else {
				System.out.println("\nYou chose option " + menu.getChoice() + "\n");
			}
		} while (!menu.didChooseExit());
		
		return menu;
	}
	
	public static Menu TestAllExitAllowed() {
		Menu menu = new Menu("Test", 4, new String[] {"First", "Second", "Third", "Fourth"}, true, -1, "Finish Test", false, "Done!", "Not yet!");
		
		do {
			menu.display(true).getInput(true);
			
			if (menu.didChooseExit()) {
				if (menu.isComplete()) {
					System.out.println("All, exit allowed test finished!\n");
				} else {
					System.out.println("You can't escape that easily!\n");
				}
			} else if (menu.isComplete()) {
				System.out.println("\nYou may finally leave this menu!\n");
			} else {
				System.out.println("\nYou've got " + menu.getNumUnchosen() + " options left!\nChoose them all to exit!\n");
			}
		} while (!menu.isComplete() || !menu.didChooseExit());
			
		return menu;
	}
	
	public static Menu TestAllExitDisallowed() {
		Menu menu = new Menu("Test 2", 2, new String[] {"Finish all", "To exit"}, false, 0, "This should not show up in the menu", true, "x", "o");
		
		do {
			menu.display(true).getInput(true);
			System.out.println(menu.getNumUnchosen() + " options left to choose!");
		} while (!menu.isComplete());
		
		System.out.println("All, exit disallowed test finished!\n");
		
		return menu;
	}
	
	public static Menu TestBuildAfterCtor() {
		Menu menu = new Menu();
		
		menu.setExitAllowed(true);
		menu.setExitOn(0);
		menu.setIncompleteExit(true);
		
		menu.setNumOptions(0);
		menu.setOptions(null);
		
		
		menu.setTitle("Test");
		menu.setExitText("Goodbye!");
		
		menu.initChosen();
		menu.setChosenText("Choosed");
		menu.setUnchosenText("Not choosed");
		
		menu.validateMembers();
		
		do {
			menu.display(true).getInput(true);
			if (menu.didChooseExit()) {
				System.out.println("Build after ctor test finished!\n");
			} else if (menu.didChooseSame()) {
				System.out.println("\nYou chose the same option as last time!\n");
			} else {
				System.out.println("\nYou chose option " + menu.getChoice() + "\n");
			}
		} while (!menu.isComplete() || !menu.didChooseExit());
		
		return menu;
		
	}
	
	public static Menu TestNonChoice() {
		Menu menu = new Menu("Test", new String[] {"This menu", "Did not", "Set the", "Number of options"}, true, 0, "All done");
		
		do {
			menu.display(true).getInput(true);
			System.out.println("You chose " + menu.getChoice() + ", and " + (menu.getLastChoice() > 0 ? "you chose " + menu.getLastChoice() + " last time!" : "this is your first choice!"));
		} while (!menu.didChooseExit());
		
		System.out.println("Until next time!");
		
		return menu;
	}
}
