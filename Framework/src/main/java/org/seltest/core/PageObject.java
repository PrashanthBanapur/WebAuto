package org.seltest.core;


/**
 * Super class for all Page Object <br/>
 * All Page Object Should implement this interface
 * 
 * @author adityas
 */
public class PageObject {
	/**
	 * An Element Class Instance which should be used to interact with the
	 * WebElements
	 */
	protected final Element element = new Element();
	/**
	 * A Browser Class Instance which should be used to interact with browsers
	 */
	protected final Browser browser = new Browser();


}
