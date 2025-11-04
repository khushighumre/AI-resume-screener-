package com.resumescreener.ui;

import com.resumescreener.extractor.DataExtractor;
import com.resumescreener.model.Candidate;
import com.resumescreener.parser.PDFParser;
import com.resumescreener.parser.DocParser;
import com.resumescreener.parser.Parser;
import com.resumescreener.storage.JSONWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ResumeScreenerGUI extends JFrame {
    private JPanel uploadPanel;
    private JLabel uploadLabel;
    private JButton parseButton;
    private JLabel messageLabel;
    private File selectedFile;

    public ResumeScreenerGUI() {
        initializeGUI();
        setupDragAndDrop();
    }

    private void initializeGUI() {
        setTitle("AI Resume Screener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with gradient background
        JPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Card panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(new Color(255, 255, 255, 240));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));

        // Title
        JLabel titleLabel = new JLabel("AI Resume Screener");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Upload panel
        uploadPanel = new JPanel();
        uploadPanel.setLayout(new BorderLayout());
        uploadPanel.setBackground(new Color(250, 250, 250));
        uploadPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createDashedBorder(new Color(102, 126, 234), 2, 5, 5, false),
            new EmptyBorder(40, 20, 40, 20)
        ));
        uploadPanel.setPreferredSize(new Dimension(500, 150));
        uploadPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        uploadLabel = new JLabel("<html><div style='text-align: center;'>📄<br><br>Drag & drop your resume here or <u>browse files</u><br><small>PDF or DOCX files only</small></div></html>");
        uploadLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        uploadLabel.setForeground(new Color(102, 102, 102));
        uploadLabel.setHorizontalAlignment(SwingConstants.CENTER);
        uploadPanel.add(uploadLabel, BorderLayout.CENTER);

        // Parse button
        parseButton = new JButton("Parse Resume");
        parseButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        parseButton.setForeground(Color.WHITE);
        parseButton.setBackground(new Color(102, 126, 234));
        parseButton.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        parseButton.setFocusPainted(false);
        parseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        parseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        parseButton.setEnabled(false);

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setVisible(false);

        // Add components to card
        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(uploadPanel);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(parseButton);
        cardPanel.add(Box.createVerticalStrut(20));
        cardPanel.add(messageLabel);

        mainPanel.add(cardPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Event listeners
        uploadPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                browseFile();
            }
        });

        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseResume();
            }
        });
    }

    private void setupDragAndDrop() {
        new DropTarget(uploadPanel, new DropTargetListener() {
            @Override
            public void dragEnter(DropTargetDragEvent dtde) {
                uploadPanel.setBackground(new Color(232, 242, 255));
            }

            @Override
            public void dragOver(DropTargetDragEvent dtde) {}

            @Override
            public void dropActionChanged(DropTargetDragEvent dtde) {}

            @Override
            public void dragExit(DropTargetEvent dte) {
                uploadPanel.setBackground(new Color(250, 250, 250));
            }

            @Override
            public void drop(DropTargetDropEvent dtde) {
                uploadPanel.setBackground(new Color(250, 250, 250));
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Transferable transferable = dtde.getTransferable();
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        validateAndSetFile(files.get(0));
                    }
                } catch (Exception e) {
                    showMessage("❌ Error handling dropped file", Color.RED);
                }
            }
        });
    }

    private void browseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Resume files (*.pdf, *.docx)", "pdf", "docx"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            validateAndSetFile(fileChooser.getSelectedFile());
        }
    }

    private void validateAndSetFile(File file) {
        String fileName = file.getName().toLowerCase();
        
        if (!fileName.endsWith(".pdf") && !fileName.endsWith(".docx")) {
            showMessage("❌ Please select a PDF or DOCX file only", Color.RED);
            return;
        }
        
        if (file.length() > 10 * 1024 * 1024) {
            showMessage("❌ File size must be less than 10MB", Color.RED);
            return;
        }
        
        selectedFile = file;
        uploadLabel.setText("<html><div style='text-align: center;'>✅<br><br>Selected: " + file.getName() + "<br><small>Ready to parse</small></div></html>");
        uploadPanel.setBackground(new Color(212, 237, 218));
        parseButton.setEnabled(true);
        hideMessage();
    }

    private void parseResume() {
        if (selectedFile == null) return;
        
        parseButton.setText("Parsing... ⏳");
        parseButton.setEnabled(false);
        hideMessage();
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            private boolean success = false;
            private String errorMessage = "";
            
            @Override
            protected Void doInBackground() throws Exception {
                try {
                    Parser parser;
                    String fileName = selectedFile.getName().toLowerCase();
                    
                    if (fileName.endsWith(".pdf")) {
                        parser = new PDFParser(selectedFile);
                    } else if (fileName.endsWith(".docx")) {
                        parser = new DocParser(selectedFile);
                    } else {
                        throw new Exception("Unsupported file type");
                    }
                    
                    String text = parser.parseText();
                    DataExtractor extractor = new DataExtractor();
                    Candidate candidate = extractor.extract(text);
                    
                    List<Candidate> candidates = new ArrayList<>();
                    candidates.add(candidate);
                    JSONWriter writer = new JSONWriter();
                    writer.writeToFile(candidates, "output/parsed_resume.json");
                    
                    success = true;
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                }
                return null;
            }
            
            @Override
            protected void done() {
                parseButton.setText("Parse Resume");
                parseButton.setEnabled(true);
                
                if (success) {
                    showMessage("✅ Your resume has been successfully parsed!", new Color(21, 87, 36));
                    resetForm();
                } else {
                    showMessage("❌ Failed to parse resume. Please try again.", Color.RED);
                }
            }
        };
        
        worker.execute();
    }

    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
        messageLabel.setVisible(true);
    }

    private void hideMessage() {
        messageLabel.setVisible(false);
    }

    private void resetForm() {
        Timer timer = new Timer(3000, e -> {
            selectedFile = null;
            uploadLabel.setText("<html><div style='text-align: center;'>📄<br><br>Drag & drop your resume here or <u>browse files</u><br><small>PDF or DOCX files only</small></div></html>");
            uploadPanel.setBackground(new Color(250, 250, 250));
            parseButton.setEnabled(false);
            hideMessage();
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Custom panel with gradient background
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            Color color1 = new Color(102, 126, 234);
            Color color2 = new Color(118, 75, 162);
            GradientPaint gradient = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ResumeScreenerGUI().setVisible(true);
        });
    }
}