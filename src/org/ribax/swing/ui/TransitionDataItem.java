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
package org.ribax.swing.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.jdom.Element;
import org.ribax.common.Messages;

import utils.log.BasicLogger;
import utils.types.NameValuePair;
import utils.xml.XMLutils;

/**
 * A DataItem that displays a set of Tabs in an ordered sequence with
 * defined transitions which can either be timed or button click
 * events.  A transition is associated with a Tab and it defines the transition 
 * procedure from the Tab it is associated with to the next Tab in the sequence.  
 * There are 2 kinds of transitions, after a delay and in response to a button click.
 * A transition can be strict or not.  In strict mode if the transition type is a DELAY type
 * and the user clicks a button then nothing will happen until the timer has elapsed.  If
 * the transition is not strict then the user can click a button to interrupt a delayed 
 * transition timer and proceed to the next Tab in the sequence.
 *
 * @version <tt>$Revision: $</tt>
 * @author  <a href="mailto:damian@ribax.org">Damian Hamill</a>
 */
/**
 * @author damian
 *
 */
public class TransitionDataItem extends SequenceDataItem {

    public static final long serialVersionUID = 1;
    /** The transition type that causes a transition after a specific time has elapsed */
    static int DELAYTYPE = 1;
    /** The transition type that waits for the user to press a button */
    static int BUTTONTYPE = 2;
    /** The set of Transitions for the Tabs */
    private Vector<Transition> transitions = new Vector<Transition>();
    private static BasicLogger LOG = new BasicLogger(TransitionDataItem.class.getName());

    /**
     * No argument Constructor - required
     */
    public TransitionDataItem() {
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.SequenceDataItem#readDescription(org.jdom.Element)
     */
    public void readDescription(Element node) {
        Element e;

        super.readDescription(node);

        // read the list of transitions
        if ((e = node.getChild("transitions")) != null) { //$NON-NLS-1$
            if (LOG.isDebugEnabled()) {
                LOG.debug(Messages.getString(BUNDLE_NAME, "TransitionDataItem.1")); //$NON-NLS-1$
            }
            // iterate through the transitions 
            List children = e.getChildren();
            Iterator iterator = children.iterator();
            while (iterator.hasNext()) {
                Element tnode = (Element) iterator.next();

                // get the name of the tab this transition is associated with
                String tabName = XMLutils.getElementString("tabName", tnode); //$NON-NLS-1$

                // get the transition type
                String transType = XMLutils.getElementString("transitionType", tnode); //$NON-NLS-1$

                // get the strictness value
                Boolean modeVal = XMLutils.getElementBoolean("strict", tnode); //$NON-NLS-1$

                // these values are mandatory
                if (tabName == null || transType == null) {
                    continue;
                }

                // default is not strict
                if (modeVal == null) {
                    modeVal = new Boolean(false);
                }

                int tType = 0;

                if ("DELAY".equals(transType.toUpperCase())) //$NON-NLS-1$
                {
                    tType = DELAYTYPE;
                } else if ("BUTTON".equals(transType.toUpperCase())) //$NON-NLS-1$
                {
                    tType = BUTTONTYPE;
                }

                // find the tab this transition is associated with
                Tab tab = findTab(tabName);

                if (tab == null) {
                    continue;
                }

                // create a new transition and add it to the vector
                Transition trans = new Transition(tab, tType, modeVal.booleanValue(), tnode);

                transitions.add(trans);
            }
        }
    }

    /**
     * Find a Tab for a given name.
     * 
     * @param name the name of the Tab to find.
     * @return the Tab corresponding to the name or null if not found.
     */
    private Tab findTab(String name) {
        if (name == null) {
            return null;
        }

        // itertae through our list of Tabs
        Iterator<Tab> it = tabs.iterator();
        while (it.hasNext()) {
            Tab tab = it.next();
            // check the name of this Tab
            if (name.equals(tab.getDataItemName())) {
                return tab;
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see org.ribax.swing.ui.SequenceDataItem#doAction(int, java.lang.String)
     */
    public void doAction(int buttonType, String action) {

        // get the current transition
        Transition trans = transitions.elementAt(position);

        // if strict mode is on ignore unless transition is button type
        if (trans.tType != BUTTONTYPE && trans.strict == true) {
            return;
        }

        // stop any timer
        cancelTimer();

        // send the event to the base class to switch Tabs
        super.doAction(buttonType, action);

        // if the button was either back or next we can start a new timer
        // if the current transition is a delay type
        if (buttonType == TabButton.BACK || buttonType == TabButton.NEXT) {
            checkTimer();
        }
    }

    /**
     * Find a Transition for a given Tab.
     * 
     * @param tab the Tab to find the Transition for.
     * @return the Transition for the Tab or null if not found.
     */
    private Transition findTransition(Tab tab) {

        // iterate through the transitions
        Iterator<Transition> it = transitions.iterator();
        while (it.hasNext()) {
            Transition t = it.next();

            // if it has the same Tab object we found it
            if (t.tab == tab) {
                return t;
            }
        }
        return null;
    }

    /**
     * Start a new timer if the current transition is a delay type.
     */
    private void checkTimer() {

        // get the Tab at the current position
        Tab tab = tabs.elementAt(position);

        if (position < transitions.size()) {
            // find the Transition for this Tab
            Transition trans = findTransition(tab);

            // if it is a DELAY type then start a timer
            if (trans != null && trans.tType == DELAYTYPE) {
                startTimer(trans.delay);
            }
        }
    }
    /* (non-Javadoc)
     * @see org.ribax.swing.ui.SequenceDataItem#loadData(java.util.ArrayList, java.lang.String)
     */

    public void loadData(ArrayList<NameValuePair> params, String action) {
        super.loadData(params, action);

        // start a timer if required
        checkTimer();
    }
    // a Timer object to schedule the TimerTask
    private Timer timer = new Timer();
    // a TimerTask object whose run() method is called by the Timer object
    // when the time period has elapsed
    private TimerTask timerTask = null;

    /**
     * Start a new Timer to elapse after the specified delay.  After the given delay
     * has elapsed a method will be called.
     * 
     * @param delay the time period for the timer.
     */
    private void startTimer(int delay) {

        // create a new Timertask that calls timerTick() when the timer elapses
        timerTask = new TimerTask() {

            public void run() {
                timerTick();
            }
        };
        // schedule the timer to elapse of delay seconds (given in milliseconds)
        timer.schedule(timerTask, delay * 1000);
    }

    /**
     * Stop any current timer.
     */
    private synchronized void cancelTimer() {
        if (timerTask != null) {
            timerTask.cancel();
        }
    }

    /**
     * The method that is called when the timer elapses.  Move to the next Tab in
     * the sequence.
     */
    private synchronized void timerTick() {
        // load the next Tab
        position++;

        if (position <= tabs.size()) {
            loadCurrentTab();
        }

        // start a new timer if required
        checkTimer();
    }

    /**
     * A private class that determines how a transition occurs from one Tab to the next.
     * 
     * @author damian
     *
     */
    private class Transition {

        /** The Tab this transition is associated with */
        Tab tab = null;
        /** The Transition type either DELAYTYPE or BUTTONTYPE */
        int tType = 0;
        /** indicates whether this transition is strict or not */
        boolean strict = false;
        /** The time period in seconds after which a transition will occur to the next Tab
         * in the sequence if the transition type is DELAYTYPE.  Default is 5 seconds */
        int delay = 5;

        /**
         * Constructor that takes a Tab, the transition type, a strictness indicator
         * and an Element.
         * 
         * @param tab the Tab associated with tis Transition.
         * @param tType indicates the transition type DELAYTYPE or BUTTONTYPE.
         * @param strict indicates whether the transition is strict.
         * @param tnode the Element for this Transition.
         */
        Transition(Tab tab, int tType, boolean strict, Element tnode) {
            this.tab = tab;
            this.tType = tType;
            this.strict = strict;

            // if the transition type is DELAYTYPE then get the time delay from the
            // Element 
            if (tType == DELAYTYPE) {
                String ds = XMLutils.getElementString("delay", tnode); //$NON-NLS-1$
                try {
                    if (ds != null) // parse the delay value
                    {
                        delay = Integer.parseInt(ds);
                    }
                } catch (NumberFormatException ex) {
                }
            }
        }
    }
}
