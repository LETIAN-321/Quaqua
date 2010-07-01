/*
 * @(#)RGBChooser.java  
 *
 * Copyright (c) 2005-2010 Werner Randelshofer
 * Hausmatt 10, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * The copyright of this software is owned by Werner Randelshofer. 
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * Werner Randelshofer. For details see accompanying license terms. 
 */

package ch.randelshofer.quaqua.colorchooser;

import ch.randelshofer.quaqua.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.colorchooser.*;
import javax.swing.plaf.*;
/**
 * RGBChooser.
 *
 * @author  Werner Randelshofer
 * @version $Id$
 */
public class RGBChooser extends AbstractColorChooserPanel implements UIResource {
    private ColorSliderModel ccModel = new RGBColorSliderModel();
    private int updateRecursion;
    
    /** Creates new form. */
    public RGBChooser() {
        initComponents();
        
        if (QuaquaManager.getProperty("java.version").startsWith("1.3")) {
            redField.setColumns(4);
            greenField.setColumns(4);
            blueField.setColumns(4);
        } 
        
        //
        Font font = UIManager.getFont("ColorChooser.font");
        redLabel.setFont(font);
        redSlider.setFont(font);
        redField.setFont(font);
        greenLabel.setFont(font);
        greenSlider.setFont(font);
        greenField.setFont(font);
        blueLabel.setFont(font);
        blueSlider.setFont(font);
        blueField.setFont(font);
        //
        int textSliderGap = UIManager.getInt("ColorChooser.textSliderGap");
        if (textSliderGap != 0) {
            Insets fieldInsets = new Insets(0,textSliderGap,0,0);
            GridBagLayout layout = (GridBagLayout) getLayout();
            GridBagConstraints gbc;
            gbc = layout.getConstraints(redField);
            gbc.insets = fieldInsets;
            layout.setConstraints(redField, gbc);
            gbc = layout.getConstraints(greenField);
            gbc.insets = fieldInsets;
            layout.setConstraints(greenField, gbc);
            gbc = layout.getConstraints(blueField);
            gbc.insets = fieldInsets;
            layout.setConstraints(blueField, gbc);
        }
        
        ccModel.configureColorSlider(0, redSlider);
        ccModel.configureColorSlider(1, greenSlider);
        ccModel.configureColorSlider(2, blueSlider);
        
        redField.setText(Integer.toString(redSlider.getValue()));
        greenField.setText(Integer.toString(greenSlider.getValue()));
        blueField.setText(Integer.toString(blueSlider.getValue()));
        
        new ColorSliderTextFieldHandler(redField, ccModel, 0);
        new ColorSliderTextFieldHandler(greenField, ccModel, 1);
        new ColorSliderTextFieldHandler(blueField, ccModel, 2);

        ccModel.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent evt) {
                if (updateRecursion++==0) {
                setColorToModel(ccModel.getColor());
                }
                updateRecursion--;
            }
        });
        redField.setMinimumSize(redField.getPreferredSize());
        greenField.setMinimumSize(greenField.getPreferredSize());
        blueField.setMinimumSize(blueField.getPreferredSize());
        VisualMargin bm = new VisualMargin(false,false,true,false);
        redLabel.setBorder(bm);
        greenLabel.setBorder(bm);
        blueLabel.setBorder(bm);
    }
    protected void buildChooser() {
    }
    
    public String getDisplayName() {
        return UIManager.getString("ColorChooser.rgbSliders");
    }
    
    public Icon getLargeDisplayIcon() {
        return UIManager.getIcon("ColorChooser.colorSlidersIcon");
    }
    
    public Icon getSmallDisplayIcon() {
        return getLargeDisplayIcon();
    }
    
    public void updateChooser() {
        updateRecursion++;
        ccModel.setColor(getColorFromModel());
        updateRecursion--;
    }
    public void setColorToModel(Color color) {
        getColorSelectionModel().setSelectedColor(color);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        redLabel = new javax.swing.JLabel();
        redSlider = new javax.swing.JSlider();
        redField = new javax.swing.JTextField();
        greenLabel = new javax.swing.JLabel();
        greenSlider = new javax.swing.JSlider();
        greenField = new javax.swing.JTextField();
        blueLabel = new javax.swing.JLabel();
        blueSlider = new javax.swing.JSlider();
        blueField = new javax.swing.JTextField();
        springPanel = new javax.swing.JPanel();

        setLayout(new java.awt.GridBagLayout());

        redLabel.setText(UIManager.getString("ColorChooser.rgbRedText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        add(redLabel, gridBagConstraints);

        redSlider.setMajorTickSpacing(255);
        redSlider.setMaximum(255);
        redSlider.setMinorTickSpacing(128);
        redSlider.setPaintTicks(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        add(redSlider, gridBagConstraints);

        redField.setColumns(3);
        redField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        redField.setText("0");
        redField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                redFieldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        add(redField, gridBagConstraints);

        greenLabel.setText(UIManager.getString("ColorChooser.rgbGreenText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        add(greenLabel, gridBagConstraints);

        greenSlider.setMajorTickSpacing(255);
        greenSlider.setMaximum(255);
        greenSlider.setMinorTickSpacing(128);
        greenSlider.setPaintTicks(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        add(greenSlider, gridBagConstraints);

        greenField.setColumns(3);
        greenField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        greenField.setText("0");
        greenField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                greenFieldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        add(greenField, gridBagConstraints);

        blueLabel.setText(UIManager.getString("ColorChooser.rgbBlueText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 0, 0, 0);
        add(blueLabel, gridBagConstraints);

        blueSlider.setMajorTickSpacing(255);
        blueSlider.setMaximum(255);
        blueSlider.setMinorTickSpacing(128);
        blueSlider.setPaintTicks(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weightx = 1.0;
        add(blueSlider, gridBagConstraints);

        blueField.setColumns(3);
        blueField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        blueField.setText("0");
        blueField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                blueFieldFocusLost(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        add(blueField, gridBagConstraints);

        springPanel.setLayout(new java.awt.BorderLayout());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 100;
        gridBagConstraints.weighty = 1.0;
        add(springPanel, gridBagConstraints);

    }// </editor-fold>//GEN-END:initComponents

    private void fieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldFocusGained
        ((JTextField) evt.getSource()).selectAll();        
    }//GEN-LAST:event_fieldFocusGained

    private void blueFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_blueFieldFocusLost
     blueField.setText(Integer.toString(ccModel.getValue(2)));
    }//GEN-LAST:event_blueFieldFocusLost

    private void greenFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_greenFieldFocusLost
      greenField.setText(Integer.toString(ccModel.getValue(1)));
    }//GEN-LAST:event_greenFieldFocusLost

    private void redFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_redFieldFocusLost
       redField.setText(Integer.toString(ccModel.getValue(0)));
    }//GEN-LAST:event_redFieldFocusLost
                
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField blueField;
    private javax.swing.JLabel blueLabel;
    private javax.swing.JSlider blueSlider;
    private javax.swing.JTextField greenField;
    private javax.swing.JLabel greenLabel;
    private javax.swing.JSlider greenSlider;
    private javax.swing.JTextField redField;
    private javax.swing.JLabel redLabel;
    private javax.swing.JSlider redSlider;
    private javax.swing.JPanel springPanel;
    // End of variables declaration//GEN-END:variables
    
}
