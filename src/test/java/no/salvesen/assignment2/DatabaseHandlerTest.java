package no.salvesen.assignment2;

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

    private final String subjectFilePathName = "src/test/files/Test_table_files/subject_test_file.csv";
    private final String lecturerFilePathName = "src/test/files/Test_table_files/lecturer_test_file.csv";
    private final String roomFilePathName = "src/test/files/Test_table_files/room_test_file.csv";
    private final String lecturerInSubjectPathName = "src/test/files/Test_table_files/lecturer_in_subject_test_file.csv";

    public DatabaseHandlerTest() throws IOException, SQLException {
        databaseHandler = new DatabaseHandler(subjectFilePathName, roomFilePathName, lecturerFilePathName, lecturerInSubjectPathName);
    }

    @Before
    public void setUp() throws Exception {
        databaseHandler.setPropertyFilePath("src/test/files/testDatabaseLogin.properties");
        databaseHandler.startConnection();
        databaseHandler.setUpDatabase();
    }

    @Test
    public void tearDownDatabaseAndSetBackUp() throws Exception {
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
        databaseHandler.tearDownDatabaseAndSetBackUp();
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    @Test
    public void tearDownTableAndSetBackUpWithNewInformation() throws Exception {
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
        databaseHandler.tearDownTableAndSetBackUpWithNewInformation("subject");
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    @Test
    public void getRowsFromTableByColumnNameAndSearchColumnValue() throws Exception {
        assert(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("subject","code","pro100").contains("Creative Testing"));
    }

}