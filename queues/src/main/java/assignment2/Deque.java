/* vi: set ts=4 sw=4 expandtab: */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdStats;


public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int count;

    /**
     * Nested class Node is an element contained inside a doublely-linked list.
     * The supplied item is wrapped with two pointers representing the next
     * and previous elements in the sequence.
     */  
    private class Node
    {
        Item item;
        Node next;
        Node prev;
    }

    /**
     * Constructor for a Deque, a doublely linked-list with a sentinal to a head 
     * and tail pointer.
     */
    public Deque()
    {
        first = null;
        last = null;
        count = 0;
    }

    /**
     * Checks if the deque has any items.
     * @return does the deque contain zero elements
     */
    public boolean isEmpty()
    {
        return (first == last && first == null);
    }

    public int size()
    {
        return count;
    }

    /**
     * Prepends an Item object to the deque
     * @param   Item    object to prepend
     */
    public void addFirst(Item item)
    {
        if(item == null)
            throw new java.lang.NullPointerException("Attempted to add a null reference.");
        Node x = new Node();
        x.item = item;
        count++;

        if(first == null)
        {
            assert(last == null);
            first = x;
            last = x;
            x.prev = null;
            x.next = null;
            return;
        }
        x.next = first;
        first.prev = x;
        first = x;

    }

    /**
     * Appends an Item object to the deque
     * @param   Item    object to append
     */
    public void addLast(Item item)
    {
        if(item == null)
            throw new java.lang.NullPointerException("Attempted to add a null reference.");
        Node x = new Node();
        x.item = item;
        count++;

        if(last == null)
        {
            assert(first == null);
            first = x;
            last = x;
            x.prev = null;
            x.next = null;
            return;
        }
        x.prev = last;
        last.next = x;
        last = x;
    }

    /**
     * Removes and returns the first element of the deque
     * @return first element object in the deque
     * @throws NoSuchElementException if deque is empty
     */
    public Item removeFirst()
    {
        if(isEmpty())
            throw new java.util.NoSuchElementException("Attempted to remove on empty list.");
        Node x = first;

        --count;
        if(x.next == x.prev && x.next == null)
        {
            assert last == x;
            first = null;
            last = null;
            return x.item;
        }

        x.next.prev= null;
        x.prev = null;
        first = x.next;
        x.next = null;
        return x.item;
    }

    /**
     * Removes and returns the last element of the deque.
     * @return last element object in the deque
     * @throws NoSuchElementException if deque is empty
     */
    public Item removeLast()
    {
        if(isEmpty())
            throw new java.util.NoSuchElementException("Attempted to remove on empty list.");

        Node x = last;

        --count;
        if(x.next == x.prev && x.prev == null)
        {
            assert first == x;
            first = null;
            last = null;
            return x.item;
        }

        x.prev.next = null;
        x.next = null;
        last = x.prev;
        x.prev = null;
        return x.item;
    }



    /**
     * Nested class DequeIterator implements the interface java.util.Iterator
     * Contains a Node reference that is initialized to first in the parent
     * class, the Iterator traverses the doublely-linked list forward, returning
     * the elements of the deque in order, from first to last.
     */
    private class DequeIterator implements Iterator<Item>
    {
        private Node x = first;

        public boolean hasNext()
        {
            return x != null;
        }

        public Item next()
        {
            if( x == null)
                throw new NoSuchElementException("Iterator has to more elements to traverse.");

            Item current = x.item;
            x = x.next;
            return current;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("DequeIterator doesn't support remove operation.");
        }
    }

    /**
     * Implemention of java.util.Iterable interface.
     * Returns an iterator over the deque's elements.
     * @return iterator at the beginning of the deque
     */
    public Iterator<Item> iterator()
    {
        return new DequeIterator();
    }


   
 
    /**
     * Unit tests
     */
    public static void main(String[] args)
    {
        Deque<Integer> i_deque = new Deque<Integer>();
        int v;
        assert i_deque.isEmpty();
        i_deque.addFirst(4);
        assert !i_deque.isEmpty();
        v = i_deque.removeFirst();
        assert v == 4;
        assert i_deque.isEmpty();
        i_deque.addFirst(5);
        assert !i_deque.isEmpty();
        v = i_deque.removeLast();
        assert v == 5;
        assert i_deque.isEmpty();

        i_deque.addFirst(10);
        i_deque.addFirst(100);
        assert i_deque.size() == 2;
        v = i_deque.removeLast();
        assert v == 10;
        v = i_deque.removeLast();
        assert v == 100;
        assert i_deque.isEmpty();


        i_deque.addFirst(100);
        i_deque.addFirst(10);
        i_deque.addLast(1000);
        assert i_deque.size() == 3;
        v = i_deque.removeLast();
        assert v == 1000;
        v = i_deque.removeLast();
        assert v == 100;
        v = i_deque.removeLast();
        assert v == 10;
        assert i_deque.isEmpty();

        i_deque.addFirst(100);
        i_deque.addFirst(10);
        i_deque.addLast(1000);
        v = i_deque.removeFirst();
        assert v == 10;
        v = i_deque.removeFirst();
        assert v == 100;
        v = i_deque.removeFirst();
        assert v == 1000;
        assert i_deque.isEmpty();

        i_deque.addLast(10);
        i_deque.addFirst(100);
        v = i_deque.removeLast();
        assert v == 10;
        v = i_deque.removeLast();
        assert v == 100;
        assert i_deque.isEmpty();

        i_deque.addLast(10);
        i_deque.addFirst(100);
        v = i_deque.removeFirst();
        assert v == 100;
        v = i_deque.removeFirst();
        assert v == 10;
        assert i_deque.isEmpty();

        try {
            i_deque.addFirst(null);
        } catch(Exception e)
        {
        }

        assert i_deque.isEmpty();

        for(int i = 10;i > 0;--i)
        {
            i_deque.addLast(i);
        }

        int[] testArr = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        Iterator<Integer> it = i_deque.iterator();

        for(int i = 0; i < 10;++i)
        {
            assert it.hasNext();
            assert testArr[i] == it.next();
        }

        int s = 10;
        for(int val : i_deque)
        {
            assert val == s--;
        }
      

    }
}
