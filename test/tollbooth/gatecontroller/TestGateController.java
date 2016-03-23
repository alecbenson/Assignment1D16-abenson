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
 * Description
 * @version Feb 15, 2016
 */
public class TestGateController implements GateController
{
	public boolean isOpen;
	boolean failureMode;
	int scheduledFailures;
	/**
	 * Constructor for the test gate controller.
	 */
	public TestGateController()
	{
		this.isOpen = false;
		this.scheduledFailures = 0;
	}
	
	/*
	 * @see tollbooth.gatecontroller.GateController#open()
	 */
	@Override
	public void open() throws TollboothException
	{
		if(this.scheduledFailures > 0){
			this.scheduledFailures--;
			throw new TollboothException("Failure to open");
		}
		isOpen = true;
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#close()
	 */
	@Override
	public void close() throws TollboothException
	{
		if(this.scheduledFailures > 0){
			this.scheduledFailures--;
			throw new TollboothException("Failure to close");
		}
		isOpen = false;
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#reset()
	 */
	@Override
	public void reset() throws TollboothException
	{
		if(this.scheduledFailures > 0){
			this.scheduledFailures--;
			throw new TollboothException("Failure to reset");
		}
		isOpen = false;
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#isOpen()
	 */
	@Override
	public boolean isOpen() throws TollboothException
	{
		return isOpen;
	}
	
	/*
	 * Schedules the next X actions to cause TollboothExceptions
	 * @param count the number of failures to schedule
	 */
	public void scheduleXFailures(int count){
		this.scheduledFailures = count;
	}
	
}
