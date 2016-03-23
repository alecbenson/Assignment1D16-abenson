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
		this.openCount = 0;
		this.closeCount = 0;
		this.maxFailures = 3;
		this.unresponsiveMode = false;
	}
	
	/**
	 * Open the gate.
	 * @throws TollboothException
	 */
	public void open() throws TollboothException
	{				
			String logMessage;
			//If the controller is already open, simply return, we're done
			if(controller.isOpen()){
				return;
			}
			
			//Otherwise, attempt to open until success or until maxFailures attempts
			for(int attempt = 1; attempt <= maxFailures; attempt++){
				//If the gate is not responding to log messages
				if(this.unresponsiveMode == true){
					logMessage = "open: will not respond";;
					this.logger.accept(new LogMessage(logMessage));
					throw new TollboothException(logMessage);
				}
				
				try{
					controller.open();
					this.openCount++;
					logMessage = "open: successful";
					this.logger.accept(new LogMessage(logMessage));
				} catch(TollboothException e) {
					if(attempt == maxFailures){
						logMessage = "open: unrecoverable malfunction";
						logger.accept(new LogMessage(logMessage));
						this.unresponsiveMode = true;
					} else{
						logMessage = "open: malfunction";
						logger.accept(new LogMessage(logMessage));
					}					
				}
			}
	}
	
	/**
	 * Close the gate
	 * @throws TollboothException
	 */
	public void close() throws TollboothException
	{
		String logMessage;
		//If the controller is already closed, simply return, we're done
		if(!controller.isOpen()){
			return;
		}
		
		//Otherwise, attempt to close until success or until maxFailures attempts
		for(int attempt = 1; attempt <= maxFailures; attempt++){
			//If the gate is not responding to log messages
			if(this.unresponsiveMode == true){
				logMessage = "close: will not respond";;
				this.logger.accept(new LogMessage(logMessage));
				throw new TollboothException(logMessage);
			}
			
			try{
				controller.close();
				this.closeCount++;
				logMessage = "close: successful";
				this.logger.accept(new LogMessage(logMessage));
			} catch(TollboothException e) {
				if(attempt == maxFailures){
					logMessage = "close: unrecoverable malfunction";
					logger.accept(new LogMessage(logMessage));
					this.unresponsiveMode = true;
				} else{
					logMessage = "close: malfunction";
					logger.accept(new LogMessage(logMessage));
				}					
			}
		}
	}
	
	/**
	 * Reset the gate to the state it was in when created with the exception of the
	 * statistics.
	 * @throws TollboothException
	 */
	public void reset() throws TollboothException
	{
		String logMessage;		
		for(int attempt = 1; attempt <= maxFailures; attempt++){
			//If the gate is not responding to log messages
			if(this.unresponsiveMode == true){
				logMessage = "open: will not respond";;
				this.logger.accept(new LogMessage(logMessage));
				throw new TollboothException(logMessage);
			}
			
			try{
				if(controller.isOpen()){
					controller.close();
				};
				logMessage = "reset: successful";
				this.logger.accept(new LogMessage(logMessage));
			} catch(TollboothException e) {
				if(attempt == maxFailures){
					logMessage = "reset: unrecoverable malfunction";
					logger.accept(new LogMessage(logMessage));
					this.unresponsiveMode = true;
				} else{
					logMessage = "reset: malfunction";
					logger.accept(new LogMessage(logMessage));
				}					
			}
		}
	}
	
	/**
	 * @return true if the gate is open
	 * @throws TollboothException 
	 */
	public boolean isOpen() throws TollboothException
	{
			return controller.isOpen();
	}
	
	/**
	 * @return the number of times that the gate has been opened (that is, the
	 *  open method has successfully been executed) since the object was created.
	 */
	public int getNumberOfOpens()
	{
		// To be completed
		return openCount;
	}
	
	/**
	 * @return the number of times that the gate has been closed (that is, the
	 *  close method has successfully been executed) since the object was created.
	 */
	public int getNumberOfCloses()
	{
		// To be completed
		return this.closeCount;
	}
}
