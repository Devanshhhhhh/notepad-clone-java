package JavaProject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class NotepadClone extends JFrame implements ActionListener{
    JTabbedPane tabbedPane;
    Font defaultFont = new Font("Times New Roman", Font.PLAIN, 16);
    private boolean isDarkMode = false;


    public NotepadClone() {
        setTitle("Notepad");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Add first tab
        addNewTab();

        setVisible(true);
    }

    // Adds a new tab
    private void addNewTab() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea textArea = new JTextArea();
        textArea.setFont(defaultFont);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu viewMenu = new JMenu("View");


        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem zoomInItem = new JMenuItem("Zoom In");
        JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
        JMenuItem resetZoomItem = new JMenuItem("Reset Zoom");
        JMenuItem DarkModeItem = new JMenuItem("Dark Mode");


        fileMenu.add(newItem); fileMenu.add(openItem); fileMenu.add(saveItem);
        editMenu.add(cutItem); editMenu.add(copyItem); editMenu.add(pasteItem);
        viewMenu.add(zoomInItem); viewMenu.add(zoomOutItem); viewMenu.add(resetZoomItem);viewMenu.add(DarkModeItem);

        menuBar.add(fileMenu); menuBar.add(editMenu); menuBar.add(viewMenu);

        // Attach ActionListeners with tab context
        newItem.addActionListener(e -> addNewTab());
        openItem.addActionListener(e -> openFile(textArea));
        saveItem.addActionListener(e -> saveFile(textArea));
        cutItem.addActionListener(e -> textArea.cut());
        copyItem.addActionListener(e -> textArea.copy());
        pasteItem.addActionListener(e -> textArea.paste());
        zoomInItem.addActionListener(e -> zoom(textArea, 2));
        zoomOutItem.addActionListener(e -> zoom(textArea, -2));
        resetZoomItem.addActionListener(e -> textArea.setFont(defaultFont));
        DarkModeItem.addActionListener(e -> toggleDarkMode());
        

        panel.add(menuBar, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("File " + (tabbedPane.getTabCount() + 1), panel);
        tabbedPane.setSelectedComponent(panel);
    }

    private void openFile(JTextArea ta) {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                ta.setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    ta.append(line + "\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to open file!");
            }
        }
    }

    private void saveFile(JTextArea ta) {
        JFileChooser chooser = new JFileChooser();
        int option = chooser.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(ta.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to save file!");
            }
        }
    }

    private void zoom(JTextArea ta, int delta) {
        Font current = ta.getFont();
        ta.setFont(new Font(current.getName(), current.getStyle(), current.getSize() + delta));
    }
    
    private void toggleDarkMode() {
    isDarkMode = !isDarkMode;

    Color bg = isDarkMode ? new Color(43, 43, 43) : Color.WHITE;
    Color fg = isDarkMode ? Color.WHITE : Color.BLACK;
    Color mbBg = isDarkMode ? new Color(60, 63, 65) : UIManager.getColor("Menu.background");

    UIManager.put("Menu.background", mbBg);
    UIManager.put("MenuItem.background", mbBg);
    UIManager.put("Menu.foreground", fg);
    UIManager.put("MenuItem.foreground", fg);
    UIManager.put("TabbedPane.selected", bg);

    // Loop through all tabs and update their components
    for (int i = 0; i < tabbedPane.getTabCount(); i++) {
        JPanel tabPanel = (JPanel) tabbedPane.getComponentAt(i);
        JScrollPane scrollPane = (JScrollPane) tabPanel.getComponent(1);
        JTextArea textArea = (JTextArea) scrollPane.getViewport().getView();

        JMenuBar mb = (JMenuBar) tabPanel.getComponent(0);
        updateMenuBarTheme(mb, mbBg, fg);

        textArea.setBackground(bg);
        textArea.setForeground(fg);
        textArea.setCaretColor(fg);
    }

    // Update tabbed pane
    tabbedPane.setBackground(bg);
    tabbedPane.setForeground(fg);

    SwingUtilities.updateComponentTreeUI(this);  // Refresh look
    }

    private void updateMenuBarTheme(JMenuBar mb, Color bg, Color fg) {
        mb.setBackground(bg);
        mb.setForeground(fg);
        for (MenuElement menu : mb.getSubElements()) {
            if (menu instanceof JMenu) {
                JMenu jm = (JMenu) menu;
                jm.setBackground(bg);
                jm.setForeground(fg);
                for (MenuElement item : jm.getSubElements()) {
                    if (item instanceof JMenuItem) {
                        JMenuItem jmi = (JMenuItem) item;
                        jmi.setBackground(bg);
                        jmi.setForeground(fg);
                    }
                }
            }
        }
    }    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NotepadClone());
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}
