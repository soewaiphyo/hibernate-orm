/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
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
 *
 */
package org.hibernate.loader.collection;

import static org.jboss.logging.Logger.Level.DEBUG;
import org.hibernate.MappingException;
import org.hibernate.engine.LoadQueryInfluencers;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.loader.JoinWalker;
import org.hibernate.persister.collection.QueryableCollection;
import org.jboss.logging.BasicLogger;
import org.jboss.logging.LogMessage;
import org.jboss.logging.Message;
import org.jboss.logging.MessageLogger;

/**
 * Loads a collection of values or a many-to-many association.
 * <br>
 * The collection persister must implement <tt>QueryableCOllection<tt>. For
 * other collections, create a customized subclass of <tt>Loader</tt>.
 *
 * @see OneToManyLoader
 * @author Gavin King
 */
public class BasicCollectionLoader extends CollectionLoader {

    private static final Logger LOG = org.jboss.logging.Logger.getMessageLogger(Logger.class,
                                                                                BasicCollectionLoader.class.getPackage().getName());

	public BasicCollectionLoader(
			QueryableCollection collectionPersister,
			SessionFactoryImplementor session,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		this( collectionPersister, 1, session, loadQueryInfluencers );
	}

	public BasicCollectionLoader(
			QueryableCollection collectionPersister,
			int batchSize,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		this( collectionPersister, batchSize, null, factory, loadQueryInfluencers );
	}

	protected BasicCollectionLoader(
			QueryableCollection collectionPersister,
			int batchSize,
			String subquery,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		super( collectionPersister, factory, loadQueryInfluencers );

		JoinWalker walker = new BasicCollectionJoinWalker(
				collectionPersister,
				batchSize,
				subquery,
				factory,
				loadQueryInfluencers
		);
		initFromWalker( walker );

		postInstantiate();

        LOG.staticSelectForCollection(collectionPersister.getRole(), getSQLString());
	}

    /**
     * Interface defining messages that may be logged by the outer class
     */
    @MessageLogger
    interface Logger extends BasicLogger {

        @LogMessage( level = DEBUG )
        @Message( value = "Static select for collection %s: %s" )
        void staticSelectForCollection( String role,
                                        String sqlString );
    }
}
