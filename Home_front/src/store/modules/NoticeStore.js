import http from "@/api/http-common";
import Constant from "@/Constant";
import router from "@/router";

const noticeStore = {
  namespaced: true,
  state: {
    notices: [],
    notice: null,
    three: [],
  },
  getters: {},
  mutations: {
    [Constant.GET_NOTICE_LIST]: (state, payload) => {
      state.notices = payload.notices;
      router.push({ name: "notice" });
    },
    [Constant.GET_NOTICE]: (state, payload) => {
      state.notice = payload.notice;
      router.push({
        name: "noticedetail",
        query: { num: state.notice.num },
      });
    },
    [Constant.SELECT_THREE]: (state, payload) => {
      state.three = payload;
      console.log(state.three);
    },
  },
  actions: {
    //[Constant.GET_NOTICE_LIST]: ({ commit }) => {
    getNoticeList: ({ commit }) => {
      http.get("/notices").then((response) => {
        commit(Constant.GET_NOTICE_LIST, { notices: response.data });
      });
    },
    [Constant.SELECT_THREE]: ({ commit }) => {
      http.get(`/notices/3`).then((response) => {
        console.log("3", response.data);
        commit(Constant.SELECT_THREE, response.data);
      });
    },
    [Constant.GET_NOTICE]: ({ commit }, num) => {
      http.get(`/notices/${num}`).then((response) => {
        commit(Constant.GET_NOTICE, { notice: response.data });
      });
    },
    [Constant.SEARCH_NOTICE]: ({ commit }, title) => {
      http
        .get(`/notices/search/${title}`)
        .then((response) => {
          commit(Constant.GET_NOTICE_LIST, { notices: response.data });
        })
        .then(() => {
          router.push({ name: "noticesearch", query: { title: title } });
        });
    },
    [Constant.ADD_NOTICE]: ({ dispatch }, payload) => {
      http.post("/notices", payload).then(() => {
        dispatch(Constant.GET_NOTICE_LIST);
      });
    },
    [Constant.DELETE_NOTICE]: ({ dispatch }, payload) => {
      console.log(payload);
      http.delete("/notices/" + payload).then(() => {
        dispatch(Constant.GET_NOTICE_LIST);
      });
    },
    [Constant.MODIFY_NOTICE]: ({ dispatch }, payload) => {
      http.put("/notices", payload).then(() => {
        dispatch(Constant.GET_NOTICE_LIST);
      });
    },
  },
};

export default noticeStore;
