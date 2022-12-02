<template>
  <div class="container text-center pt-5">
    <h4>공지 수정</h4>
    <div class="form">
      <div class="form-group text-left">
        <label for="content">제 목:</label>
        <input class="form-control" id="title" name="title" v-model="notice.title" />
      </div>
      <div class="input-group">
        <div class="input-group-prepend w-100">
          <!-- <label for="content">내 용:</label>
        <textarea
          class="form-control"
          rows="5"
          id="content"
          name="content"
          v-model="notice.content"
        >
        </textarea> -->
          <vue-editor class="w-100" v-model="notice.content" />
        </div>
      </div>
    </div>
    <br />
    <div class="mt-5">
      <button
        type="submit"
        class="btn btn-dark mr-3"
        @click="modifyNotice({ num: notice.num, title: notice.title, content: notice.content })"
      >
        수정 완료
      </button>
      <router-link class="btn btn-dark" to="/notice/list">공지 목록</router-link>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import Constant from "@/Constant";
import { VueEditor } from "vue2-editor";

const noticeStore = "NoticeStore";

export default {
  name: "NoticeModify",
  computed: {
    ...mapState(noticeStore, ["notice"]),
  },
  components: {
    VueEditor,
  },
  methods: {
    ...mapActions(noticeStore, [Constant.MODIFY_NOTICE]),
    alert(msg) {
      window.alert(msg);
    },
  },
};
</script>

<style scoped>
h4 {
  font-weight: 600;
}
</style>
