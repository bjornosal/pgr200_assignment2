package no.salvesen.assignment2;

public class Menu {


    private String[] tableChoices;
    private String[] searchChoices;
    private String[] mainMenuChoices;
    private String[] propertiesMenuChoices;

    /**
     * Constructor. Initializes all menus.
     */
    public Menu() {
        setTableMenuOptions();
        setSearchMenuOptions();
        setMainMenuOptions();
        setPropertiesMenuOptions();
    }


    public String mainMenu() {
        return menuHeader() + menuBuilder(getMainMenuChoices(), false);
    }
    public String tableMenu() {
        return menuHeader() + menuBuilder(getTableChoices(), true);
    }
    public String searchMenu() {
        return  menuHeader() + menuBuilder(getSearchChoices(), true);
    }
    public String propertiesMenu() {return menuHeader() + menuBuilder(getPropertiesMenuChoices(), false);}

    /**
     * Builds the menu based on menu and if it should have the last options.
     * @param menuOptions Which menu options to set in menu.
     * @param addMainMenuAndQuitOptions If a menu should have the two options return to main menu and quit
     * @return Returns a finished menu, ready to be output to user.
     */
    private String menuBuilder(String[] menuOptions, boolean addMainMenuAndQuitOptions) {

        StringBuilder menu = new StringBuilder();

        for(int i = 0; i < menuOptions.length; i++) {

            menu.append(i + 1).append(": ").append(menuOptions[i]);
            if(i < menuOptions.length - 1) {
                menu.append("\n");
            }
            if(i == menuOptions.length - 1 && addMainMenuAndQuitOptions) {
                menu.append("\n");
                menu.append(i + 2).append(": Return to main menu\n");
                menu.append(i + 3).append(": Quit");
            }
        }

        return menu.toString();
    }

    /**
     * Sets the content of the properties menu
     */
    private void setPropertiesMenuOptions() {
        propertiesMenuChoices = new String[2];
        propertiesMenuChoices[0] = "Use default database properties.";
        propertiesMenuChoices[1] = "Set session-login.";
    }

    /**
     * Sets the content of the main menu.
     */
    private void setMainMenuOptions() {
        mainMenuChoices = new String[2];
        mainMenuChoices[0] = "Get information from table.";
        mainMenuChoices[1] = "Add information to table.";
    }

    /**
     * Sets the content of the search menu.
     */
    private void setSearchMenuOptions() {
        searchChoices = new String[7];
        searchChoices[0] = "Get information on a subject";
        searchChoices[1] = "Get information on all subjects";
        searchChoices[2] = "Get information on a lecturer";
        searchChoices[3] = "Get information on all lecturers";
        searchChoices[4] = "Get information on a room";
        searchChoices[5] = "Get information on all rooms";
        searchChoices[6] = "Get information on subject and lecturer";
    }

    /**
     * Sets the content of the fill table menu.
     */
    private void setTableMenuOptions() {
        tableChoices = new String[6];
        tableChoices[0] = "Add new filepath for \'subject\' table information";
        tableChoices[1] = "Add new filepath for \'room\' table information";
        tableChoices[2] = "Add new filepath for \'lecturer\' table information";
        tableChoices[3] = "Add new filepath for \'lecturer_in_subject\' table information";
        tableChoices[4] = "Use existing files in files folder";
        tableChoices[5] = "Fill a table with information from file.";
    }


    /**
     * Gets menu header
     * @return String A header for the result to be printed.
     */
    private String menuHeader() {
        String menuSeparator = "----------------------------------------------";
        return menuSeparator +
                "\nEnter the number before action and press enter\n" +
                menuSeparator +
                "\n";
    }

    private String[] getTableChoices() {
        return tableChoices;
    }

    private String[] getSearchChoices() {
        return searchChoices;
    }

    private String[] getMainMenuChoices() {
        return mainMenuChoices;
    }

    private String[] getPropertiesMenuChoices() {
        return propertiesMenuChoices;
    }
}
