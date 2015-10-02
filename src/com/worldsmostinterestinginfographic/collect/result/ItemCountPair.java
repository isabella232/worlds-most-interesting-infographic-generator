package com.worldsmostinterestinginfographic.collect.result;

/**
 * A simple data structure that maintains a single item and its count.
 *
 * @param <I>
 * @param <C>
 */
public interface ItemCountPair<I, C> {
	public I getItem();
	public C getCount();
}
