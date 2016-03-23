/*******************************************************************************
 * This file was developed by Alec Benson
 * for CS4233: Object-Oriented Analysis & Design Project 1.
 *******************************************************************************/

package tollbooth;

import java.util.LinkedList;
import java.util.Queue;

/**
 * The TollboothLogger is an implementation of the SimpleLogger interface. 
 * It provides a FIFO queue for storing messages that are produced by the TollGate
 **/
public class TollboothLogger implements SimpleLogger {
	
	private Queue<LogMessage> logMessageQueue;
	
	/**
	 * The basic constructor for a new TollboothLogger.
	 * Creates an empty queue for storing LogMessage objects
	 */
	public TollboothLogger(){
		logMessageQueue = new LinkedList<LogMessage>();
	}

	@Override
	public void accept(LogMessage message) {
		logMessageQueue.add(message);
	}

	@Override
	public LogMessage getNextMessage() {
		if(logMessageQueue.isEmpty()){
			return null;
		} else{
			return logMessageQueue.poll();
		}
	}
	
	/**
	 * Returns the size of the log message queue
	 * @return the number of LogMessage objects in the logger queue
	 */
	public int logSize(){
		return logMessageQueue.size();
	}
}
