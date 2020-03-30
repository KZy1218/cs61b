import java.awt.color.ICC_ProfileRGB;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author Yi Zhang
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        Node target = track(s);
        if (target == null) {
            _root = new Node(s);
        } else {
            int c = target.s.compareTo(s);
            if (c > 0) {
                target.left = new Node(s);
            }
            if (c < 0) {
                target.right = new Node(s);
            }
        }
    }

    /** Find the node and return it if the BST already contains string S,
     *  otherwise return the node that should be the parent of Node with string S. */
    private Node track(String str) {
        Node res = _root;
        if (res == null) {
            return null;
        }
        while (true) {
            int c = res.s.compareTo(str);
            if (c == 0) {
                return res;
            } else if (c > 0) {
                if (res.left == null) {
                    return res;
                }
                res = res.left;
            } else {
                if (res.right == null) {
                    return res;
                }
                res = res.right;
            }
        }
    }

    @Override
    public boolean contains(String s) {
        return (track(s) != null) && (track(s).s.equals(s));
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (String s : this) {
            result.add(s);
        }
        return result;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    /** An iterator over BSTs within certain range. */
    private static class inOrderIterator implements Iterator<String> {
        private Stack<Node> _toDo = new Stack<>();
        private String _low; private String _high;

        inOrderIterator(Node n, String low, String high) {
            _low = low;
            _high = high;
            addTree(n);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void addTree(Node node) {
            while (node != null) {
                int cu = node.s.compareTo(_high);
                int cl = node.s.compareTo(_low);
                if ((cu < 0) && (cl >= 0)) {
                    _toDo.push(node);
                    node = node.left;
                } else if (cu >= 0) {
                    node = node.left;
                } else {
                    node = node.right;
                }
            }
        }
    }

    @Override
    public Iterator<String> iterator(String L, String H) {
        return new inOrderIterator(_root, L, H);
    }

    /** Root node of the tree. */
    private Node _root;
}
