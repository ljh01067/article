package org.koreait.session;

import org.koreait.dto.Member;
public class Session {
    public Member loginedMember;
    public int loginedMemberId;
    public Session() {
        loginedMember = null;
        loginedMemberId = -1;
    }

    public void logout() {
        loginedMember = null;
        loginedMemberId = -1;
    }

    public void login(Member member) {
        loginedMember = member;
        loginedMemberId = member.getId();
    }

    public boolean isLogined() {
        return loginedMemberId != -1;
    }
}
