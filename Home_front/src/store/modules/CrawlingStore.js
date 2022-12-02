import http from "@/api/http-common";
import Constant from "@/Constant";

const CrawlingStore = {
  namespaced: true,
  state: {
    blogList: [],
    newsList: [],
  },
  mutations: {
    [Constant.GET_BLOG_LIST]: (state, payload) => {
      state.blogList = payload;
    },
    [Constant.GET_NEWS_LIST]: (state, payload) => {
      state.newsList = payload;
    },
  },
  actions: {
    getBlogList({ commit }) {
      http.get("/crawling/blog").then((response) => {
        // console.log(response.data);
        commit(Constant.GET_BLOG_LIST, response.data);
      });
    },
    getNewsList({ commit }) {
      http.get("/crawling/news").then((response) => {
        console.log(response.data);
        commit(Constant.GET_NEWS_LIST, response.data);
      });
    },
  },
};

export default CrawlingStore;
