/* vi: set ts=4 sw=4 expandtab: */
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;
import java.lang.IllegalArgumentException;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdStats;

/*
 * A randomized queue is a datastructure that supports the operation to iterate over all
 * elements in a randomized order.
 *
 * The underlying datastructure is a resizing array.  Elements of the array are reserved for the queue or null.
 * When attempting to insert a new element into the array that causes the array to be full, the array
 * is resized to double its length, and the old elements are copied into the new array.
 *
 * Conversely, when an array has removed elements such that the size of the array ( number of elements 
 * in the queue ) is half of the number of elements allocated for the array ( the length ), then the
 * length is reduced to by half.
 *
 *
 * performance:
 * Indexing:    O(1)
 * Insertion:   O(1) amortized.  O(n) worst
 * Deletion: O(1) amortized.
 *
 */
public class RandomizedQueue<Item> implements Iterable<Item>
{
    private Item[] array;               // Queue storage
    private int array_len = 10;         // Size of array allocated
    private int array_size = 0;         // Number of elements in the queue
    private int array_offs = 0;         // Offset from start of array containing the first queue element

    /**
     * Construct an empty randomized queue
     */
    public RandomizedQueue()
    {

        @SuppressWarnings("unchecked")
        Item[] new_array = (Item[]) new Object[array_len];
        array = new_array;
    }

    /**
     * Resizes array to new length, and copies elements from old array
     * into new array.  Updates array_len, and resets array_offs to zero.
     *
     * @param newlen new value for array length to resize to
     */
    private void resizeArray(int newlen)
    {
        int elements_count = size();
        assert elements_count <= newlen;

        @SuppressWarnings("unchecked")
        Item[] new_array = (Item[]) new Object[newlen];

        for(int i = 0;i < elements_count;++i)
        {
            int array_index = (array_offs + i)%array_len;
            new_array[i] = 
                array[array_index];
        }

        array = new_array;
        array_offs = 0;
        array_len = newlen;
        array_size = elements_count;
    }

    /**
     * @return true if there are zero elements in the queue
     */
    public boolean isEmpty()
    {
        return array_size == array_offs;
    }

    /**
     * @return number of items in the queue
     */
    public int size()
    {
        if(array_offs <= array_size)
            return array_size - array_offs;

        return (array_len - array_offs) + array_size;
    }

    /**
     * @return true if there array is full of elements.  Used by
     * enqueue() to check if array resizing is needed.
     */
    private boolean isFull()
    {
        return ((array_size + 1)%array_len == array_offs);
    }

    /**
     * Add an Item to the queue
     * @param item Item object to add
     */
    public void enqueue(Item item)
    {
        if(item == null)
            throw new java.lang.NullPointerException("Attempted to enqueue a null reference.");
 
        if(isFull())
        {
            resizeArray(array_len*2);
        }

        int index = array_size % array_len;
        array[index] = item;
        array_size = (array_size + 1)% array_len;
    }

    /**
     * helper function to swap elements indexed in array a, at locations i and j.
     *
     * @param a Array of Item
     * @param i valid index inside a
     * @param j valid index inside a
     */
    private static <Item> void swap(Item[] a, int i, int j)
    {
        Item t = a[i];
        a[i] = a[j];
        a[j] = t;
    }


    /**
     * Remove and return a random item from the queue
     * @return item Item from a random location inside the queue
     * A random index is chosen.  The value at that location is swapped with the 
     * location at the end of the array.  Then the element at the end of the array
     * is removed from the queue, and it's value is returned.
     */
    public Item dequeue()
    {
        int size;
        if((size = size()) == 0)
            throw new java.util.NoSuchElementException("Attempted to dequeue from an empty queue.");


        /**
         * Take a random number from 0 to num of elements.
         * Add the offset into the array our current first dequeue element resides
         * swap the first element with the randomized index, 
         * and dequeue the first element.
         */
        int rnd_index = (array_offs + StdRandom.uniform(size))%array_len;
        swap(array,array_offs,rnd_index);

        int index = array_offs;
        Item ret = array[index];
        array_offs = (array_offs + 1)%array_len;
        array[index] = null;

        if(size() < array_len/2)
            resizeArray(array_len/2);

        return ret;
     }

    /**
     * return (but do not remove) a random item.
     * @return item a random item from the queue.
     */
    public Item sample()
    {
        int size;
        if((size = size()) == 0)
            throw new java.util.NoSuchElementException("Attempting to sample from an empty queue.");

        int index_off = StdRandom.uniform(0,size);
        int index = (array_offs + index_off)%array_len;
        return array[index];
    }

    /**
     * @return an independent iterator over items in random order
     */
    public Iterator<Item> iterator()
    {
        return new RandomizedQueueIterator();
    }

    /**
     * Nested class RandomizedQueueIterator implements the java.util.Iterator
     * Constructor initializes an Array of index locations to retreive from
     * the RandomizedQueue, and then calls StdRandom.shuffle() to randomize
     * the locations.
     */
    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int arr[];
        private int arr_size;
        private int arr_it;

        RandomizedQueueIterator()
        {
            arr_size = size();
            arr = new int[arr_size];

            for(int i = 0;i < arr_size;i++)
                arr[i] = i;

            StdRandom.shuffle(arr);
            arr_it = 0;
        }

        public boolean hasNext()
        {
            return arr_it < arr_size;
        }

        public Item next()
        {
            if(!hasNext())
                throw new NoSuchElementException("Iterator has to more elements to traverse.");
            
            int index = arr[arr_it++];
            index = (index + array_offs)%array_len;
            Item ret = array[index];
            return ret;
        }
 
        public void remove()
        {
            throw new UnsupportedOperationException("DequeIterator doesn't support remove operation.");
        }

    }


    /**
     * Unit tests
     */
    public static void main(String[] args)
    {

        {
            String v;
            RandomizedQueue<String> q = new RandomizedQueue<String>();
            q.enqueue("first");
            q.enqueue("first");
            q.enqueue("first");
            assert q.size() == 3;
            v = q.dequeue();
            assert v == "first";
            assert q.size() == 2;
            assert !q.isEmpty();
            v = q.dequeue();
            assert v == "first";
            assert q.size() == 1;
            assert !q.isEmpty();
            v = q.dequeue();
            assert v == "first";
            assert q.size() == 0;
            assert q.isEmpty();
    
            int s = 0;
            {
                for(String v2 : q)
                {
                    s++;
                }
            }
            assert s == q.size();
        }

        {
            String v;
            RandomizedQueue<String> q = new RandomizedQueue<String>();
            q.resizeArray(1);
            q.enqueue("first");
            q.enqueue("second");
            q.enqueue("third");
            assert q.size() == 3;
            v = q.dequeue();
            assert q.size() == 2;
            assert !q.isEmpty();
            v = q.dequeue();
            assert q.size() == 1;
            assert !q.isEmpty();
            v = q.dequeue();
            assert q.size() == 0;
            assert q.isEmpty();
        }
        {
            Integer v;
            RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
            q.resizeArray(4);
            q.enqueue(1);
            q.enqueue(2);
            q.enqueue(3);
            q.dequeue();
            q.dequeue();
            q.enqueue(4);
            q.enqueue(5);
 
  //          assert q.array_offs > q.array_size;
            assert q.size() == 3;

            q.resizeArray(8);
 //           assert q.array_offs < q.array_size;
            assert q.size() == 3;


            v = q.dequeue();
            v = q.dequeue();
            v = q.dequeue();
        }
        {
            int SIZE = 100;
            int[] check_values = new int[SIZE];
            int[] queue_values = new int[SIZE];
            int[] dequeue_values = new int[SIZE];

            for(int i = 0;i < SIZE;++i)
            {
                dequeue_values[i] = 0;
                check_values[i] = 0;
                queue_values[i] = StdRandom.uniform(SIZE);
            }

            RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();

            for(int val : queue_values)
            {
                q.enqueue(val);
            }

            assert q.size() == SIZE;

            {
                int i = 0;
                for(int q_value : q)
                {
                    check_values[i++] = q_value;
                }
            }

            assert check_values.length == SIZE;
            assert queue_values.length == SIZE;
           
            java.util.Arrays.sort(check_values);
            java.util.Arrays.sort(queue_values);

            assert java.util.Arrays.equals(check_values,queue_values);


        }
    

        {
            RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
            boolean caught = false;
            try {
                q.dequeue();
            } catch(NoSuchElementException e) {
                caught = true;
            } catch(Exception e) {

            }
            assert caught;
        }

        {
            RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
            boolean caught = false;
            try {
                q.sample();
            } catch(NoSuchElementException e) {
                caught = true;
            } catch(Exception e) {

            }
            assert caught;
        }

    }
}
