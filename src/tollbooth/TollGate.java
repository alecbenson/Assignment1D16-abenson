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

import tollbooth.gatecontroller.GateController;

/**
 * The TollGate contains everything about a tollgate in a tollbooth.
 * @version Feb 3, 2016
 */
public class TollGate
{
	private final GateController controller;
	private final SimpleLogger logger;
	private int openCount;
	private int closeCount;
	private final int maxFailures;
	private boolean unresponsiveMode;
	
	/**
	 * Constructor that takes the actual gate controller and the logger.
	 * @param controller the GateController object.
	 * @param logger the SimpleLogger object.
	 */
	public TollGate(GateController controller, SimpleLogger logger) {
		this.controller = controller;
		this.logger = logger;
		openCount = 0;
		closeCount = 0;
		maxFailures = 3;
		unresponsiveMode = false;
	}
	
	/**
	 * Specifies the different types of actions that the gate controller can take
	 */
	public enum Action{
		OPEN, CLOSE, RESET
	}
	
	/**
	 * Open the gate.
	 * @throws TollboothException
	 */
	public void open() throws TollboothException
	{	
		if(controller.isOpen()){
			return;
		}
		this.actionDispatch(controller, Action.OPEN);
	}
	
	/**
	 * Close the gate
	 * @throws TollboothException
	 */
	public void close() throws TollboothException
	{
		if(!controller.isOpen()){
			return;
		}
		this.actionDispatch(controller, Action.CLOSE);
	}
	
	/**
	 * Reset the gate to the state it was in when created with the exception of the
	 * statistics.
	 * @throws TollboothException
	 */
	public void reset() throws TollboothException
	{
		this.actionDispatch(controller, Action.RESET);
	}
	
	/**
	 * runs a method from the controller API based on the action passed to it
	 * @param act an enum specifying which action to take
	 * @throws TollboothException
	 */
	public void takeControllerAction(Action act) throws TollboothException{
			switch(act){
				case OPEN:
					controller.open();
					//Increment if no exception thrown
					openCount++;
					break;
				case CLOSE:
					controller.close();
					//Increment if no exception thrown
					closeCount++;
					break;
				case RESET:
					controller.reset();
					//Set unresponsive mode back to false
					unresponsiveMode = false;
					break;
			}
	}
	
	/**
	 * Attempts to run the specified action up to maxFailure times.
	 * This method also produces log messages detailing the status 
	 * of the actions and whether or not they executed successfully.
	 * @param controller the GateController object
	 * @param act the action to perform on the controller
	 * @throws TollboothException
	 */
	public void actionDispatch(GateController controller, Action act) throws TollboothException{
		String logMessage;
		String actionName = this.getActionName(act);
		
		//Attempt to close until success or until maxFailures attempts
		for(int attempt = 1; attempt <= maxFailures; attempt++){
			try{
				//if in unresponsive mode, throw exception unless action is reset
				if(unresponsiveMode && act != Action.RESET){
					logMessage = String.format("%s: will not respond", actionName);
					throw new TollboothException(logMessage);
				}
				//make a call to the gate controller API
				this.takeControllerAction(act);
				//Log successful action taken
				logMessage = String.format("%s: successful", actionName);
				logger.accept(new LogMessage(logMessage));
				return;
			} catch(TollboothException e) {
				//Throw the exception if in unresponsive mode
				if(unresponsiveMode){
					logger.accept(new LogMessage(e.getMessage(), e));
					throw e;
				}
				//If this is the third try, set unrecoverable mode
				if(attempt == maxFailures){
					logMessage = String.format("%s: unrecoverable malfunction", actionName);
					logger.accept(new LogMessage(logMessage));
					unresponsiveMode = true;
				//Otherwise just log another malfunction
				} else{
					logMessage = String.format("%s: malfunction", actionName);
					logger.accept(new LogMessage(logMessage));
				}					
			}
		}
	}
	
	/**
	 * Given an action enum type, returns the name of the action. 
	 * Used for generating detailed logging messages
	 * @param act the action to perform
	 * @return the name of the action
	 */
	public String getActionName(Action act){
		String actionName = "Unknown";
		switch(act){
			case OPEN:
				actionName = "open";
				break;
			case CLOSE:
				actionName = "close";
				break;
			case RESET:
				actionName = "reset";
				break;
		}
		return actionName;
	}
	
	/**
	 * @return true if the gate is open
	 * @throws TollboothException 
	 */
	public boolean isOpen() throws TollboothException
	{
		if(unresponsiveMode){
			throw new TollboothException("Gate is in unresponsive mode");
		} else{
			return controller.isOpen();
		}
	}
	
	/**
	 * @return the number of times that the gate has been opened (that is, the
	 *  open method has successfully been executed) since the object was created.
	 */
	public int getNumberOfOpens()
	{
		return openCount;
	}
	
	/**
	 * @return the number of times that the gate has been closed (that is, the
	 *  close method has successfully been executed) since the object was created.
	 */
	public int getNumberOfCloses()
	{
		return closeCount;
	}
	
	/**
	 * Used for testing purposes, gets whether or not the gate is in responsive mode
	 * @return true if in unresponsive mode, false otherwise
	 */
	public boolean getUnresponsiveMode(){
		return unresponsiveMode;
	}
}
