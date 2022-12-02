<template>
  <div class="container text-center pt-5">
    <h4>QnA 수정</h4>
    <div class="form">
      <div class="form-group text-left">
        <label for="title">제 목:</label>
        <input class="form-control" id="title" name="title" v-model="qna.title" />
      </div>
      <div class="form-group text-left">
        <label for="content">내 용:</label>
        <textarea class="form-control" rows="5" id="content" name="content" v-model="qna.content">
        </textarea>
      </div>
    </div>
    <br />
    <button
      type="submit"
      class="btn btn-dark mr-3"
      @click="modifyQna({ num: qna.num, title: qna.title, content: qna.content })"
    >
      수정 완료
    </button>
    <router-link class="btn btn-dark" to="/qna/list">QnA 목록</router-link>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import Constant from "@/Constant";

const qnaStore = "QnaStore";

export default {
  name: "QnaModify",
  computed: {
    ...mapState(qnaStore, ["qna"]),
  },
  methods: {
    ...mapActions(qnaStore, [Constant.MODIFY_QNA]),
  },
};
</script>

<style scoped>
h4 {
  font-weight: 600;
}
</style>
