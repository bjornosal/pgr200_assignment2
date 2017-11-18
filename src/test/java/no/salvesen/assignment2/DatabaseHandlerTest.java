package no.salvesen.assignment2;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * The type Database handler test.
 */
public class DatabaseHandlerTest {


    private DatabaseHandler databaseHandler;
    private PropertiesHandler propertiesHandler;
    private FileReader fileReader;

    private final String subjectFilePathName = "src/test/files/Test_table_files/subject_test_file.csv";
    private final String lecturerFilePathName = "src/test/files/Test_table_files/lecturer_test_file.csv";
    private final String roomFilePathName = "src/test/files/Test_table_files/room_test_file.csv";
    private final String lecturerInSubjectPathName = "src/test/files/Test_table_files/lecturer_in_subject_test_file.csv";

    /**
     * Instantiates a new Database handler test.
     *
     * @throws IOException  the io exception
     * @throws SQLException the sql exception
     */
    public DatabaseHandlerTest() throws IOException, SQLException {
        fileReader = new FileReader(subjectFilePathName, roomFilePathName, lecturerFilePathName, lecturerInSubjectPathName);
        propertiesHandler = new PropertiesHandler();
        databaseHandler = new DatabaseHandler(propertiesHandler, fileReader);
    }

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
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


    /**
     * Tear down database and set back up.
     *
     * @throws Exception the exception
     */
    @Test
    public void tearDownDatabaseAndSetBackUp() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    /**
     * Tear down table and set back up with new information.
     *
     * @throws Exception the exception
     */
    @Test
    public void tearDownTableAndSetBackUpWithNewInformation() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
        databaseHandler.tearDownTableAndSetBackUpWithNewInformation("room");
        assertThat(databaseHandler.getArrayListOfTableNames().size(), is(4));
    }

    /**
     * Gets rows from table by column name and search column value.
     *
     * @throws Exception the exception
     */
    @Test
    public void getRowsFromTableByColumnNameAndSearchColumnValue() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();

        assert(databaseHandler.getRowsFromTableByColumnNameAndSearchColumnValue("subject","code","pro100").contains("Creative Testing"));
    }

    /**
     * Database exists after being set up.
     *
     * @throws Exception the exception
     */
    @Test
    public void databaseExistsAfterBeingSetUp() throws Exception {
        databaseHandler.createDatabase();
        assertTrue(databaseHandler.databaseExists());
    }

    /**
     * Gets all rows by table name.
     *
     * @throws Exception the exception
     */
    @Test
    public void getAllRowsByTableName() throws Exception {
        databaseHandler.tearDownDatabaseAndSetBackUp();
        String allRowsFromSubjectTable = databaseHandler.getAllRowsByTableName("subject");

        assertTrue(allRowsFromSubjectTable.contains("PGR200"));
        assertTrue(allRowsFromSubjectTable.contains("PGR100"));
        assertTrue(allRowsFromSubjectTable.contains("PRO100"));
        assertTrue(allRowsFromSubjectTable.contains("PRO101"));
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        if(databaseHandler.databaseExists()) {
            databaseHandler.dropDatabase();
        }
    }
}