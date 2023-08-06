package com.bunny.dmp;

import java.util.List;
import java.util.Set;

public interface DMP {

	/**
	 * get tagIds with value true for user uid
	 *
	 * @param uid
	 * @return uer portrait
	 */
	Set<Integer> getUserPortraitByUid(Long uid);

	/**
	 * query uid set by tagIds
	 *
	 * @param tagIds for example: tagIds = [1, 3, 5] means query all the uid with tag1=1,
	tag3=1 and tag5=1
	 * @return uid set
	 */
	Set<Long> getUserCrowdByTag(List<Integer> tagIds);
}
