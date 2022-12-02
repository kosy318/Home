<template>
  <div>
    <div v-if="links.length == 0" class="dropdown-item">알림이 없습니다.</div>
    <div
      v-for="(link, index) in links"
      @click="getQna({ num: link.num, id: userInfo.id })"
      :key="index"
      class="dropdown-item"
    >
      질문 "{{ getTitle(link.title) }}" 에 답변이 달렸습니다.
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import Constant from "@/Constant";

const userStore = "UserStore";
const qnaStore = "QnaStore";

export default {
  name: "UserAlarm",
  computed: {
    ...mapState(userStore, ["userInfo"]),
    ...mapState(qnaStore, ["links"]),
  },
  created() {
    this.$store.dispatch(qnaStore + "/" + Constant.SEARCH_UNCHECKED, { id: this.userInfo.id });
  },
  methods: {
    ...mapActions(qnaStore, [Constant.GET_QNA]),
    getTitle(title) {
      if (title.length > 8) return title.substring(0, 8) + "...";
      else return title;
    },
  },
};
</script>

<style scoped>
.dropdown-item {
  text-decoration: none;
  color: #4b555e !important;
  font-weight: bold;
}
.dropdown-menu {
  background-color: #aebdca;
}
.dropdown-item:hover,
.dropdown-item:active {
  background-color: white;
  cursor: pointer;
  text-decoration: underline;
}
</style>
