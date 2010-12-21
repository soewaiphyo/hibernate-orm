/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.util.xml;

import static org.jboss.logging.Logger.Level.ERROR;
import java.io.Serializable;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Implements an {@link ErrorHandler} that mainly just logs errors/warnings.  However, it does track
 * the intial error it encounters and makes it available via {@link #getError}.
 *
 * @author Steve Ebersole
 */
public class ErrorLogger implements ErrorHandler, Serializable {

    private static final Logger LOG = org.jboss.logging.Logger.getMessageLogger(Logger.class,
                                                                                ErrorLogger.class.getPackage().getName());

	private SAXParseException error; // capture the initial error

	/**
	 * Retrieve the initial error encountered, or null if no error was encountered.
	 *
	 * @return The initial error, or null if none.
	 */
	public SAXParseException getError() {
		return error;
	}

	/**
	 * {@inheritDoc}
	 */
	public void error(SAXParseException error) {
        LOG.parsingXmlError(error.getLineNumber(), error.getMessage());
        if (this.error == null) this.error = error;
	}

	/**
	 * {@inheritDoc}
	 */
	public void fatalError(SAXParseException error) {
		error( error );
	}

	/**
	 * {@inheritDoc}
	 */
	public void warning(SAXParseException warn) {
        LOG.parsingXmlError(error.getLineNumber(), error.getMessage());
	}

	public void reset() {
		error = null;
	}

    /**
     * Interface defining messages that may be logged by the outer class
     */
    @MessageLogger
    interface Logger extends BasicLogger {

        @LogMessage( level = ERROR )
        @Message( value = "Error parsing XML (%d) : %s" )
        void parsingXmlError( int lineNumber,
                              String message );

        @LogMessage( level = ERROR )
        @Message( value = "Warning parsing XML (%d) : %s" )
        void parsingXmlWarning( int lineNumber,
                                String message );
    }
}
