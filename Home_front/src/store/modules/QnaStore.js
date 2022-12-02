import http from "@/api/http-common";
import Constant from "@/Constant";
import router from "@/router";

const qnaStore = {
  namespaced: true,
  state: {
    qnalist: [],
    qna: null,
    myqnalist: [],
    links: [],
  },
  getters: {},
  mutations: {
    [Constant.GET_QNA_LIST]: (state, payload) => {
      state.qnalist = payload.qnalist;
      router.push({ name: "qnalist" });
    },
    [Constant.GET_QNA]: (state, payload) => {
      state.qna = payload.qna;
      router.push({
        name: "qnadetail",
        query: { num: state.qna.num },
      });
    },
    [Constant.GET_MY_QNA_LIST]: (state, payload) => {
      state.myqnalist = payload.myqnalist;
    },
    [Constant.SEARCH_UNCHECKED]: (state, payload) => {
      console.log("links", payload);
      state.links = payload;
    },
  },
  actions: {
    [Constant.GET_QNA_LIST]: ({ commit }) => {
      http.get("/qna").then((response) => {
        commit(Constant.GET_QNA_LIST, { qnalist: response.data });
      });
    },
    [Constant.GET_MY_QNA_LIST]: ({ commit }, payload) => {
      http.get(`/qna/myqna/${payload.id}`).then((response) => {
        commit(Constant.GET_MY_QNA_LIST, { myqnalist: response.data });
      });
    },
    [Constant.GET_QNA]: ({ commit, dispatch }, payload) => {
      console.log;
      http
        .get(`/qna/${payload.num}`)
        .then((response) => {
          commit(Constant.GET_QNA, { qna: response.data });
        })
        .then(() => {
          dispatch(Constant.CHECK_QNA, { id: payload.id, num: payload.num });
        });
    },
    [Constant.ADD_QNA]: ({ dispatch }, payload) => {
      http.post("/qna", payload).then(() => {
        console.log(payload);
        dispatch(Constant.GET_QNA_LIST);
      });
    },
    [Constant.ADD_QNA_ANSWER]: ({ dispatch }, payload) => {
      http.put("/qna/answer", payload).then(() => {
        dispatch(Constant.GET_QNA_LIST);
      });
    },
    [Constant.MODIFY_QNA]: ({ dispatch }, payload) => {
      http.put("/qna/modify", payload).then(() => {
        dispatch(Constant.GET_QNA_LIST);
      });
    },
    [Constant.DELETE_QNA]: ({ dispatch }, payload) => {
      console.log(payload);
      http.delete("/qna/" + payload).then(() => {
        dispatch(Constant.GET_QNA_LIST);
      });
    },
    [Constant.SEARCH_QNA]: ({ commit }, title) => {
      http
        .get(`/qna/search/${title}`)
        .then((response) => {
          commit(Constant.GET_QNA_LIST, { qnalist: response.data });
        })
        .then(() => {
          router.push({ name: "qnasearch", query: { title: title } });
        });
    },
    [Constant.SEARCH_UNCHECKED]: ({ commit }, payload) => {
      http.get(`/qna/unchecked/${payload.id}`).then((response) => {
        commit(Constant.SEARCH_UNCHECKED, response.data);
      });
    },
    [Constant.CHECK_QNA]: ({ dispatch }, payload) => {
      http.put(`/qna/check/${payload.num}`).then(() => {
        console.log(payload.id);
        dispatch(Constant.SEARCH_UNCHECKED, payload);
      });
    },
  },
};

export default qnaStore;
