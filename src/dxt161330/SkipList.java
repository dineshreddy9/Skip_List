/*
 * @author 
 * Dinesh Reddy Thokala
 * Anirudh Erabelly
 * Sai Krishna Reddy
 * Sreyas Reddy
 * 
 * SkipList
 * VER 1.0: 09/30/2018
 * SkipList is Generalization of sorted linked lists for implementing Dictionary ADT (insert, delete, find, min, succ) 
 * in O(log n) expected time per operation.
 */
package dxt161330;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class SkipList<T extends Comparable<? super T>> {
	static final int PossibleLevels = 33;
	Entry<T> head, tail, retemp; // dummy nodes
	int size, maxLevel;
	Entry<T>[] last;
	int[] lastIndex; //used to store the index of the elements
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
		tail.prev = head;
		size = 0;
		maxLevel = 1;
		last = new Entry[PossibleLevels];
		lastIndex = new int[PossibleLevels];
		for(int i=(PossibleLevels-1); i>=0; i--) {
			last[i] = head;
			last[i].next[i] = tail;
			last[i].span[i] = 1;
		}
		random = new Random();
	}

	// helper method to search for x
	// sets last[i] = node at which search came down from level i to i-1
	public void find(T x) {
		Entry<T> p = head;
		int indexSum = 0;
		for(int i=(maxLevel-1); i>=0; i--) {
			while(p.next[i] != null && p.next[i].element != null && p.next[i].element.compareTo(x) == -1) {
				indexSum += p.span[i];
				p = p.next[i];
			}
			last[i] = p;
			lastIndex[i] = indexSum;
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
			int temp = last[i].span[i];
			last[i].span[i] = lastIndex[0]+1-lastIndex[i];
			newEntry.span[i] = temp+1-last[i].span[i];
			
		}
		for(int i=level; i < PossibleLevels; i++) {
			last[i].span[i] += 1;
		}
		newEntry.next[0].prev = newEntry;
		newEntry.prev = last[0];
		size = size + 1;
		return true;
	}
	
	// Generates a random level
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
		if(last[0].next[0].element!=null && last[0].next[0].element.equals(x)) return true;
		return false;
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
			return getLog(n);
		}
	}

	// O(n) algorithm for get(n)
	public T getLinear(int n) {
		Entry<T> p = head;
		for(int i=0; i<=n; i++) {
			p = p.next[0];
		}
		return p.element;
	}

	// O(log n) expected time for get(n).
	public T getLog(int n) {
		Entry<T> p = head;
		n = n+1;
		for(int i = maxLevel - 1; i >= 0; i--) {
			while(n >= p.span[i]) {
				n = n - p.span[i];
				p = p.next[i];
			}
		}
		return p.element;
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

	// Reorganize the elements of the list into a perfect skip list
	public void rebuild() {
		
		Entry<T>[] entryArray = new Entry[this.size];
		int maxlevel = (int) Math.ceil((Math.log(this.size) / Math.log(2)));
		rebuild(entryArray, 0, this.size - 1, maxlevel);
		SkipList<T> list = new SkipList<>();
		list.size = this.size;
		list.maxLevel = maxlevel;
		int i = 0;
		
		while(i<size) {
			T x = get(i);
			entryArray[i].element = x;
			list.find(x);
			for(int j = 0; j < entryArray[i].next.length; j++) {
				list.last[j].next[j] = entryArray[i];
			}
			i++;
		}
		
		this.head = list.head;
		this.maxLevel = maxlevel;
		this.retemp = list.head;
		list.retemp = list.head;
		for(int k=0; k< this.size; k++) {
			for(int j = 0; j<retemp.next.length; j++) {
				this.retemp.next[j] = list.retemp.next[j];
				this.retemp.span[j] = ((int) Math.pow(2,j));
			}
			list.retemp = list.retemp.next[0];
			this.retemp = list.retemp;
		}
	}
	
	public void rebuild(Entry<T>[] entryArray, int start, int end, int level) {
		if(start <= end) {
			if(level == 1) {
				for(int i = start; i <= end; i++)
					entryArray[i] = new Entry<T>(null, 1);
			}
			else {
				int mid = (start + end)/2;
				entryArray[mid] = new Entry<>(null, level);
				rebuild(entryArray, start, mid - 1, level - 1);
				rebuild(entryArray, mid + 1, end, level - 1);
			}
		}
	}
	
	// Method to check whether the skiplist is perfect or not
	public void checkRebuild() {
		
		Entry<T> temp = head;
		int i = 0;
		System.out.println("Max level is " + maxLevel);
		while(i < maxLevel) {
			System.out.print("Level "+(i+1)+" elements: ");
			while(temp!=null) {
				if(temp.next[i]==null) {
					break;
				}
				else {
					System.out.print(temp.next[i].element+" ");
				}
				temp = temp.next[i];
			}
			System.out.println();
			temp = head;
			i++;
		}
	}
	
	// Remove x from list.  Removed element is returned. Return null if x not in list
	public T remove(T x) {
		if(!contains(x)) {
			return null;
		}
		Entry<T> removeEntry = last[0].next[0];
		int length = removeEntry.next.length;
		for(int i=0; i<length; i++) {
			last[i].next[i] = removeEntry.next[i];
			last[i].span[i] += removeEntry.span[i] - 1; 
		}
		for(int i=length; i<PossibleLevels; i++) {
			last[i].span[i] -= 1; 
		}
		size = size-1;
		return removeEntry.element;
	}

	// Return the number of elements in the list
	public int size() {
		return size;
	}
	
	// Print the elements of the skiplist
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
		for(int i=1;i<15;i=i+2) {
			sk.add(i);
		}
		System.out.print("List is ");
		sk.printList();
		sk.rebuild();
		System.out.println("check rebuild method ");
		sk.checkRebuild();
		System.out.println("remove "+sk.remove(9));
		System.out.println("ceiling "+sk.ceiling(9));
		System.out.println("contains "+sk.contains(11));
		System.out.println("first " + sk.first());
		System.out.println("floor " + sk.floor(9));
		System.out.println("get2 "+ sk.get(2) + " get4 "+ sk.get(4));
		System.out.println("isEmpty " + sk.isEmpty());
		System.out.println("last " + sk.last());
		System.out.println("size " + sk.size());
		Iterator<Integer> it = sk.iterator();
		System.out.println("it.hasNext() " + it.hasNext());
		System.out.println("it.next " + it.next());
		it.remove();
		System.out.print("List is ");
		sk.printList();
	}
}