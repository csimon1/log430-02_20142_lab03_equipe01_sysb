package ca.etsmtl.log430.lab3;

import java.io.PipedReader;
import java.io.PipedWriter;

/**
 * This class is intended to be a filter that will key on a particular state
 * provided at instantiation.  Note that the stream has to be buffered so that
 * it can be checked to see if the specified severity appears on the stream.
 * If this string appears in the input stream, teh whole line is passed to the
 * output stream.
 * 
 * <pre>
 * Pseudo Code:
 *
 * connect to input pipe
 * connect to output pipe
 *
 * while not end of line
 *
 *		read input pipe
 *
 *		if specified severity appears on line of text
 *			write line of text to output pipe
 *			flush pipe
 *		end if
 *
 * end while
 * close pipes
 * </pre>
 *
 * @author A.J. Lattanze
 * @author Samuel - added missing comments.
 * @version 1.1   - added new class member : rejectedOutputPipe
 * 				  - renamed old member  : outputPipe , to : acceptedOutputPipe
 */

public class StateFilter extends Thread 
{
	// Declarations

	boolean done;
	String severity;
	PipedReader inputPipe = new PipedReader();
	PipedWriter acceptedOutputPipe = new PipedWriter();
	PipedWriter rejectedOutputPipe = new PipedWriter();
	
	/**
	 * Class constructor.
	 * 
	 * @param severity the project state
	 * @param inputPipe the pipe from which the flow came
	 * @param acceptedOutputPipe the pipe where the flow will go
	 * @param rejectedOutputPipe the pipe that contains other projects states, that are not treated
	 * in this class.
	 */
	public StateFilter(String severity, PipedWriter inputPipe,
			PipedWriter acceptedOutputPipe, PipedWriter rejectedOutputPipe) 
	{
		this.severity = severity;

		try 
		{
			// Connect inputPipe
			this.inputPipe.connect(inputPipe);
			System.out.println("StateFilter " + severity
					+ ":: connected to upstream filter.");

			// Connect output pipes
			this.acceptedOutputPipe = acceptedOutputPipe;
			System.out.println("StateFilter_" + severity + ":: connected to downstream filter.");
			
			this.rejectedOutputPipe = rejectedOutputPipe;
			System.out.println("StateFilter_Non-matchingProject:: connected to downstream filter.");
		}
		catch (Exception Error) 
		{
			System.out.println("StateFilter_" + severity + ":: Error connecting to other filters.");
			System.out.println("StateFilter_Non-matchingProject:: Error connecting to other filters.");
		} // try/catch
	} // Constructor

	/**
	 *  This is the method that is called when the thread is started
	 */
	public void run() 
	{
		// Declarations

		char[] characterValue = new char[1];
		// char array is required to turn char into a string
		String lineOfText = "";
		// string is required to look for the keyword
		int integerCharacter; // the integer value read from the pipe

		try 
		{
			done = false;

			while (!done) 
			{
				integerCharacter = this.inputPipe.read();
				characterValue[0] = (char) integerCharacter;

				if (integerCharacter == -1) 
				{ 
					// pipe is closed
					done = true;
				} 
				else 
				{
					if (integerCharacter == '\n') 
					{
						// end of line
						System.out.println("StateFilter " + severity
								+ ":: received: " + lineOfText + ".");
						
						// If we found the project state in the current line we are reading...
						if (lineOfText.indexOf(severity) != -1) 
						{
							System.out.println("StateFilter " + severity + ":: sending: "
									+ lineOfText + " to output pipe.");
							
							// Create a string that contains all the characters from the character array
							// then add this string to the lineOfText
							lineOfText += new String(characterValue);
							
							this.acceptedOutputPipe.write(lineOfText, 0, lineOfText.length());
							this.acceptedOutputPipe.flush();
						} // if
						else
						{
							// send the other project state to the rejected pipe
							// display the rejected severity (project state)
							System.out.println("StateFilter " + severity + ":: sending: " + lineOfText + " to rejected output pipe.");
							
							lineOfText += new String(characterValue);
							
							this.rejectedOutputPipe.write(lineOfText, 0 , lineOfText.length());
							this.rejectedOutputPipe.flush();
						}

						lineOfText = "";
					} 
					else 
					{
						lineOfText += new String(characterValue);
					} // if //
				} // if
			} // while
		} 
		catch (Exception error) 
		{
			System.out.println("StateFilter::" + severity + " Interrupted.");
			System.out.println("StateFilter_Non-matchingProject:: Interrupted.");
		} // try/catch

		// closing the pipes
		try 
		{
			this.inputPipe.close();
			System.out.println("StateFilter " + severity + ":: input pipe closed.");
			
			this.acceptedOutputPipe.close();
			System.out.println("StateFilter " + severity + ":: output pipe closed.");
			
			this.rejectedOutputPipe.close();
			System.out.println("StateFilter_Non-matchingProject:: output pipe closed.");
		} 
		catch (Exception error) 
		{
			System.out.println("StateFilter " + severity + ":: Error closing pipes.");
		} // try/catch
	} // run
} // class