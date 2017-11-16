package no.salvesen.assignment2;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DatabaseHandlerTest {


    private DatabaseHandler databaseHandler;
    private PropertiesHandler propertiesHandler;
    private FileReader fileReader;

    private final String subjectFilePathName = "src/test/files/Test_table_files/subject_test_file.csv";
    private final String lecturerFilePathName = "src/test/files/Test_table_files/lecturer_test_file.csv";
    private final String roomFilePathName = "src/test/files/Test_table_files/room_test_file.csv";
    private final String lecturerInSubjectPathName = "src/test/files/Test_table_files/lecturer_in_subject_test_file.csv";

    public DatabaseHandlerTest() throws IOException, SQLException {
        fileReader = new FileReader();
        propertiesHandler = new PropertiesHandler();
        databaseHandler = new DatabaseHandler(propertiesHandler, fileReader);
    }

    @Before
    public void setUp() throws Exception {
        fileReader.setLecturer_in_subject_file(new File(lecturerInSubjectPathName));
        fileReader.setRoomFile(new File(roomFilePathName));
        fileReader.setSubjectFile(new File(subjectFilePathName));
        fileReader.setLecturerFile(new File(lecturerFilePathName));

        propertiesHandler.setPropertyFilePath("src/test/files/testDatabaseLogin.properties");
        databaseHandler.startConnection();
        databaseHandler.setUpDatabase();
    }


    @Test
    public void tearDownDatabaseAndSetBackUp() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    @Test
    public void tearDownTableAndSetBackUpWithNewInformation() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
        databaseHandler.tearDownTableAndSetBackUpWithNewInformation("room");
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    @Test
    public void getRowsFromTableByColumnNameAndSearchColumnValue() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();

        assert(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("subject","code","pro100").contains("Creative Testing"));
    }

    @Test
    public void databaseExists() throws Exception {
        databaseHandler.createDatabase();
        assertTrue(databaseHandler.databaseExists());
    }

    @Test
    public void getAllRowsByTableName() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        String allRowsFromSubjectTable = databaseHandler.getAllRowsByTableName("subject");

        assertTrue(allRowsFromSubjectTable.contains("PGR200"));
        assertTrue(allRowsFromSubjectTable.contains("PGR100"));
        assertTrue(allRowsFromSubjectTable.contains("PRO100"));
        assertTrue(allRowsFromSubjectTable.contains("PRO101"));
    }

    @After
    public void tearDown() throws Exception {
        databaseHandler.dropDatabase();
    }
}