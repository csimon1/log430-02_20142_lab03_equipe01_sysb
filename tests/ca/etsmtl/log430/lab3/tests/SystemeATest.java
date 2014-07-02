/**
 * 
 */
package ca.etsmtl.log430.lab3.tests;

import static org.junit.Assert.fail;

import java.io.PipedWriter;

import org.junit.Test;

import ca.etsmtl.log430.lab3.ColumnChoiceFilter;
import ca.etsmtl.log430.lab3.ColumnSortFilter;
import ca.etsmtl.log430.lab3.ConsoleWriterFilter;
import ca.etsmtl.log430.lab3.DataSortFilter;
import ca.etsmtl.log430.lab3.FileReaderFilter;
import ca.etsmtl.log430.lab3.MergeFilter;
import ca.etsmtl.log430.lab3.StateFilter;
import ca.etsmtl.log430.lab3.StatusFilter;

/**
 * @author Charly
 * 
 */
public class SystemeATest {

    @Test
    public void test() {

        String argv0 = "datain.txt";

        // These are the declarations for the pipes.
        PipedWriter pipe01 = new PipedWriter();
        PipedWriter pipe02 = new PipedWriter();
        PipedWriter pipe03 = new PipedWriter();
        PipedWriter pipe04 = new PipedWriter();
        PipedWriter pipe05 = new PipedWriter();
        PipedWriter pipe06 = new PipedWriter();
        PipedWriter pipeR04 = new PipedWriter();
        PipedWriter pipeR05 = new PipedWriter();
        PipedWriter pipeR06 = new PipedWriter();

        PipedWriter pipe07 = new PipedWriter();
        PipedWriter pipe08 = new PipedWriter();
        PipedWriter pipe09 = new PipedWriter();
        PipedWriter pipeR07 = new PipedWriter();
        PipedWriter pipeR08 = new PipedWriter();
        PipedWriter pipeR09 = new PipedWriter();

        // Instantiate Filter Threads
        Thread fileReaderFilter = new FileReaderFilter(argv0, pipe01);

        Thread statusFilter = new StatusFilter(pipe01, pipe02, pipe03);

        Thread stateFilter1 = new StateFilter("RIS", pipe02, pipe04, pipeR04);
        Thread stateFilter2 = new StateFilter("DIF", pipe03, pipe05, pipeR05);

        Thread mergeFilter1 = new MergeFilter(pipe04, pipe05, pipe06);
        Thread mergeFilter2 = new MergeFilter(pipeR04, pipeR05, pipeR06);

        Thread columnChoiceFilterAccepted = new ColumnChoiceFilter(pipe06, pipe07);
        Thread columnSortFilterAccepted = new ColumnSortFilter(pipe07, pipe08);
        Thread dataSortFilterAccepted = new DataSortFilter(pipe08, pipe09);

        Thread columnChoiceFilterRejected = new ColumnChoiceFilter(pipeR06, pipeR07);
        Thread columnSortFilterRejected = new ColumnSortFilter(pipeR07, pipeR08);
        Thread dataSortFilterRejected = new DataSortFilter(pipeR08, pipeR09);

        Thread consoleWriterFilterAccept = new ConsoleWriterFilter("accept", pipe09);
        Thread consoleWriterFilterReject = new ConsoleWriterFilter("reject", pipeR09);

        // Start the threads
        fileReaderFilter.start();

        statusFilter.start();

        stateFilter1.start();
        stateFilter2.start();

        mergeFilter1.start();
        mergeFilter2.start();

        columnChoiceFilterAccepted.start();
        columnSortFilterAccepted.start();
        dataSortFilterAccepted.start();

        columnChoiceFilterRejected.start();
        columnSortFilterRejected.start();
        dataSortFilterRejected.start();

        consoleWriterFilterAccept.start();
        consoleWriterFilterReject.start();

        fail("OutPut not Tested");
    }

}
