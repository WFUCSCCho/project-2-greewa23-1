/**********************************************************************
 * @file AvlTree.java
 * @brief This program implements the AvlTree and AvlNode classes. The
 * class contains various operations that can be performed on an AVL Tree.
 * @author Wynne Greene
 * @date: October 24, 2024
 ***********************************************************************/

// AvlTree class
//
// CONSTRUCTION: with no initializer
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// void remove( x )       --> Remove x (unimplemented)
// boolean contains( x )  --> Return true if x is present
// boolean remove( x )    --> Return true if x was present
// Comparable findMin( )  --> Return smallest item
// Comparable findMax( )  --> Return largest item
// boolean isEmpty( )     --> Return true if empty; else false
// void makeEmpty( )      --> Remove all items
// void printTree( )      --> Print tree in sorted order
// ******************ERRORS********************************
// Throws UnderflowException as appropriate

import java.util.Stack;

/**
 * Implements an AVL tree.
 * Note that all "matching" is based on the compareTo method.
 */
public class AvlTree<AnyType extends Comparable<? super AnyType>> {

    /**
     * Construct the tree.
     */
    public AvlTree() {
        //this = null;
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     *
     * @param x the item to insert.
     */
    public void insert(AnyType x) {
        root = insert(x, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     *
     * @param x the item to remove.
     */
    public void remove(AnyType x) {
        root = remove(x, root);
    }


    /**
     * Internal method to remove from a subtree.
     *
     * @param x the item to remove.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> remove(AnyType x, AvlNode<AnyType> t) {
        // FINISH ME
        if (t == null) {
            return null;
        }
        //Compare the key with the current node. If its value is less than the current
        //Node, it should be in the node's left subtree. Otherwise, it goes in the right.
        if (t.element.compareTo(x) > 0) {
            t.left = remove(x, t.left);
        } else if (t.element.compareTo(x) < 0) {
            t.right = remove(x, t.right);
        } else {
            //After finding the key, remove it.
            //Check for a leaf node.
            if (t.left == null && t.right == null) {
                return null;
            }
            //Check for if the node has one child.
            if (t.left == null)
                return t.right;
            else if (t.right == null)
                return t.left;
            else {
                //The node has two children. Replace the node's data with
                //the largest data in the left subtree and delete the node.
                AvlNode<AnyType> temp = findMax(t.left);
                t.element = temp.element;
                t.left = remove(t.element, t.left);
            }
        }
        return t;
    }

    /**
     * Find the smallest item in the tree.
     *
     * @return smallest item or null if empty.
     */
    public AnyType findMin() {
        if (isEmpty())
            throw new UnderflowException();
        return findMin(root).element;
    }

    /**
     * Find the largest item in the tree.
     *
     * @return the largest item of null if empty.
     */
    public AnyType findMax() {
        if (isEmpty())
            throw new UnderflowException();
        return findMax(root).element;
    }

    /**
     * Find an item in the tree.
     *
     * @param x the item to search for.
     * @return true if x is found.
     */
    public boolean contains(AnyType x) {
        return contains(x, root);
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree() {
        if (isEmpty())
            System.out.println("Empty tree");
        else
            printTree(root);
    }

    private static final int ALLOWED_IMBALANCE = 1;

    // Assume t is either balanced or within one of being balanced
    private AvlNode<AnyType> balance(AvlNode<AnyType> t) {
        // FINISH ME
        if (t == null) {
            return t;
        }

        //We check if the height of the left and right subtrees of the node differ by
        //more than 1.

        //Check if the height of the left or right subtree is greater than the other subtree
        //by more than one.
        if (height(t.left) - height(t.right) > ALLOWED_IMBALANCE) {
            //Check whether to make a single or double rotation based on which
            //subtree is larger.
            if (height(t.left.left) >= height(t.left.right)) {
                t = rotateWithLeftChild(t);
            } else {
                t = doubleWithLeftChild(t);
            }
        } else if (height(t.right) - height(t.left) > ALLOWED_IMBALANCE) {
            //Check whether to make a single or double rotation based on which
            //subtree is larger.
            if (height(t.right.right) >= height(t.right.left)) {
                t = rotateWithRightChild(t);
            } else {
                t = doubleWithRightChild(t);
            }
        }
        //Update the height of the tree after the tree is balanced.
        t.height = Math.max(height(t.left), height(t.right)) + 1;
        return t;
    }

    public void checkBalance() {
        checkBalance(root);
    }

    private int checkBalance(AvlNode<AnyType> t) {
        if (t == null)
            return -1;

        if (t != null) {
            int hl = checkBalance(t.left);
            int hr = checkBalance(t.right);
            if (Math.abs(height(t.left) - height(t.right)) > 1 ||
                    height(t.left) != hl || height(t.right) != hr)
                System.out.println("OOPS!!");
        }

        return height(t);
    }


    /**
     * Internal method to insert into a subtree.
     *
     * @param x the item to insert.
     * @param t the node that roots the subtree.
     * @return the new root of the subtree.
     */
    private AvlNode<AnyType> insert(AnyType x, AvlNode<AnyType> t) {
        // FINISH ME
        //Check if the node is null.
        if (t == null) {
            return new AvlNode<AnyType>(x);
        }
        //Calculate the difference between x and the current node.
        int compareResult = x.compareTo(t.element);

        //Compare the node's value to the key then
        //Update the left or right nodes. Unless they are equal.
        //Then we ignore duplicates.
        if (compareResult < 0) {
            t.left = insert(x, t.left);
        } else if (compareResult > 0) {
            t.right = insert(x, t.right);
        } else {
            ;
        }
        return balance(t);
    }

    /**
     * Internal method to find the smallest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode<AnyType> findMin(AvlNode<AnyType> t) {
        // FINISH ME
        //Keep traversing the left subtree until we find the leftmost node.
        while (t.left != null) {
            t = t.left;
        }
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     *
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode<AnyType> findMax(AvlNode<AnyType> t) {
        // FINISH ME
        //Keep traversing the right subtree until we find the rightmost node.
        while (t.right != null) {
            t = t.right;
        }
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     *
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return true if x is found in subtree.
     */
    private boolean contains(AnyType x, AvlNode<AnyType> t) {
        // FINISH ME
        //Check if node is null.
        if (t == null) {
            return false;
        }
        //If the key is less than the value of the current node, search
        //the left subtree. Otherwise, if the key is not equal to the node, the right
        //subtree is searched.
        if (t.element.compareTo(x) > 0) {
            return contains(x, t.left);
        } else if (t.element.compareTo(x) == 0) {
            //check if the objects are equal, if not continue
            return true;
        } else
            return contains(x, t.right);
    }

    /**
     * Internal method to print a subtree in (sorted) order.
     *
     * @param t the node that roots the tree.
     */
    private void printTree(AvlNode<AnyType> t) {
        // FINISH ME
        //Check if the node is null
        if(t==null)
            return;
        //Visit the node's left node, then visit the node,
        //and then visit its right node. This is inorder traversal.
        printTree(t.left);
        System.out.println(t.element);
        printTree(t.right);
    }

    /**
     * Return the height of node t, or -1, if null.
     */
    private int height(AvlNode<AnyType> t) {
        return t == null ? -1 : t.height;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithLeftChild(AvlNode<AnyType> k2) {
        // FINISH ME
        //k1 is the current node's left node. Set the left node of k2 to
        //the right node of k1. Then set k1's right node to k2.
        AvlNode<AnyType> k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        //The height of k2 is the max of the height of its left and right subtrees + 1.
        k2.height = Math.max(height(k2.left), height(k2.right)) + 1;
        //The height of k1 is the max of the height of its left subtree and k2's height + 1.
        k1.height = Math.max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> rotateWithRightChild(AvlNode<AnyType> k1) {
        // FINISH ME
        //k2 is the current node's left node. Set the right node of k1 to
        //the left node of k2. Then set k2's left node to k1.
        AvlNode<AnyType> k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        //The height of k1 is the max of the height of its right and left subtrees + 1.
        k1.height = Math.max(height(k1.right), height(k1.left)) + 1;
        //The height of k2 is the max of the height of its right subtree and k1's height + 1.
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithLeftChild(AvlNode<AnyType> k3) {
        // FINISH ME
        //First rotate between k3's child and grandchild, then a rotation
        //between k3 and its new child, formerly its grandchild.
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private AvlNode<AnyType> doubleWithRightChild(AvlNode<AnyType> k1) {
        // FINISH ME
        //First rotate between k1's child and grandchild, then a rotation
        //between k1 and its new child, formerly its grandchild.
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }


    private static class AvlNode<AnyType> {
        // Constructors
        AvlNode(AnyType theElement) {
            this(theElement, null, null);
        }

        AvlNode(AnyType theElement, AvlNode<AnyType> lt, AvlNode<AnyType> rt) {
            element = theElement;
            left = lt;
            right = rt;
            height = 0;
        }

        AnyType element;      // The data in the node
        AvlNode<AnyType> left;         // Left child
        AvlNode<AnyType> right;        // Right child
        int height;       // Height
    }

    /**
     * The tree root.
     */
    private AvlNode<AnyType> root;
}

