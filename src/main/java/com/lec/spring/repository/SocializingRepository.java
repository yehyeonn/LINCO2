package com.lec.spring.repository;

import com.lec.spring.domain.Socializing;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SocializingRepository {
    int save(Socializing socializing);
    Socializing findById(Long id);
    List<Socializing> findAll();
    int update(Socializing socializing);
    int delete(Socializing socializing);

    List<Socializing> selectAllRow(int from, int rows);

    List<Socializing> selectFromRow(int from, int rows
            , @Param("param3") String selectaddress
            , @Param("param4") String selectcategory
            , @Param("param5") String selectdetailcategory
    );
    int countAll();
    int countSelectAddress(String address, String catetory, String selectdetailcategory);
}
