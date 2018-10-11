/*
 * @author Dinesh Reddy Thokala
 * SkipList
 * Ver 1.0: 09/30/2018
 */
package dxt161330;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class SkipList<T extends Comparable<? super T>> {
	static final int PossibleLevels = 33;
	Entry<T> head, tail; // dummy nodes
	int size, maxLevel;
	Entry<T>[] last;
	Random random;

	static class Entry<E> {
		E element;
		Entry<E>[] next;
		Entry<E> prev;
		int[] span; // for indexing

		//constructor
		public Entry(E x, int lev) {
			element = x;
			next = new Entry[lev];
			span = new int[lev];
		}

		public E getElement() {
			return element;
		}
	}

	// Constructor
	public SkipList() {
		head = new Entry<>(null, PossibleLevels);
		tail = new Entry<>(null, PossibleLevels);
		size = 0;
		maxLevel = 1;
		last = new Entry[PossibleLevels];
		for(int i=(PossibleLevels-1); i>=0; i--) {
			last[i] = head;
			last[i].next[i] = tail;
		}
		random = new Random();
	}
	
	// helper method to search for x
	// sets last[i] = node at which search came down from level i to i-1
	public void find(T x) {
		Entry<T> p = head;
		for(int i=(maxLevel-1); i>=0; i--) {
			while(p.next[i]!=null && (p.next[i].element!=null)&&(p.next[i].element.compareTo(x))==-1) {
				//System.out.println("find"+x);
				p = p.next[i];
			}
			last[i] = p;
		}
	}

	// Add x to list. If x already exists, reject it. Returns true if new node is added to list
	public boolean add(T x) {
		if(contains(x)) {
			return false; 
		}
		int level = chooseLevel();
		Entry<T> newEntry = new Entry<T>(x, level);
		for(int i=0; i<level; i++) {
			newEntry.next[i] = last[i].next[i];
			last[i].next[i] = newEntry;
		}
		newEntry.next[0].prev = newEntry;
		newEntry.prev = last[0];
		size = size + 1;
		return true;
	}

	public int chooseLevel() {
		int level = 1 + Integer.numberOfTrailingZeros(random.nextInt());
		if(level > maxLevel) {
			maxLevel = level;
		}
		return level;
	}

	// Find smallest element that is greater or equal to x
	public T ceiling(T x) {
		find(x);
		if(!last[0].next[0].equals(tail))
			return last[0].next[0].element;
		return null;
	}

	// Does list contain x?
	public boolean contains(T x) {
		if(isEmpty()) {
			return false;
		}
		find(x);
		return (last[0].next[0].element == x);
	}

	// Return first element of list
	public T first() {
		if(!head.next[0].equals(tail))
			return head.next[0].element;
		return null;
	}

	// Find largest element that is less than or equal to x
	public T floor(T x) {
		find(x);
		if (last[0].next[0] != null && last[0].next[0].element != null && last[0].next[0].element.equals(x))
			return x;
		else
			return last[0].element;	
	}

	// Return element at index n of list.  First element is at index 0.
	public T get(int n) {
		if(n<0 || n>(size-1)) {
			throw new NoSuchElementException();
		}
		else {
			return getLinear(n);
		}
	}

	// O(n) algorithm for get(n)
	public T getLinear(int n) {
		Entry<T> p = head;
		for(int i=0; i<=n; i++) {
			p = p.next[0];
		}
		System.out.println(p.element);
		return p.element;
	}

	// Optional operation: Eligible for EC.
	// O(log n) expected time for get(n). Requires maintenance of spans, as discussed in class.
	public T getLog(int n) {
		return null;
	}

	// Is the list empty?
	public boolean isEmpty() {
		return (size == 0);
	}

	// Iterate through the elements of list in sorted order
	public Iterator<T> iterator() {
		return new SLIterator<T>(this);
	}
	private class SLIterator<E extends Comparable<? super E>> implements Iterator<E> {
		SkipList<E> list;
		Entry<E> cursor, prev;
		boolean ready;

		SLIterator(SkipList<E> list) {
			this.list = list;
			cursor = (Entry<E>) list.head;
			prev = null;
			ready = false;
		}

		public boolean hasNext() {
			return !cursor.next[0].equals(tail);
		}

		public E next() {
			if (hasNext()) {

				prev = cursor;
				cursor = cursor.next[0];
				ready = true;
				return cursor.element;
			}
			return null;
		}
		public void remove() {
			if (!ready) {
				throw new NoSuchElementException();
			}
			list.remove(cursor.element);
			cursor = prev;
			ready = false;

		}
	}


	// Return last element of list
	public T last() {
		if(!tail.prev.equals(head))
			return tail.prev.element;
		return null;
	}

	// Optional operation: Reorganize the elements of the list into a perfect skip list
	// Not a standard operation in skip lists. Eligible for EC.
	public void rebuild() {

	}

	// Remove x from list.  Removed element is returned. Return null if x not in list
	public T remove(T x) {
		if(!contains(x)) {
			return null;
		}
		Entry<T> removeEntry = last[0].next[0];
		for(int i=0; i<removeEntry.next.length; i++) {
			last[i].next[i] = removeEntry.next[i];
		}
		size = size-1;
		return removeEntry.element;
	}

	// Return the number of elements in the list
	public int size() {
		return size;
	}
	
	public void printList() {
		Entry<T> temp = head;
		for(int i=0; i<size; i++) {
			System.out.print(temp.next[0].element + " ");
			temp = temp.next[0];
		}
		System.out.println();
	}
	
	// to test the program
	public static void main(String[] args) {
		SkipList<Integer> sk = new SkipList<>();
		Scanner sc = new Scanner(System.in);
		for(int i=1;i<13;i=i+2) {
			sk.add(i);
		}
		sk.printList();
		System.out.println(sk.remove(9));
		sk.printList();
	}
}
