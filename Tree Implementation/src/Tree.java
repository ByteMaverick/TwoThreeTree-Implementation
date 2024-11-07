
import java.util.ArrayList;

import java.util.Collections;

public class Tree {

    public Node root = null;
    public Node lastChecked = null;
    public boolean added;

    // if search returns true means newElement is a duplicate.
    public boolean search(int newElement) {

        Node current = root;

        while (current != null) {

            if (current.searchElement(newElement)) {
                return true;
            } else if (current.leftValue() > newElement) {

                lastChecked = current;
                current = current.left;
            } else if (current.rightValue() < newElement) {
                lastChecked = current;
                current = current.right;
            } else {
                lastChecked = current;
                current = current.middle;
            }
        }

        return false;
    }

    // Dividing the problem into three cases
    public boolean insert(int newElement) {

        boolean added = false;
        // Duplicate
        boolean found = search(newElement);
        if (found) {
            return false;
        }

        // if tree is empty
        else if (lastChecked == null) {

            Node newNode = new Node();
            newNode.insertElement(newElement);
            root = newNode;
            root.parent = null;
            added = true;
        }

        else {

            if (!lastChecked.full()) {
                lastChecked.insertElement(newElement);

                added = true;
            }

            else {

                lastChecked.insertElement(newElement);

                if (lastChecked.parent == null) {

                    Node newRoot = new Node();
                    int mid = lastChecked.split();

                    newRoot.insertElement(mid);
                    newRoot.left = lastChecked.left;
                    newRoot.right = lastChecked.right;
                    root = newRoot;
                    added = true;
                }
                // Case where the parent is not full

                // maybe something is wrong with parent logic
                // From here to the end of the method is not functional
                else if (!lastChecked.parent.full()) {
                    int mid = lastChecked.split(); // Split the lastChecked node
                    lastChecked.parent.insertElement(mid);

                    added = true;
                } else {

                    // Case where the parent is full

                    recursiveInsertion(newElement);
                    added = true;

                }
            }

        }

        return added;

    }

    // try debugging
    public void recursiveInsertion(int newElement) {
        lastChecked = lastChecked.parent;

        if (!lastChecked.full()) {
            int mid = lastChecked.split(); // Split the lastChecked node
            lastChecked.parent.insertElement(mid); // Promote the middle value to the parent
            added = true;
        }

        else {
            recursiveInsertion(newElement);

        }

    }

    // Works fine with small tree have to check the functioning with bigger trees
    // once I fix the insertion method.
    public int size() {
        return sizeHelper(root);
    }

    private int sizeHelper(Node node) {
        // TODO Auto-generated method stub
        if (node == null) {
            return 0;
        }

        int nodeSize = node.keySize();

        nodeSize = nodeSize + sizeHelper(node.left);
        nodeSize = nodeSize + sizeHelper(node.right);
        nodeSize = nodeSize + sizeHelper(node.middle);

        return nodeSize;

    }

    public int size(int x) {
        Node node = findX(root, x);
        if (node == null) {
            return 0; //
        }
        return sizeHelper(node);
    }

    public Node findX(Node current, int x) {

        while (current != null) {

            if (current.searchElement(x)) {
                return current;
            } else if (current.leftValue() > x) {

                lastChecked = current;
                current = current.left;
            } else if (current.rightValue() < x) {
                lastChecked = current;
                current = current.right;
            } else {
                lastChecked = current;
                current = current.middle;
            }
        }

        return null;
    }

    // get value in the tree at index x
    // implement in order traversal

    public int get(int x) {
        if (x > size()) {
            return -1;
        }

        return getHelper(root, x, 0);
    }

    public int getHelper(Node node, int x, int counts) {

        // base
        if (node == null) {
            return -1;
        }

        // left subtree
        // left
        if (node.left != null) {
            int leftResult = getHelper(node.left, x, counts);
            counts += sizeHelper(node.left);
            if (leftResult != -1) {
                return leftResult;
            }

        }

        // node
        for (int i = 0; i < node.keySize(); i++) {
            if (counts == x) {
                return node.keys.get(i);
            }
            counts++;
        }

        // middle
        if (node.middle != null) {
            int middleResult = getHelper(node.middle, x, counts);
            counts += sizeHelper(node.middle);
            if (middleResult != -1) {
                return middleResult;
            }


        }

        // right

        if (node.right != null) {
            return getHelper(node.right, x, counts);

        }

        return -1;
    }

    public void printTreeStructure() {
        System.out.println("Tree Structure:");
        printTreeStructureHelper(root, 0);
    }

    private void printTreeStructureHelper(Node node, int depth) {
        if (node == null) {
            return; // Base case: if the node is null, return
        }

        String indent = " ".repeat(depth * 4);
        System.out.println(indent + "Node (depth " + depth + "): " + node.keys);

        if (node.left != null) {
            System.out.println(indent + "Left:");
            printTreeStructureHelper(node.left, depth + 1);
        }

        if (node.middle != null) {
            System.out.println(indent + "Middle:");
            printTreeStructureHelper(node.middle, depth + 1);
        }

        if (node.right != null) {
            System.out.println(indent + "Right:");
            printTreeStructureHelper(node.right, depth + 1);
        }
    }

    public class Node {
        public ArrayList<Integer> keys;
        public Node left;
        public Node right;
        public Node middle;
        public Node parent;
        public ArrayList<Integer> children;

        public Node() {
            keys = new ArrayList<>(2);
        }

        public void insertElement(int newElement) {
            keys.add(newElement);
            // Since there are only two to three items in the list using a sort method is
            // probably not the most efficient.
            Collections.sort(keys);
        }

        public boolean searchElement(int newElement) {
            return keys.contains(newElement);
        }

        public int leftValue() {
            return keys.get(0);
        }

        public int rightValue() {
            // something wrong

            if (keys.size() == 2) {
                return keys.get(1);
            } else {
                return keys.get(0);
            }
        }

        public boolean full() {
            return keys.size() >= 2;
        }

        // calling only when node is full;
        // reason my split method not saving previous items in the node after splitting
        public int split() {
            int mid = keys.get(1);
            Node newLeftNode = new Node();
            Node newRightNode = new Node();

            newLeftNode.insertElement(keys.get(0));
            newRightNode.insertElement(keys.get(2));

            newLeftNode.parent = this.parent;
            newRightNode.parent = this.parent;

            // probably wrong logic

            this.left = newLeftNode;
            this.right = newRightNode;

            return mid;
        }

        public int keySize() {
            return keys.size();
        }



    }

    public static void main(String[] args) {
        Tree tree = new Tree();

        // Inserting elements into the tree
        int[] elements = {10, 20, 5,40,8,80};
        for (int element : elements) {
            tree.insert(element);
        }

        // Print the structure of the tree
        tree.printTreeStructure();
    }
//CHANGE THE LOGIC OF LEFT RIGHT MIDDLE INSTEAD DO KEEP THE TRACK OF CHILDREN TO THE SAME 
} 