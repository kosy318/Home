import jwtDecode from "jwt-decode";
import http from "@/api/http-common";
import Constant from "@/Constant";
import router from "@/router";

const UserStore = {
  namespaced: true,
  state: {
    isLogin: false,
    isLoginError: false,
    userInfo: null,
    isValidToken: false,
    favorites: null,
    showModal: false,
    modalMessage: "",
    showBtn: true,
    sendComplete: false,
    code: null,
  },
  getters: {
    [Constant.CHECK_USER_INFO]: (state) => {
      return state.userInfo;
    },
    [Constant.CHECK_TOKEN]: (state) => {
      return state.isValidToken;
    },
  },
  mutations: {
    [Constant.SET_IS_LOGIN]: (state, isLogin) => {
      state.isLogin = isLogin;
    },
    [Constant.SET_IS_LOGIN_ERROR]: (state, isLoginError) => {
      state.isLoginError = isLoginError;
    },
    [Constant.SET_IS_VALID_TOKEN]: (state, isValidToken) => {
      state.isValidToken = isValidToken;
    },
    [Constant.SET_USER_INFO]: (state, userInfo) => {
      state.isLogin = true;
      state.userInfo = userInfo;
    },
    [Constant.SET_USER_FAVORITES]: (state, favorites) => {
      state.favorites = favorites;
    },
    [Constant.SET_SHOW_MODAL]: (state, showModal) => {
      state.showModal = showModal;
    },
    [Constant.SET_MODAL_MESSAGE]: (state, message) => {
      state.modalMessage = message;
    },
    setShowBtn(state, showBtn) {
      state.showBtn = showBtn;
    },
    [Constant.SEND_EMAIL_VALID]: (state, payload) => {
      state.code = payload.code;
      state.sendComplete = payload.state;
    }
  },
  actions: {
    ///////////////////////login///////////////////////////
    [Constant.USER_LOGIN]: async ({ commit }, user) => {
      await http
        .post(`/users/login`, user)
        .then(({ data }) => {
          // console.log(data.message);
          if (data.message === "success") {
            // console.log(data.message);
            let accessToken = data["access-token"];
            let refreshToken = data["refresh-token"];
            commit(Constant.SET_IS_LOGIN, true);
            commit(Constant.SET_IS_LOGIN_ERROR, false);
            commit(Constant.SET_IS_VALID_TOKEN, true);
            sessionStorage.setItem("access-token", accessToken);
            sessionStorage.setItem("refresh-token", refreshToken);
          } else {
            commit(Constant.SET_IS_LOGIN, false);
            commit(Constant.SET_IS_LOGIN_ERROR, true);
            commit(Constant.SET_IS_VALID_TOKEN, false);
            commit("setShowBtn", false);
            commit(Constant.SET_MODAL_MESSAGE, "비밀번호를 다시 확인해주세요!!");
            commit(Constant.SET_SHOW_MODAL, true);
          }
        })
        .catch(({ data }) => {
          console.log(data.message);
        });
    },
    [Constant.GET_USER_INFO]: async ({ commit, dispatch }, token) => {
      let decodeToken = jwtDecode(token);
      await http
        .get(`/users/` + decodeToken.id, {
          headers: {
            "access-token": sessionStorage.getItem("access-token"),
          },
        })
        .then(({ data }) => {
          if (data.message === "success") {
            commit(Constant.SET_USER_INFO, data.userInfo);
          } else {
            console.log("유저 정보 없음");
          }
        })
        .catch((response) => {
          console.log(response);
          console.log("getUserInfo() error code [토큰 만료되어 사용 불가능.] ::: " + response.status);
          commit(Constant.SET_IS_VALID_TOKEN, false);
          dispatch(Constant.TOKEN_REGENERATION);
        });
    },
    [Constant.TOKEN_REGENERATION]: async ({ commit, state }) => {
      await http
        .post(`/users/refresh`, state.userInfo, {
          headers: {
            "refresh-token": sessionStorage.getItem("refresh-token"),
          },
        })
        .then(({ data }) => {
          if (data.message === "success") {
            let accessToken = data["access-token"];
            console.log("재발급 완료 >> 새로운 토큰 : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit(Constant.SET_IS_VALID_TOKEN, true);
          }
        })
        .catch(async (response) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken 기간 만료 >> 다시 로그인!!!!
          if (response.status === 401) {
            console.log("갱신 실패");
            // 로그아웃
            await http
              .get(`/users/logout/` + state.userInfo.id)
              .then(({ data }) => {
                if (data.message === "success") {
                  console.log("리프레시 토큰 제거 성공");
                } else {
                  console.log("리프레시 토큰 제거 실패");
                }
                alert("RefreshToken 기간 만료!!! 다시 로그인해 주세요.");
                commit(Constant.SET_IS_LOGIN, false);
                commit(Constant.SET_USER_INFO, null);
                commit(Constant.SET_IS_VALID_TOKEN, false);
                router.push({ name: "login" });
              })
              .catch(({ error }) => {
                console.log(error);
                commit(Constant.SET_IS_LOGIN, false);
                commit(Constant.SET_USER_INFO, null);
              });
          }
        });
    },
    [Constant.USER_LOGOUT]: async ({ commit }, id) => {
      await http
        .get(`/users/logout/` + id)
        .then(({ data }) => {
          if (data.message === "success") {
            commit(Constant.SET_IS_LOGIN, false);
            commit(Constant.SET_USER_INFO, null);
            commit(Constant.SET_IS_VALID_TOKEN, false);
            commit(Constant.SET_USER_FAVORITES, null);
            router.push({ name: "main" });
          } else {
            console.log("유저 정보 없음!!!!");
          }
        })
        .catch((response) => {
          console.log(response.status);
        });
    },
    [Constant.USER_LOGIN_NAVER]: async ({ commit }, { body }) => {
      console.log("try naver login");
      await http.post("/users/login/naver", body).then(({ data }) => {
        // 로그인에 성공한 경우
        if (data.message === "success") {
          let accessToken = data["access-token"];
          let refreshToken = data["refresh-token"];
          commit(Constant.SET_IS_LOGIN, true);
          commit(Constant.SET_IS_LOGIN_ERROR, false);
          commit(Constant.SET_IS_VALID_TOKEN, true);
          sessionStorage.setItem("access-token", accessToken);
          sessionStorage.setItem("refresh-token", refreshToken);
        }
        // 회원가입되어있지 않은 사용자일 경우
        else if (data.message == "goJoin") {
          // 회원가입유도
          commit(Constant.SET_MODAL_MESSAGE, "네이버 이메일로 회원가입해주세요!!");
          commit(Constant.SET_SHOW_MODAL, true);
          // alert("네이버 이메일로 회원가입해주세요!!");
          commit(Constant.SET_IS_LOGIN, false);
          commit(Constant.SET_IS_LOGIN_ERROR, true);
          commit(Constant.SET_IS_VALID_TOKEN, false);
          router.push({ name: "login" });
        }
        // 실패한 경우
        else {
          console.log("토큰 얻어오기 실패");
          commit(Constant.SET_IS_LOGIN, false);
          commit(Constant.SET_IS_LOGIN_ERROR, true);
          commit(Constant.SET_IS_VALID_TOKEN, false);
        }
      });
    },
    [Constant.USER_LOGIN_GOOGLE]: async ({ commit }, { body }) => {
      console.log("try google login");
      await http.post("/users/login/google", body).then(({ data }) => {
        // 로그인에 성공한 경우
        if (data.message === "success") {
          let accessToken = data["access-token"];
          let refreshToken = data["refresh-token"];
          commit(Constant.SET_IS_LOGIN, true);
          commit(Constant.SET_IS_LOGIN_ERROR, false);
          commit(Constant.SET_IS_VALID_TOKEN, true);
          sessionStorage.setItem("access-token", accessToken);
          sessionStorage.setItem("refresh-token", refreshToken);
        }
        // 회원가입되어있지 않은 사용자일 경우
        else if (data.message == "goJoin") {
          // 회원가입유도
          commit(Constant.SET_MODAL_MESSAGE, "구글 이메일로 회원가입해주세요!!");
          commit(Constant.SET_SHOW_MODAL, true);
          // alert("구글 이메일로 회원가입해주세요!!");
          commit(Constant.SET_IS_LOGIN, false);
          commit(Constant.SET_IS_LOGIN_ERROR, true);
          commit(Constant.SET_IS_VALID_TOKEN, false);
          router.push({ name: "login" });
        }
        // 실패한 경우
        else {
          console.log("토큰 얻어오기 실패");
          commit(Constant.SET_IS_LOGIN, false);
          commit(Constant.SET_IS_LOGIN_ERROR, true);
          commit(Constant.SET_IS_VALID_TOKEN, false);
        }
      });
    },
    [Constant.CHANGE_USER_PASS]: async ({ dispatch, state }, change) => {
      let user = {
        id: state.userInfo.id,
        pass: change,
      }
      await http.put(`/users/changepass`, user)
        .then(({ data }) => {
          if (data.message == 'success') {
            let token = sessionStorage.getItem("access-token");
            dispatch(Constant.GET_USER_INFO, token);
            alert("비밀번호가 변경되었습니다!!");
            router.push({ name: "userpage" });
          } else {
            alert("비밀번호를 다시 확인해주세요!!");
            console.log('error');
          }
      });
    },
    [Constant.SEND_EMAIL_VALID]: async ({ commit }, email) => {
      await http.get(`/users/email/check/` + email)
        .then(({ data }) => {
          if (data.message == 'success') {
            let payload = {
              code: data.code,
              state: true,
            }
            alert("이메일이 발송되었습니다!!")
            commit(Constant.SEND_EMAIL_VALID, payload);
          } else if(data.message == 'duplicated'){
            alert("이미 가입되어있는 이메일입니다!")
          } else {
            console.log("error");
          }
      })
    },
    "deleteUser" : async ({state}) => {
      await http.delete(`/users/` + state.userInfo.id)
        .then(({ data }) => {
          if (data == 'success') {
            alert("탈퇴가 완료되었습니다.");
          } else {
            console.log('error in delete user');
          }
        });
    },
    ///////////////////////favorite///////////////////////////
    [Constant.GET_USER_FAVORITES]: async ({ commit, state }) => {
      await http
        .get(`/favorites/` + state.userInfo.id)
        .then(({ data }) => {
          if (data.message === "success") {
            commit(Constant.SET_USER_FAVORITES, data.favorites);
          } else {
            console.log("등록된 관심지역이 없습니다.");
          }
        })
        .catch((response) => {
          console.log(response.status);
        });
    },
    [Constant.ADD_USER_FAVORITE]: async ({ dispatch, state }, dongCode) => {
      // 만료된 유저인지 확인
      let token = sessionStorage.getItem("access-token");
      dispatch(Constant.GET_USER_INFO, token);
      let favorite = {
        id: state.userInfo.id,
        dongCode,
      };
      await http
        .post(`/favorites`, favorite)
        .then(({ data }) => {
          if (data.message == "success") {
            dispatch(Constant.GET_USER_FAVORITES);
          } else {
            console.log("error");
          }
        })
        .catch((response) => {
          console.log(response.status);
        });
    },
    [Constant.DELETE_USER_FAVORITE]: async ({ dispatch, state }, dongCode) => {
      // 만료된 유저인지 확인
      let token = sessionStorage.getItem("access-token");
      dispatch(Constant.GET_USER_INFO, token);
      await http
        .delete(`/favorites/` + state.userInfo.id + "/" + dongCode)
        .then(({ data }) => {
          if (data.message == "success") {
            dispatch(Constant.GET_USER_FAVORITES);
          } else {
            console.log("error");
          }
        })
        .catch((response) => {
          console.log(response.status);
        });
    },
  },
};

export default UserStore;
