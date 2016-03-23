/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package tollbooth;

/**
 * The TollboothLogger implements a FIFO queue that only allows a message to be added
 * and removed from the queue. The implementation of the logger may vary depending upon
 * the system requirements.
 * @version Feb 18, 2016
 */
public interface SimpleLogger
{
	/**
	 * Accept a message and add it to the message queue
	 * @param message the LogMessage object to add to the queue
	 */
	void accept(LogMessage message);
	
	/**
	 * Remove and return the next message from the message queue.
	 * @return the LogMessage object or null if there is no object in the queue
	 */
	LogMessage getNextMessage();
}
