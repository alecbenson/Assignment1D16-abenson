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
		controller.setIsOpen(true);
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
		LogMessage message = logger.getNextMessage();
		assertEquals(null, message);
		controller.scheduleXFailures(3);
		gate.open();
		message = logger.getNextMessage();
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
		assertEquals(0, logger.logSize());
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
		assertEquals(0, logger.logSize());
		controller.scheduleXFailures(3);
		controller.setIsOpen(true);
		gate.close();
		assertEquals(3, logger.logSize());
	}
	
	@Test
	public void testLogSizeOnFourFailedCloses() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final TollboothLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, logger.logSize());
		controller.scheduleXFailures(4);
		controller.setIsOpen(true);
		gate.close();
		controller.setIsOpen(true);
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
		controller.setIsOpen(true);
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
		controller.setIsOpen(false);
		gate.close();
		LogMessage message = logger.getNextMessage();
		assertEquals(null, message);
	}
	
	@Test
	public void gateOpenedTwiceCheck() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfOpens());
		controller.setIsOpen(false);
		gate.open();
		controller.setIsOpen(false);
		gate.open();
		assertEquals(2, gate.getNumberOfOpens());
	}
	
	@Test
	public void gateOpenedTwiceWithUnrecoverableFailureInBetweenCheck() throws TollboothException
	{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		assertEquals(0, gate.getNumberOfCloses());
		assertEquals(0, gate.getNumberOfOpens());
		controller.scheduleXFailures(1);
		controller.setIsOpen(false);
		gate.open();
		controller.setIsOpen(false);
		controller.scheduleXFailures(2);
		gate.open();
		controller.setIsOpen(false);
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
		assertEquals(0, gate.getNumberOfOpens());
		assertEquals(0, gate.getNumberOfCloses());
		controller.scheduleXFailures(1);
		controller.setIsOpen(true);
		gate.close();
		controller.setIsOpen(true);
		controller.scheduleXFailures(2);
		gate.close();
		controller.setIsOpen(true);
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
		assertEquals(false, gate.getUnresponsiveMode());
		controller.scheduleXFailures(1);
		controller.setIsOpen(true);
		gate.reset();
		assertEquals(false, gate.isOpen());
		controller.setIsOpen(true);
		controller.scheduleXFailures(2);
		gate.reset();
		assertEquals(false, gate.isOpen());
		controller.setIsOpen(true);
		controller.scheduleXFailures(4);
		gate.reset();
		assertEquals(true, gate.getUnresponsiveMode());
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
	private ExpectedException expectedEx = ExpectedException.none();
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
	
	@Test
	public void loggerHasCause() throws TollboothException{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(3);
		gate.open();
		for(int i = 0; i < 3; i++){
			logger.getNextMessage();
		}
		try{
			gate.open();
		}catch(TollboothException e){
			LogMessage msg = logger.getNextMessage();
			assertEquals(true, msg.hasCause());
			assertEquals("open: will not respond",msg.getCause().getMessage());
		}
	}
	
	@Test
	public void loggerHasNoCause() throws TollboothException{
		final TestGateController controller = new TestGateController();
		final SimpleLogger logger = new TollboothLogger();
		final TollGate gate = new TollGate(controller, logger);
		controller.scheduleXFailures(1);
		gate.open();
		LogMessage msg = logger.getNextMessage();
		assertEquals(false, msg.hasCause());
	}
}
