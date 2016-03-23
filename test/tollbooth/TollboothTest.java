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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import tollbooth.gatecontroller.*;
import tollbooth.TollboothException;

/**
 * Test cases for the Tollbooth, TollGate class.
 * @version Feb 3, 2016
 */
public class TollboothTest
{

	@Test
	public void createNewTollGateWithNoController()
	{
		final SimpleLogger logger = new TollboothLogger();
		assertNotNull(new TollGate(null, logger));
	}
	
	@Test
	public void createNewTollGateWithNoLogger()
	{
		final TestGateController controller = new TestGateController();
		assertNotNull(new TollGate(controller, null));
	}
	
	@Test
	public void createNewTollGateWithAController()
	{
		final SimpleLogger logger = new TollboothLogger();
		assertNotNull(new TollGate(new TestGateController(), logger));
	}
	
	@Test
	public void newGateControllerIsClosed() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertFalse(gate.isOpen());
	}

	@Test
	public void gateControllerIsOpenAfterOpenMessage() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		gate.open();
		assertTrue(gate.isOpen());
	}
	
	@Test
	public void gateOpensAfterOneMalfunction() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(1);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: successful", message.getMessage());
	}
	
	@Test
	public void gateOpensAfterTwoMalfunctions() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(2);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: successful", message.getMessage());
	}
	
	@Test
	public void unrecoverableCloseMalfunctionAfterThreeMalfunctions() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(3);
		controller.isOpen = true;
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertEquals("close: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("close: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("close: unrecoverable malfunction", message.getMessage());
	}
	
	@Test
	public void unrecoverableOpenMalfunctionAfterThreeMalfunctions() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(3);
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: malfunction", message.getMessage());
		message = logger.getNextMessage();
		assertEquals("open: unrecoverable malfunction", message.getMessage());
		
	}
	
	@Test
	public void testLogSizeOnThreeFailedOpens() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(3);
		gate.open();
		assertEquals(3, logger.logSize());
	}
	
	@Test
	public void testLogSizeOnThreeFailedCloses() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(3);
		controller.isOpen = true;
		gate.close();
		assertEquals(3, logger.logSize());
	}
	
	@Test
	public void testLogSizeOnFourFailedCloses() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(4);
		controller.isOpen = true;
		gate.close();
		controller.isOpen = true;
		try{
			gate.close();
		}catch(TollboothException e){
			assertEquals(4, logger.logSize());
		}
	}
	
	@Test
	public void gateAlreadyOpenNoLogMessage() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.isOpen = true;
		gate.open();
		LogMessage message = logger.getNextMessage();
		assertEquals(message, null);
	}
	
	@Test
	public void gateAlreadyClosedNoLogMessage() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.isOpen = false;
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertEquals(message, null);
	}
	
	@Test
	public void gateOpenedTwiceCheck() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.isOpen = false;
		gate.open();
		controller.isOpen = false;
		gate.open();
		assertEquals(2, gate.getNumberOfOpens());
	}
	
	@Test
	public void gateOpenedTwiceWithUnrecoverableFailureInBetweenCheck() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(1);
		controller.isOpen = false;
		gate.open();
		controller.isOpen = false;
		controller.scheduleXFailures(2);
		gate.open();
		controller.isOpen = false;
		controller.scheduleXFailures(4);
		gate.open();
		assertEquals(0, gate.getNumberOfCloses());
		assertEquals(2, gate.getNumberOfOpens());
	}
	
	@Test
	public void gateClosedTwiceWithUnrecoverableFailureInBetweenCheck() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(1);
		controller.isOpen = true;
		gate.close();
		controller.isOpen = true;
		controller.scheduleXFailures(2);
		gate.close();
		controller.isOpen = true;
		controller.scheduleXFailures(4);
		gate.close();
		assertEquals(0, gate.getNumberOfOpens());
		assertEquals(2, gate.getNumberOfCloses());
	}
	
	@Test
	public void gateResetTwiceWithUnrecoverableFailureInBetweenCheck() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(1);
		controller.isOpen = true;
		gate.reset();
		assertEquals(false, gate.isOpen());
		controller.isOpen = true;
		controller.scheduleXFailures(2);
		gate.reset();
		assertEquals(false, gate.isOpen());
		controller.isOpen = true;
		controller.scheduleXFailures(4);
		gate.reset();
		assertEquals(true, gate.unresponsiveMode);
	}
	
	@Test(expected=tollbooth.TollboothException.class)
	public void gateIsOpenUnrecoverableException() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(3);
		gate.open();
		gate.isOpen();
	}
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Test
	public void notRespondingMessageOnFourthOpenMalfunction() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(4);
		gate.open();
		for(int i = 0; i < 3; i++){
			logger.getNextMessage();
		}
		expectedEx.expect(tollbooth.TollboothException.class);
		expectedEx.expectMessage("open: will not respond");
		gate.open();
	}
}
