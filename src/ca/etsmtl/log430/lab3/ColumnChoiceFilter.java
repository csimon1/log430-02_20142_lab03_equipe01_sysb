package ca.etsmtl.log430.lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is intended to be a filter that will analyse the input stream,retains only  
 * data from the following fields:numero de projet, statut, taux, état;
 * removes data from fields that are different from the fields previously listed
 * and send the data retained to his output stream.<br><br>
 * 
 */
public class ColumnChoiceFilter extends Thread
{
	// Declarations

	// Create local pipe to that will connect to upstream filter
	PipedReader inputPipe = new PipedReader();
	
	// Create local pipe to that will connect to downstream filter
	PipedWriter outputPipe = new PipedWriter();
	
	public ColumnChoiceFilter(PipedWriter InputPipe, PipedWriter OutputPipe)
	{
		try 
		{
			// Connect InputPipe to upstream filter
			this.inputPipe.connect(InputPipe);
			System.out.println("ColumnChoiceFilter:: connected to upstream filter.");
		}
		catch (Exception Error) 
		{
			System.out.println("ColumnChoiceFilter:: Error connecting input pipes.");
		} // try/catch
		
		try 
		{
			// Connect outputPipe to downstream filter
			this.outputPipe = OutputPipe;
			System.out.println("ColumnChoiceFilter:: connected to downstream filter.");
		}
		catch (Exception Error) 
		{
			System.out.println("ColumnChoiceFilter:: Error connecting output pipe.");
		} // catch
	}

	/**
	 * Name:retainFieldData
	 * 
	 * This method receives in parameter a string representing
	 * a record.It retains the data of the following fields:Statut,
	 * Etat,Taux,Numero de projet.Its also removes the data
	 * of fields that are not in the aforementioned list of fields and
	 * return  a string that only contains the data from the fields
	 * that are present in the aforementioned list .
	 */
	String retainFieldData(String lineOfText)
	{
		String space = " ";
		String intermediateLineOfText;
		String finalLineOfText = "";
		int spaceIndex = 0;
		int expressionBeginningIndex = 0;
		int counter = 0;
		
		
		Pattern motif = Pattern.compile(space);
		Matcher correspondance = motif.matcher(lineOfText);
		
		while( correspondance.find() && (counter < 8)  )
		{
				counter += 1;
				spaceIndex = correspondance.start();
				intermediateLineOfText = lineOfText.substring(expressionBeginningIndex, spaceIndex);
			
				if( (counter == 1) || (counter == 2) || (counter == 5) || (counter == 6) )
				{
					finalLineOfText += intermediateLineOfText + space;
				}
				
				expressionBeginningIndex = correspondance.end();			
		}
		
		finalLineOfText = finalLineOfText.substring( 0, finalLineOfText.length() - 1);

		return finalLineOfText; 
	}
	
	public void run()
	{
		boolean done;// Flag for reading from a pipe
		
		try
		{
			// Declarations

			// Need to be an array for easy conversion to string
			char[] characterValue = new char[1];
			
			// Indicate when you are done reading on pipe
			done = false;
			
			// integer value read from the pipe
			int integerCharacter;
			
			// lines of text from input pipe
			String lineOfText = "";
			
			// Indicate whether lines of text are ready to be output
			// to downstream filter
			boolean write = false;
			
			//The line that contains only the data of the
			//following fields:statut,etat,taux,numero de projet.
			//This line will be sent to the output pipe.
			String finalLine = null;
			
			// Loop for reading data
			
			while(!done)
			{
				integerCharacter = inputPipe.read();
				characterValue[0] = (char)integerCharacter;
				
				if(integerCharacter == -1)// pipe is closed
				{
					done = true;
					System.out.println("ColumnChoiceFilter:: Input pipe closed.");
					
					try 
					{
						inputPipe.close();
					}
					catch (Exception Error) 
					{
						System.out.println("ColumnChoiceFilter:: Error closing input pipe .");
					} // try/catch
				}// end if ( IntegerCharacter == -1 )
				else
				{
					if(integerCharacter == '\n') // end of line
					{
						System.out.println("ColumnChoiceFilter:: Received: "+ lineOfText +
											" on input pipe.");
				
						finalLine = retainFieldData(lineOfText);
						write = true;
					}// end if ( IntegerCharacter == '\n' )
					else
					{
						lineOfText += new String(characterValue);
					}		
				}
				
				if (write) 
				{
					write = false;
					
					try 
					{
						System.out.println("ColumnChoiceFilter:: Sending " + finalLine + " to output pipe.");
						finalLine += "\n";
						outputPipe.write(finalLine, 0, finalLine.length());
						outputPipe.flush();
					} 
					catch (Exception IOError) 
					{
						System.out.println("ColumnChoiceFilter:: Write Error.");
					} // try/catch
					
					lineOfText = "";
				}
			}// end while (!Done)
		}
		catch (Exception Error) 
		{
			System.out.println("ColumnChoiceFilter:: Interrupted.");
		} // catch
		
		try 
		{
			System.out.println("ColumnChoiceFilter:: output pipe closed.");
			outputPipe.close();
		}
		catch (Exception Error) 
		{
			System.out.println("ColumnChoiceFilter:: Error closing pipe");
		} // try/catch
		
	}
}