package nl.tudelft.footballmanager.model.xml;
import java.io.File;

import org.xml.sax.Attributes;


/**
 * @author Boris Schrijver <boris@radialcontext.nl>
 */
public abstract class XML {
	protected File file;		// File location and filename to read from or write to.
	
	XML(File file) {
		this.file = file;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}
	
	/**
	 * @param attributes
	 * @param qName
	 * @return
	 */
	public static String getValueIgnoreCase(Attributes attributes, String qName){
		String qn = null;
	    for(int i = 0; i < attributes.getLength(); i++){
	        qn = attributes.getQName(i);
	        if(qn.equalsIgnoreCase(qName)){
	            return attributes.getValue(i);
	        }
	    }
	    return null;
	}
}
