package net.sf.openrocket.DragCCalc;

import javax.swing.*;

import net.sf.openrocket.document.Simulation;
import net.sf.openrocket.gui.SpinnerEditor;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.components.BasicSlider;
import net.sf.openrocket.gui.components.UnitSelector;
import net.sf.openrocket.plugin.Plugin;
import net.sf.openrocket.simulation.extension.AbstractSwingSimulationExtensionConfigurator;
import net.sf.openrocket.unit.UnitGroup;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@Plugin
public class DragCCalcConfigurator extends AbstractSwingSimulationExtensionConfigurator<DragCCalc> {
    public DragCCalcConfigurator() {
        super(DragCCalc.class);
    }

    @Override
    protected JComponent getConfigurationComponent(DragCCalc dragCCalc, Simulation simulation, JPanel jPanel) {
        jPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);  // Add padding around components

        // Title Label
        JLabel titleLabel = new JLabel("Drag Coefficient Calculator");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        jPanel.add(titleLabel, gbc);

        // File Path Label
        JLabel filePathLabel = new JLabel("No file selected");
        String savedPath = dragCCalc.getCsvFilePath();
        if (savedPath != null && !savedPath.isEmpty()) {
            filePathLabel.setText("Selected file: " + savedPath);
        }
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        jPanel.add(filePathLabel, gbc);

        // File Chooser Button
        JButton fileChooserButton = new JButton("Select CSV File");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        jPanel.add(fileChooserButton, gbc);

        fileChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select CSV File");
                int result = fileChooser.showOpenDialog(jPanel);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    dragCCalc.setCsvFilePath(selectedFile.getAbsolutePath());
                    filePathLabel.setText("Selected file: " + selectedFile.getAbsolutePath());
                }
            }
        });

        return jPanel;
    }
//    @Override
//    protected JComponent getConfigurationComponent(DragCCalc dragCCalc, Simulation simulation, JPanel jPanel) {
//        jPanel.add(new JLabel("Drag Coefficient Calculator"));
//
//        // File Chooser Button
//        JButton fileChooserButton = new JButton("\nSelect CSV File: ");
//        JLabel filePathLabel = new JLabel("No file selected");
//
//        // Display the saved file path if it exists
//        String savedPath = dragCCalc.getCsvFilePath();
//        if (savedPath != null && !savedPath.isEmpty()) {
//            filePathLabel.setText("\nSelected file: " + savedPath);
//        }
//
//        fileChooserButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFileChooser fileChooser = new JFileChooser();
//                fileChooser.setDialogTitle("Select CSV File");
//                int result = fileChooser.showOpenDialog(jPanel);
//
//                if (result == JFileChooser.APPROVE_OPTION) {
//                    File selectedFile = fileChooser.getSelectedFile();
//                    dragCCalc.setCsvFilePath(selectedFile.getAbsolutePath());
//                    filePathLabel.setText("Selected file: " + selectedFile.getAbsolutePath());
//                }
//            }
//        });
//
//        jPanel.add(fileChooserButton);
//        jPanel.add(filePathLabel);
//
//        return jPanel;
//    }
}
