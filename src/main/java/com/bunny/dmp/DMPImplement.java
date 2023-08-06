package com.bunny.dmp;

import com.bunny.dao.UserTagDao;
import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DMPImplement implements DMP {

	private final UserTagDao userTagDao;

	public DMPImplement(UserTagDao userTagDao) {
		this.userTagDao = userTagDao;
	}

	public void insertUserOfTags(Long userId, Set<Integer> tags) {
		userTagDao.insertUserPortrait(userId, tags);
	}

	public void batchInsertTags(Set<Integer> tags) {
		userTagDao.batchInsertTag(tags);
	}

	@Override
	public Set<Integer> getUserPortraitByUid(Long uid) {
		RoaringBitmap portraitValue = userTagDao.getUserPortraitByUid(uid);
		return convertBitMapToTagIds(portraitValue);
	}

	@Override
	public Set<Long> getUserCrowdByTag(List<Integer> tagIds) {
		List<Roaring64NavigableMap> userIdsOfTags = userTagDao.batchGetUserCrowdByTag(new HashSet<>(tagIds));
		if (userIdsOfTags.size() == 0) {
			return Collections.emptySet();
		}
		Roaring64NavigableMap bitMapOfRes = new Roaring64NavigableMap();
		bitMapOfRes.or(userIdsOfTags.get(0));
		for (int i = 1; i < userIdsOfTags.size(); i++) {
			bitMapOfRes.and(userIdsOfTags.get(i));
		}
		return convertBitMapToUserId(bitMapOfRes);
	}

	private Set<Integer> convertBitMapToTagIds(RoaringBitmap bitMapOfTags) {
		HashSet<Integer> tags = new HashSet<>();
		Iterator<Integer> iterator = bitMapOfTags.iterator();
		while (iterator.hasNext()) {
			tags.add(iterator.next());
		}
		return tags;
	}

	private Set<Long> convertBitMapToUserId(Roaring64NavigableMap bitMapOfUserIds) {
		HashSet<Long> tags = new HashSet<>();
		Iterator<Long> iterator = bitMapOfUserIds.iterator();
		while (iterator.hasNext()) {
			tags.add(iterator.next());
		}
		return tags;
	}
}
