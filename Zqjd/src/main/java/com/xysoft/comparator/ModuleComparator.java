package com.xysoft.comparator;

import java.util.Comparator;
import com.xysoft.entity.Module;

public class ModuleComparator implements Comparator<Module>{

	public int compare(Module o1, Module o2) {
		if (o1.getLevel() > o2.getLevel()) return 1;
		else if (o1.getLevel() < o2.getLevel()) return -1;
		else return 0;
	}

}
