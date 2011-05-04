/*
 * RIBAX, Making Web Applications Easy 
 * Copyright (C) 2006 Damian Hamill and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.ribax.common.data;

/**
 * Classes that implement this interface can receive notifications
 * from a DataModel that the data has changed.
 * 
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 *
 */
public interface DataChangeListener {

    /** Receive a notification from a Data Model that the data in the model has changed.
     *  The notification means that some data in the model has changed but it does not mean
     *  that the specific data that the listener is interested in has changed so a listener 
     *  may be informed of a data change yet the data the listener is using in the model has
     *  not changed.  The granularity of the notfication is on all the data contained in the
     *  data model and not any subset.
     */
    public void dataChanged(DataModel model, String path);
}
