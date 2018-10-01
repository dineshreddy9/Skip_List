/*
 * @author Dinesh Reddy Thokala
 * SkipList
 * Ver 1.0: 09/30/2018
 */
package dxt161330;

import java.util.Iterator;
import java.util.Random;

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
		random = new Random();
	}

	// Add x to list. If x already exists, reject it. Returns true if new node is added to list
	public boolean add(T x) {
		return true;
	}

	// Find smallest element that is greater or equal to x
	public T ceiling(T x) {
		return null;
	}

	// Does list contain x?
	public boolean contains(T x) {
		return false;
	}

	// Return first element of list
	public T first() {
		return null;
	}

	// Find largest element that is less than or equal to x
	public T floor(T x) {
		return null;
	}

	// Return element at index n of list.  First element is at index 0.
	public T get(int n) {
		return null;
	}

	// O(n) algorithm for get(n)
	public T getLinear(int n) {
		return null;
	}

	// Optional operation: Eligible for EC.
	// O(log n) expected time for get(n). Requires maintenance of spans, as discussed in class.
	public T getLog(int n) {
		return null;
	}

	// Is the list empty?
	public boolean isEmpty() {
		return false;
	}

	// Iterate through the elements of list in sorted order
	public Iterator<T> iterator() {
		return null;
	}

	// Return last element of list
	public T last() {
		return null;
	}

	// Optional operation: Reorganize the elements of the list into a perfect skip list
	// Not a standard operation in skip lists. Eligible for EC.
	public void rebuild() {

	}

	// Remove x from list.  Removed element is returned. Return null if x not in list
	public T remove(T x) {
		return null;
	}

	// Return the number of elements in the list
	public int size() {
		return 0;
	}
}
