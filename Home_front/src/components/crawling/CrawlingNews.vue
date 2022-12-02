<template>
  <div class="mt-3 ml-3">
    <crawling-news-item class="card row mb-3" v-for="(news, index) in newsList" :key="index" :news="news"> </crawling-news-item>
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapState } from "vuex";
import CrawlingNewsItem from "./CrawlingNewsItem.vue";

const crawlingStore = "CrawlingStore";

export default {
  name: "CrawlingNews",
  components: {
    CrawlingNewsItem,
  },
  computed: {
    ...mapState(crawlingStore, ["newsList"]),
  },
  created() {
    this.$store.dispatch(crawlingStore + "/" + Constant.GET_NEWS_LIST);
  },
};
</script>

<style scoped>
.card {
  padding: 10px;
  flex-direction: row;
}
.card:hover {
  background-color: #d3dde6;
  cursor: pointer;
}
</style>
<style scoped src="@/views/style/style.css"></style>
