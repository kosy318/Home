<template>
  <tr
    @click="
      qna.secret == 'Y' && (userInfo == null || (userInfo.id != 'admin' && qna.id != userInfo.id))
        ? alert('비공개 게시물입니다.')
        : getQna({ num: qna.num, id: userInfo.id })
    "
  >
    <td>{{ qna.num }}</td>
    <td>{{ isAnswered }}</td>
    <td>
      <span v-if="qna.secret == 'Y'"><b-icon-lock /></span> {{ qna.title }}
    </td>
    <td>{{ idCover }}***</td>
    <td>{{ qna.regdate }}</td>
    <td>{{ qna.count }}</td>
  </tr>
</template>

<script>
import Constant from "@/Constant";
import { mapActions, mapState } from "vuex";

const qnaStore = "QnaStore";
const userStore = "UserStore";

export default {
  name: "QnaListItem",
  props: {
    qna: {},
  },
  computed: {
    ...mapState(userStore, ["userInfo"]),
    idCover() {
      console.log(this.qna);
      return this.qna.id.substring(0, 3);
    },
    isAnswered() {
      if (this.qna.answer != null) return "✔️";
      else return "❌";
    },
  },
  methods: {
    ...mapActions(qnaStore, [Constant.GET_QNA]),
    alert(msg) {
      window.alert(msg);
    },
  },
};
</script>

<style></style>
