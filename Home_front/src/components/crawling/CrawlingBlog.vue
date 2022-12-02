<template>
  <div class="mt-3 ml-3">
    <crawling-blog-item class="card row mb-3" v-for="(blog, index) in blogList" :key="index" :blog="blog"> </crawling-blog-item>
  </div>
</template>

<script>
import Constant from "@/Constant";
import { mapState } from "vuex";
import CrawlingBlogItem from "./CrawlingBlogItem.vue";

const crawlingStore = "CrawlingStore";

export default {
  name: "CrawlingBlog",
  components: {
    CrawlingBlogItem,
  },
  computed: {
    ...mapState(crawlingStore, ["blogList"]),
  },
  created() {
    this.$store.dispatch(crawlingStore + "/" + Constant.GET_BLOG_LIST);
  },
};
</script>

<style scoped>
.card {
  padding: 10px;
  flex-direction: row;
}
.card:hover {
  background-color: #f5efe6;
  cursor: pointer;
}
</style>
<style scoped src="@/views/style/style.css"></style>
