<template>
  <div class="container text-center pt-5">
    <h4>{{ notice.title }}</h4>

    <br />
    <div class="card mb-3 m-auto">
      <div class="card-header text-right" style="font-size: 0.8em">
        등록 날짜 : {{ notice.regdate }}<br />조회수 : {{ notice.count }}
      </div>
      <div class="card-body" style="height: 300px">
        <p class="card-text text-left" v-html="notice.content"></p>
      </div>
    </div>
    <br />
    <div class="btn-group">
      <router-link
        v-if="userInfo != null && userInfo.id == 'admin'"
        class="btn btn-dark"
        to="/notice/modify"
        >수정하기</router-link
      >
      <button
        v-if="userInfo != null && userInfo.id == 'admin'"
        class="btn btn-dark"
        @click="deleteNotice(notice.num)"
      >
        삭제하기
      </button>
      <router-link class="btn btn-dark" to="/notice/list">공지 목록</router-link>
    </div>
    <br />
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import Constant from "@/Constant";

const noticeStore = "NoticeStore";
const userStore = "UserStore";

export default {
  name: "NoticeDetail",
  computed: {
    ...mapState(noticeStore, ["notice"]),
    ...mapState(userStore, ["userInfo"]),
  },
  created() {},
  methods: {
    ...mapActions(noticeStore, [Constant.DELETE_NOTICE]),
  },
};
</script>

<style scoped>
h4 {
  font-weight: 600;
}
.card {
  border: #ffffff solid 1px;
  border-bottom-color: rgba(0, 0, 0, 0.125);
}
</style>
