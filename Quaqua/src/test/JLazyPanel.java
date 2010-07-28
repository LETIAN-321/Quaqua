/*
 * @(#)JLazyPanel.java
 *
 * Copyright (c) 2005-2009 Werner Randelshofer
 * Hausmatt 10, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Werner Randelshofer. For details see accompanying license terms. 
 */

package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * A JLazyPanel creates its child component only when it becomes visible or
 * when it is painted.
 * JLazyPanel is intended for use as a child of a JTabbedPane.
 *
 * @author  Werner Randelshofer
 * @version $Id$
 */
public class JLazyPanel extends javax.swing.JPanel {
    private String childClassName;
    
    
    /**
     * Creates a new instance.
     */
    public JLazyPanel() {
        initComponents();
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                instantiateChild();
                JLazyPanel.this.removeComponentListener(this);
            }
        });
    }
    public JLazyPanel(String childClassName) {
        this();
        setChildClassName(childClassName);
    }
    
    public void setChildClassName(String childClassName) {
        this.childClassName = childClassName;
    }
    
    private void instantiateChild() {
        if (childClassName != null) {

            long start = System.currentTimeMillis();
            try {
                Class childClass = Class.forName(childClassName);
                Component child = (Component) childClass.newInstance();
                add(child);
            } catch (Throwable e) {
                add(new JLabel("Unable to instantiate "+childClassName));
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();
            System.out.println("create "+childClassName+" "+(end-start));
            childClassName = null;
            start = end;
            validate();
            end = System.currentTimeMillis();
            System.out.println("validate "+(end-start));
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (childClassName != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() { instantiateChild(); }
            });
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
