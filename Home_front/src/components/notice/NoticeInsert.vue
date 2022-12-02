<template>
  <div class="container text-center pt-5">
    <h4>공지 추가</h4>
    <div action="notice-insert" method="post">
      <div class="input-group mb-3">
        <div class="input-group-prepend">
          <span class="input-group-text" id="title">제목</span>
        </div>
        <input type="text" class="form-control" v-model="title" autofocus />
      </div>

      <div class="input-group">
        <div class="input-group-prepend w-100">
          <!-- <textarea
            class="form-control"
            rows="10"
            aria-label="With textarea"
            v-model="content"
            ></textarea> -->
          <vue-editor class="w-100" v-model="content" />
        </div>
      </div>
    </div>
    <br />
    <div class="mt-5">
      <button
        type="submit"
        class="btn btn-dark mr-3"
        @click="
          title == null || title.length == 0
            ? alert('제목을 입력해주세요')
            : addNotice({ title, content })
        "
      >
        등록
      </button>
      <router-link class="btn btn-dark" to="/notice/list">공지 목록</router-link>
    </div>
    <br />
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapActions } from "vuex";
import { VueEditor } from "vue2-editor";

const noticeStore = "NoticeStore";

export default {
  name: "NoticeInsert",
  data() {
    return {
      title: "",
      content: "",
    };
  },
  components: {
    VueEditor,
  },
  methods: {
    ...mapActions(noticeStore, [Constant.ADD_NOTICE]),
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
