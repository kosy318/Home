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
      @keyup.enter="searchQna(title)"
    />
    <button type="button" class="btn btn-dark" @click="searchQna(title)">검색</button>
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapActions } from "vuex";

const qnaStore = "QnaStore";

export default {
  name: "QnaSearch",
  data() {
    return {
      title: "",
    };
  },
  methods: {
    ...mapActions(qnaStore, [Constant.SEARCH_QNA]),
  },
};
</script>

<style></style>
