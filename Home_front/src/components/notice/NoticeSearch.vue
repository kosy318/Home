<template>
  <div class="btn-group ml-auto">
    <input
      type="text"
      name="word"
      v-model="title"
      class="form-control"
      placeholder="제목"
      aria-label="Search"
      aria-describedby="search-addon"
      style="max-width: 150px"
      @keyup.enter="searchNotice(title)"
    />
    <button type="button" class="btn btn-dark" @click="searchNotice(title)">검색</button>
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapActions } from "vuex";

const noticeStore = "NoticeStore";

export default {
  name: "NoticeSearch",
  data() {
    return {
      title: "",
    };
  },
  methods: {
    ...mapActions(noticeStore, [Constant.SEARCH_NOTICE]),
  },
};
</script>

<style></style>
