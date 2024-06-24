package com.lec.spring.repository;

import com.lec.spring.domain.Socializing;
import com.lec.spring.domain.UserSocializing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SocializingRepository {
    int save(Socializing socializing);
    Socializing findById(Long id);
    int update(Socializing socializing);
    int delete(Socializing socializing);

    List<Socializing> selectFromRow(int from, int rows
            , @Param("param3") String selectaddress
            , @Param("param4") String selectcategory
            , @Param("param5") String selectdetailcategory
    );
    int membercnt(Long id);
    int countSelectAddress(String address
            , String catetory
            , String selectdetailcategory);

    List<UserSocializing> findBySocialMembers(@Param("param1") Long id);


    UserSocializing findBySocializingMaster(@Param("param1") Long id);

    List<Socializing> findAll();
}
