package com.Organizer3000.eclipse.ide.first;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;


public class Organizer extends JFrame{
    private final int WINDOW_WIDTH= 500;
    private final int WINDOW_HEIGHT= 300;
    public String category;
    public Category categoryObj;
    public JList categoryList;
    public JList deleteCategoryList;

    public JPanel viewCatPanel;
    
    public Organizer() {
        setTitle("Organizer 3000");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //panels
        JPanel viewPanel= new JPanel();
        JPanel createPanel= new JPanel();
        JPanel deletePanel= new JPanel();
        
        //(viewPanel) button components
        JButton viewCategoryButton= new JButton("View");
        JButton editCategoryButton= new JButton("Edit");
        
        //(createPanel) button components
        JButton addCategoryButton= new JButton("Add Category");
        
      //(deletePanel) button components
        JButton deleteCategoryButton= new JButton("Delete Category");
        
        //JList Component
        categoryList= new JList(returnCategories());
        deleteCategoryList= new JList(returnCategories());
        
        //view panel
        viewPanel.add(categoryList);
        viewPanel.add(viewCategoryButton);
        viewPanel.add(editCategoryButton);
        viewCategoryButton.addActionListener(new ViewButtonListener());
        editCategoryButton.addActionListener(new EditButtonListener());
        
        //create panel
        createPanel.add(addCategoryButton, BorderLayout.CENTER);
        addCategoryButton.addActionListener(new addCategoryButtonListener());
        
        //delete panel
        deletePanel.add(deleteCategoryList);
        deletePanel.add(deleteCategoryButton);
        deleteCategoryButton.addActionListener(new deleteCategoryButtonListener());
        
        
        JTabbedPane tabbedPane= new JTabbedPane();
        tabbedPane.addTab("View/Append-to Categories", viewPanel);
        tabbedPane.addTab("Create Category", createPanel);
        tabbedPane.addTab("Delete Category", deletePanel);
        add(tabbedPane);
        
        setVisible(true);
        
    }
    
    public static String[] returnCategories() {
        File file = new File("/Users/theryanspivey/Organizer3000/Categories");
        String[] listOfFiles = file.list();
        int dot;
        int lengthOfString;
       
        for(int i=0; i < listOfFiles.length; i++) {
            StringBuilder list= new StringBuilder(listOfFiles[i]);
            dot= listOfFiles[i].length() - 4;
            lengthOfString= listOfFiles[i].length();
           
            list.delete(dot, lengthOfString);
            listOfFiles[i]= list.toString(); 
        }
        
        return listOfFiles;
    }
    
    private class ViewButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           JFrame frame= new JFrame();
           JButton copyButton= new JButton("Copy to clipboard");
           JLabel label= new JLabel("View/Copy-to-clipboard");
           frame.setTitle("View");
           frame.setSize(1600, 900);
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setLayout(new BorderLayout());
           frame.add(label, BorderLayout.NORTH);
           frame.add(copyButton, BorderLayout.CENTER);
           copyButton.addActionListener(new CopyButtonListener());
           
           category= (String) categoryList.getSelectedValue();
           try {
            categoryList= new JList(returnContentArray(category));
            frame.add(categoryList, BorderLayout.WEST);
           } 
           catch (FileNotFoundException e1) {
            e1.printStackTrace();
           }
           frame.setVisible(true);
        }
    }
    
    private class EditButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           JFrame frame= new JFrame();
           JButton addButton= new JButton("Add Link");
           JButton deleteButton= new JButton("Delete Link");
           JLabel label= new JLabel("Add/Delete a link");
           frame.setTitle("Edit");
           frame.setSize(1600, 900);
           frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           frame.setLayout(new BorderLayout());
           frame.add(label, BorderLayout.NORTH);
           frame.add(addButton, BorderLayout.CENTER);
           frame.add(deleteButton, BorderLayout.EAST);
           deleteButton.addActionListener(new DeleteButtonListener());
           addButton.addActionListener(new AddButtonListener());
           
           String category= (String) categoryList.getSelectedValue();
           categoryObj= new Category(category);
           try {
            categoryList= new JList(returnContentArray(category));
            frame.add(categoryList, BorderLayout.WEST);
           } 
           catch (FileNotFoundException e1) {
            e1.printStackTrace();
           }
           frame.setVisible(true);
        }
    }
    
    //add refreshing to JList
    //TO-DO: update delete button listener when titling is added to create content
    public class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String content= (String) categoryList.getSelectedValue();
            try {
                ArrayList<String> contentList = new ArrayList<String>();
                Collections.addAll(contentList, returnContentArray(categoryObj.toString()));
                for (int i=0; i < contentList.size(); i++) {
                    if(content.equals(contentList.get(i))) {
                        contentList.remove(i);
                        break;
                    }
                }
                File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", categoryObj.toString() + ".txt");
                PrintWriter pw = new PrintWriter(new FileOutputStream(newFile, false));
                for(int i=0; i < contentList.size(); i++) {
                    pw.write(contentList.get(i));
                    pw.println();
                }
                pw.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "That entry will be deleted next time you open the application...");
        }
    }
    
    //add refreshing to JList
    public class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String newContent= JOptionPane.showInputDialog("Enter a new link:");
            try {
                ArrayList<String> contentList = new ArrayList<String>();
                Collections.addAll(contentList, returnContentArray(categoryObj.toString()));
                contentList.add(newContent);
                File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", categoryObj.toString() + ".txt");
                PrintWriter pw = new PrintWriter(new FileOutputStream(newFile, false));
                for(int i=0; i < contentList.size(); i++) {
                    pw.write(contentList.get(i));
                    pw.println();
                }
                pw.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            JOptionPane.showMessageDialog(null, "That entry will be added next time you open the application...");
        }
    }
    
    private class CopyButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String selectedLink= (String) categoryList.getSelectedValue();
            StringSelection stringSelection= new StringSelection(selectedLink);
            Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
    }
    
    private class addCategoryButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String newCategory= JOptionPane.showInputDialog("Enter the title of a new category:");
            File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", newCategory + ".txt");
            try {
                PrintWriter pw = new PrintWriter(new FileOutputStream(newFile, false));
                pw.close();
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
    
    private class deleteCategoryButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String content= (String) deleteCategoryList.getSelectedValue();
            File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", content + ".txt");
            newFile.delete();
        }
    }
    public static String[] returnContentArray(String categoryName) throws FileNotFoundException {
        File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", categoryName + ".txt");
        Scanner inputFile= new Scanner(newFile);
        ArrayList<String> lines= new ArrayList<String>();
        while (inputFile.hasNextLine()) {
            lines.add(inputFile.nextLine());
        }
        
        String[] contentArray= lines.toArray(new String[0]);
        inputFile.close();
        return contentArray;
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        new Organizer();
    }
}
