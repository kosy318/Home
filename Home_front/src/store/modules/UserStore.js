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
            commit(Constant.SET_MODAL_MESSAGE, "??????????????? ?????? ??????????????????!!");
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
            console.log("?????? ?????? ??????");
          }
        })
        .catch((response) => {
          console.log(response);
          console.log("getUserInfo() error code [?????? ???????????? ?????? ?????????.] ::: " + response.status);
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
            console.log("????????? ?????? >> ????????? ?????? : {}", accessToken);
            sessionStorage.setItem("access-token", accessToken);
            commit(Constant.SET_IS_VALID_TOKEN, true);
          }
        })
        .catch(async (response) => {
          // HttpStatus.UNAUTHORIZE(401) : RefreshToken ?????? ?????? >> ?????? ?????????!!!!
          if (response.status === 401) {
            console.log("?????? ??????");
            // ????????????
            await http
              .get(`/users/logout/` + state.userInfo.id)
              .then(({ data }) => {
                if (data.message === "success") {
                  console.log("???????????? ?????? ?????? ??????");
                } else {
                  console.log("???????????? ?????? ?????? ??????");
                }
                alert("RefreshToken ?????? ??????!!! ?????? ???????????? ?????????.");
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
            console.log("?????? ?????? ??????!!!!");
          }
        })
        .catch((response) => {
          console.log(response.status);
        });
    },
    [Constant.USER_LOGIN_NAVER]: async ({ commit }, { body }) => {
      console.log("try naver login");
      await http.post("/users/login/naver", body).then(({ data }) => {
        // ???????????? ????????? ??????
        if (data.message === "success") {
          let accessToken = data["access-token"];
          let refreshToken = data["refresh-token"];
          commit(Constant.SET_IS_LOGIN, true);
          commit(Constant.SET_IS_LOGIN_ERROR, false);
          commit(Constant.SET_IS_VALID_TOKEN, true);
          sessionStorage.setItem("access-token", accessToken);
          sessionStorage.setItem("refresh-token", refreshToken);
        }
        // ???????????????????????? ?????? ???????????? ??????
        else if (data.message == "goJoin") {
          // ??????????????????
          commit(Constant.SET_MODAL_MESSAGE, "????????? ???????????? ????????????????????????!!");
          commit(Constant.SET_SHOW_MODAL, true);
          // alert("????????? ???????????? ????????????????????????!!");
          commit(Constant.SET_IS_LOGIN, false);
          commit(Constant.SET_IS_LOGIN_ERROR, true);
          commit(Constant.SET_IS_VALID_TOKEN, false);
          router.push({ name: "login" });
        }
        // ????????? ??????
        else {
          console.log("?????? ???????????? ??????");
          commit(Constant.SET_IS_LOGIN, false);
          commit(Constant.SET_IS_LOGIN_ERROR, true);
          commit(Constant.SET_IS_VALID_TOKEN, false);
        }
      });
    },
    [Constant.USER_LOGIN_GOOGLE]: async ({ commit }, { body }) => {
      console.log("try google login");
      await http.post("/users/login/google", body).then(({ data }) => {
        // ???????????? ????????? ??????
        if (data.message === "success") {
          let accessToken = data["access-token"];
          let refreshToken = data["refresh-token"];
          commit(Constant.SET_IS_LOGIN, true);
          commit(Constant.SET_IS_LOGIN_ERROR, false);
          commit(Constant.SET_IS_VALID_TOKEN, true);
          sessionStorage.setItem("access-token", accessToken);
          sessionStorage.setItem("refresh-token", refreshToken);
        }
        // ???????????????????????? ?????? ???????????? ??????
        else if (data.message == "goJoin") {
          // ??????????????????
          commit(Constant.SET_MODAL_MESSAGE, "?????? ???????????? ????????????????????????!!");
          commit(Constant.SET_SHOW_MODAL, true);
          // alert("?????? ???????????? ????????????????????????!!");
          commit(Constant.SET_IS_LOGIN, false);
          commit(Constant.SET_IS_LOGIN_ERROR, true);
          commit(Constant.SET_IS_VALID_TOKEN, false);
          router.push({ name: "login" });
        }
        // ????????? ??????
        else {
          console.log("?????? ???????????? ??????");
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
            alert("??????????????? ?????????????????????!!");
            router.push({ name: "userpage" });
          } else {
            alert("??????????????? ?????? ??????????????????!!");
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
            alert("???????????? ?????????????????????!!")
            commit(Constant.SEND_EMAIL_VALID, payload);
          } else if(data.message == 'duplicated'){
            alert("?????? ?????????????????? ??????????????????!")
          } else {
            console.log("error");
          }
      })
    },
    "deleteUser" : async ({state}) => {
      await http.delete(`/users/` + state.userInfo.id)
        .then(({ data }) => {
          if (data == 'success') {
            alert("????????? ?????????????????????.");
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
            console.log("????????? ??????????????? ????????????.");
          }
        })
        .catch((response) => {
          console.log(response.status);
        });
    },
    [Constant.ADD_USER_FAVORITE]: async ({ dispatch, state }, dongCode) => {
      // ????????? ???????????? ??????
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
      // ????????? ???????????? ??????
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
