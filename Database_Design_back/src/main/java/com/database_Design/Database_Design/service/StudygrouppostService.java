package com.database_Design.Database_Design.service;

import com.database_Design.Database_Design.Repository.StudygrouppostRepository;
import com.database_Design.Database_Design.Repository.StudygroupRepository;
import com.database_Design.Database_Design.entity.Study_group;
import com.database_Design.Database_Design.entity.Study_group_member;
import com.database_Design.Database_Design.entity.Study_group_post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StudygrouppostService {

	private final StudygroupRepository studyGroupRepository;
	private final StudygrouppostRepository studyGroupPostRepository;

	/**
	 * 스터디 그룹 공지사항 수정
	 *
	 * @param groupId      수정할 스터디 그룹의 ID
	 * @param userId       요청을 보낸 사용자의 ID
	 * @param newGroupRule 새로운 공지사항 내용
	 * @throws IllegalArgumentException 권한 없음 또는 스터디 그룹이 존재하지 않는 경우 예외 발생
	 */
	@Transactional
	public void updateGroupRule(Long groupId, Long userId, String newGroupRule) {
		// 스터디 그룹 조회
		Study_group studyGroup = studyGroupRepository.findById(groupId)
				.orElseThrow(() -> new IllegalArgumentException("해당 스터디 그룹이 존재하지 않습니다."));

		// 요청 사용자가 스터디 리더인지 확인
		if (!studyGroup.getStd_leader().getId().equals(userId)) {
			throw new IllegalArgumentException("공지사항을 수정할 권한이 없습니다.");
		}

		// 공지사항 수정
		studyGroup.setGroup_rule(newGroupRule);

		// 변경 사항 저장
		studyGroupRepository.save(studyGroup);
	}


	@Transactional
	public void createGroupPost(Long groupId, Long userId, String postContent) {
		// 1. 스터디 그룹 조회
		Study_group studyGroup = studyGroupRepository.findById(groupId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 그룹입니다."));

		// 2. 작성자 확인: 작성자가 스터디 멤버인지 확인
		Study_group_member member = studyGroup.getStd_members().stream()
				.filter(m -> m.getUser().getId().equals(userId))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("작성자는 이 스터디 그룹의 멤버가 아닙니다."));

		// 3. 새로운 게시글 생성
		Study_group_post newPost = new Study_group_post();
		newPost.setStudyGroup(studyGroup); // 스터디 그룹 설정
		newPost.setGroup_post_writer(member); // 작성자를 멤버로 설정
		newPost.setGroup_post_content(postContent); // 게시글 내용 설정
//		newPost.setGroup_post_date(new Date()); // 작성 날짜 설정

		// 4. 게시글 저장
		studyGroupPostRepository.save(newPost);
	}

	@Transactional
	public void updateGroupPost(Long groupId, Long postId, Long userId, String updatedContent) {
		// 1. 스터디 그룹 확인
		Study_group studyGroup = studyGroupRepository.findById(groupId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 그룹입니다."));

		// 2. 게시글 확인 (그룹 ID와 게시글 ID로 조회)
		Study_group_post post = studyGroupPostRepository.findById(postId)
				.filter(p -> p.getStudyGroup().getstd_Id().equals(groupId)) // 게시글이 해당 그룹에 속하는지 확인
				.orElseThrow(() -> new IllegalArgumentException("해당 그룹에 게시글이 존재하지 않습니다."));

		// 3. 수정 권한 확인
		if (!post.getGroup_post_writer().getUser().getId().equals(userId)) {
			throw new SecurityException("게시글을 수정할 권한이 없습니다.");
		}

		// 4. 게시글 내용 업데이트
		post.setGroup_post_content(updatedContent);

		// 5. 저장
		studyGroupPostRepository.save(post);
	}


	@Transactional
	public void deleteGroupPost(Long groupId, Long postId, Long userId) {
		// 1. 스터디 그룹 유효성 검사
		Study_group studyGroup = studyGroupRepository.findById(groupId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 스터디 그룹입니다."));

		// 2. 게시글 유효성 검사
		Study_group_post post = studyGroupPostRepository.findById(postId)
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

		// 3. 게시글과 그룹 관계 확인
		if (!post.getStudyGroup().getstd_Id().equals(groupId)) {
			throw new IllegalArgumentException("게시글이 해당 스터디 그룹에 속하지 않습니다.");
		}

		// 4. 삭제 권한 확인
		if (!post.getGroup_post_writer().getUser().getId().equals(userId)) {
			throw new IllegalArgumentException("삭제 권한이 없습니다.");
		}

		// 5. 게시글 삭제
		studyGroupPostRepository.delete(post);
	}

}