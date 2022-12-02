import Vue from "vue";
import Vuex from "vuex";
import createPersistedState from "vuex-persistedstate";
import AptStore from "@/store/modules/AptStore";
import NoticeStore from "@/store/modules/NoticeStore";
import QnaStore from "@/store/modules/QnaStore";
import UserStore from "@/store/modules/UserStore";
import CrawlingStore from "@/store/modules/CrawlingStore";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    AptStore,
    NoticeStore,
    QnaStore,
    UserStore,
    CrawlingStore,
  },
  plugins: [
    createPersistedState({
      // 브라우저 종료시 제거하기 위해 localStorage가 아닌 sessionStorage로 변경. (default: localStorage)
      storage: sessionStorage,
    }),
  ],
});
