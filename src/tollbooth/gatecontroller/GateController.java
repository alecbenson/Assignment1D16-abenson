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

package tollbooth.gatecontroller;

import tollbooth.TollboothException;

/**
 * This interface defines the behavior expected of any tollgate controller.
 * @version Feb 3, 2016
 */
public interface GateController
{
	/**
	 * Open the gate
	 * @throws TollboothException
	 */
	void open() throws TollboothException;
	
	/**
	 * Close the gate
	 * @throws TollboothException
	 */
	void close() throws TollboothException;
	
	/**
	 * Reset the gate to the original state.
	 * @throws TollboothException
	 */
	void reset() throws TollboothException;
	
	
	/**
	 * @return true if the gate is in the open state.
	 * @throws TollboothException
	 */
	boolean isOpen() throws TollboothException;
}
