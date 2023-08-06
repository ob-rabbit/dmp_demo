package com.bunny.dmp

import com.bunny.dao.UserTagDao
import com.bunny.dao.UserTagDaoImpl
import spock.lang.Specification

/**
 @author Bunny
 @create 2023-08-06 22:09
 */
class UserTagDaoImplTest extends Specification {

    def "test DMP"() {
        given:
        UserTagDao userTagDao = new UserTagDaoImpl();
        DMP dmpImplement = new DMPImplement(userTagDao);

        Set<Integer> tags = new HashSet<>([1, 2, 3, 4, 5, 6, 7, 8, 9, 10] as Set)
        dmpImplement.batchInsertTags(tags)

        dmpImplement.insertUserOfTags(1,[1,3,5] as Set)
        dmpImplement.insertUserOfTags(2,[1] as Set)
        dmpImplement.insertUserOfTags(3,[2,6] as Set)
        dmpImplement.insertUserOfTags(4,[7] as Set)
        dmpImplement.insertUserOfTags(5,[7,8] as Set)
        dmpImplement.insertUserOfTags(6,[8,10] as Set)
        dmpImplement.insertUserOfTags(7,[7,8] as Set)
        dmpImplement.insertUserOfTags(8,[1,3,4,5,6,7] as Set)
        dmpImplement.insertUserOfTags(9,[2,8] as Set)
        dmpImplement.insertUserOfTags(10,[1,3,9] as Set)

        when:
        def tagsOfUser1 = dmpImplement.getUserPortraitByUid(1L)
        def tagsOfUser8 = dmpImplement.getUserPortraitByUid(8L)
        def userIds1 = dmpImplement.getUserCrowdByTag([1,3,5])
        def userIds2 = dmpImplement.getUserCrowdByTag([8])

        then:
        tagsOfUser1 == [1,3,5] as Set
        tagsOfUser8 == [1,3,4,5,6,7] as Set
        userIds1 == [1,8] as Set
        userIds2 == [5,6,7,9] as Set
    }
}
