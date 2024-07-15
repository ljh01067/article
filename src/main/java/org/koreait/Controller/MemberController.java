package org.koreait.Controller;

import org.koreait.App;
import org.koreait.Article;
import org.koreait.service.MemberService;
import org.koreait.util.DBUtil;
import org.koreait.util.Member;
import org.koreait.util.SecSql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class MemberController {
    private String cmd;
    private Connection conn;
    private Scanner sc;

    private MemberService memberService;

    public MemberController(Scanner sc, Connection conn) {
        this.sc = sc;
        this.conn = conn;
        this.memberService = new MemberService(conn);
    }
    public void doAction(String cmd, String actionMethodName) throws SQLException {
            this.cmd = cmd;
            switch (actionMethodName) {
                case "join":
                    doJoin();
                    break;
                case "login":
                    doLogin();
                    break;
                case "logout":
                    doLogout();
                    break;
                default:
                    System.out.println("명령어 확인 (actionMethodName) 오류");
                    break;
            }
        }


    public void doJoin() {
        String loginId = null;
        String loginPw = null;
        String loginPwConfirm = null;
        String name = null;

        System.out.println("==회원가입==");
        while (true) {
            System.out.print("로그인 아이디 : ");
            loginId = sc.nextLine().trim();

            if (loginId.length() == 0 || loginId.contains(" ")) {
                System.out.println("아이디 똑바로 써");
                continue;
            }

            boolean isLoindIdDup = memberService.isLoginIdDup(conn,loginId);

            if (isLoindIdDup) {
                System.out.println(loginId + "는(은) 이미 사용중");
                continue;
            }
            break;
        }
        while (true) {
            System.out.print("비밀번호 : ");
            loginPw = sc.nextLine().trim();

            if (loginPw.length() == 0 || loginPw.contains(" ")) {
                System.out.println("비번 똑바로 입력해");
                continue;
            }

            boolean loginPwCheck = true;

            while (true) {
                System.out.print("비밀번호 확인 : ");
                loginPwConfirm = sc.nextLine().trim();

                if (loginPwConfirm.length() == 0 || loginPwConfirm.contains(" ")) {
                    System.out.println("비번 확인 똑바로 써");
                    continue;
                }
                if (loginPw.equals(loginPwConfirm) == false) {
                    System.out.println("일치하지 않아");
                    loginPwCheck = false;
                }
                break;
            }
            if (loginPwCheck) {
                break;
            }
        }

        while (true) {
            System.out.print("이름 : ");
            name = sc.nextLine();

            if (name.length() == 0 || name.contains(" ")) {
                System.out.println("이름 똑바로 써");
                continue;
            }
            break;
        }

        int id = memberService.doJoin(loginId, loginPw, name);



        System.out.println(id + "번 회원이 생성되었습니다");
    }
    public void doLogin(){
        if (Member.islog()){
            System.out.println("이미 로그인 되어 있습니다");
            return;
        }
        if (App.getlogi() >= 3) {
            System.out.println("비번 다시 확인하고 시도해");
            return;
        }
        System.out.println("==로그인==");
        System.out.print("로그인 아이디 : ");
        String loginId = sc.nextLine().trim();
        System.out.print("비밀번호 : ");
        String loginPw = sc.nextLine();
        List<Article> articles = new ArrayList<>();
        SecSql sql = new SecSql();
        sql.append("SELECT *");
        sql.append(" FROM `member` ");
        sql.append("WHERE logid = ? and",loginId);
        sql.append(" logpw = ?;",loginPw);
        List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);
        for (Map<String, Object> articleMap : articleListMap) {
            articles.add(new Article(articleMap));
        }
        if (articles.size() == 0) {
            System.out.println("아이디 입력 오류");
            App.setlogi();
            return;
        }else{
            System.out.println("로그인 되었습니다");
            Member.islogined(loginId,loginPw);
        }
    }
    public void doLogout(){
        if(Member.islog() == false){
            System.out.println("로그인이 되어있지 않습니다.");
        }
        else {
            System.out.println("로그아웃 되셨습니다");
            Member.islogouted();
        }
    }
}
