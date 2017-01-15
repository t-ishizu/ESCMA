package jp.ac.osaka.u.ist.t_ishizu.WhirlWind;

import java.util.Comparator;

public class WeightComparator implements Comparator<CloneSet>{
	public int compare(CloneSet cs1,CloneSet cs2){
		int weight1 = cs1.getWeight();
		int weight2 = cs2.getWeight();
		
		if(weight1>weight2){
			return -1;
		}else if(weight1==weight2){
			return 0;
		}else{
			return 1;
		}
	}
}
