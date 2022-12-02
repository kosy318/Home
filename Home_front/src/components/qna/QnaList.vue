<template>
  <div class="container">
    <div class="mb-2 input-group d-flex justify-content-between">
      <div class="btn-group">
        <button v-if="userInfo != null" class="btn btn-dark" @click="writeQna">글쓰기</button>
        <button class="btn btn-dark" @click="getQnaList">전체 목록</button>
      </div>
      <qna-search></qna-search>
    </div>

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
        <qna-list-item v-for="qna in selectedQnaList" :key="qna.num" :qna="qna"></qna-list-item>
      </tbody>
    </table>
    <section class="py-7">
      <div class="container">
        <div class="py-2">
          <div class="">
            <ul class="pagination pagination-primary mt-4 d-flex justify-content-center">
              <li class="page-item">
                <a
                  class="page-link"
                  aria-label="Previous"
                  :class="{ hide: hidePrevious }"
                  @click="previousPage"
                >
                  <span aria-hidden="true"><b-icon icon="chevron-left"></b-icon></span>
                </a>
              </li>
              <li
                class="page-item"
                v-for="(page, index) in pages"
                :key="index"
                :class="{ active: page == curPage }"
              >
                <a class="page-link" @click="getList(page)">{{ page }}</a>
              </li>
              <li class="page-item">
                <a
                  class="page-link"
                  aria-label="Next"
                  :class="{ hide: hideNext }"
                  @click="nextPage"
                >
                  <span aria-hidden="true"><b-icon icon="chevron-right"></b-icon></span>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import Constant from "@/Constant";
import QnaListItem from "@/components/qna/QnaListItem.vue";
import QnaSearch from "@/components/qna/QnaSearch.vue";

const qnaStore = "QnaStore";
const userStore = "UserStore";
const ITEM_NUM = 10;

export default {
  name: "QnaList",
  data() {
    return {
      pages: [],
      selectedQnaList: [],
      dataLength: 0,
      totalPage: 0,
      startPage: 1,
      endPage: 5,
      curPage: 1,
      hidePrevious: true,
      hideNext: true,
    };
  },
  components: {
    QnaListItem,
    QnaSearch,
  },
  created() {
    this.$store.dispatch(qnaStore + "/" + Constant.GET_QNA_LIST);
    window.scroll({
      top: 0,
      behavior: "smooth",
    });
  },
  computed: {
    ...mapState(qnaStore, ["qnalist"]),
    ...mapState(userStore, ["userInfo"]),
  },
  watch: {
    qnalist: function () {
      this.dataLength = this.qnalist.length;
      this.totalPage = this.dataLength / ITEM_NUM;

      console.log(this.totalPage);
      for (let i = this.startPage; i <= Math.min(this.endPage, Math.ceil(this.totalPage)); i++) {
        this.pages.push(i);
      }

      for (let i = 0; i < Math.min(ITEM_NUM, this.qnalist.length); i++) {
        this.selectedQnaList.push(this.qnalist[i]);
      }

      if (this.totalPage > 5) this.flagNext(false);
    },
  },
  methods: {
    writeQna() {
      this.$router.push({
        name: "qnainsert",
      });
    },
    ...mapActions(qnaStore, ["getQnaList"]),
    flagNext(flag) {
      this.hideNext = flag;
    },
    flagPrevious(flag) {
      this.hidePrevious = flag;
    },
    getList(page) {
      this.curPage = page;

      this.selectedQnaList = [];
      for (let i = (page - 1) * ITEM_NUM; i < Math.min(page * ITEM_NUM, this.qnalist.length); i++) {
        this.selectedQnaList.push(this.qnalist[i]);
      }
    },
    previousPage() {
      this.startPage -= 5;
      this.endPage -= 5;
      this.curPage = this.endPage;

      this.pages = [];
      for (let i = this.startPage; i <= Math.min(this.endPage, this.totalPage); i++) {
        this.pages.push(i);
      }

      this.getList(this.curPage);

      this.flagNext(false);
      if (this.startPage == 1) this.flagPrevious(true);
    },
    nextPage() {
      this.startPage += 5;
      this.curPage = this.startPage;
      this.endPage += 5;

      this.pages = [];
      for (let i = this.startPage; i <= Math.min(this.endPage, this.totalPage); i++) {
        this.pages.push(i);
      }

      this.getList(this.curPage);

      this.flagPrevious(false);
      if (this.totalPage <= this.endPage) this.flagNext(true);
    },
  },
};
</script>

<style scoped>
h4 {
  font-weight: 600;
}
.page-link {
  width: 45px;
  height: 45px;
  text-align: center;
  line-height: 25px;
  border-radius: 50% !important;
  margin: 0 3px;
}
.hide {
  display: none;
}
.page-item.active .page-link {
  width: 45px;
  height: 45px;
  text-align: center;
  border-radius: 50% !important;
  background-color: #7895b2 !important;
  border-color: #7895b2 !important;
}
</style>
