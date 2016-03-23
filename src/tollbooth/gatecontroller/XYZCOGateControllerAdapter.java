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
import wpi.hw.xyzco.GateControllerException;

/**
 * Description
 * @version Feb 10, 2016
 */
public class XYZCOGateControllerAdapter implements GateController
{
	private final wpi.hw.xyzco.GateController controller;
	
	/**
	 * Constructor for the XYZ Co. gate controller adapter.
	 * @param controller an XYZ Co. gate controller.
	 */
	public XYZCOGateControllerAdapter(wpi.hw.xyzco.GateController controller)
	{
		this.controller = controller;
	}
	
	/*
	 * @see tollbooth.gatecontroller.GateController#open()
	 */
	@Override
	public void open() throws TollboothException
	{
		try {
			controller.open();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on open", e);
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#close()
	 */
	@Override
	public void close() throws TollboothException
	{
		try {
			controller.close();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on close", e);
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#reset()
	 */
	@Override
	public void reset() throws TollboothException
	{
		try {
			controller.reset();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on reset", e);
		}
	}

	/*
	 * @see tollbooth.gatecontroller.GateController#isOpen()
	 */
	@Override
	public boolean isOpen() throws TollboothException
	{
		try {
			return controller.isOpen();
		} catch (GateControllerException e) {
			throw new TollboothException("Hardware threw exception on isOpen", e);
		}
	}

}
