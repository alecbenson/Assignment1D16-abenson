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
 * Description
 * @version Feb 3, 2016
 */
public class TollboothException extends Exception
{
	/**
	 * Exception with just a message
	 * @param msg
	 */
	public TollboothException(String msg)
	{
		super(msg);
	}
	
	/**
	 * Exception with a message and a causing exception.
	 * @param msg
	 * @param cause
	 */
	public TollboothException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
