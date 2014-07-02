package ca.etsmtl.log430.lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.StringTokenizer;

/**
 * This class is intended to be a filter that will key on a particular state
 * provided at instantiation. Note that the stream has to be buffered so that it
 * can be checked to see if the specified comparatorSymbol + valueToCompare
 * appears on the stream. If this string appears in the input stream, teh whole
 * line is passed to the output stream.
 * 
 * <pre>
 * Pseudo Code:
 * 
 * connect to input pipe
 * connect to output pipe
 * 
 * while not end of line
 * 
 * 	read input pipe
 * 
 * 	if specified comparatorSymbol +  valueToCompare appears on line of text
 * 		write line of text to output pipe
 * 		flush pipe
 * 	end if
 * 
 * end while
 * close pipes
 * </pre>
 * 
 * @author A.J. Lattanze
 * @author Samuel - added missing comments.
 * @version 1.1 - added new class member : rejectedOutputPipe - renamed old
 *          member : outputPipe , to : acceptedOutputPipe
 */

public class RateProgressionFilter extends Thread {
    // Declarations

    boolean done;

    PipedReader inputPipe = new PipedReader();
    PipedWriter acceptedOutputPipe = new PipedWriter();
    PipedWriter rejectedOutputPipe = new PipedWriter();

    private String comparatorSymbol;
    private String valueToCompare;

    /**
     * Class constructor.
     * 
     * @param comparatorSymbol
     *            + valueToCompare the project state
     * @param inputPipe
     *            the pipe from which the flow came
     * @param acceptedOutputPipe
     *            the pipe where the flow will go
     * @param rejectedOutputPipe
     *            the pipe that contains other projects states, that are not
     *            treated in this class.
     */
    public RateProgressionFilter(String comparatorSymbol, String valueToCompare, PipedWriter inputPipe, PipedWriter acceptedOutputPipe,
            PipedWriter rejectedOutputPipe) {
        this.comparatorSymbol = comparatorSymbol;
        this.valueToCompare = valueToCompare;

        try {
            // Connect inputPipe
            this.inputPipe.connect(inputPipe);
            System.out.println("RateProgressionFilter " + comparatorSymbol + valueToCompare + ":: connected to upstream filter.");

            // Connect output pipes
            this.acceptedOutputPipe = acceptedOutputPipe;
            System.out.println("RateProgressionFilter_" + comparatorSymbol + valueToCompare + ":: connected to downstream filter.");

            this.rejectedOutputPipe = rejectedOutputPipe;
            System.out.println("RateProgressionFilter_Non-matchingProject:: connected to downstream filter.");
        } catch (Exception Error) {
            System.out.println("RateProgressionFilter_" + comparatorSymbol + valueToCompare + ":: Error connecting to other filters.");
            System.out.println("RateProgressionFilter_Non-matchingProject:: Error connecting to other filters.");
        } // try/catch
    } // Constructor

    /**
     * This is the method that is called when the thread is started
     */
    @Override
    public void run() {
        // Declarations

        char[] characterValue = new char[1];
        // char array is required to turn char into a string
        String lineOfText = "";
        // string is required to look for the keyword
        int integerCharacter; // the integer value read from the pipe

        try {
            done = false;

            while (!done) {
                integerCharacter = this.inputPipe.read();
                characterValue[0] = (char) integerCharacter;

                if (integerCharacter == -1) {
                    // pipe is closed
                    done = true;
                } else {
                    if (integerCharacter == '\n') {

                        StringTokenizer tokenizer = new StringTokenizer(lineOfText, " ");

                        Integer progressionRate = null;

                        while (tokenizer.hasMoreTokens()) {

                            String token = tokenizer.nextToken();

                            // check with mask(regex) if it's progression column

                        }

                        if (progressionRate == null) {
                            this.rejectedOutputPipe.write(lineOfText, 0, lineOfText.length());
                            this.rejectedOutputPipe.flush();
                        }

                        boolean respectComparasion = false;

                        // szitch for compare

                        lineOfText = "";
                    } else {
                        lineOfText += new String(characterValue);
                    } // if //
                } // if
            } // while
        } catch (Exception error) {
            System.out.println("RateProgressionFilter::" + comparatorSymbol + valueToCompare + " Interrupted.");
            System.out.println("RateProgressionFilter_Non-matchingProject:: Interrupted.");
        } // try/catch

        // closing the pipes
        try {
            this.inputPipe.close();
            System.out.println("RateProgressionFilter " + comparatorSymbol + valueToCompare + ":: input pipe closed.");

            this.acceptedOutputPipe.close();
            System.out.println("RateProgressionFilter " + comparatorSymbol + valueToCompare + ":: output pipe closed.");

            this.rejectedOutputPipe.close();
            System.out.println("RateProgressionFilter_Non-matchingProject:: output pipe closed.");
        } catch (Exception error) {
            System.out.println("RateProgressionFilter " + comparatorSymbol + valueToCompare + ":: Error closing pipes.");
        } // try/catch
    } // run
} // class