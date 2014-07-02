/**
 * 
 */
package ca.etsmtl.log430.lab3;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PipedWriter;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ca.etsmtl.log430.lab3.utils.FilesUtil;

/**
 * @author Charly
 * 
 */
public class TauxFilterTest {

    private String inputFile;

    private String outputFileAcceptGenerated;
    private String outputFileRejectGenerated;

    private String outputFileAcceptComparing;
    private String outputFileRejectComparing;

    /**
     * generate ouputsFiles
     * 
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        inputFile = "datain.txt";

        outputFileAcceptGenerated = "testsFile/generatedFiles/outputTauxFilterAccepted.txt";
        outputFileRejectGenerated = "testsFile/generatedFiles/outputTauxFilterRejected.txt";

        outputFileAcceptComparing = "testsFile/comparingFiles/outputTauxFilterAccepted.txt";
        outputFileRejectComparing = "testsFile/comparingFiles/outputTauxFilterRejected.txt";

        // Instantiate Filter Threads
        PipedWriter inputPipe = new PipedWriter();

        PipedWriter acceptPipe = new PipedWriter();
        PipedWriter rejectPipe = new PipedWriter();

        Thread fileReaderFilter = new FileReaderFilter(inputFile, inputPipe);

        // Thread tauxFilter = new TauxFilter("", pipe01, acceptPipe,
        // rejectPipe);

        Thread fileWriterFilterAccepted = new FileWriterFilter(outputFileAcceptGenerated, acceptPipe);
        Thread fileWriterFilterRejected = new FileWriterFilter(outputFileRejectGenerated, rejectPipe);

        // Start the threads
        fileReaderFilter.start();
        // tauxFilter.start();
        fileWriterFilterAccepted.start();
        fileWriterFilterRejected.start();

        // wait for duration if thread doesn't stop

        Date startTime = new Date();

        // 4s
        long duration = 4000L;

        while ((fileReaderFilter.isAlive() || fileWriterFilterRejected.isAlive()) && (((new Date()).getTime() - startTime.getTime()) < duration))

            ;
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAccept() {

        try {
            assertTrue(FilesUtil.SameFiles(outputFileAcceptGenerated, outputFileAcceptComparing));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReject() {
        try {
            assertTrue(FilesUtil.SameFiles(outputFileRejectGenerated, outputFileRejectComparing));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}