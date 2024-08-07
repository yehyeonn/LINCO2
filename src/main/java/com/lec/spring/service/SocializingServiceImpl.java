package com.lec.spring.service;

import com.lec.spring.domain.Socializing;
import com.lec.spring.domain.User;
import com.lec.spring.domain.UserSocializing;
import com.lec.spring.repository.SocializingRepository;
import com.lec.spring.repository.UserRepository;
import com.lec.spring.util.U;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

@Service
public class SocializingServiceImpl implements SocializingService {


    // 첨부파일 업로드
    @Value("upload")
    private String uploadDir;

    @Value("${app.pagination.write_pages}")
    private int WRITE_PAGES;

    @Value("${app.pagination.page_rows}")
    private int PAGE_ROWS;


    private UserRepository userRepository;
    private SocializingRepository socializingRepository;

    @Autowired
    public SocializingServiceImpl(SqlSession sqlSession) {
        userRepository = sqlSession.getMapper(UserRepository.class);
        socializingRepository = sqlSession.getMapper(SocializingRepository.class);

//        System.out.println("SocializingService() 생성");
    }

    @Override
    public int write(Socializing socializing) {
        User user = U.getLoggedUser();
        socializing.setUser(user);  // 글 작성자

        int cnt = socializingRepository.save(socializing);  // 글 저장 성공 여부

        return cnt;
    }


    // 소셜의 세부사항
    @Override
    @Transactional
    public Socializing detail(Long id) {
        Socializing socializing = socializingRepository.findById(id);
        return socializing;
    }

    //해당 소셜에 인원수를 뽑아옴
    @Override
    public int membercnt(Long id) {
        int socialMemberCnt = socializingRepository.membercnt(id);
        return socialMemberCnt;
    }

    //해당 소셜에 해당하는 사람들의 이름, 전화번호, 권한 뽑기
    @Override
    public List<UserSocializing> socializingMemberList(Long id) {
        List<UserSocializing> userSocializing = socializingRepository.findBySocialMembers(id);
        return userSocializing;
    }


    //list 페이지에서 주소, 카테고리, 소분류로 나눠 각각에 맞는 컬럼들을 뽑아서 페이지로 보여줌
    @Override
    public List<Socializing> list(Integer page, Model model, String selectaddress, String selectcategory, String selectdetailcategory) {

        // 현재페이지
        if (page == null || page < 1) page = 1; //디폴트 1page
        // 페이징
        // writePages: 한 [페이징] 당 몇개의 페이지가 표시되나
        // pageRows: 한 '페이지'에 몇개의 글을 리스트 할것인가?
        HttpSession session = U.getSession();
        Integer writePages = (Integer) session.getAttribute("writePages");
        if (writePages == null) writePages = WRITE_PAGES;
        Integer pageRows = (Integer) session.getAttribute("pageRows");
        if (pageRows == null) pageRows = PAGE_ROWS;
        session.setAttribute("page", page); // 현재 페이지 번호 -> session 에 저장한다.

        long cnt = socializingRepository.countSelectAddress(selectaddress, selectcategory, selectdetailcategory);  //글 목록 전체의 개수
        int totalPage = (int) Math.ceil(cnt / (double) pageRows);  // 총 몇 페이지인지 분량

        //[페이징] 에 표시할 '시작페이지' 와 '마지막페이지'
        int startPage = 0;
        int endPage = 0;

        // 해당 '페이지'의 글 목록
        List<Socializing> list = null;

        if (cnt > 0) {  // 데이터가 최소 1개 이상 있는 경우만 페이징
            // page 값 보정
            if (page > totalPage) {
                page = totalPage;
            }

            // fromRow : 몇번째 데이터부터
            int fromRow = (page - 1) * pageRows;

            // [페이징] 에 표시할 '시작페이지' 와 '마지막페이지' 계산
            startPage = (((page - 1) / writePages) * writePages) + 1;
            endPage = startPage + writePages - 1;
            if (endPage >= totalPage) endPage = totalPage;

            // 해당 페이지의 글 목록 읽어오기
            list = socializingRepository.selectFromRow(fromRow, pageRows, selectaddress, selectcategory, selectdetailcategory);

            model.addAttribute("list", list);
            model.addAttribute("address", selectaddress);
            model.addAttribute("category", selectcategory);
            model.addAttribute("detailcategory", selectdetailcategory);
        } else {
            page = 0;
        }

        model.addAttribute("cnt", cnt);  // 전체 글 개수
        model.addAttribute("page", page); // 현재 페이지
        model.addAttribute("totalPage", totalPage);  // 총 '페이지' 수
        model.addAttribute("pageRows", pageRows);  // 한 '페이지' 에 표시할 글 개수


        // [페이징]
        model.addAttribute("url", U.getRequest().getRequestURI());  // 목록 url
        model.addAttribute("writePages", writePages); // [페이징] 에 표시할 숫자 개수
        model.addAttribute("startPage", startPage);  // [페이징] 에 표시할 시작 페이지
        model.addAttribute("endPage", endPage);   // [페이징] 에 표시할 마지막 페이지

        return list;
    }

    @Override
    public UserSocializing findBySocializingMaster(long id) {
        return socializingRepository.findBySocializingMaster(id);
    }

    //수정
    @Override
    public Socializing selectById(Long id) {
        Socializing socializing = socializingRepository.findById(id);

        return socializing;
    }

    @Override
    public int update(Socializing socializing) {
        int result = 0;
        result = socializingRepository.update(socializing);
        return result;
    }

    @Override
    public int deleteById(Long id) {
        int result = 0;
        Socializing socializing = socializingRepository.findById(id);
        if (socializing != null) {
            result = socializingRepository.delete(socializing);
        }
        return result;
    }

    @Override
    public List<String> getAllCategories() {
        // 실제 구현 내용
        return Arrays.asList("운동", "공연", "공부");
    }

    @Override
    public List<String> getDetailCategories(String category) {
        switch (category) {
            case "운동":
                return Arrays.asList("축구", "야구", "농구");
            case "공연":
                return Arrays.asList("전시", "댄스", "영화");
            case "공부":
                return Arrays.asList("컴퓨터", "영어", "수학");
            default:
                return Arrays.asList();
        }
    }

    @Override
    public List<Socializing> findAll() {
        return socializingRepository.findAll();
    }


}
