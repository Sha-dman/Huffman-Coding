import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class RahmanS_Project3_Main {
    public static void main(String[] args) throws IOException {
        BufferedReader infile = new BufferedReader(new FileReader(args[0])); // test1 test2 test3
        PrintStream outFile1 = new PrintStream(args[1]);
        PrintStream debug = new PrintStream(args[2]);
        PrintStream debug2 = new PrintStream (args[3]);

        HuffmanCode h = new HuffmanCode();
        h.computeCharCounts(infile);
        h.printCharAry(outFile1);

        LLlist l = new LLlist();
        h.constructHuffmanLList(l.listHead, debug, l);
        outFile1.println("Below is LLlist");
        l.printList(l.listHead, outFile1);

        h.constructHuffmanBinTree(l.listHead, outFile1, debug, l);
        binTree huffBinTree = new binTree();
        huffBinTree.root = l.listHead.next;
        outFile1.println("\n\n\nBelow is preOrder of HuffmanBinTree");
        huffBinTree.preOrder(huffBinTree.root, outFile1);
        outFile1.println("null");
        outFile1.println("\n\n\nBelow is inOrder of HuffmanBinTree");
        huffBinTree.inOrder(huffBinTree.root, outFile1);
        outFile1.println("null");

        outFile1.println("\n\n\nBelow is postOrder of HuffmanBinTree");
        huffBinTree.postOrder(huffBinTree.root, outFile1);
        outFile1.println("null");
        h.constructCharCode(huffBinTree.root, "", huffBinTree);
        // part2
        h.printCodeTable(outFile1);
        h.userInterface(huffBinTree.root, debug2,huffBinTree);
        infile.close();
        debug.close();
        outFile1.close();
        debug2.close();
    }
}
class treeNode{
    String chStr;
    String code;
    int freq;
    treeNode left;
    treeNode right;
    treeNode next;

    treeNode(String chStr, int freq){
        this.chStr = chStr;
        this.freq = freq;
        code = "";
        left = null;
        right= null;
        next  = null;
    }
    void printNode(treeNode node, PrintStream file){
        if (node.left == null && node.right == null && node.next == null)
        {
        file.print("  ( " + node.chStr + ", " + node.freq + ", " + node.code + ", " + "NULL" + ", " + "NULL" + ", " + "NULL" + " ) ->  ") ;
        } 

        else if (node.left == null && node.right == null){
        file.print(" ( " + node.chStr + ", " + node.freq + ", " + node.code + ", " + "NULL" + ", " + "NULL" + ", " + node.next.chStr + " ) ->") ;
    }
        else if (node.next == null){
        file.print("  ( " + node.chStr + ", " + node.freq + ", " + node.code + ", " + node.left.chStr + ", " + node.right.chStr + ", " + "NULL" + " ) -> ") ;
    }
        else{
        file.print("  ( " + node.chStr + ", " + node.freq + ", " + node.code + ", " + node.left.chStr + ", " + node.right.chStr + ", " + node.next.chStr + " )  ") ;
        }
    }  

    
}
class LLlist{
    treeNode listHead;
    
    LLlist(){
        listHead = new treeNode("dummy", 0);
    }

    treeNode findSpot(treeNode listHead, treeNode newNode, PrintStream debug){
        debug.println("Entering findSpot");
        treeNode spot = listHead;
        while(spot.next != null && spot.next.freq < newNode.freq){
        spot = spot.next;

            if (spot.next!= null){
                debug.println("in findSpot: Spot.next’s frequency is " + spot.next.freq + " and newNode.frequency is " + newNode.freq);
            }
        }
        debug.println("Leaving findSpot");
        return spot;
    }

    void insertOneNode(treeNode spot, treeNode newNode, treeNode listHead){

        newNode.next = spot.next;
        spot.next = newNode; 

    }

    void printList(treeNode listHead, PrintStream debug){
        treeNode node = listHead.next;
        while (node.next != null){
            node.printNode(node, debug);
            node = node.next;
        }
        debug.print("NULL");
    }
}

class binTree{
    treeNode root;

    binTree(){
        root = null;
    }

    void preOrder(treeNode root, PrintStream file){
        if (isLeaf(root)){
            root.printNode(root, file);
        }
        else{
            root.printNode(root, file);
            preOrder(root.left, file);
            preOrder(root.right, file);
        }
    }
    void inOrder(treeNode root, PrintStream file){
        if (isLeaf(root)){
            root.printNode(root, file);
        }
        else{
            inOrder(root.left, file);
            root.printNode(root, file);
            inOrder(root.right, file);
        }
    }
    void postOrder(treeNode root, PrintStream file){
        if (isLeaf(root)){
            root.printNode(root, file);
        }
        else{
            postOrder(root.left, file);
            postOrder(root.right, file);
            root.printNode(root, file);
        }
    }
        boolean isLeaf(treeNode node){
        if((node.left == null ) && (node.right == null)){
            return true;
        }
        else return false;
    }
}

class HuffmanCode{
    String codeTable[];
    int charCountAry[];
    HuffmanCode(){
        codeTable = new String[256];
        charCountAry = new int[256];
    }
    void computeCharCounts(BufferedReader infile)throws IOException{
        int index = 0;
        while (infile.ready()){
            index = infile.read();
            charCountAry[index]++;
        }
    }
    void printCharAry(PrintStream outFile1){
        outFile1.println("Index\tChar\tcount");
        outFile1.println("====================");
        for (int i = 0; i < 256; i++){
            if ( i == 10 || i == 13 || i == 32){
            outFile1.println(i +"\t\t" + i + "\t\t" + charCountAry[i]);
            }
            else if(charCountAry[i] >0){
            outFile1.println(i +"\t\t" + (char)i + "\t\t" + charCountAry[i]);
        }
    }
        outFile1.println();
    }
    void constructHuffmanLList(treeNode listHead, PrintStream debug, LLlist l){
        debug.println("entering constructHuffmanLList");
        for (int i = 0; i < 256; i++){
            int freq = 0;
            String chr ="";
            if (charCountAry[i] > 0){
                freq = charCountAry[i];
                chr = Character.toString((char)i);
                if ( i == 10 || i == 13|| i == 32){
                 chr = Integer.toString((char)i);
              }
            treeNode newNode = new treeNode(chr, freq);
            newNode.printNode(newNode, debug);
            newNode.chStr = String.valueOf((char)i);
            treeNode spot = l.findSpot(listHead, newNode, debug);
            l.insertOneNode(spot, newNode,l.listHead);
            l.printList(listHead, debug);
            }
        }
        debug.println("leaving constructHuffmanLList");
    }

    void constructHuffmanBinTree(treeNode listHead, PrintStream outFile1, PrintStream debug, LLlist l){
        debug.println("Entering constructHuffmanBinTree");
        while (listHead.next.next != null){
        treeNode newNode = new treeNode(listHead.next.chStr + listHead.next.next.chStr, listHead.next.freq + listHead.next.next.freq);
        newNode.left =listHead.next;
        newNode.right = listHead.next.next;
        newNode.next = null;
        newNode.printNode(newNode, debug);
        treeNode spot = l.findSpot(listHead, newNode, debug);
        l.insertOneNode(spot, newNode, l.listHead);
        listHead.next = listHead.next.next.next;
        }
        debug.println("leaving constructHuffmanBinTree");
    }

    void constructCharCode(treeNode node, String code, binTree tree){
        if (tree.isLeaf(node)){
            node.code = code;
            int index =(int) node.chStr.charAt(0);
            codeTable[index] = code;
        }
        else{
            constructCharCode(node.left, code + "0", tree);
            constructCharCode(node.right, code + "1", tree);
        }
    }


    // part 2
    void printCodeTable(PrintStream outFile1){
        outFile1.println("Index\t" + "Char\t" + "Code");
        outFile1.println("=========================");
        for(int i = 0; i < 256; i++){
            if ( codeTable[i] == null)continue;
            if ( i == 10 || i == 13 || i == 32){
                outFile1.println(i + "\t" + i + "\t" + codeTable[i]);
            }
            else   outFile1.println(i + "\t\t" + (char)i + "\t\t" + codeTable[i]);

        }
    }


    void userInterface(treeNode root, PrintStream debug2, binTree tree) throws IOException{
        //step 1
        String nameOrg ="";
        String nameComp ="";
        String nameDeComp = "";
        char yN = '.';
        Scanner scanner = new Scanner(System.in);
        while(yN != 'N'){

        System.out.println("Would you like to encode a file? ");
        yN = scanner.next().charAt(0);
        if (yN == 'N'){
            System.out.println("EXITING");
            System.exit(0);
        }
        
        else if (yN == 'Y'){
            System.out.println("Enter name of file without .txt: ");
            nameOrg = scanner.next();
            //step 2
            nameComp = nameOrg + "_Compressed.txt";
            nameDeComp = nameOrg + "_Decompressed.txt";
            nameOrg = nameOrg+ ".txt";
            //step 3
            BufferedReader orgFile = new BufferedReader(new FileReader(nameOrg));
            PrintStream compFile = new PrintStream(new File(nameComp));
            PrintStream deCompFile = new PrintStream(new File(nameDeComp));


            //step 4
            enCode(orgFile, compFile, debug2);
            compFile.close();
            BufferedReader compFilere = new BufferedReader (new FileReader(nameComp));
            deCode(compFilere, deCompFile, root, debug2, tree);
            orgFile.close();
            compFilere.close();
            deCompFile.close();
        }
        }
        scanner.close();
    }
    void enCode(BufferedReader inFile, PrintStream outFile, PrintStream deBug) throws IOException{
        deBug.println("Entering enCode!");
        while (inFile.ready()){
        char charIn = (char)inFile.read();
        int index = (int)charIn;
        String code = codeTable[index];
        outFile.print(code);
        deBug.println("Inside incode()");
        deBug.println(index + " " + code);
        }
        deBug.println("Leaving Encode method!");
    }
    void deCode(BufferedReader inFile, PrintStream outFile, treeNode root, PrintStream debug, binTree tree) throws IOException{
        debug.println("Entering decode!");
        treeNode spot = root;
        while (inFile.ready()){
        if (tree.isLeaf(spot)){
            outFile.print(spot.chStr);
            debug.println("Inside decode()");
            debug.println(spot.chStr);
            spot = root;
        }

        char oneBit = (char)inFile.read();
            if (oneBit == '0'){
                spot = spot.left;
            }
            else if (oneBit == '1'){
                spot = spot.right;
            }
            else{
                debug.println("Error! The compress file contains invalid character!");
                return;
            }
        }

        if (!inFile.ready() && !tree.isLeaf(spot)){
            System.out.println("Error: The compress file is corrupted!");
            return;
        }
        debug.println("Leaving deCode!");

    }
}
