package com.Organizer3000.eclipse.ide.first;
import java.util.*;
import java.io.*;
//Basic TO-DOs: Implement a user interface, debug everything (try/catch + close scanners), clean everything up, & introduce an interface so UI building is simpler/organized... 

public class Organizer3000 {

    public static void main(String[] args) throws IOException {
        int menuSelection;
        int categorySelection;
       
        Scanner menuSelector= new Scanner(System.in);
        Scanner namePicker= new Scanner(System.in);
        Scanner content= new Scanner(System.in);
        
        File file = new File("/Users/theryanspivey/Organizer3000/Categories");
        String[] listOfFiles = file.list();
        
        System.out.print("Would you like to view categories"
                + ", create a category, or delete a category?\n"
                + "1.) View Categories\n"
                + "2.) Create Category\n"
                + "3.) Delete Category"
                + "\n~ ");
        menuSelection= menuSelector.nextInt();
        
        if(menuSelection == 1) {
            printCategories(menuSelection, returnDirectoryFile());

            System.out.print("\nWould you like to inspect a category or append to a category?"
                    + "\n1.) Inspect"
                    + "\n2.) Append"
                    + "\n~ ");
            int manipulateCategory=menuSelector.nextInt();
            
            if(manipulateCategory == 1) {
                inspectCategory(manipulateCategory, returnDirectoryFile());
            }
            else if(manipulateCategory == 2) {
                appendCategory(manipulateCategory, returnDirectoryFile());
            }
        
        }   //end of if statement if inspect/append cat is chosen
        
        else if(menuSelection == 2) {               //OPTION 2 OF MENU SELECTION- create a new file
            System.out.print("\nEnter the name of the new category: ");
            String categoryName= namePicker.nextLine();            
            createCategory(categoryName);
            
        }
        
        else if(menuSelection == 3) {               //OPTION 3 OF MENU SELECTION- delete a file
            System.out.print("Explicitly name the file which you'd like to delete: ");
            String catToDelete= content.nextLine();
            deleteCategory(catToDelete);           
            }
        
        else
            System.out.println("Nah");
            }
    
    public static String[] returnDirectoryFile() {
        File file = new File("/Users/theryanspivey/Organizer3000/Categories");
        String[] listOfFiles = file.list();
        
        return listOfFiles;
    }

    public static void printCategories(int menuSelection, String[] listOfFiles) {
        
        int counter= 0;
        if(listOfFiles.length == 0)
            System.out.println("0 Categories Found");
        else if(menuSelection == 1) {                   //OPTION 1 OF MENU SELECTION- inspect file or append to file
            System.out.println("\n" + listOfFiles.length + " Categories Found: ");
           
            for (String files:listOfFiles) {
                counter++;
                System.out.println(counter + ": " + files);      
            } 
        
        }
    }
    
    public static void inspectCategory(int manipulateCategory, String[] listOfFiles) throws IOException {
        while(manipulateCategory != 1 || manipulateCategory != 2) {
            Scanner scan= new Scanner(System.in);

            if(manipulateCategory == 1) {       //inspect contents of a file
                
                System.out.print("\nWhich category would you like to inspect?\n"
                        + "(Enter number corresponding to file...)\n"
                        + "~ ");
                int categorySelection= scan.nextInt();
                
                String actualFile= listOfFiles[categorySelection-1];
                File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", actualFile);
                Scanner inputFile= new Scanner(newFile);
                
                while(inputFile.hasNext()) {
                    String name= inputFile.nextLine();
                    System.out.println(name);
                }
                
            }       
        }   //closes primary while loop of method
    }   //closes inspectCategory method
    
    public static void appendCategory(int manipulateCategory, String[] listOfFiles) throws IOException {
        Scanner scan= new Scanner(System.in);
        
        System.out.print("\nWhich category would you like to append to?\n"
                + "(Enter number corresponding to file...)\n"
                + "~ ");
        int categorySelection= scan.nextInt();
             
        String newContent= "";
        
        while(newContent != "-1") {
            String actualFile= listOfFiles[categorySelection-1];
            File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", actualFile);
            BufferedWriter bw = new BufferedWriter(new FileWriter(newFile, true));
            newContent= scan.nextLine();
            bw.write(newContent);
            bw.newLine();
            bw.close();
        }
        scan.close();
    }

    public static void createCategory(String categoryName) throws IOException{
        File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", categoryName);
        PrintWriter writer = new PrintWriter(newFile, "UTF-8");
        
        writer.close();
    }

    public static void deleteCategory(String catToDelete) throws IOException{
        File newFile= new File("/Users/theryanspivey/Organizer3000/Categories", catToDelete);
        newFile.delete();
    }
}
