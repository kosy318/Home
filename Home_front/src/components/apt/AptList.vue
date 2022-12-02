<template>
  <div>
    <b-overlay id="overlay-background" :show="loading">
      <div>
        <b-container
          v-if="selectedApts != null && selectedApts.length != 0"
          class="card bv-example-row mt-3"
        >
          <apt-list-item
            v-for="(apt, index) in selectedApts"
            :key="index"
            :index="index"
            :show="show"
            :apt="apt"
          />
          <b-modal id="modal-1" centered :title="apt.apartmentName" ok-only>
            <b-alert show variant="secondary">
              {{ sidoName }} {{ gugunName }}
              {{ apt.roadName }}
              {{ getRoadNum(apt.roadNameBuildingBonCode, apt.roadNameBuildingBuCode) }}
            </b-alert>
            <div class="row">
              <b class="col-4">거래 날짜</b>
              <div class="col-8">{{ apt.dealYear }}-{{ apt.dealMonth }}-{{ apt.dealDay }}</div>
            </div>
            <div class="row">
              <b class="col-4">거래 가격</b>
              <div class="col-8 price">{{ apt.dealAmount }}만 원</div>
            </div>
            <div class="row">
              <div class="col-4"><b>건축년도</b></div>
              <div class="col-8">{{ apt.buildYear }}년</div>
            </div>
            <div class="row">
              <div class="col-4"><b>평수</b></div>
              <div class="col-8">{{ apt.area * 1 * 0.3025 }}평</div>
            </div>
            <div class="row">
              <div class="col-4"><b>층</b></div>
              <div class="col-8">{{ apt.floor }}층</div>
            </div>
          </b-modal>
        </b-container>
        <b-container v-else class="bv-example-row mt-3">
          <b-row>
            <b-col><b-alert show>아파트 목록이 없습니다.</b-alert></b-col>
          </b-row>
        </b-container>
      </div>
    </b-overlay>
    <section class="py-7" v-show="!loading">
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
import AptListItem from "@/components/apt/AptListItem";
import { mapState } from "vuex";

const aptStore = "AptStore";
const ITEM_NUM = 5;

export default {
  name: "AptList",
  components: {
    AptListItem,
  },
  data() {
    return {
      pages: [],
      selectedApts: [],
      dataLength: 0,
      totalPage: 0,
      startPage: 1,
      endPage: 5,
      curPage: 1,
      hidePrevious: true,
      hideNext: true,
    };
  },
  props: {
    show: Boolean,
  },
  computed: {
    ...mapState(aptStore, ["apts", "loading", "apt", "sidoName", "gugunName"]),
  },
  watch: {
    apts: function () {
      this.selectedApts = [];
      this.pages = [];
      this.hidePrevious = true;
      this.hideNext = true;
      this.dataLength = this.apts.length;
      this.totalPage = this.dataLength / ITEM_NUM;

      for (let i = this.startPage; i <= Math.min(this.endPage, this.totalPage); i++) {
        this.pages.push(i);
      }

      for (let i = 0; i < Math.min(ITEM_NUM, this.apts.length); i++) {
        this.selectedApts.push(this.apts[i]);
      }

      if (this.totalPage > 5) this.flagNext(false);
    },
  },
  methods: {
    flagNext(flag) {
      this.hideNext = flag;
    },
    flagPrevious(flag) {
      this.hidePrevious = flag;
    },
    getList(page) {
      this.curPage = page;

      this.selectedApts = [];
      for (let i = (page - 1) * ITEM_NUM; i < Math.min(page * ITEM_NUM, this.apts.length); i++) {
        this.selectedApts.push(this.apts[i]);
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
    getRoadNum(bon, bu) {
      let res = parseInt(bon);
      if (parseInt(bu) != 0) res += "-" + parseInt(bu);
      return res;
    },
  },
};
</script>

<style scoped>
.spinner {
  color: #7895b2;
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
