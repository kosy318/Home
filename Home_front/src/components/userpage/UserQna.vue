<template>
  <div>
    <h4>내 질문</h4>
    <table class="table table-hover">
      <colgroup>
        <col style="width: 5%" />
        <col style="width: 10%" />
        <col style="width: 45%" />
        <col style="width: 15%" />
        <col style="width: 15%" />
        <col style="width: 10%" />
      </colgroup>
      <thead>
        <tr>
          <th>#</th>
          <th>답변여부</th>
          <th>제목</th>
          <th>아이디</th>
          <th>날짜</th>
          <th>조회수</th>
        </tr>
      </thead>
      <tbody>
        <qna-list-item v-for="qna in myqnalist" :key="qna.num" :qna="qna"></qna-list-item>
      </tbody>
    </table>
  </div>
</template>

<script>
import { mapState } from "vuex";
import QnaListItem from "@/components/qna/QnaListItem.vue";
import Constant from "@/Constant";

const qnaStore = "QnaStore";
const userStore = "UserStore";

export default {
  name: "UserQna",
  components: {
    QnaListItem,
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
    ...mapState(qnaStore, ["myqnalist"]),
  },
  created() {
    this.$store.dispatch(qnaStore + "/" + Constant.GET_MY_QNA_LIST, { id: this.userInfo.id });
  },
};
</script>

<style>
h4 {
  font-weight: 600;
}
</style>
