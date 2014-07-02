package ca.etsmtl.log430.lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is intended to be a filter that will analyse the input stream corresponding  
 * to a record that contains only data from the following
 * fields:numero de projet, statut, taux, état;
 * and classify these data in that order of fields:Statut, Etat, Taux, Numero de projet;
 * and send the record containing the data classified in the aforementioned order of fields
 * to the output stream.
 * 
 */

public class ColumnSortFilter extends Thread
{
	// Declarations

	// Create local pipe to that will connect to upstream filter
	PipedReader inputPipe = new PipedReader();
	
	// Create local pipe to that will connect to downstream filter
	PipedWriter outputPipe = new PipedWriter();
	
	public ColumnSortFilter(PipedWriter InputPipe, PipedWriter OutputPipe)
	{
		try 
		{
			// Connect InputPipe to upstream filter
			this.inputPipe.connect(InputPipe);
			System.out.println("ColumnSortFilter:: connected to upstream filter.");
		}
		catch (Exception Error) 
		{
			System.out.println("ColumnSortFilter:: Error connecting input pipe.");
		} // try/catch
		
		try 
		{
			// Connect outputPipe to downstream filter
			this.outputPipe = OutputPipe;
			System.out.println("ColumnSortFilter:: connected to downstream filter.");
		}
		catch (Exception Error) 
		{
			System.out.println("ColumnSortFilter:: Error connecting output pipe.");
		} // catch
	}
	
	/**
	 * Name:orderDataByField
	 * 
	 * This method receives in parameter a string representing
	 * a record containing values of the following fields:Numero de projet, Statut, taux, Etat.
	 * This method classify these data in that order of fields:Statut, Etat, Taux, Numero de projet.
	 * This method return the string corresponding to a record whose data have been classified
	 * in the aforementioned order of fields.
	 */
	String orderDataByField(String lineOfText)
	{
		String space = " ";
		String intermediateLineOfText;
		String finalLineOfText = "";
		int spaceIndex = 0;
		int expressionBeginningIndex = 0;
		ArrayList<String> tableauValeurChamp = new ArrayList<String>();
		lineOfText = lineOfText + space;
		
		Pattern motif = Pattern.compile(space);
		Matcher correspondance = motif.matcher(lineOfText);
		
		while( correspondance.find() )
		{
				spaceIndex = correspondance.start();
				intermediateLineOfText = lineOfText.substring(expressionBeginningIndex, spaceIndex);
			
				tableauValeurChamp.add(intermediateLineOfText);
				
				expressionBeginningIndex = correspondance.end();			
		}
		
		finalLineOfText += tableauValeurChamp.get(1) + space;
		finalLineOfText += tableauValeurChamp.get(3) + space;
		finalLineOfText += tableauValeurChamp.get(2) + space;
		finalLineOfText += tableauValeurChamp.get(0);
		
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
					System.out.println("ColumnSortFilter:: Input pipe closed.");
					
					try 
					{
						inputPipe.close();
					}
					catch (Exception Error) 
					{
						System.out.println("ColumnSortFilter:: Error closing input pipe .");
					} // try/catch
				}// end if ( IntegerCharacter == -1 )
				else
				{
					if(integerCharacter == '\n') // end of line
					{
						System.out.println("ColumnSortFilter:: Received: "+ lineOfText +
											" on input pipe.");
						finalLine = orderDataByField(lineOfText);
						write = true;
					}// end if ( IntegerCharacter == '\n' )
					else
					{
						lineOfText += new String(characterValue);
					}		
				}
				
				if(write)
				{
					write = false;
					
					try 
					{
						System.out.println("ColumnSortFilter:: Sending " + finalLine + " to output pipe.");
						finalLine += "\n";
						outputPipe.write(finalLine, 0, finalLine.length());
						outputPipe.flush();
					} 
					catch (Exception IOError) 
					{
						System.out.println("ColumnSortFilter:: Write Error.");
					} // try/catch
					
					lineOfText = "";
				}// end if ( Write )
			}// end while (!Done)	
		}
		catch(Exception Error)
		{
			System.out.println("ColumnSortFilter:: Interrupted.");
		}
		
		try 
		{
			System.out.println("ColumnSortFilter:: output pipe closed.");
			outputPipe.close();
		}
		catch (Exception Error) 
		{
			System.out.println("ColumnSortFilter:: Error closing pipe");
		} // try/catch
	}
}
