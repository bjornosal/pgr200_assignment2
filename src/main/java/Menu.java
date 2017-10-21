
public class Menu {


    private String[] tableChoices;
    private String[] searchChoices;
    private String[] mainMenu;


    public Menu() {
        setTableChoices();
        setSearchChoices();
        setMainMenuChoices();
    }

    public String mainMenu() {
        return menuHeader() + menuChoices(getMainMenuChoices());
    }
    public String tableMenu() {
        return menuHeader() + menuChoices(getTableChoices());
    }
    public String searchMenu() {
        return  menuHeader() + menuChoices(getSearchChoices());
    }



    /**
     * TODO Set up total menu logic
     * Todo when logic is clear, switches can be set up with recursion?
     */


    //Make it dynamic
    //One param, an array that contains all choices
    private String menuHeader() {
        StringBuilder menuHeader = new StringBuilder();

        String menuSeparator = "#####################################\n";

        menuHeader.append("\t\t\tMenu\n");
        menuHeader.append(menuSeparator);
        menuHeader.append("What would you like to do? \nEnter the number before action and press enter\n");
        menuHeader.append(menuSeparator);

        return menuHeader.toString();
    }

    private String menuChoices(String[] choices) {

        StringBuilder menuChoices = new StringBuilder();

        for(int i = 0; i < choices.length; i++) {

            menuChoices.append(i + 1).append(": ").append(choices[i]).append("\n");
            if(i == choices.length - 1) {
                menuChoices.append(i + 2 + ": Return to main menu\n");
                menuChoices.append(i + 3 + ": Quit\n");
            }
        }

        return menuChoices.toString();
    }

    //menuchoices for all searches that a user can do.
    private void setSearchChoices() {
        searchChoices = new String[6];
        searchChoices[0] = "Get information on a subject";
        searchChoices[1] = "Get information on all subjects";
        searchChoices[2] = "Get information on a lecturer";
        searchChoices[3] = "Get information on all lecturers";
        searchChoices[4] = "Get information on a room";
        searchChoices[5] = "Get information on all rooms";

    }

    //Choices that makes a change on tables
    private void setTableChoices() {
        tableChoices = new String[4];
        tableChoices[0] = "Enter information for \'subject\' table";
        tableChoices[1] = "Enter information for \'room\' table";
        tableChoices[2] = "Enter information for \'lecturer\' table";
        tableChoices[3] = "Use existing files n files folder";
    }

    private void setMainMenuChoices() {
        mainMenu = new String[2];
        mainMenu[0] = "Get information";
        mainMenu[1] = "Enter information";
    }

    private String[] getTableChoices() {
        return tableChoices;
    }

    private String[] getSearchChoices() {
        return searchChoices;
    }

    private String[] getMainMenuChoices() {
        return mainMenu;
    }
}
