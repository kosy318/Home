<template>
  <div class="container text-center pt-5">
    <h4>{{ qna.title }}</h4>

    <br />
    <div v-if="qna.answer == null" class="alert alert-danger" role="alert">
      <span>아직 답변이 등록되지 않았습니다.</span>
      <button
        v-if="userInfo != null && userInfo.id == 'admin'"
        class="btn btn-outline-danger ml-3"
        style="font-size: 0.8em"
        @click="writeQnaAnswer"
      >
        답변하기
      </button>
    </div>
    <div class="card mb-3 m-auto">
      <div class="card-header d-flex justify-content-between" style="font-size: 0.8em">
        <div><br />아이디 : {{ idCover }}***</div>
        <div>등록 날짜 : {{ qna.regdate }}<br />조회수 : {{ qna.count }}</div>
      </div>
      <div class="card-body card" style="height: 300px">
        <p class="card-text text-left">
          {{ qna.content }}
        </p>
      </div>
      <div v-if="qna.answer != null" class="d-flex">
        <div class="card-body" style="width: 10%">
          <p class="card-text text-left">
            <b-icon-arrow-return-right></b-icon-arrow-return-right>
          </p>
        </div>
        <div class="card-body" style="width: 90%; max-height: 300px">
          <p class="card-text text-left">
            {{ qna.answer }}
          </p>
        </div>
      </div>
    </div>
    <br />
    <div class="btn-group">
      <router-link
        v-if="userInfo != null && qna.id == userInfo.id && qna.answer == null"
        class="btn btn-dark"
        to="/qna/modify"
        >수정하기</router-link
      >
      <button
        v-if="
          ((userInfo != null && qna.id == userInfo.id) ||
            (userInfo != null && userInfo.id == 'admin')) &&
          qna.answer == null
        "
        class="btn btn-dark"
        @click="deleteQna(qna.num)"
      >
        삭제하기
      </button>
      <router-link class="btn btn-dark" to="/qna/list">QnA 목록</router-link>
    </div>
    <br />
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import Constant from "@/Constant";

const qnaStore = "QnaStore";
const userStore = "UserStore";

export default {
  name: "QnaDetail",
  computed: {
    ...mapState(qnaStore, ["qna"]),
    ...mapState(userStore, ["userInfo"]),
    idCover() {
      return this.qna.id.substring(0, 3);
    },
  },
  // created() {
  //   console.log("qnadetail changed");
  //   this.$store.dispatch(qnaStore + "/" + [Constant.CHECK_QNA], {
  //     id: this.userInfo.id,
  //     num: this.qna.num,
  //   });
  // },
  methods: {
    writeQnaAnswer() {
      this.$router.push({
        name: "qnaanswerinsert",
      });
    },
    ...mapActions(qnaStore, [Constant.DELETE_QNA]),
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
