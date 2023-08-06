package com.bunny.dao;

import org.roaringbitmap.RoaringBitmap;
import org.roaringbitmap.longlong.Roaring64NavigableMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class UserTagDaoImpl implements UserTagDao {

	/**
	 * 存储每一个用户的画像值，key为userId
	 */
	private static ConcurrentHashMap<Long, RoaringBitmap> userPortraitTable = new ConcurrentHashMap<>();

	/**
	 * 存储每一个画像标签的用户，key为tagId
	 */
	private static ConcurrentHashMap<Integer, Roaring64NavigableMap> tagUserTable = new ConcurrentHashMap<>();


	@Override
	public RoaringBitmap getUserPortraitByUid(Long userId) {
		return userPortraitTable.get(userId);
	}

	@Override
	public Roaring64NavigableMap getUserCrowdByTag(Integer tagId) {
		return tagUserTable.get(tagId);
	}

	@Override
	public List<Roaring64NavigableMap> batchGetUserCrowdByTag(Set<Integer> tagIds) {
		List<Roaring64NavigableMap> res = new ArrayList<>();
		for (Integer tagId : tagIds) {
			Roaring64NavigableMap tmp = getUserCrowdByTag(tagId);
			if (!Objects.isNull(tmp)) {
				res.add(tmp);
			}
		}
		return res;
	}

	/**
	 * 预处理，插入每个用户的特征值时，建立userId的索引
	 * 同时修改每个特征对应的用户
	 * @param userId
	 * @param tagValue
	 * @return
	 */
	@Override
	public int insertUserPortrait(Long userId, Set<Integer> tagValue) {
		RoaringBitmap userPortraitByUid = getUserPortraitByUid(userId);
		if (Objects.isNull(userPortraitByUid)) {
			RoaringBitmap portraitValue = new RoaringBitmap();
			tagValue.forEach(tag -> {
				portraitValue.add(tag);
				updateTagUser(tag, userId);
			});
			userPortraitTable.put(userId, portraitValue);
			return 1;
		}
		tagValue.forEach(tag -> {
			userPortraitByUid.add(tag);
			updateTagUser(tag, userId);
		});
		return 1;
	}

	private int updateTagUser(Integer tagId, Long userId) {
		Roaring64NavigableMap userIds = tagUserTable.get(tagId);
		userIds.add(userId);
		return 1;
	}

	@Override
	public int insertTag(Integer tagId) {
		if (!tagUserTable.containsKey(tagId)) {
			tagUserTable.put(tagId, new Roaring64NavigableMap());
			return 1;
		}
		return 0;
	}

	@Override
	public int batchInsertTag(Set<Integer> tagIds) {
		tagIds.forEach(this::insertTag);
		return 1;
	}
}
