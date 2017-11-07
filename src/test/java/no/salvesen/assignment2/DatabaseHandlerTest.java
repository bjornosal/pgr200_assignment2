package no.salvesen.assignment2;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DatabaseHandlerTest {
    private DatabaseHandler databaseHandler;
    private FileReader fileReader;
    private ArrayList<String> foreignKeysToBeAdded;

    public DatabaseHandlerTest() {
        databaseHandler = new DatabaseHandler();
        fileReader = new FileReader();
        foreignKeysToBeAdded = new ArrayList<>();
    }

    @Before
    public void setUp() throws Exception {
        fileReader.setSubjectFile(new File("src/test/Test_table_files/subject_test_file.csv"));
        fileReader.setLecturerFile(new File("src/test/Test_table_files/lecturer_test_file.csv"));
        fileReader.setRoomFile(new File("src/test/Test_table_files/room_test_file.csv"));

        databaseHandler.setPropertyFilePath("src/files/testDatabaseLogin.properties");
        databaseHandler.startConnection();
    }

    @Test
    public void tearDownDatabaseAndSetBackUp() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(3));
    }

    @Test
    public void tearDownTableAndSetBackUpWithNewInformation() throws Exception {
        databaseHandler.tearDownTableAndSetBackUpWithNewInformation("subject");
        assert(databaseHandler.getAllRowsByTableName("subject").contains("Code   | Name                          | Attending Students | Teaching Form | Duration |"));
    }

    @Test
    public void getRowsFromTableByColumnNameAndSearchColumnValue() throws Exception {
    }

    @Test
    public void getAllRowsByTableName() throws Exception {
    }

    @Test
    public void createDatabase() throws Exception {
    }

    @Test
    public void startConnection() throws Exception {
    }

}