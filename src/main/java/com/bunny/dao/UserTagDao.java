package com.bunny.dao;

import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import java.util.List;
import java.util.Set;

public interface UserTagDao {

	RoaringBitmap getUserPortraitByUid(Long userId);


	Roaring64NavigableMap getUserCrowdByTag(Integer tagId);

	List<Roaring64NavigableMap> batchGetUserCrowdByTag(Set<Integer> tagIds);

	int insertUserPortrait(Long userId, Set<Integer> tagValue);

	int insertTag(Integer tagId);

	int batchInsertTag(Set<Integer> tagIds);
}
