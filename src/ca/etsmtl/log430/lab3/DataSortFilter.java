package ca.etsmtl.log430.lab3;

import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.ArrayList;

/**
 * This class is intended to be a filter that will analyse the input stream corresponding  
 * to a record that contains only data classified in this following 
 * order of fields:Statut, Etat, Taux, Numero de projet ;
 * and classify these data in alphabetical order of the field named "Etat";
 * and send the record containing the data classified in the alphabetical order of field
 * named "Etat" to the output stream.
 * 
 */
public class DataSortFilter extends Thread
{
	// Declarations

	// Create local pipe to that will connect to upstream filter
	PipedReader inputPipe = new PipedReader();
		
	// Create local pipe to that will connect to downstream filter
	PipedWriter outputPipe = new PipedWriter();
	
	public DataSortFilter(PipedWriter InputPipe, PipedWriter OutputPipe)
	{
		try 
		{
			// Connect InputPipe to upstream filter
			this.inputPipe.connect(InputPipe);
			System.out.println("DataSortFilter:: connected to upstream filter.");
		}
		catch (Exception Error) 
		{
			System.out.println("DataSortFilter:: Error connecting input pipe.");
		} // try/catch
		
		try 
		{
			// Connect outputPipe to downstream filter
			this.outputPipe = OutputPipe;
			System.out.println("DataSortFilter:: connected to downstream filter.");
		}
		catch (Exception Error) 
		{
			System.out.println("DataSortFilter:: Error connecting output pipe.");
		} // catch
	}
	
	/**
	 * Name:orderDataByAlphabeticalOrderOfField
	 * 
	 * This method receives in parameter an arrayList containing the records that are made up of
	 * the values of the following fields:Numero de projet, Statut, taux, Etat.These values are
	 * classified in this order:Statut, Etat, Taux, Numero de projet.
	 * This method classify these data in the alphabetical order of the field named "Etat".
	 * This method return an arrayList of string containing the records.The order in which the
	 * string that represent the records have been added to this arrayList  represent the alphabetical
	 * order by field named "Etat"
	 */
	static ArrayList<String> orderDataByAlphabeticalOrderOfField(ArrayList<String> tableauEnregistrement)
	{
		ArrayList<String> tableau = new ArrayList<String>();
		
		for(String ligne: tableauEnregistrement)
		{
			if( ligne.contains("DIF") )
			{
				tableau.add(ligne);
			}
		}
		
		for(String ligne: tableauEnregistrement)
		{
			if( ligne.contains("PRO") )
			{
				tableau.add(ligne);
			}
		}
		
		for(String ligne: tableauEnregistrement)
		{
			if( ligne.contains("RIS") )
			{
				tableau.add(ligne);
			}
		}
		
		return tableau;
	}
	
	public void run()
	{
		boolean done;// Flag for reading from a pipe
		ArrayList<String> tableauEnregistrement = new ArrayList<String>();
		
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
			
			// Loop for reading data
			
			while(!done)
			{
				integerCharacter = inputPipe.read();
				characterValue[0] = (char)integerCharacter;
				
				if(integerCharacter == -1)// pipe is closed
				{
					done = true;
					System.out.println("DataSortFilter:: Input pipe closed.");
					
					try 
					{
						inputPipe.close();
					}
					catch (Exception Error) 
					{
						System.out.println("DataSortFilter:: Error closing input pipe .");
					} // try/catch
				}// end if ( IntegerCharacter == -1 )
				else
				{
					if(integerCharacter == '\n') // end of line
					{
						System.out.println("DataSortFilter:: Received: "+ lineOfText +
											" on input pipe.");
					tableauEnregistrement.add(lineOfText);
					lineOfText = "";
					}// end if ( IntegerCharacter == '\n' )
					else
					{
						lineOfText += new String(characterValue);
					}		
				}
			}// end while (!Done)
		}
		
		catch(Exception Error)
		{
			System.out.println("DataSortFilter:: Interrupted.");
		}
		
		ArrayList<String> tableauEnregistrementClasse = orderDataByAlphabeticalOrderOfField(tableauEnregistrement);
		try 
		{
			for(String enregistrement : tableauEnregistrementClasse)
			{
				System.out.println("DataSortFilter:: Sending " + enregistrement + " to output pipe.");
				enregistrement += "\n";
				outputPipe.write(enregistrement, 0, enregistrement.length());
				outputPipe.flush();
			}
		} 
		catch (Exception IOError) 
		{
			System.out.println("DataSortFilter:: Write Error.");
		} // try/catch
		
		try 
		{
			System.out.println("DataSortFilter:: output pipe closed.");
			outputPipe.close();
		}
		catch (Exception Error) 
		{
			System.out.println("DataSortFilter:: Error closing pipe");
		} // try/catch
	}
}
