package com.organizer.eclipse.ide.first;
/* TO-DO:
 * 
 * Clean up styling:

 * 		use for-each loops when necessary (Object O: objectCollection) (Research this a little bit)
 * 		comment everything
 * 
 * Return accurate exception messages
 * 
 * 
 * Add refreshing to JList when something is altrered (this should become more doable after code is cleaned up a little)
 *
 *		
 *
 *		Extra Features:
 *			*also add tags, comments, & ratings
 *			*make it so whatever is in the clipboard is automatically in the textfield for input when entering a new link/content
 *			*Add sorting to the content added to a specific category- Date added, alphabetic, etc
 *			*Search for specific words within links
 *			*Check for duplicates when trying to add a link
 *			*Transition to storing content in XML
 *			*Add login features to program
 *			*Add import bookmarks from browser
 *			*Research adding content to program when highlighting text and right-clicking(this will most likely require installing to the system)
 *			*Check for broken/dead links every time a category is opened
 *			*Add duplicate a category
 *			*Splice title off beginning of link when copying to clipboard
 *			*Switch to JScrollPane
 */

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class Organizer extends JFrame {
	private final int WINDOW_WIDTH = 650;
	private final int WINDOW_HEIGHT = 450;
	public String category;
	public Category categoryObj;
	public JList categoryList;
	DefaultListModel deleteModel = new DefaultListModel();
	public JList deleteCategoryList;
	public JList editButtonList;
	public JList viewButtonList;
	public JList southPanelList;
	public JPanel viewCatPanel;
	public JPanel southPanel = new JPanel();

	public Organizer() {
		setTitle("Organizer 3000");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// panels
		JPanel viewPanel = new JPanel();
		JPanel createPanel = new JPanel();
		JPanel deletePanel = new JPanel();

		// (viewPanel) button components
		JButton viewCategoryButton = new JButton("View");
		JButton editCategoryButton = new JButton("Edit");

		// (createPanel) button components
		JButton addCategoryButton = new JButton("Add Category");

		// (deletePanel) button components
		JButton deleteCategoryButton = new JButton("Delete Category");

		// JList Component
		categoryList = new JList(returnCategories());
		deleteCategoryList = new JList(returnCategories());

		// view panel
		viewPanel.add(categoryList);
		viewPanel.add(viewCategoryButton);
		viewPanel.add(editCategoryButton);
		viewCategoryButton.addActionListener(e -> {
			try {
				viewButtonListener();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		editCategoryButton.addActionListener(e -> {
			try {
				editButtonListener();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		// create panel
		createPanel.add(addCategoryButton, BorderLayout.CENTER);
		addCategoryButton.addActionListener(e -> addCategoryButtonListener());

		// delete panel
		deletePanel.add(deleteCategoryList);
		deletePanel.add(deleteCategoryButton);
		deleteCategoryButton.addActionListener(e -> deleteCategoryButtonListener());

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("View/Append-to Categories", viewPanel);
		tabbedPane.addTab("Create Category", createPanel);
		tabbedPane.addTab("Delete Category", deletePanel);
		add(tabbedPane, BorderLayout.NORTH);

		// southern panel of application
		southPanel.setLayout(new BorderLayout());
		add(southPanel, BorderLayout.CENTER);

		setVisible(true);

	}

	public static String[] returnCategories() {
		File file = new File("C:\\Users\\Ryan\\Documents\\Organizer\\Categories");
		String[] listOfFiles = file.list();
		int dot;
		int lengthOfString;

		for (int i = 0; i < listOfFiles.length; i++) {
			StringBuilder list = new StringBuilder(listOfFiles[i]);
			dot = listOfFiles[i].length() - 4;
			lengthOfString = listOfFiles[i].length();

			list.delete(dot, lengthOfString);
			listOfFiles[i] = list.toString();
		}

		return listOfFiles;
	}

	public void viewButtonListener() throws FileNotFoundException {
		category = (String) categoryList.getSelectedValue();
		southPanel.removeAll();
		southPanelList = new JList(returnContentArray(category));
		southPanel.add(southPanelList, BorderLayout.CENTER);
		JButton copyButton2 = new JButton("Copy to clipboard");
		copyButton2.addActionListener(e -> copyButtonListener());
		southPanel.add(copyButton2, BorderLayout.EAST);
		southPanel.revalidate();
	}

	public void editButtonListener() throws FileNotFoundException {

		category = (String) categoryList.getSelectedValue();
		categoryObj = new Category(category);
		southPanel.removeAll();
		southPanelList = new JList(returnContentArray(category));
		southPanel.add(southPanelList, BorderLayout.CENTER);
		JButton addButton1 = new JButton("Add");
		JButton deleteButton1 = new JButton("Delete");
		addButton1.addActionListener(e -> addButtonListener());
		deleteButton1.addActionListener(e -> deleteButtonListener());
		southPanel.add(addButton1, BorderLayout.EAST);
		southPanel.add(deleteButton1, BorderLayout.SOUTH);
		southPanel.revalidate();
	}

	// add refreshing to JList
	// TO-DO: update delete button listener when titling is added to create content
	public void deleteButtonListener() {
		String content = (String) southPanelList.getSelectedValue();
		try {
			ArrayList<String> contentList = new ArrayList<String>();
			Collections.addAll(contentList, returnContentArray(categoryObj.toString()));
			for (int i = 0; i < contentList.size(); i++) {
				if (content.equals(contentList.get(i))) {
					contentList.remove(i);
					break;
				}
			}
			File newFile = new File("C:\\Users\\Ryan\\Documents\\Organizer\\Categories",
					categoryObj.toString() + ".txt");
			PrintWriter pw = new PrintWriter(new FileOutputStream(newFile, false));
			for (int i = 0; i < contentList.size(); i++) {
				pw.write(contentList.get(i));
				pw.println();
			}
			pw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "That entry will be deleted next time you open the application...");
	}

	// add refreshing to JList
	public void addButtonListener() {
		String title = JOptionPane.showInputDialog("Enter a title for the link:") + ": ";
		String newContent = JOptionPane.showInputDialog("Enter a new link:");
		try {
			ArrayList<String> contentList = new ArrayList<String>();
			Collections.addAll(contentList, returnContentArray(categoryObj.toString()));
			contentList.add(title + newContent);
			File newFile = new File("C:\\Users\\Ryan\\Documents\\Organizer\\Categories",
					categoryObj.toString() + ".txt");
			PrintWriter pw = new PrintWriter(new FileOutputStream(newFile, false));
			for (int i = 0; i < contentList.size(); i++) {
				pw.write(contentList.get(i));
				pw.println();
			}
			pw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		JOptionPane.showMessageDialog(null, "That entry will be added next time you open the application...");
	}

	// Copies text from content JList to clipboard
	public void copyButtonListener() {
		String selectedLink = (String) southPanelList.getSelectedValue();
		StringSelection stringSelection = new StringSelection(selectedLink);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	public void addCategoryButtonListener() {
		String newCategory = JOptionPane.showInputDialog("Enter the title of a new category:");
		File newFile = new File("C:\\Users\\Ryan\\Documents\\Organizer\\Categories", newCategory + ".txt");
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(newFile, false));
			pw.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		categoryList.revalidate();
	}

	public void deleteCategoryButtonListener() {
		String content = (String) deleteCategoryList.getSelectedValue();
		File newFile = new File("C:\\Users\\Ryan\\Documents\\Organizer\\Categories", content + ".txt");
		newFile.delete();
	}

	public static String[] returnContentArray(String categoryName) throws FileNotFoundException {
		File newFile = new File("C:\\Users\\Ryan\\Documents\\Organizer\\Categories", categoryName + ".txt");
		Scanner inputFile = new Scanner(newFile);
		ArrayList<String> lines = new ArrayList<String>();
		while (inputFile.hasNextLine()) {
			lines.add(inputFile.nextLine());
		}

		String[] contentArray = lines.toArray(new String[0]);
		inputFile.close();
		return contentArray;
	}

	public static void main(String[] args) throws FileNotFoundException {
		new Organizer();
	}
}
