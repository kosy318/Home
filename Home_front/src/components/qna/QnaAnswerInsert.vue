<template>
  <div class="container text-center pt-5">
    <h4>답변하기</h4>
    <div class="card mb-3 m-auto">
      <div class="card-header font-weight-bold">
        {{ qna.title }}
      </div>
      <div class="card-body card" style="max-height: 300px">
        <p class="card-text text-left">
          {{ qna.content }}
        </p>
      </div>
    </div>
    <div class="d-flex mt-1">
      <div style="width: 10%">
        <p class="card-text text-center">
          <b-icon-arrow-return-right></b-icon-arrow-return-right>
        </p>
      </div>
      <div style="width: 90%; max-height: 300px">
        <textarea class="form-control" rows="10" aria-label="With textarea" v-model="answer">
        </textarea>
      </div>
    </div>
    <br />
    <button type="submit" class="btn btn-dark mr-3" @click="addQnaAnswer({ num: qna.num, answer })">
      등록
    </button>
    <router-link class="btn btn-dark" to="/qna/list">QnA 목록</router-link>

    <br />
  </div>
</template>

<script>
import { mapActions, mapState } from "vuex";
import Constant from "@/Constant";

const qnaStore = "QnaStore";

export default {
  name: "QnaAnswerInsert",
  data() {
    return {
      answer: "",
    };
  },
  computed: {
    ...mapState(qnaStore, ["qna"]),
  },
  methods: {
    ...mapActions(qnaStore, [Constant.ADD_QNA_ANSWER]),
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
