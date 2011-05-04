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

package org.ribax.common.log;

import utils.log.BasicLogger;
import utils.log.LogComponent;

/**
 * A class that implements logging for network requests.  This is used
 * by client classes to log messages concerning outgoing network requests.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
public class RequestLog extends BasicLogger {

    private static RequestLog instance = null;

    private RequestLog(String classname, LogComponent panel) {
        super(classname, panel);
    }

    /**
     * Get the single instance of the RequestLog.
     *
     * @param classname the name of the class getting the RequestLog instance.
     * @return the single instance of the RequestLog.
     */
    public static RequestLog getInstance(String classname) {
        if (instance == null) {
            instance = new RequestLog(classname, null);
        }

        return instance;
    }

    /**
     * Get the single instance of the RequestLog and define the LogComponent that
     * will receive log messages.
     *
     * @param classname the name of the class getting the RequestLog instance.
     * @param panel the LogComponent that will receive log messages.
     * @return the single instance of the RequestLog.
     */
    public static RequestLog getInstance(String classname, LogComponent panel) {
        if (instance == null) {
            instance = new RequestLog(classname, panel);
        } else if (instance.loggingComponent == null) {
            instance.loggingComponent = panel;
            instance.flushMessages();
        }
        return instance;
    }
}
