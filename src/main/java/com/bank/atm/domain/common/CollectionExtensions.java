package com.bank.atm.domain.common;

import java.util.ArrayList;

public class CollectionExtensions {
  public static <T> ArrayList<T> addItem(ArrayList<T> coll, T item) {
    coll.add(item);
    return coll;
	}
}
