package com.example.testsecurity.repository;

import com.example.testsecurity.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {

    // jpa 커스텀 메서드를 작성할 수 있다
    // null일 경우를 대비해서 Optional로 반환도 생각해보자
    UserEntity findByUsername(String username);
}
