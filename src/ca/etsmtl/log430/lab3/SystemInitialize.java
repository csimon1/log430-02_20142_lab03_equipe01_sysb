package ca.etsmtl.log430.lab3;

import java.io.PipedWriter;

/**
 * This class contains the main method for assignment 3. The program
 * consists of these files:<br><br>
 * 
 * 1) SystemInitialize: instantiates all filters and pipes, starts all filters.<br>
 * 2) FileReaderFilter: reads input file and sends each line to its output pipe.<br>
 * 3) StatusFilter: separates the input stream in two project types (REG, CRI)
 * and writes lines to the appropriate output pipe.<br>
 * 4) StateFilter: determines if an entry contains a particular state specified
 *    at instantiation. If so, sends the whole line to its output pipe.<br>
 * 5) MergeFilter: accepts inputs from 2 input pipes and writes them to its output pipe.<br>
 * 6) FileWriterFilter: sends its input stream to a text file.<br><br>
 * 
 * Pseudo Code:
 * <pre>
 * instantiate all filters and pipes
 * start FileReaderFilter
 * start StatusFilter
 * start StateFilter for RIS
 * start StateFilter for DIF
 * start MergeFilter
 * start FileWriterFilter
 * </pre>
 * 
 * Running the program:
 * <pre>
 * java SystemInitialize InputFile OutputFile > DebugFile
 * 
 * SystemInitialize - Program name
 * InputFile - Text input file (see comments below)
 * OutputFile - Text output file with students
 * DebugFile - Optional file to direct debug statements
 * </pre>
 * 
 * @author A.J. Lattanze
 * @author Samuel -added missing comments
 * @version 1.0 - initial version
 */

/*public class SystemInitialize {
    public static void main(String argv[]) {

        systemeA(argv);

    } // main

    private static void systemeA(String argv[]) {

        // Let's make sure that input and output files are provided on the
        // command line
        if (argv.length != 3) {
            System.out.println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
            System.out.println("\njava SystemInitialize <fichier d'entree> <fichier de sortie> <fichier de sortie 2>");
        } else {

            // These are the declarations for the pipes.
            PipedWriter pipe01 = new PipedWriter();
            PipedWriter pipe02 = new PipedWriter();
            PipedWriter pipe03 = new PipedWriter();
            PipedWriter pipe04 = new PipedWriter();
            PipedWriter pipe05 = new PipedWriter();
            PipedWriter pipe06 = new PipedWriter();
            PipedWriter pipeR04 = new PipedWriter();
            PipedWriter pipeR05 = new PipedWriter();
            PipedWriter pipe07 = new PipedWriter();

            // Instantiate Filter Threads
            Thread fileReaderFilter = new FileReaderFilter(argv[0], pipe01);
            Thread statusFilter = new StatusFilter(pipe01, pipe02, pipe03);
            Thread stateFilter1 = new StateFilter("RIS", pipe02, pipe04, pipeR04);
            Thread stateFilter2 = new StateFilter("DIF", pipe03, pipe05, pipeR05);
            Thread mergeFilter = new MergeFilter(pipe04, pipe05, pipe06);
            Thread fileWriterFilter = new FileWriterFilter(argv[1], pipe06);
            Thread rejectedFilter = new RejectedFilter(pipeR04, pipeR05, pipe07);
            Thread fileWriterFilter2 = new FileWriterFilter(argv[2], pipe07);

            // Start the threads
            fileReaderFilter.start();
            statusFilter.start();
            stateFilter1.start();
            stateFilter2.start();
            mergeFilter.start();
            fileWriterFilter.start();
            rejectedFilter.start();
            fileWriterFilter2.start();
        }
    }

    private static void systemeB(String argv[]) {

    }

} // SystemInitialize

*/
public class SystemInitialize 
{
	public static void main(String argv[]) 
	{
		// Let's make sure that input and output files are provided on the
		// command line
		if (argv.length != 3) 
		{
			System.out
					.println("\n\nNombre incorrect de parametres d'entree. Utilisation:");
			System.out
					.println("\njava SystemInitialize <fichier d'entree> <fichier de sortie> <fichier de sortie 2>");
		}
		else 
		{
			// These are the declarations for the pipes.
			PipedWriter pipe01 = new PipedWriter();
			PipedWriter pipe02 = new PipedWriter();
			PipedWriter pipe03 = new PipedWriter();
			PipedWriter pipe04 = new PipedWriter();
			PipedWriter pipe05 = new PipedWriter();
			PipedWriter pipeR04 = new PipedWriter();
			PipedWriter pipeR05 = new PipedWriter();
			PipedWriter pipe06 = new PipedWriter();
			PipedWriter pipe07 = new PipedWriter();
			PipedWriter pipe08 = new PipedWriter();
			PipedWriter pipe09 = new PipedWriter();
			PipedWriter pipe10 = new PipedWriter();
			PipedWriter pipe11 = new PipedWriter();
			PipedWriter pipe12 = new PipedWriter();
			PipedWriter pipe13 = new PipedWriter();
			
			// Instantiate Filter Threads
			Thread fileReaderFilter = new FileReaderFilter(argv[0], pipe01);
			Thread statusFilter = new StatusFilter(pipe01, pipe02, pipe03);
			Thread stateFilter1 = new StateFilter("RIS", pipe02, pipe04, pipeR04);
			Thread stateFilter2 = new StateFilter("DIF", pipe03, pipe05, pipeR05);
			Thread mergeFilter = new MergeFilter(pipe04, pipe05, pipe06);
			Thread columnChoiceFilter = new ColumnChoiceFilter(pipe06, pipe07);
			Thread columnSortFilter = new ColumnSortFilter(pipe07, pipe08);
			Thread dataSortFilter = new DataSortFilter(pipe08, pipe09);
			Thread fileWriterFilter = new FileWriterFilter(argv[1], pipe09);
			Thread rejectedFilter = new RejectedFilter(pipeR04,pipeR05,pipe10);
			Thread columnChoiceFilter2 = new ColumnChoiceFilter(pipe10, pipe11);
			Thread columnSortFilter2 = new ColumnSortFilter(pipe11, pipe12);
			Thread dataSortFilter2 = new DataSortFilter(pipe12, pipe13);
			Thread fileWriterFilter2 = new FileWriterFilter(argv[2], pipe13);
			
			// Start the threads
			fileReaderFilter.start();
			statusFilter.start();
			stateFilter1.start();
			stateFilter2.start();
			mergeFilter.start();
			columnChoiceFilter.start();
			columnSortFilter.start();
			dataSortFilter.start();
			rejectedFilter.start();
			fileWriterFilter.start();
			columnChoiceFilter2.start();
			columnSortFilter2.start();
			dataSortFilter2.start();
			fileWriterFilter2.start();
			
		}  // if
	} // main
} // SystemInitialize