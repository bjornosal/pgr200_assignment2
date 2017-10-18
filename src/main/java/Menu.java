
public class Menu {

    private String[] tableChoices;

    public Menu() {
        setTableChoices();

    }

    public String tableMenu() {
        StringBuilder tableMenu = new StringBuilder();

        tableMenu.append(menuHeader());
        tableMenu.append(menuChoices(getTableChoices()));

        return tableMenu.toString();
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
            menuChoices.append(i+1).append(": ").append(choices[i]).append("\n");
        }

        return menuChoices.toString();
    }

    private void setTableChoices() {
        tableChoices = new String[4];
        tableChoices[0] = "Read in information for tables";
        tableChoices[1] = "Search for subject";
        tableChoices[2] = "Get information on all subjects";
        tableChoices[3] = "Quit";
    }

    private String[] getTableChoices() {
        return tableChoices;
    }



}
