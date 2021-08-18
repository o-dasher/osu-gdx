package lt.ekgame.beatmap_analyzer.utils;

import com.badlogic.gdx.utils.Array;


/**
 * Author: Hari Chinnan
 * Original code: https://github.com/harichinnan/Java-Algorithms/blob/master/Introsort.java
 * 
 * Modified by ekgame to only perform Quicksort (couldn't find an algorithm to perform Quicksort on objects).
*/

public class Quicksort {

	private static int size_threshold = 16;

	public static <T extends Comparable<? super T>> void sort(Array<T> list) {
		introSortLoop(list, 0, list.size);

	}

	public static <T extends Comparable<? super T>> void sort(Array<T> list, int begin, int end) {
		if (begin < end) {
			introSortLoop(list, begin, end);
		}
	}

	/*
	 * Quicksort algorithm modified for Introsort
	 */
	private static <T extends Comparable<? super T>> void introSortLoop(Array<T> list, int lo, int hi) {
		while (hi - lo > size_threshold) {
			int p = partition(list, lo, hi, medianof3(list, lo, lo + ((hi - lo) / 2) + 1, hi - 1));
			introSortLoop(list, p, hi);
			hi = p;
		}
		insertionSort(list, lo, hi);
	}

	private static <T extends Comparable<? super T>> int partition(Array<T> list, int lo, int hi, T x) {
		int i = lo, j = hi;
		while (true) {
			while (list.get(i).compareTo(x) < 0)
				i++;
			j = j - 1;
			while (x.compareTo(list.get(j)) < 0)
				j = j - 1;
			if (!(i < j))
				return i;
			exchange(list, i, j);
			i++;
		}
	}

	private static <T extends Comparable<? super T>> T medianof3(Array<T> list, int lo, int mid, int hi) {

		if (list.get(mid).compareTo(list.get(lo)) < 0) {
			if (list.get(hi).compareTo(list.get(mid)) < 0)
				return list.get(mid);
			else {
				if (list.get(hi).compareTo(list.get(lo)) < 0)
					return list.get(hi);
				else
					return list.get(lo);
			}
		} else {
			if (list.get(hi).compareTo(list.get(mid)) < 0) {
				if (list.get(hi).compareTo(list.get(lo)) < 0)
					return list.get(lo);
				else
					return list.get(hi);
			} else
				return list.get(mid);
		}
	}

	/*
	 * Insertion sort algorithm
	 */
	private static <T extends Comparable<? super T>> void insertionSort(Array<T> list, int lo, int hi) {
		int i, j;
		T t;
		for (i = lo; i < hi; i++) {
			j = i;
			t = list.get(i);
			while (j != lo && t.compareTo(list.get(j - 1)) < 0) {
				list.set(j, list.get(j - 1));
				j--;
			}
			list.set(j, t);
		}
	}

	/*
	 * Common methods for all algorithms
	 */
	private static <T extends Comparable<? super T>> void exchange(Array<T> list, int i, int j) {
		T t = list.get(i);
		list.set(i, list.get(j));
		list.set(j, t);
	}
}