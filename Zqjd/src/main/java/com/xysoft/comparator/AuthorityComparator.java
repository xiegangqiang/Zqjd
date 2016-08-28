package com.xysoft.comparator;

import java.util.Comparator;
import com.xysoft.entity.Authority;


public class AuthorityComparator implements Comparator<Authority> {

	public int compare(Authority o1, Authority o2) {
		if (o1.getLevel() > o2.getLevel()) return 1;
		else if (o1.getLevel() < o2.getLevel()) return -1;
		else return 0;
	}

}
