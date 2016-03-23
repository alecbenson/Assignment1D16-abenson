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

import static org.junit.Assert.*;
import org.junit.Test;
import tollbooth.gatecontroller.*;

/**
 * Test cases for the Tollbooth, TollGate class.
 * @version Feb 3, 2016
 */
public class TollboothTest
{

	@Test
	public void createNewTollGateWithNoController()
	{
		assertNotNull(new TollGate(null, null));
	}
	
	@Test
	public void createNewTollGateWithAController()
	{
		assertNotNull(new TollGate(new TestGateController(), null));
	}
	
	@Test
	public void newGateControllerIsClosed() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final TollGate gate = new TollGate(controller, null);
		assertFalse(gate.isOpen());
	}

	@Test
	public void gateControllerIsOpenAfterOpenMessage() throws TollboothException
	{
		final GateController controller = new TestGateController();
		final TollGate gate = new TollGate(controller, null);
		gate.open();
		assertTrue(gate.isOpen());
	}
	
	@Test
	public void gateOpensAfterOneMalfunction() throws TollboothException
	{
		/**
		 * NOTE: you will need to have a different test GateController or modify the
		 * current one to be programmed to fail opening one time before opening correctly.
		 * You will also need to create your own instance of a SimpleLogger and pass it into the constructor. 
		 */
		final GateController controller = new TestGateController();
		final SimpleLogger logger = null;		// put in an instance of your SimpleLogger implementation
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: successful", message.getMessage());
	}
	
}
