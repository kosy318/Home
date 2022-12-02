import Vue from "vue";
import VueRouter from "vue-router";
import AppMain from "@/views/AppMain";

import AppApt from "@/views/AppApt";
import AppQna from "@/views/AppQna";
import AppSignup from "@/views/AppSignup";
import AppLogin from "@/views/AppLogin";
import AptSearch from "@/components/apt/AptSearch";
import AppNotice from "@/views/AppNotice";

import NoticeList from "@/components/notice/NoticeList";
import NoticeDetail from "@/components/notice/NoticeDetail";
import NoticeInsert from "@/components/notice/NoticeInsert";
import NoticeModify from "@/components/notice/NoticeModify";
import NoticeSearch from "@/components/notice/NoticeSearch";

import QnaList from "@/components/qna/QnaList";
import QnaDetail from "@/components/qna/QnaDetail";
import QnaInsert from "@/components/qna/QnaInsert";
import QnaAnswerInsert from "@/components/qna/QnaAnswerInsert";
import QnaModify from "@/components/qna/QnaModify";
import QnaSearch from "@/components/qna/QnaSearch";

import NaverLogin from "@/components/login/NaverLogin";
import GoogleLogin from "@/components/login/GoogleLogin";

import UserFindPass from "@/components/userpage/UserFindPass";

import store from "@/store";

Vue.use(VueRouter);

const onlyAuthUser = async (to, from, next) => {
  const checkUserInfo = store.getters["UserStore/checkUserInfo"];
  console.log(checkUserInfo);
  const checkToken = store.getters["UserStore/checkToken"];
  let token = sessionStorage.getItem("access-token");
  console.log("로그인 처리 전", checkUserInfo, token);

  if (checkUserInfo != null && token) {
    console.log("토큰 유효성 체크하러 가기");
    await store.dispatch("UserStore/getUserInfo", token);
  }
  if (!checkToken || checkUserInfo === null) {
    alert("로그인이 필요한 페이지입니다..");
    // next({ name: "login" });
    router.push({ name: "login" });
  } else {
    console.log("로그인 했음");
    next();
  }
};

const routes = [
  {
    path: "/",
    name: "main",
    component: AppMain,
  },
  {
    path: "/apt",
    name: "apt",
    component: AppApt,
    children: [
      {
        path: "search",
        name: "aptsearch",
        component: AptSearch,
      },
    ],
  },
  {
    path: "/userpage",
    name: "userpage",
    beforeEnter: onlyAuthUser,
    component: () => import("@/views/AppUserpage"),
    redirect: "/userpage/view",
    children: [
      {
        path: "view",
        name: "userview",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/userpage/UserView"),
      },
      {
        path: "favorite",
        name: "userfavorite",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/userpage/UserFavorite"),
      },
      {
        path: "modify",
        name: "usermodify",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/userpage/UserModify"),
      },
      {
        path: "changepass",
        name: "userchangepass",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/userpage/UserChangePass"),
      },
      {
        path: "qna",
        name: "userqna",
        beforeEnter: onlyAuthUser,
        component: () => import("@/components/userpage/UserQna"),
      },
    ],
  },
  {
    path: "/findpass",
    name: "findpass",
    component: UserFindPass,
  },
  {
    path: "/notice",
    name: "notice",
    component: AppNotice,
    redirect: "/notice/list",
    children: [
      {
        path: "list",
        name: "noticelist",
        component: NoticeList,
        children: [
          {
            path: "search",
            name: "noticesearch",
            component: NoticeSearch,
          },
        ],
      },
      {
        path: "detail",
        name: "noticedetail",
        component: NoticeDetail,
      },
      {
        path: "insert",
        name: "noticeinsert",
        component: NoticeInsert,
      },
      {
        path: "modify",
        name: "noticemodify",
        component: NoticeModify,
      },
    ],
  },
  {
    path: "/qna",
    name: "qna",
    component: AppQna,
    redirect: "/qna/list",
    children: [
      {
        path: "list",
        name: "qnalist",
        component: QnaList,
        children: [
          {
            path: "search",
            name: "qnasearch",
            component: QnaSearch,
          },
        ],
      },
      {
        path: "detail",
        name: "qnadetail",
        component: QnaDetail,
      },
      {
        path: "insert",
        name: "qnainsert",
        component: QnaInsert,
      },
      {
        path: "answer",
        name: "qnaanswerinsert",
        component: QnaAnswerInsert,
      },
      {
        path: "modify",
        name: "qnamodify",
        component: QnaModify,
      },
    ],
  },
  {
    path: "/signup",
    name: "signup",
    component: AppSignup,
  },
  {
    path: "/login",
    name: "login",
    component: AppLogin,
  },
  {
    path: "/naver",
    name: "naverlogin",
    component: NaverLogin,
  },
  {
    path: "/google",
    name: "googlelogin",
    component: GoogleLogin,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
