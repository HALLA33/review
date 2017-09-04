package spring.controller.member;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import spring.model.member.Member;
import spring.model.member.MemberDao;

@Controller
public class MemberController {
	
	@Autowired
	private MemberDao memberDao;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	//이용약관 뷰
	@RequestMapping("/tos")
	public String tosview(){
		System.out.println("실행됨");
		
		return "member/tos";				
	}
	
	//회원가입 뷰
	@RequestMapping("/sign")
	public String signview() {
		
		
		return "member/sign";
	}
	
	//회원가입 처리
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	public String sign(
			@ModelAttribute Member member, @RequestParam String rpw) throws Exception {
		log.info("sign() 실행");
		
		if(!member.getPw().equals(rpw)) {
			throw new Exception("비밀번호와 확인비밀번호 불일치");
		}else if(member.getPw().equals(rpw)) {
			boolean result = memberDao.sign(member);
			
			if(result) {
				log.info("잘 시작됨");
				return "redirect:/";
			}else {
				throw new Exception("비정상적인 회원가입 발생");
			}
		}
		return null;
		
	}
	
	//관리자 전용 멤버리스트
	@RequestMapping("/member")
	public String memberlist(HttpServletRequest request, HttpServletRequest respons) {
		
		List<Member> list = memberDao.memberlist();
		
		request.setAttribute("list", list);
		
		return "member/member";
		
	}
	//로그인 처리
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(
			@RequestParam String id,@RequestParam String pw, HttpSession session
			) {
		
		log.info("로그인 실행");
		
		Member member = memberDao.login(id, pw);
		
		log.info(member.getName());
		
		session.setAttribute("member", member);

		return "redirect:/";
	}
	//로그아웃 처리
	@RequestMapping("logout")
	public String logout(HttpSession session) {
		
		session.invalidate();
		
		return "redirect:/";
	}
	//아이디 찾기 뷰
	@RequestMapping("/forget")
	public String findidview() {
		
		
		return "member/forget";
	}
	//아이디 찾기 처리
	@RequestMapping(value = "/forget", method = RequestMethod.POST)
	public String findid(@RequestParam String name, String email, HttpServletRequest request) throws Exception {
		
		String id = memberDao.findid(name, email);
		
		request.setAttribute("id",id);
		
		return "member/forget_suc";
	}
	

}