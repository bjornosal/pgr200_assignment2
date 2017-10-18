
public class Menu {

    public Menu() {
        setTableChoices();

    }

    private String[] tableChoices;

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

    public String menuChoices(String[] choices) {

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

    public String[] getTableChoices() {
        return tableChoices;
    }

    /*
    "1: Read in information for tables\n" +
            "2: Search for subject\n" +
            "3: Get information on all subjects\n" +
            "4: Quit\n";*/

}
