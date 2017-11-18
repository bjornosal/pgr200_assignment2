package no.salvesen.assignment2;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FileReaderTest {

    private FileReader fileReader;

    @Before
    public void setUp() throws Exception {
        String subjectPathName = "src/test/files/Test_table_files/subject_test_file.csv";
        String lecturerPathName = "src/test/files/Test_table_files/lecturer_test_file.csv";
        String lecturerInSubjectPathName = "src/test/files/Test_table_files/lecturer_in_subject_test_file.csv";
        String roomPathName = "src/test/files/Test_table_files/room_test_file.csv";

        fileReader = new FileReader(subjectPathName, roomPathName,lecturerPathName, lecturerInSubjectPathName);

    }

    /**
     * Asserts that the readFile method works as intended
     * by checking that all fields is the correct size according to the file.
     * @throws Exception exception
     */
    @Test
    public void readFile() throws Exception {
        fileReader.readFile(fileReader.getFileByTableName("subject"));
        assertEquals(fileReader.getTableColumnCount(), 5);
        assertThat(fileReader.getColumnNames().size(), is(5));
        //Column names and primary key
        assertThat(fileReader.getColumnSQLValues().size(), is(6));
        assertThat(fileReader.getInsertionValues().size(), is(45));
    }
}