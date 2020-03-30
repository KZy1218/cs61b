import java.awt.*;
import java.awt.dnd.DnDConstants;
import java.util.List;

/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Vivant Sakore (1/29/2020)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }



    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }



    /**
     * @return The number of elements in this list.
     */
    public int size() {
        int size = 0;
        DNode help = _front;
        while(help != null) {
            size += 1;
            help = help._next;
        }
        return size;
    }

    /**
     * @param i index of element to return,
     *          where i = 0 returns the first element,
     *          i = 1 returns the second element,
     *          i = -1 returns the last element,
     *          i = -2 returns the second to last element, and so on.
     *          You can assume i will always be a valid index, i.e 0 <= i < size for positive indices
     *          and -size <= i <= -1 for negative indices.
     * @return The integer value at index i
     */
    public int get(int i) {
        boolean input_is_nonne = (i >= 0);
        DNode help1 = _front;
        DNode help2 = _back;
        while ((i > 0) || (i < -1)) {
            if (i > 0) {
                help1 = help1._next;
                i -= 1;
            }
            if (i < 0) {
                help2 = help2._prev;
                i += 1;
            }
        }
        if (input_is_nonne) {
            return help1._val;
        }
        else {
            return help2._val;
        }
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        if (size() == 0) {
            _front = _back = new DNode(null, d, null);
        }
        else{
            DNode first = new DNode(null, d, _front);
            _front._prev = first;
            _front = first;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        DNode last = new DNode(_back, d, null);
        if (_back == null){
            _front = _back = last;
        }
        else {
            _back._next = last;
            _back = last;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position,
     *              index = -1 inserts at the back,
     *              index = -2 inserts at the second to last position, and so on.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size for positive indices (including insertions at front and back)
     *              and -(size+1) <= index <= -1 for negative indices (including insertions at front and back).
     */
    public void insertAtIndex(int d, int index) {
        if ((index == 0) || (index == -size()-1)) {
            insertFront(d);
        }

        else if ((index == -1) || (index == size())) {
            insertBack(d);
        }

        else {
            boolean input_is_nonne = (index >= 0);
            DNode help1 = _front;
            DNode help2 = _back;
            while ((index > 0) || (index < -1)) {
                if (index > 0) {
                    help1 = help1._next;
                    index -= 1;
                }
                if (index < 0) {
                    help2 = help2._prev;
                    index += 1;
                }
            }

            if (input_is_nonne) {
                DNode insert = new DNode(help1._prev, d, help1);
                help1._prev._next = insert;
                help1._prev = insert;
            } else {
                DNode insert = new DNode(help2, d, help2._next);
                help2._next._prev = insert;
                help2._next = insert;
            }
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        int value = _front._val;
        _front = _front._next;
        if (_front != null) {
            _front._prev = null;
        }
        else {
            _back = null;
        }
        return value;
    }

    /**
     * Removes the last item in the IntDList and returns it.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        int value = _back._val;
        _back = _back._prev;
        if (_back != null) {
            _back._next = null;
        }
        else {
            _front = null;
        }
        return value;
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element,
     *          index = -1 will delete the last element,
     *          index = -2 will delete the second to last element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size for positive indices (including deletions at front and back)
     *              and -size <= index <= -1 for negative indices (including deletions at front and back).
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        int value = get(index);
        if ((index == 0) || (index == -size())) {
            deleteFront();
        }
        else if ((index == -1) || (index == size()-1)) {
            deleteBack();
        }
        else {
            boolean input_is_nonne = (index >= 0);
            DNode help1 = _front;
            DNode help2 = _back;
            while ((index > 0) || (index < -1)) {
                if (index > 0) {
                    help1 = help1._next;
                    index -= 1;
                }
                if (index < 0) {
                    help2 = help2._prev;
                    index += 1;
                }
            }

            if (input_is_nonne) {
                help1._prev._next = help1._next;
                help1._next._prev = help1._prev;
            } else {
                help2._prev._next = help2._next;
                help2._next._prev = help2._prev;
            }
        }

        return value;
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    static class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

}
