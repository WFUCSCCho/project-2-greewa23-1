/**********************************************************************
 * @file Proj2.java
 * @brief This program implements the Proj2 class which reads the input data
 * file and writes to an output file the times for BST and AVL Tree operations
 * based on unsorted and randomized arraylists.
 * @author Wynne Greene
 * @date: October 24, 2024
 ***********************************************************************/

import javax.xml.datatype.DatatypeConstants;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;


public class Proj2 {
    public static void main(String[] args) throws IOException {
        // Use command line arguments to specify the input file
        if (args.length != 2) {
            System.err.println("Usage: java TestAvl <input file> <number of lines>");
            System.exit(1);
        }
        //C:\Users\wynne\IdeaProjects\project-2-greewa23-1\src\volcanoes_around_the_world_in_2021.csv

        //Get the input file name from the first argument and the number of lines
        //from the second argument.
        String inputFileName = args[0];
        int numLines = Integer.parseInt(args[1]);

        // For file input
        FileInputStream inputFileNameStream = null;
        Scanner inputFileNameScanner = null;

        // Open the input file
        inputFileNameStream = new FileInputStream(inputFileName);
        inputFileNameScanner = new Scanner(inputFileNameStream);

        // ignore first line
        inputFileNameScanner.nextLine();

	    // FINISH ME
        //Create an arraylist to store the data of each element.
        ArrayList<Volcano> volcanoList = new ArrayList<Volcano>();
        int count = 0; //count keeps track of the line count.
        //Traverse numLines.
        while (count < numLines) {//inputFileNameScanner.hasNext()) {
            //Scan in the line.
            String line = inputFileNameScanner.nextLine();
            String[] parts = line.split(","); // split the string into multiple parts

            Volcano v; //Store the data in v.
            //Condition for exceptions
            if (parts.length != 11) {
                //String[] command = {line};
                //data stores the values of each variable.
                ArrayList<String> data = new ArrayList<>();
                //s is updated with entries that need to be combined.
                String s = "";
                for (int i = 0; i < parts.length; i++) {
                    //System.out.println("in");
                    //System.out.println(data);
                    if (parts[i].indexOf("\"") != -1) {
                        s += parts[i];
                        i++;
                        //Loop until the last quote is found.
                        while (i < parts.length) {
                            s += "," + parts[i];
                            if (parts[i].indexOf("\"") != -1) {
                                break;
                            }
                            i++;
                        }
                        data.add(s);
                    } else {
                        data.add(parts[i]);
                    }
                    //System.out.println(data);
                }
                //Update v with the data.
                v = new Volcano(data.get(0), data.get(1), data.get(2), data.get(3), data.get(4),
                        Double.parseDouble(data.get(5)), Double.parseDouble(data.get(6)), Integer.parseInt(data.get(7)), data.get(8), data.get(9), data.get(10));
            }
            else {
                //Update v with the data from the array.
                v = new Volcano(parts[0], parts[1], parts[2], parts[3], parts[4],
                        Double.parseDouble(parts[5]), Double.parseDouble(parts[6]), Integer.parseInt(parts[7]), parts[8], parts[9], parts[10]);
            }
            volcanoList.add(v); // add the data onto the ArrayList
            count++;
        }
        inputFileNameStream.close();

        /*
        Insert each element of the sorted and randomized ArrayLists into BST
        and AVL Trees. In total, there will be four trees.
         */
        //The try block attempts to open the file and write to it. The catch block catches any exceptions.
        FileWriter out = null;
        try {
            //Create or open the file to write to it.
            out = new FileWriter("output.txt", true);

            //Store the data from the original matrix.
            ArrayList<Volcano> orgList = new ArrayList<>();
            for(int i = 0; i<volcanoList.size(); i++) {
                orgList.add(volcanoList.get(i));
            }

            //Sort the arraylist and add the elements to sorted.
            Collections.sort(volcanoList);
            ArrayList<Volcano> sorted = new ArrayList<>();
            for(int i = 0; i<volcanoList.size(); i++) {
                sorted.add(volcanoList.get(i));
            }


            //Shuffle the arraylist.
            Collections.shuffle(volcanoList);
            //Create objects to store each of the four trees.
            BST<Volcano> mySortedBST = new BST<>();
            AvlTree<Volcano> mySortedAVL = new AvlTree<>();
            BST<Volcano> myShuffledBST = new BST<>();
            AvlTree<Volcano> myShuffledAVL = new AvlTree<>();
            //out.write("Line Number, Sorted Insert, Sorted Search, Unsorted Insert, Unsorted Search\n");

            //Check for error in number of lines.
            if(numLines > volcanoList.size() || numLines<0) {
                System.err.println("Usage: java TestAvl <number of lines>");
                System.exit(1);
            }
            /*
            For each of the trees, insert elements from the sorted or randomized arraylists
            and keep track of how many ns each operation takes.
             */
            //Sorted insert into BST and AVL
            long start1 = System.nanoTime();
            for(int i = 0; i<sorted.size(); i++) {
                mySortedBST.insert(sorted.get(i));
            }
            long end1 = System.nanoTime()-start1;
            long start2 = System.nanoTime();
            for(int i = 0; i<sorted.size(); i++) {
                mySortedAVL.insert(sorted.get(i));
            }
            long end2 = System.nanoTime()-start2;

            //Unsorted insert into BST and AVL
            long start3 = System.nanoTime();
            for(int i = 0; i<volcanoList.size(); i++) {
                myShuffledBST.insert(volcanoList.get(i));
            }
            long end3 = System.nanoTime()-start3;
            long start4 = System.nanoTime();
            for(int i = 0; i<volcanoList.size(); i++) {
                myShuffledAVL.insert(volcanoList.get(i));
            }
            long end4 = System.nanoTime()-start4;

            //Sorted search of BST and AVL
            long startSearch = System.nanoTime();
            for(int i = 0; i<orgList.size(); i++) {
                mySortedBST.search(orgList.get(i));
            }
            long endSearch = System.nanoTime()-startSearch;
            long startSearch2 = System.nanoTime();
            for(int i = 0; i<orgList.size(); i++) {
                mySortedAVL.contains(orgList.get(i));
            }
            long endSearch2 = System.nanoTime()-startSearch2;

            //Unsorted search of BST and AVL
            long startSearch3 = System.nanoTime();
            for(int i = 0; i<orgList.size(); i++) {
                myShuffledBST.search(orgList.get(i));
            }
            long endSearch3 = System.nanoTime()-startSearch3;
            long startSearch4 = System.nanoTime();
            for(int i = 0; i<orgList.size(); i++) {
                myShuffledAVL.contains(orgList.get(i));
            }
            long endSearch4 = System.nanoTime()-startSearch4;

            //Write the number of lines and the time it takes to complete each operation for each tree.
            out.write(numLines + "," + end1 + "," + end2 + "," + end3 + "," + end4 + "," + endSearch + "," + endSearch2 + ","
                     + endSearch3 + "," + endSearch4 + "\n");

        }
        catch(Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        finally {
            out.close();
        }
    }
}
